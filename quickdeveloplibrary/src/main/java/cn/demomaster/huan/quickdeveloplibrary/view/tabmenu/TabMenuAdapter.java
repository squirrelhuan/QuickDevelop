package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;

/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class TabMenuAdapter extends BaseAdapter {
    private List<OptionsMenu.Menu> lists = new ArrayList();
    private Context context;
    private LayoutInflater inflater;

    public TabMenuAdapter(Context context, List<OptionsMenu.Menu> lists) {
        this.context = context;
        this.lists = lists;
        this.inflater = ((Activity)context).getLayoutInflater();
    }

    public int getCount() {
        return this.lists.size();
    }

    public Object getItem(int position) {
        return this.lists.get(position);
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_tab_menu, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_title.setText(((OptionsMenu.Menu)this.lists.get(position)).getTitle());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;

        private ViewHolder() {
        }
    }

}
