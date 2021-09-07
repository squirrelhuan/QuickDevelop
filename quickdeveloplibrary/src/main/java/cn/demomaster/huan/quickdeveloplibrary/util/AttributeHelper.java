package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

import cn.demomaster.qdrouter_library.base.OnReleaseListener;

/**
 * AttributeSet 帮助类,读取Android的属性
 */
public class AttributeHelper implements OnReleaseListener {

    public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";

    private final WeakReference<Context> contextWeakReference;
    private AttributeSet mAttrs;

    public AttributeHelper(Context context, AttributeSet attrs) {
        contextWeakReference = new WeakReference<>(context);
        mAttrs = attrs;
    }


    /**
     * 判断是否有指定的属性
     *
     * @param attribute
     * @return true 有,false 没有
     */
    public boolean hasAttr(String attribute) {
        return getValue(attribute) != null;
    }

    /**
     * 获取指定属性的值,不一定是真正的值,比如 15sp 返回的是15sp而不是15
     *
     * @param attribute
     * @return 返回字符串或者null
     */
    public String getValue(String attribute) {
        if (mAttrs == null) {
            return null;
        }

        return mAttrs.getAttributeValue(ANDROID_NAMESPACE, attribute);
    }

    /**
     * 获取 string值,
     *
     * @param attribute 属性名称
     * @return 如果找到返回相应的值, 否则返回null
     */
    public String getString(String attribute) {
        String string = getValue(attribute);

        if (string != null && string.startsWith("@")) {// 资源文件
            string = contextWeakReference.get().getResources().getString(
                    Integer.parseInt(string.substring(1)));
        }
        return string;
    }

    /**
     * 获取文本数组
     *
     * @param attribute
     * @return 如果没有找到, 返回null
     */
    public String[] getTextArray(String attribute) {
        String string = getValue(attribute);
        if (string != null && string.startsWith("@")) {// 资源文件
            return contextWeakReference.get().getResources().getStringArray(
                    Integer.parseInt(string.substring(1)));
        }
        return null;
    }

    /**
     * 获取 string值,
     *
     * @param attribute 属性名称
     * @return 如果找到返回相应的值, 否则返回false
     */
    public boolean getBoolean(String attribute) {
        String string = getValue(attribute);

        if (string != null && string.startsWith("@")) {// 资源文件
            return contextWeakReference.get().getResources().getBoolean(
                    Integer.parseInt(string.substring(1)));
        }
        return Boolean.parseBoolean(string);
    }

    @Override
    public void onRelease(Object self) {
        if(contextWeakReference!=null){
            contextWeakReference.clear();
        }
        mAttrs = null;
    }
}