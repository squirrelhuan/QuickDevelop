package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

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

/**
 * Created by Squirrel桓 on 2018/11/11.
 */
public class OptionsMenuAdapter extends BaseAdapter {

    private List<OptionsMenu.Menu> lists=new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    public OptionsMenuAdapter(Context context, List<OptionsMenu.Menu> lists) {
        this.context = context;
        this.lists = lists;
        inflater = ((Activity)context).getLayoutInflater();
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null) {
            convertView = inflater.inflate(R.layout.item_option_menu, parent, false); //加载布局
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(lists.get(position).getTitle());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;
    }

}

