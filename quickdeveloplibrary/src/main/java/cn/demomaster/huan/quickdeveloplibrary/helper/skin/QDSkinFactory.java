package cn.demomaster.huan.quickdeveloplibrary.helper.skin;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.core.view.LayoutInflaterFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

public class QDSkinFactory implements LayoutInflaterFactory {
    //自定义的控件，这里面放包控件的包路径
    public static String[] sClassPrefixList = {
            "andfix.shenbin.com.skindemo.struct"
    };
    //存放符合条件的皮肤数据源
    private List<SkinView> skinCacheList = new ArrayList<>();


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        if (!name.contains(".")) {  //表示系统的控件  android.widget.LinearLayout
            view = createView(context, attrs, "android.widget." + name);
        } else {  //自定义view
            for (String str : sClassPrefixList) {
                view = createView(context, attrs, str + "." + name);
                if (view != null) {
                    break;
                }
            }
        }

        if (view != null) {
            parseSkinView(context, attrs, view);
        }

        return view;
    }

    private View createView(Context context, AttributeSet attrs, String name) {
        try {
            //获得当前的类加载器
            ClassLoader classLoader = context.getClass().getClassLoader();
            //通过名字，获得控件的类
            Class clz = classLoader.loadClass(name);
            Constructor<? extends View> constructor = clz.getConstructor(new Class[]{Context.class, AttributeSet.class});
            //返回控件
            return constructor.newInstance(context, attrs);
        } catch (ClassNotFoundException e) {
            QDLogger.e(e);
        } catch (InvocationTargetException e) {
            QDLogger.e(e);
        } catch (NoSuchMethodException e) {
            QDLogger.e(e);
        } catch (InstantiationException e) {
            QDLogger.e(e);
        } catch (IllegalAccessException e) {
            QDLogger.e(e);
        }

        return null;
    }

    /**
     * 筛选，过滤，把符合条件的控件和资源存储起来
     *
     * @param context
     * @param attrs
     * @param view
     */
    private void parseSkinView(Context context, AttributeSet attrs, View view) {
        List<SkinItem> lists = new ArrayList<>();

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);  //textColor
            String attrValue = attrs.getAttributeValue(i); //@7f030000 R.java
            //如果是背景或者textColor属性
            if ("background".equalsIgnoreCase(attrName) || "textColor".equalsIgnoreCase(attrName)) {
                //得到属性值id
                int id = Integer.parseInt(attrValue.substring(1));
                //得到属性类型
                String attrValueType = context.getResources().getResourceTypeName(id);
                //得到属性名
                String attrValueName = context.getResources().getResourceEntryName(id);
                //创建皮肤实例
                SkinItem skinItem = new SkinItem(attrName, id, attrValueType, attrValueName);
                //放到集合中
                lists.add(skinItem);

            }
        }

        if (!lists.isEmpty()) {
            //创建实例，保存,用来换肤
            SkinView skinView = new SkinView(view, lists);
            skinCacheList.add(skinView);
        }
    }


    /**
     * 皮肤类
     */
    class SkinItem {
        String attrName;  //background
        int refId;  //R 文件标示的id
        String attrValueType;  //资源类型，@drawable  、@color
        String atrrValueName;  //资源名字 ic_launcher

        public SkinItem(String attrName, int refId, String attrValueType, String atrrValueName) {
            this.attrName = attrName;
            this.refId = refId;
            this.attrValueType = attrValueType;
            this.atrrValueName = atrrValueName;
        }
    }

    class SkinView {
        private View view;
        private List<SkinItem> lists;

        public SkinView(View view, List<SkinItem> lists) {
            this.view = view;
            this.lists = lists;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void apply() {
            for (SkinItem skinItem : lists) {
                //切换textView的字体颜色
                if ("textColor".equalsIgnoreCase(skinItem.attrName)) {
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(QDSkinManager.getInstance().getColor(skinItem.refId));
                    }
                }
                //这个是切换背景颜色
//                else if("background".equalsIgnoreCase(skinItem.attrName)){
//
//                    ((View)view).setBackgroundColor(SkinManager.getInstance().getColor(skinItem.refId));
//
//                }
                //切换背景图片
                else if ("background".equalsIgnoreCase(skinItem.attrName)) {

                    ((View) view).setBackground(QDSkinManager.getInstance().getDrawable(skinItem.refId));

                }
            }
        }
    }

    public void apply() {
        for (SkinView skinView : skinCacheList) {
            skinView.apply();
        }
    }
}