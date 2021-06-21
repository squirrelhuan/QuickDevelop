package cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Element {
    private String tag;
    private String id;
    private String cssClass;
    private String android_view_class;
    private List<Element> nodes;
    private Map<String, Object> propertyMap;

    public Element() {
        this.propertyMap = new HashMap<>();
    }

    public void addProperty(String key, Object object) {
        propertyMap.put(key, object);
    }

    public Object getProperty(String key, Object object) {
        return propertyMap.get(key);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getAndroid_view_class() {
        return android_view_class;
    }

    public void setAndroid_view_class(String android_view_class) {
        this.android_view_class = android_view_class;
    }

    public List<Element> getNodes() {
        return nodes;
    }

    public void setNodes(List<Element> nodes) {
        this.nodes = nodes;
    }

    public void addNode(Element node) {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        node.setParent(this);
        nodes.add(node);
    }

    Element parent;

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public Element getParent() {
        return parent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        if (propertyMap.containsKey("text")) {
            return (String) propertyMap.get("text");
        }
        return "";
    }

    public String getSrc() {
        if (propertyMap.containsKey("src")) {
            return (String) propertyMap.get("src");
        }
        return "";
    }

    public float getWidth() {
        float width = ViewGroup.LayoutParams.WRAP_CONTENT;
        String widthStr = "" + width;
        if (propertyMap.containsKey("width")) {
            widthStr = (String) propertyMap.get("width");
        }
        if (!TextUtils.isEmpty(widthStr)) {
            if (widthStr.endsWith("px")) {
                width = Integer.valueOf(widthStr.replace("px", ""));
            } else if (widthStr.endsWith("dp")) {
                width = Integer.valueOf(widthStr.replace("dp", ""));
            } else if (widthStr.contains("%") || widthStr.contains(".") || widthStr.contains("f")) {
                widthStr = widthStr.replace("%", "");
                widthStr = widthStr.replace("f", "");
                width = Float.valueOf(widthStr);
            } else {
                try {
                    width = Integer.valueOf(widthStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return width;
    }

    public float getHeight() {
        float height = ViewGroup.LayoutParams.WRAP_CONTENT;
        String heightStr = "" + height;
        if (propertyMap.containsKey("height")) {
            heightStr = (String) propertyMap.get("height");
        }
        if (!TextUtils.isEmpty(heightStr)) {
            if (heightStr.endsWith("px")) {
                height = Integer.valueOf(heightStr.replace("px", ""));
            } else if (heightStr.endsWith("dp")) {
                height = Integer.valueOf(heightStr.replace("dp", ""));
            } else if (heightStr.contains("%") || heightStr.contains(".") || heightStr.contains("f")) {
                heightStr = heightStr.replace("%", "");
                heightStr = heightStr.replace("f", "");
                height = Float.valueOf(heightStr);
            } else {
                try {
                    height = Integer.valueOf(heightStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return height;
    }

    public int getOrientation() {
        int orientation = LinearLayout.HORIZONTAL;
        if (propertyMap.containsKey("orientation")) {
            String orientationStr = (String) propertyMap.get("orientation");
            if (orientationStr.equals("vertical")) {
                orientation = LinearLayout.VERTICAL;
            } else if (orientationStr.equals("horizontal")) {
                orientation = LinearLayout.HORIZONTAL;
            }
        }
        return orientation;
    }

    boolean parseEnd;//解析完成

    public boolean isParseEnd() {
        return parseEnd;
    }

    public void setParseEnd(boolean parseEnd) {
        this.parseEnd = parseEnd;
    }

    public String getOnClickMethodStr() {
        if (propertyMap.containsKey("onclick")) {
            return (String) propertyMap.get("onclick");
        }
        return null;
    }

    public String getAttribute(String attributeName) {
        if (TextUtils.isEmpty(attributeName)) {
            return null;
        }
        if (propertyMap.containsKey(attributeName.toLowerCase())) {
            return (String) propertyMap.get(attributeName.toLowerCase());
        }
        return null;
    }
}
