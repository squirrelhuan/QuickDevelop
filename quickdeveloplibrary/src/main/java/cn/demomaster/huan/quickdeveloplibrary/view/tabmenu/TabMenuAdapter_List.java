package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;

import static cn.demomaster.huan.quickdeveloplibrary.ApplicationParent.TAG;

/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class TabMenuAdapter_List extends BaseAdapter {
    private List<TabListViewItem> tabListViewItems = new ArrayList();
    private List<TabMenuModel> tabMenuModels;
    private int tabIndex;
    private int selectCount = 1;
    private Context context;
    private LayoutInflater inflater;
    private boolean isSingle;//单个tab中的状态集合（多选则为list,单选则为int）

    public TabMenuAdapter_List(Context context, List<TabMenuModel> tabMenuModels, int tabIndex) {
        this.context = context;
        this.tabIndex = tabIndex;
        this.tabMenuModels = tabMenuModels;
        TabMenuModel tabMenuModel = tabMenuModels.get(tabIndex);
        //判断单选还是多选
        selectCount = tabMenuModel.getSelectCount();
        if (selectCount == 1) {
            isSingle = true;
        } else {
            isSingle = false;
        }

        //遍历循环初始化viewData
        tabListViewItems.clear();
        for (int i = 0; i < tabMenuModel.getTabItems().length; i++) {
            TabListViewItem item = new TabListViewItem();
            item.setPosition(i);
            item.setItemName(tabMenuModel.getTabItems()[i]);
            if (selectCount < tabMenuModel.getSelectDeftData().size()) {
                Log.e(TAG, "默认选中个数不能超过最大个数");
                return;
            }
            if (tabMenuModel.getSelectDeftData() != null ) {
                for (int j=0 ;j< tabMenuModel.getSelectDeftData().size();j++) {
                    if (tabMenuModel.getSelectDeftData().get(j)!=null&&item.getPosition() == tabMenuModel.getSelectDeftData().get(j)) {
                        item.setSelected(true);
                    }
                }
            }
            tabListViewItems.add(item);
        }

        this.inflater = ((Activity) context).getLayoutInflater();
    }

    public int getCount() {
        return this.tabListViewItems.size();
    }

    public Object getItem(int position) {
        return this.tabListViewItems.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_tab_menu, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (tabListViewItems.get(position).isSelected()) {
            holder.tv_title.setTextColor(Color.RED);
        } else {
            holder.tv_title.setTextColor(Color.BLACK);
        }
        holder.tv_title.setText(((TabListViewItem) this.tabListViewItems.get(position)).getItemName());
        return convertView;
    }

    public void setOnItemClicked(int position) {
        boolean b = !tabListViewItems.get(position).isSelected();
        TabMenuModel tabMenuModel = tabMenuModels.get(tabIndex);
        List<Integer> current = tabMenuModel.getSelectDeftData();
        if (Arrays.asList(current).contains(position)) {//如果存在则remove
            current.remove((Object)position);
        } else {//如果不存在
            if (current.size() + 1 > tabMenuModel.getSelectCount()) {//判断个数是否超出，没超出则追加，超出则remove第一个
                tabListViewItems.get(current.get(0)).setSelected(false);
                int c = current.size();
                current.remove(0);
                current.add(position);
            } else {
                current.add(position);
            }
        }
        tabMenuModel.setSelectDeftData(current);
        tabMenuModels.set(tabIndex, tabMenuModel);
        tabListViewItems.get(position).setSelected(b);
        notifyDataSetChanged();
    }

    Integer[] reSort(Integer[] arr) {
        Integer[] tmp = new Integer[arr.length];
        for (int i = 0; i < arr.length - 1; i++) {
            tmp[i] = arr[i + 1];
        }
        return tmp;
    }

    Integer[] removeSort(Integer[] arr, int index) {
        if (arr.length == 1) {
            return new Integer[selectCount];
        }
        Integer[] tmp = arr.clone();
        for (int i = index; i < arr.length - 1; i++) {
            tmp[i] = arr[i + 1];
        }
        return tmp;
    }


    public List<TabMenuModel> getTabMenuModels() {
        return tabMenuModels;
    }

    private class ViewHolder {
        TextView tv_title;

        private ViewHolder() {
        }
    }

}