package cn.demomaster.huan.quickdevelop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.widget.AutoCenterHorizontalScrollView;

/**
 * Created by Squirrel桓 on 2018/12/15.
 */
public class HorizontalAdapter implements AutoCenterHorizontalScrollView.HAdapter {
    List<String> names = new ArrayList<>();
    private Context context;

    public HorizontalAdapter(Context context, List<String> names) {
        this.names = names;
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public View getItemView(int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_warp2, null, false);
        ((TextView) v.findViewById(R.id.tv_tab_name)).setText(names.get(i));
        return v;
    }
}
