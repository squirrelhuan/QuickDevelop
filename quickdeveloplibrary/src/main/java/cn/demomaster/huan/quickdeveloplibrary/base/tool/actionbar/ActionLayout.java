package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

public class ActionLayout extends FrameLayout {
    public ActionLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ActionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ActionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        initChild();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        initChild();
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        initChild();
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        initChild();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        initChild();
    }

    int childPaddingTop;
    private void initChild() {
        /*if (getChildCount() ==1) {
            View view = getChildAt(0);
            childPaddingTop = view.getPaddingTop();
            setBackground(view.getBackground());
            //resetPadding();
        }*/
        init();
    }

    private int statusHeight = DisplayUtil.getStatusBarHeight(getContext());
    private ImageTextView it_actionbar_title;
    private ImageTextView it_actionbar_common_left;
    private ImageTextView it_actionbar_common_right;

    private void init() {
        it_actionbar_title = findViewById(R.id.it_actionbar_common_title);
        it_actionbar_common_left = findViewById(R.id.it_actionbar_common_left);
        it_actionbar_common_right = findViewById(R.id.it_actionbar_common_right);

        if (getContext() instanceof AppCompatActivity && ((AppCompatActivity) getContext()).getTitle() != null) {
            setTitle(((AppCompatActivity) getContext()).getTitle().toString());
        }

        if (it_actionbar_common_left != null) {
            //it_actionbar_common_left.setOnClickListener(leftOnClickListener);
            if (leftOnClickListener != null) {
                it_actionbar_common_left.setOnClickListener(leftOnClickListener);
            }
        }

        if (it_actionbar_common_right != null) {
            if (rightOnClickListener != null) {
                it_actionbar_common_right.setOnClickListener(rightOnClickListener);
            } else {
                it_actionbar_common_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getContext() instanceof AppCompatActivity) {
                            ScreenShotUitl.shot(((AppCompatActivity) getContext()));
                        }
                    }
                });
            }
        }
    }

   /* private void resetPadding() {
        if (getChildCount() > 0) {
            View view = getChildAt(0);
            QDLogger.e(getContext(),"hasStatusBar="+hasStatusBar+",childPaddingTop="+childPaddingTop);
            view.setPadding(view.getPaddingLeft(), (hasStatusBar ? statusHeight : 0), view.getPaddingRight(), view.getPaddingBottom());
            *//*LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int height = layoutParams.height;
            if(height!=ViewGroup.LayoutParams.MATCH_PARENT&&height!=ViewGroup.LayoutParams.WRAP_CONTENT){
               QdToast.show(getContext(),layoutParams.height +"", Toast.LENGTH_LONG);
            }*//*
        }
        //setPadding(0, statusHeight, 0, 0);
    }*/

    public boolean hasStatusBar = true;//
    public void setHasStatusBar(boolean hasStatusBar) {
        this.hasStatusBar = hasStatusBar;
        if(getChildCount()>0){
            View view = getChildAt(0);
            if(hasStatusBar) {
                view.setPadding(view.getPaddingLeft(), statusHeight, view.getPaddingRight(), view.getPaddingBottom());
            }else {
                view.setPadding(view.getPaddingLeft(), 0 , view.getPaddingRight(), view.getPaddingBottom());
            }
        }
    }

    private View.OnClickListener rightOnClickListener;
    private View.OnClickListener leftOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getParent() instanceof ActionBar) {
                ((ActionBar) getParent()).onClickBack();
            }
        }
    };

    public void setLeftOnClickListener(OnClickListener leftOnClickListener) {
        this.leftOnClickListener = leftOnClickListener;
        if (it_actionbar_common_left != null)
            it_actionbar_common_left.setOnClickListener(leftOnClickListener);
    }

    public void setRightOnClickListener(OnClickListener rightOnClickListener) {
        this.rightOnClickListener = rightOnClickListener;
        if (it_actionbar_common_right != null)
            it_actionbar_common_right.setOnClickListener(rightOnClickListener);
    }

    public void setTitle(CharSequence title) {
        if (it_actionbar_title != null) {
            it_actionbar_title.setText(title==null?"":title.toString());
        }
    }

    public View getTitleView(){
        return findViewById(R.id.it_actionbar_common_title);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ((ActionBar) getParent()).switchActivonBarType();
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (getChildCount() > 0) {
            getChildAt(0).setBackgroundColor(color);
        }
    }
}
