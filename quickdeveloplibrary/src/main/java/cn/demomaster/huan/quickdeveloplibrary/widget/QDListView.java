package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

public class QDListView extends ListView {
    public QDListView(Context context) {
        super(context);
    }

    public QDListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QDListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public QDListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //弹性效果,maxOverScrollY最大滚出距离默认0
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY,
                scrollX, scrollY,
                scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY,
                isTouchEvent);
    }
}
