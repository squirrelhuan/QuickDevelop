package cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout;

import android.content.Context;
import android.view.View;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Field;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2019/3/19.
 * description：
 */
public class AjsSaxHandler<T> extends DefaultHandler {
    Element rootElement;
    View rootView;
    Context mContext;

    public AjsSaxHandler(Class<T> clazz, OnParseCompleteListener onParseCompleteListener) {
        super();
        mOnParseCompleteListener = onParseCompleteListener;
        //QDLogger.i("xml加载目标类：" + clazz.getName());
    }
    public AjsSaxHandler(Context context, View rootView, OnParseCompleteListener onParseCompleteListener) {
        super();
        this.rootView = rootView;
        mOnParseCompleteListener = onParseCompleteListener;
        mContext = context;
        //QDLogger.i("xml加载目标类：" + clazz.getName());
    }

    /**
     * 当SAX解析器解析到XML文档开始时，会调用的方法
     */
    @Override
    public void startDocument() {
        //elements = new ArrayList<>();
        //QDLogger.i("开始解析根元素实体类：" + mClazz.getName());
    }

    /**
     * 当SAX解析器解析到XML文档结束时，会调用的方法
     */
    @Override
    public void endDocument() {
        //QDLogger.i("解析结果：" + JSON.toJSON(targetInstance));
        if(mOnParseCompleteListener!=null){
            mOnParseCompleteListener.onComplete(mContext,getElementResult(),rootView);
        }
    }

    private Element getElementResult() {
       return getElement(currentElement);
    }
    private Element getElement(Element element) {
        Element parent = element.getParent();
        if(parent==null){
            return element;
        }else {
            return getElement(parent);
        }
    }

    boolean useLowerCase = false;//是否格式化为小写样式，针对name，attributes

    public void setUseLowerCase(boolean useLowerCase) {
        this.useLowerCase = useLowerCase;
    }

    Element parentElement;
    Element currentElement;
    /**
     * 当SAX解析器解析到某个元素开始时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void startElement(String uri, String name,
                             String qName, Attributes atts) {
        //QDLogger.i("开始解析元素: qName=" + qName + ",name=" + name);
        Element element = new Element();
        if(atts!=null){
            for(int i=0;i<atts.getLength();i++){
                String attsQName = atts.getQName(i);
                String value = atts.getValue(attsQName);
                //QDLogger.i("元素: attsQName=" + attsQName + ",value="+value);
                element.addProperty(useLowerCase?attsQName.toLowerCase():attsQName,value);
            }
        }
        element.setTag(useLowerCase?name.toLowerCase():name);

        if(currentElement!=null&&!currentElement.isParseEnd()){
            parentElement = currentElement;
        }

        if(parentElement==null){
            parentElement = element;
        }else {
            parentElement.addNode(element);
        }
        currentElement= element;
    }

    /**
     * 当SAX解析器解析到某个元素结束时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void endElement(String uri, String name, String qName) {
        //QDLogger.i("结束解析元素:" + name);
        //removeLastElement();

        currentElement.setParseEnd(true);
        if (currentElement.getParent() != null) {
            parentElement = currentElement.getParent();
            currentElement = parentElement;
        }else {
            rootElement = currentElement;
        }
    }


    /**
     * 当SAX解析器解析到某个属性值时，会调用的方法
     * 其中参数ch记录了这个属性值的内容
     */
    @Override
    public void characters(char ch[], int start, int length) {
        String content = new String(ch, start, length);
        //QDLogger.i(content);
    }
/*
    private void addElement(String name) {
        //获取当前元素
        Object currentElement = getCurrentElement();
        //1.得到Class对象
        Class c = currentElement.getClass();
        //2.返回字段的数组
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                field.setAccessible(true);
                field.getType();
                QDLogger.i(field.getName() + "," + field.getType());
                try {
                    field.set(currentElement, name);
                } catch (IllegalAccessException e) {
            QDLogger.e(e);
                }
            }
            QDLogger.i(field.getName());
        }
    }*/

    private T addField2(T targetObj, String name, String value) {
        //1.得到Class对象
        Class c = targetObj.getClass();
        //2.返回字段的数组
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                field.setAccessible(true);
                // QDLogger.i(field.getName() + "," + field.getType());
                try {
                    field.set(targetObj, value);
                } catch (IllegalAccessException e) {
                    QDLogger.e(e);
                }
            }
            // QDLogger.i(field.getName());
        }
        return targetObj;
    }
    /**
     * 添加属性
     *
     * @param name
     */
    private Object addField(Object targetObj, String name, String value) {
        //1.得到Class对象
        Class c = targetObj.getClass();
        //2.返回字段的数组
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                field.setAccessible(true);
               // QDLogger.i(field.getName() + "," + field.getType());
                try {
                    field.set(targetObj, value);
                } catch (IllegalAccessException e) {
                    QDLogger.e(e);
                }
            }
           // QDLogger.i(field.getName());
        }

        return targetObj;
    }

    private OnParseCompleteListener mOnParseCompleteListener;

    public void setOnParseCompleteListener(OnParseCompleteListener onParseCompleteListener) {
        this.mOnParseCompleteListener = onParseCompleteListener;
    }

    public static interface OnParseCompleteListener<T> {
        void onComplete(Context context, Element element, View rootView);
    }
}
