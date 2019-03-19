package cn.demomaster.huan.quickdeveloplibrary.util.xml;

import android.content.res.AssetManager;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * @author squirrel桓
 * @date 2019/3/19.
 * description：
 */
public class QDSaxHandler extends DefaultHandler {
    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    public QDSaxHandler()
    {
        super();
        QDLogger.i("start.....QDSaxHandler");
    }

    /**
     * 当SAX解析器解析到XML文档开始时，会调用的方法
     */
    @Override
    public void startDocument ()
    {
        QDLogger.i("Start document");
    }
    /**
     * 当SAX解析器解析到XML文档结束时，会调用的方法
     */
    @Override
    public void endDocument ()
    {
        QDLogger.i("End document");
    }

    /**
     * 当SAX解析器解析到某个元素开始时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void startElement (String uri, String name,
                              String qName, Attributes atts)
    {
        if ("".equals (uri))
            QDLogger.i("Start element: " + qName);
        else
            QDLogger.i("Start element: {" + uri + "}" + name);
    }
    /**
     * 当SAX解析器解析到某个元素结束时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void endElement (String uri, String name, String qName)
    {
        if ("".equals (uri))
            QDLogger.i("End element: " + qName);
        else
            QDLogger.i("End element:   {" + uri + "}" + name);
    }
    /**
     * 当SAX解析器解析到某个属性值时，会调用的方法
     * 其中参数ch记录了这个属性值的内容
     */
    @Override
    public void characters (char ch[], int start, int length)
    {
        QDLogger.i("Characters:    \"");
        for (int i = start; i < start + length; i++) {
            switch (ch[i]) {
                case '\\':
                    QDLogger.i("\\\\");
                    break;
                case '"':
                    QDLogger.i("\\\"");
                    break;
                case '\n':
                    QDLogger.i("\\n");
                    break;
                case '\r':
                    QDLogger.i("\\r");
                    break;
                case '\t':
                    QDLogger.i("\\t");
                    break;
                default:
                    QDLogger.i(ch[i]);
                    break;
            }
        }
        QDLogger.i("\"\n");
    }
}
