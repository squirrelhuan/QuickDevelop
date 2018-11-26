package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;

/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class TabMenuAdapter extends BaseAdapter {
    private List<TabListViewItem> lists = new ArrayList();
    private Context context;
    private LayoutInflater inflater;
    private List<Integer> selected_arr;
    private int selected_index;
    private boolean isSingle;

    public TabMenuAdapter(Context context, List<TabListViewItem> lists, Object selected_arr) {
        this.context = context;
        if (!(selected_arr instanceof List)) {
            isSingle = true;
        }
        if (!isSingle) {
            for (int i = 0; i < lists.size(); i++) {
                TabListViewItem item = lists.get(i);
                item.setPosition(i);
                for (int j : (List<Integer>) selected_arr) {
                    if (item.getPosition() == j) {
                        item.setSelected(true);
                    }
                }
            }
            this.selected_arr = (List<Integer>) selected_arr;
        } else {
            selected_index = (int) selected_arr;
            for (int i = 0; i < lists.size(); i++) {
                TabListViewItem item = lists.get(i);
                item.setPosition(i);
                if (item.getPosition() == selected_index) {
                    item.setSelected(true);
                }
            }
        }

        this.lists = lists;
        this.inflater = ((Activity) context).

                getLayoutInflater();
    }

    public int getCount() {
        return this.lists.size();
    }

    public Object getItem(int position) {
        return this.lists.get(position);
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

        if (lists.get(position).isSelected()) {
            holder.tv_title.setTextColor(Color.RED);
        } else {
            holder.tv_title.setTextColor(Color.BLACK);
        }
        holder.tv_title.setText(((TabListViewItem) this.lists.get(position)).getItemName());
        return convertView;
    }

    public void setOnItemClicked(int position) {
        if (!isSingle) {
            lists.get(position).setSelected(!lists.get(position).isSelected());
            if (lists.get(position).isSelected()) {
                if (!selected_arr.contains(position)) {
                    selected_arr.add(position);
                }
            } else {
                if (selected_arr.contains(position)) {
                    selected_arr.remove((Object) position);
                }
            }
            for (int i = 0; i < lists.size(); i++) {
                TabListViewItem item = lists.get(i);
                item.setPosition(i);
                for (int j : selected_arr) {
                    if (item.getPosition() == j) {
                        item.setSelected(true);
                    }
                }
            }
        } else {
            for (int i = 0; i < lists.size(); i++) {
                TabListViewItem item = lists.get(i);
                item.setPosition(i);
                item.setSelected(false);
            }
            selected_index = position;
            lists.get(position).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public Object getSelectedList() {
        if (!isSingle) {
            List<Integer> a = new ArrayList<>();
            for (TabListViewItem item : lists) {
                if (item.isSelected()) {
                    a.add(item.getPosition());
                }
            }
            return a;
        } else {
            return selected_index;
        }

    }

    private class ViewHolder {
        TextView tv_title;

        private ViewHolder() {
        }
    }

}
