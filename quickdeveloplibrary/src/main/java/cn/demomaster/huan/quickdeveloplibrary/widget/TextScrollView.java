package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

public class TextScrollView extends ListView {
    public TextScrollView(Context context) {
        super(context);
        init();
    }

    public TextScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    setLineNum(getFirstVisiblePosition());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void setLineNum(int firstVisiblePosition) {
        smoothScrollToPosition(firstVisiblePosition);
    }

    boolean singleLine = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);*/
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        /*QDLogger.println("widthMode="+widthMode+",heightMode="+heightMode+" ("+widthSize+","+heightSize+")");
        switch (heightMode) {
            case MeasureSpec.AT_MOST://AT_MOST 自适应模式，根据设置的行数动态申请高度
                QDLogger.println("MeasureSpec.AT_MOST="+MeasureSpec.AT_MOST);
                break;
            case MeasureSpec.UNSPECIFIED:
                QDLogger.println("MeasureSpec.UNSPECIFIED="+MeasureSpec.UNSPECIFIED);
                break;
            case MeasureSpec.EXACTLY:
                QDLogger.println("MeasureSpec.EXACTLY="+MeasureSpec.EXACTLY);
                break;
        }*/
        //当listview嵌套在scrollview内时效果 MeasureSpec.UNSPECIFIED=0
        //当listview不被嵌套时 MeasureSpec.AT_MOST 自适应模式，根据设置的行数动态申请高度
        if (singleLine) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize,
                    View.MeasureSpec.UNSPECIFIED);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        QDLogger.println("onScrollChanged oldt = " + oldt + ",top = " + t);
    }

    List<String> stringlist;
    Handler handler = new Handler();

    public void setTextArray(String[] textArray, int simple_note_list_item_1) {
        if(textArray!=null&&textArray.length>0) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), simple_note_list_item_1
                    , textArray);//android.R.layout.simple_list_item_1
            setAdapter(arrayAdapter);
            startScrollTask();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int lineIndex = getFirstVisiblePosition();
            lineIndex = lineIndex + 1;
            if (getAdapter() != null && getAdapter().getCount() != 0) {
                int index = lineIndex % getAdapter().getCount();
                smoothScrollToPosition(index);
                startScrollTask();
            }
        }
    };

    private void startScrollTask() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);
    }
}
