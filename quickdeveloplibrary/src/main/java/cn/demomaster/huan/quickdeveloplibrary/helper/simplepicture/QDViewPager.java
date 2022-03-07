package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class QDViewPager extends ViewPager {
    public QDViewPager(@NonNull Context context) {
        super(context);
    }

    public QDViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        handAttributeSet(attrs);
    }
    boolean canScroll = true;
    private void handAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QDViewPager);
            canScroll = a.getBoolean(R.styleable.QDViewPager_canScroll, true);
            //hide_with_child = a.getBoolean(R.styleable.VisibleLayout_hide_with_child, hide_with_child);
            a.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(canScroll) {
            try {
                return super.onTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(canScroll) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
