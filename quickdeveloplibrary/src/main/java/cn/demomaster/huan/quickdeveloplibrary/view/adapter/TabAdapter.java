
package cn.demomaster.huan.quickdeveloplibrary.view.adapter;

import android.view.View;
import android.view.ViewGroup;

public interface TabAdapter {
    View getView(int position, ViewGroup viewGroup);//

    int getViewCount();

    boolean onSelectedChange(int position, View view, boolean isSelected);//选中回调
}
