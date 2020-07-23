package cn.demomaster.huan.quickdeveloplibrary.util.xml;

import android.content.Context;
import android.content.res.AssetManager;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * @author squirrel桓
 * @date 2019/3/19.
 * description：
 */
public class QDSaxXml {

    public static void parseXmlFilePath(String path){

    }
    public static void parseXmlFile(File file){

    }
    public static void parseXmlString(String txt){

    }

    // 泛型方法 printArray
    public static < E > void printArray( E[] inputArray )
    {
        // 输出数组元素
        for ( E element : inputArray ){
           System.out.printf( "%s ", element );
        }
    }

    public static < T > void parseXmlAssets(Context context, String xmlPath, Class<T> clazz, QDSaxHandler.OnParseCompleteListener onParseCompleteListener){
        XMLReader xr = null;
        try {
            //使用工厂方法初始化SAXParserFactory变量spf
            SAXParserFactory spf = SAXParserFactory.newInstance();
            //通过SAXParserFactory得到SAXParser的实例
            SAXParser sp = spf.newSAXParser();
            //通过SAXParser得到XMLReader的实例
            xr = sp.getXMLReader();

            QDSaxHandler handler = new QDSaxHandler(clazz,onParseCompleteListener);
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);


            //获取AssetManager管理器对象
            AssetManager as = context.getAssets();
            //通过AssetManager的open方法获取到beauties.xml文件的输入流
            InputStream is = as.open(xmlPath);
            //通过获取到的InputStream来得到InputSource实例
            InputSource is2 = new InputSource(is);
            xr.parse(is2);
        } catch (SAXException e) {
            QDLogger.e(e);
        } catch (IOException e) {
            QDLogger.e(e);
        } catch (ParserConfigurationException e) {
            QDLogger.e(e);
        }
    }
}
