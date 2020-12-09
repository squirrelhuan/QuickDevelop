package cn.demomaster.huan.quickdeveloplibrary.util.xml;

import com.alibaba.fastjson.annotation.JSONField;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

public class NodeElement {
    private String nodeName;
    private String uri;
    private String qName;
    private List<NodeProperty> attributes;
    private List<NodeElement> childNodes;

    @JSONField(serialize = false)
    boolean parseEnd;//解析完成标志

    public NodeElement(String uri, String localName, String qName, Attributes attributes) {
        this.uri = uri;
        this.nodeName = localName;
        this.qName = qName;
        this.attributes = new ArrayList<>();
        this.childNodes = new ArrayList<>();
        if(attributes!=null){
            for(int i=0;i<attributes.getLength();i++){
                String attsQName = attributes.getQName(i);
                String value = attributes.getValue(attsQName);
                String type = attributes.getType(attsQName);
                //QDLogger.i("元素: attsQName=" + attsQName + ",value="+value+",type="+type);
                addProperty(attsQName,value);
            }
        }
    }

    public void addNode(NodeElement node){
        childNodes.add(node);
    }
    public void addProperty(String attrName, String attrValue) {
        attributes.add(new NodeProperty(attrName,attrValue));
    }
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public boolean isParseEnd() {
        return parseEnd;
    }

    public void setParseEnd(boolean parseEnd) {
        this.parseEnd = parseEnd;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getqName() {
        return qName;
    }

    public void setqName(String qName) {
        this.qName = qName;
    }

    public List<NodeProperty> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<NodeProperty> attributes) {
        this.attributes = attributes;
    }

    public List<NodeElement> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<NodeElement> childNodes) {
        this.childNodes = childNodes;
    }

    public static class NodeProperty{
        String name;
        String value;
        String type;

        public NodeProperty(String nodeName, String nodeValue) {
            this.name = nodeName;
            this.value = nodeValue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
