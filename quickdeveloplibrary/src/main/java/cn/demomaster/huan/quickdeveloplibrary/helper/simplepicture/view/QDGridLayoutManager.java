package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @author squirrel桓
 * @date 2019/3/26.
 * description：
 */
public class QDGridLayoutManager extends GridLayoutManager {

    public QDGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public QDGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public QDGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }
}
