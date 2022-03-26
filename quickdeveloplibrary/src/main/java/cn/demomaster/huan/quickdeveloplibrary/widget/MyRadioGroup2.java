package cn.demomaster.huan.quickdeveloplibrary.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatRadioButton;

import cn.demomaster.qdlogger_library.QDLogger;


public class MyRadioGroup2 extends FrameLayout {

    public MyRadioGroup2(@NonNull Context context) {
        super(context);
    }

    public MyRadioGroup2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioGroup2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyRadioGroup2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        QDLogger.i("onFinishInflate c="+getChildCount());
        setChildListener(this);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        QDLogger.i("onViewAdded c="+getChildCount());
        setChildListener(this);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        return super.addViewInLayout(child, index, params);
    }

    private void setChildListener(ViewGroup pView) {
        int count = pView.getChildCount();
        for(int i=0;i<count;i++){
            Object child = pView.getChildAt(i);
            if(child instanceof AppCompatRadioButton){
                if(((AppCompatRadioButton) child).getId()==-1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        ((AppCompatRadioButton) child).setId(View.generateViewId());
                    }
                }
                ((AppCompatRadioButton) child).setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked){
                        setChildState(MyRadioGroup2.this,buttonView.getId());
                    }
                });
            }else if(child instanceof ViewGroup){
                setChildListener((ViewGroup) child);
            }
        }
    }

    /**
     * 恢复其他child状态
     * @param pView
     * @param selectId
     */
    private void setChildState(ViewGroup pView, int selectId) {
        int count = pView.getChildCount();
        for(int i=0;i<count;i++){
            Object child = pView.getChildAt(i);
            //QDLogger.i("setChildState id="+((View)child).getId()+",sid="+selectId+",child="+child);
            if(child instanceof AppCompatRadioButton && ((AppCompatRadioButton) child).getId() != selectId){
                ((AppCompatRadioButton) child).setChecked(false);
            }else if(child instanceof ViewGroup){
                //QDLogger.e("setChildState ViewGroup");
                setChildState((ViewGroup) child,selectId);
            }
        }
    }
}
