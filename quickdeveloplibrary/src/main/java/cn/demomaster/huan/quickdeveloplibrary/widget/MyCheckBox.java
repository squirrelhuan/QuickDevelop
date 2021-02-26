package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import org.jetbrains.annotations.NotNull;

import cn.demomaster.qdlogger_library.QDLogger;

public class MyCheckBox extends AppCompatImageView {
    public MyCheckBox(@NonNull @NotNull Context context) {
        super(context);
        init();
    }

    public MyCheckBox(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCheckBox(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(null);
    }

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    boolean mChecked;
    public void setChecked(boolean checked) {
        mChecked = checked;
       // postInvalidate();
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
        }
    }

    public void setCheckedState(boolean checked) {
        mChecked = checked;
        if (getDrawable() != null) {
            refreshDrawableState();
            // resizeFromDrawable();
        }
    }

    private int[] mState = null;
    private boolean mMergeState = false;
    private Drawable mDrawable = null;

    public void setImageState(int[] state, boolean merge) {
        mState = state;
        mMergeState = merge;
        if (mDrawable != null) {
            refreshDrawableState();
            // resizeFromDrawable();
        }
    }

    @Override
    public void setImageDrawable(@Nullable @org.jetbrains.annotations.Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        mDrawable = drawable;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    public Drawable getDrawableByState() {
        return getDrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //mDrawable = getDrawable();
        // setImageState(new int[]{android.R.attr.state_checked, android.R.attr.state_pressed}, false);
        //mDrawable.setState(new int[]{android.R.attr.state_checked, android.R.attr.state_pressed});
        //mDrawable.setState(new int[]{isChecked?android.R.attr.state_checked:android.R.attr.state_empty, android.R.attr.state_pressed});
        // setImageDrawable(mDrawable);

        //super.onDraw(canvas);

        QDLogger.e("getPaddingLeft()="+ getPaddingLeft());
        QDLogger.formatArray("getDrawableState", getDrawableState());
        mDrawable = getDrawable();

        if (getMatrix() == null && getPaddingTop() == 0 && getPaddingLeft() == 0) {
            mDrawable.draw(canvas);
        } else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();

            canvas.clipRect(getPaddingLeft(), getPaddingTop(),
                    getMeasuredWidth() - getPaddingRight(),
                    getMeasuredHeight() - getPaddingBottom());

            canvas.translate(getPaddingLeft(), getPaddingTop());

            if (getImageMatrix() != null) {
                canvas.concat(getImageMatrix());
            }
            mDrawable.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onClick(v);
                }
                mChecked = !mChecked;
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChanged(v, mChecked);
                }
            }
        };
        super.setOnClickListener(listener);
    }

    OnCheckedChangeListener mOnCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onChangeListener) {
        mOnCheckedChangeListener = onChangeListener;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(View buttonView, boolean isChecked);
    }
}
