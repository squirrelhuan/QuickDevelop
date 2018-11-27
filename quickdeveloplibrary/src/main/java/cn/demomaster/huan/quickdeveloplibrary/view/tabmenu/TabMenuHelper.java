package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Squirrel桓 on 2018/11/28.
 */
public class TabMenuHelper {

    private List<View> tabButtons;
    private ViewGroup tabGroup;
    public void setOnTabChangeListener(final OnTabCheckedChangeListener onCheckedChangeListener) {
        for (int i = 0; i < tabButtons.size(); i++) {
            View view = tabButtons.get(i);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCheckedChangeListener.onCheckedChanged(view, (int) (view.getTag()));
                }
            });
        }
    }


    public interface OnTabCheckedChangeListener {
        void onCheckedChanged(View var1, int var2);
    }
}
