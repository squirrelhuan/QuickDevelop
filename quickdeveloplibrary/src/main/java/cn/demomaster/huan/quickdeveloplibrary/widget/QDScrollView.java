package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

import java.util.AbstractList;
import java.util.ArrayList;

public class QDScrollView extends ScrollView {
    public QDScrollView(Context context) {
        super(context);
        init();
    }

    public QDScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QDScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public QDScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private View convertView;
    //获取初始控件
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //由于是scrollview 内部只能有一个控件
        convertView = getChildAt(0);
    }

    Rect originalRect = new Rect();
    //记录初始位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //用rect记录 scrollview的子控件的上下左右
        originalRect.set(convertView.getLeft(), convertView.getTop(), convertView.getRight(), convertView.getBottom());
    }

  //  事件分发
    float startY,startX;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                startX = (int) ev.getX();
                break;

            case MotionEvent.ACTION_UP:
                //还原位置，回弹动画， 可以自己定于需要的动画
                TranslateAnimation animation = new TranslateAnimation(0, 0, convertView.getTop(), originalRect.top);
                animation.setDuration(200);
                convertView.setAnimation(animation);
                convertView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);

                break;

            case MotionEvent.ACTION_MOVE:

                int detalY = (int) (ev.getY() - startY);
                int detalX = (int) (ev.getX() - startX);

                if (Math.abs(detalX) < Math.abs(detalY)) {
                    //detalY 乘以0.2 使得很难的效果
                    convertView.layout(originalRect.left, (int) (originalRect.top + detalY * 0.2),
                            originalRect.right, (int) (originalRect.bottom + detalY * 0.2));
                }
                break;

        }

        return super.dispatchTouchEvent(ev);
    }

}
