package cn.demomaster.huan.quickdeveloplibrary.util.xml;

import android.content.Context;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author squirrel桓
 * @date 2019/3/19.
 * description：
 */
public class DomSaxHandler<T> extends DefaultHandler {
    Context mContext;

    public DomSaxHandler(Context context) {
        super();
        this.mContext = context;
    }

    public DomSaxHandler(Context context, OnParseCompleteListener onParseCompleteListener) {
        super();
        this.mOnParseCompleteListener = onParseCompleteListener;
        this.mContext = context;
    }

    NodeElement rootElement;
    List<NodeElement> nodeElementList;

    public NodeElement getRootElement() {
        return rootElement;
    }

    /**
     * 当SAX解析器解析到XML文档开始时，会调用的方法
     */
    @Override
    public void startDocument() {
        nodeElementList = new ArrayList<>();
    }

    /**
     * 当SAX解析器解析到XML文档结束时，会调用的方法
     */
    @Override
    public void endDocument() {
        nodeElementList.clear();
        if (mOnParseCompleteListener != null) {
            mOnParseCompleteListener.onComplete(mContext, rootElement);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //QDLogger.i("解析元素:uri="+uri+",localName="+localName+",qName=" + qName + ",attributes=" + JSON.toJSONString(attributes));
        NodeElement element = new NodeElement(uri, localName, qName, attributes);
        if (nodeElementList.size() == 0) {
            rootElement = element;
        }
        nodeElementList.add(element);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (nodeElementList.size() > 1) {
            NodeElement parent = nodeElementList.get(nodeElementList.size() - 2);
            NodeElement child = nodeElementList.get(nodeElementList.size() - 1);
            parent.addNode(child);
            nodeElementList.remove(nodeElementList.size() - 1);
        }
    }

    /**
     * 当SAX解析器解析到某个属性值时，会调用的方法
     * 其中参数ch记录了这个属性值的内容
     */
    @Override
    public void characters(char ch[], int start, int length) {
        String content = new String(ch, start, length);
        //QDLogger.i("属性值===>"+ content);
    }

    private OnParseCompleteListener mOnParseCompleteListener;

    public void setOnParseCompleteListener(OnParseCompleteListener onParseCompleteListener) {
        this.mOnParseCompleteListener = onParseCompleteListener;
    }

    public static interface OnParseCompleteListener<T> {
        void onComplete(Context context, NodeElement nodeElements);
    }

}
