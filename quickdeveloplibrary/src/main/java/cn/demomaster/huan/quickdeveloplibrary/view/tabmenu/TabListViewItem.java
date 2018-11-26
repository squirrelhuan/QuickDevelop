package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

/**
 * Created by Squirrel桓 on 2018/11/24.
 */
public class TabListViewItem {

    private boolean isSelected;
    private String itemName;
    private int position;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
