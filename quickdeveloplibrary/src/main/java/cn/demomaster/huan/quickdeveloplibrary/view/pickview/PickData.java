package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class PickData {
    String[] tags = new String[]{};
    Map<String, List<PickDataItem>> dataMap = new LinkedHashMap<>();
    int[] selectIndexs = new int[]{};

    public PickData() {
        this.tags = getTags();
        this.selectIndexs = getDefautIndex();
        this.dataMap.clear();
        for(String str :tags){
            dataMap.put(str,getDataByTag(str,selectIndexs));
        }
    }
    public List<PickDataItem> getDataByTag(String tag){
        int tagIndex =getTagIndex(tag);
        return dataMap.get(tag);
    }
    public abstract List<PickDataItem> getDataByTag(String tag,int[] selectIndexs);
    public abstract String[] getTags();
    public abstract int[] getDefautIndex();
    public int getDefautIndex(String tag){
        int tagIndex =getTagIndex(tag);
        return selectIndexs[tagIndex];
    }
    private int getTagIndex(String tag) {
        for(int i=0;i<tags.length;i++){
            String tagStr = tags[i];
            if(tagStr.equals(tag)){
                return i;
            }
        }
        return 0;
    }

    public static class PickDataItem{
        private String title;//显示名
        private String tag;//数据标签
        private Object data;//真实数据

        public PickDataItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
