package cn.demomaster.huan.quickdeveloplibrary.util.xml;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * @author squirrel桓
 * @date 2019/3/19.
 * description：
 */
public class QDSaxHandler<T> extends DefaultHandler {
    private Class<T> mClazz;
    private T targetInstance;
    private List<Object> elements;//当前元素
    //private Object currentElement;

    // Event handlers.
    public QDSaxHandler(Class<T> clazz,OnParseCompleteListener onParseCompleteListener) {
        super();
        mClazz = clazz;
        mOnParseCompleteListener = onParseCompleteListener;
        //QDLogger.i("xml加载目标类：" + clazz.getName());
    }

    /**
     * 当SAX解析器解析到XML文档开始时，会调用的方法
     */
    @Override
    public void startDocument() {
        elements = new ArrayList<>();
        //QDLogger.i("开始解析根元素实体类：" + mClazz.getName());
    }

    /**
     * 当SAX解析器解析到XML文档结束时，会调用的方法
     */
    @Override
    public void endDocument() {
        //QDLogger.i("解析结果：" + JSON.toJSON(targetInstance));
        if(mOnParseCompleteListener!=null){
            mOnParseCompleteListener.onComplete(targetInstance);
        }
    }

    /**
     * 当SAX解析器解析到某个元素开始时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void startElement(String uri, String name,
                             String qName, Attributes atts) {
        //QDLogger.i("开始解析元素: qName=" + qName + ",name=" + name);
        if (elements.size() == 0) {//根元素初始化
            try {
                targetInstance = mClazz.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            elements.add(targetInstance);
        } else {
            addElement(elements.get(elements.size() - 1), name, atts);
        }
    }


    /**
     * 当SAX解析器解析到某个元素结束时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void endElement(String uri, String name, String qName) {
        //QDLogger.i("结束解析元素:" + name);
        removeLastElement();
    }

    private void removeLastElement() {
        if (elements != null && elements.size() > 0) {
            int index = elements.size() - 1;
            //QDLogger.i("移除" + index + "元素:" + elements.get(index));
            elements.remove(index);
        }
    }

    /**
     * 当SAX解析器解析到某个属性值时，会调用的方法
     * 其中参数ch记录了这个属性值的内容
     */
    @Override
    public void characters(char ch[], int start, int length) {
        String content = new String(ch, start, length);
       // QDLogger.i(content);
    }


    /**
     * 添加element
     *
     * @param elementName
     */
    private void addElement(Object parentElement, String elementName, Attributes atts) {
        /****************   确定要添加元素类型生成实体类  *******************/
        Object newElement = elementName;
        //1.得到Class对象
        Class c = parentElement.getClass();
        //2.返回字段的数组
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
           // QDLogger.i(field.getName() + "<----->" + elementName);
            if (field.getName().endsWith(elementName)) {
                field.setAccessible(true);
                if (field.getType() == java.lang.String.class) {//属性:String
                    newElement = elementName;
                    try {
                        field.set(parentElement, elementName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (field.getType() == java.util.List.class) {//属性:list
                    // 如果是List类型，得到其Generic的类型
                    Type genericType = field.getGenericType();
                    if (genericType == null) continue;
                    // 如果是泛型参数的类型
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        //得到泛型里的class类型对象
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        //QDLogger.i(field.getName() + "的泛型：" + genericClazz);

                        Method m = null;
                        try {
                            m = (Method) parentElement.getClass().getMethod("get" + getMethodName(field.getName()));
                            List list = (List) m.invoke(parentElement);
                            if (list == null) list = new ArrayList();
                            newElement = genericClazz.newInstance();
                            for (int i = 0; atts != null && i < atts.getLength(); i++) {
                                String attName = atts.getQName(i);
                                String attValueString = atts.getValue(i);
                                addField(newElement, attName, attValueString);
                               // QDLogger.i(" " + attName + "=" + attValueString);
                            }
                            list.add(newElement);
                           // QDLogger.d(JSON.toJSON(list));
                            field.set(parentElement, list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            elements.add(newElement);
            //QDLogger.i(field.getName() + "类型：" + field.getType());
        }
    }

    private static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
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
                    e.printStackTrace();
                }
            }
            QDLogger.i(field.getName());
        }
    }*/

    /**
     * 添加属性
     *
     * @param name
     */
    private void addField(Object targetObj, String name, String value) {
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
                    e.printStackTrace();
                }
            }
           // QDLogger.i(field.getName());
        }
    }

    private OnParseCompleteListener mOnParseCompleteListener;

    public void setOnParseCompleteListener(OnParseCompleteListener onParseCompleteListener) {
        this.mOnParseCompleteListener = onParseCompleteListener;
    }

    public static interface OnParseCompleteListener<T> {
        void onComplete(T result);
    }
}
