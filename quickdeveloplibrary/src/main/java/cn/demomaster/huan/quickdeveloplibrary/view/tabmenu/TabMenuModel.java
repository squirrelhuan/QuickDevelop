package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.graphics.Color;
import android.view.View;

import java.util.List;

/**
 * @author squirrel桓
 * @date 2018/11/26.
 * description：
 */
public class TabMenuModel {

    private int selectCount = 1;//单选多选（及多选个数·默认唯1）
    private List<Integer> selectDeftData;//默认选项
    private String tabName;//Tab名称
    private String[] tabItems;//子内容列表
    private int columnCount = 1;//默认内容列表显示几列
    private int contentResId = -1;//自定义布局id
    private int colorSelect_content = Color.RED;
    private int colorNormal_content = Color.BLACK;
    private TabRadioGroup.TabRadioButton tabButtonView;
    private OnCreatTabContentView onCreatTabContentView;

    public TabMenuModel(String tabName, String[] tabItems, int selectCount, List<Integer> selectDeftData) {
        this.selectCount = selectCount;
        this.selectDeftData = selectDeftData;
        this.tabName = tabName;
        this.tabItems = tabItems;
    }

    public TabMenuModel(String tabName, String[] tabItems, List<Integer> selectDeftData) {
        this.selectDeftData = selectDeftData;
        this.tabName = tabName;
        this.tabItems = tabItems;
    }

    public TabMenuModel(String tabName, int contentResId, OnCreatTabContentView onCreatTabContentView) {
        this.tabName = tabName;
        this.contentResId = contentResId;
        this.onCreatTabContentView = onCreatTabContentView;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public List<Integer> getSelectDeftData() {
        return selectDeftData;
    }

    public void setSelectDeftData(List<Integer> selectDeftData) {
        this.selectDeftData = selectDeftData;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String[] getTabItems() {
        return tabItems;
    }

    public void setTabItems(String[] tabItems) {
        this.tabItems = tabItems;
    }

    public int getColorSelect_content() {
        return colorSelect_content;
    }

    public void setColorSelect_content(int colorSelect_content) {
        this.colorSelect_content = colorSelect_content;
    }

    public int getColorNormal_content() {
        return colorNormal_content;
    }

    public void setColorNormal_content(int colorNormal_content) {
        this.colorNormal_content = colorNormal_content;
    }

    public void setColorContent(int colorSelect_content, int colorNormal_content) {
        this.colorSelect_content = colorSelect_content;
        this.colorNormal_content = colorNormal_content;
    }

    public TabRadioGroup.TabRadioButton getTabButtonView() {
        return tabButtonView;
    }

    public void setTabButtonView(TabRadioGroup.TabRadioButton tabButtonView) {
        this.tabButtonView = tabButtonView;
    }

    public int getColumnCount() {
        if (contentResId != -1 && tabItems == null) {
            return -1;
        }
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getContentResId() {
        return contentResId;
    }

    public void setContentResId(int contentResId) {
        this.contentResId = contentResId;
    }

    public OnCreatTabContentView getOnCreatTabContentView() {
        return onCreatTabContentView;
    }

    public void setOnCreatTabContentView(OnCreatTabContentView onCreatTabContentView) {
        this.onCreatTabContentView = onCreatTabContentView;
    }

    public static interface OnCreatTabContentView {
        void onCreat(View root);
    }
}
