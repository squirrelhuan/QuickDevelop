package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;

public class MyRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {
    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleCustomAttrs(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleCustomAttrs(context, attrs);
        init();
    }

    Drawable srcLeftDrawable, srcTopDrawable, srcRightDrawable, srcBottomDrawable;
    int drawableHeight, drawableWidth, drawableMargin;
    Drawable mDrawable;
    private void handleCustomAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton);
        //int srcTop = typedArray.getColor(R.styleable.MyRadioButton_srcTop, -1);
        srcLeftDrawable = typedArray.getDrawable(R.styleable.MyRadioButton_srcLeft);
        srcTopDrawable = typedArray.getDrawable(R.styleable.MyRadioButton_srcTop);
        srcRightDrawable = typedArray.getDrawable(R.styleable.MyRadioButton_srcRight);
        srcBottomDrawable = typedArray.getDrawable(R.styleable.MyRadioButton_srcBottom);
        drawableMargin = typedArray.getDimensionPixelOffset(R.styleable.MyRadioButton_srcMargin, 0);
        if (srcLeftDrawable != null) {
            mDrawable = srcLeftDrawable;
        } else if (srcTopDrawable != null) {
            mDrawable = srcTopDrawable;
        } else if (srcRightDrawable != null) {
            mDrawable = srcRightDrawable;
        } else if (srcBottomDrawable != null) {
            mDrawable = srcBottomDrawable;
        }
        if (mDrawable != null) {
            Bitmap bitmap = QDBitmapUtil.getBitmapByDrawable(mDrawable);
            drawableWidth = bitmap.getWidth();
            drawableHeight = bitmap.getHeight();
        }

        drawableWidth = typedArray.getDimensionPixelOffset(R.styleable.MyRadioButton_srcWidth, drawableWidth);
        drawableHeight = typedArray.getDimensionPixelOffset(R.styleable.MyRadioButton_srcHeight, drawableHeight);

        typedArray.recycle();
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
    }

    /*OnClickListener mOnClickListener ;
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null) {
                    mOnClickListener.onClick(v);
                }
                setChecked(true);
            }
        };
        super.setOnClickListener(onClickListener);
    }*/

    private void init() {
        // setOnClickListener(mOnClickListener);
    }

    int textHeight = 0;
    int textWidth = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        textHeight = getMeasuredHeight();
        textWidth = getMeasuredWidth();
        //QDLogger.e("getMeasuredWidth="+getMeasuredWidth()+",getMeasuredHeight="+getMeasuredHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mDrawable != null) {
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthMode != MeasureSpec.EXACTLY && widthMode != MeasureSpec.AT_MOST) {
                // throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
            } else if (heightMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.AT_MOST) {
                // throw new IllegalStateException("Height must have an exact value or MATCH_PARENT");
            }
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            if ((widthMode == MeasureSpec.AT_MOST) && (mDrawable == srcLeftDrawable || mDrawable == srcRightDrawable)) {
                width = getMeasuredWidth() + drawableWidth + drawableMargin;
            }
            if ((heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) && (mDrawable == srcTopDrawable || mDrawable == srcBottomDrawable)) {
                height = getMeasuredHeight() + drawableHeight + drawableMargin;
            } else {
                height = Math.max(getMeasuredHeight(), drawableHeight);
            }
            setMeasuredDimension(width, height);
            // QDLogger.e("getMeasuredWidth=" + getMeasuredWidth()+",textWidth="+textWidth+",widthMode="+widthMode);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawable != null) {
            if (mDrawable.isStateful()) {
                mDrawable.setState(getDrawableState());
            }
            textWidth = (int) getPaint().measureText(getText().toString());
            int widht = Math.min(drawableWidth, getMeasuredWidth());
            int height = Math.min(drawableHeight, getMeasuredHeight()) * widht / drawableWidth;
            int left = 0;
            int top = 0;

            if (mDrawable == srcTopDrawable || mDrawable == srcBottomDrawable) {
                left = getMeasuredWidth() / 2 - widht / 2;
                if (mDrawable == srcBottomDrawable) {
                    top = getMeasuredHeight() - height;
                }
            } else {
                top = getMeasuredHeight() / 2 - height / 2;
                if (mDrawable == srcRightDrawable) {
                    left = getMeasuredWidth() - widht;
                }
            }

            int dx = 0;
            int dy = 0;

            //int gravity_h = getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
            int gravity_h = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
            int gravity_v = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;

            switch (gravity_v) {
                case Gravity.CENTER_VERTICAL:
                    if (mDrawable == srcTopDrawable || mDrawable == srcBottomDrawable) {
                        if (mDrawable == srcTopDrawable) {
                            dy = getMeasuredHeight() - (getMeasuredHeight() - height) / 2 - getMeasuredHeight() / 2;
                        } else {
                            dy = -(getMeasuredHeight() - (getMeasuredHeight() - height) / 2 - getMeasuredHeight() / 2);
                        }
                    }
                    break;
                case Gravity.TOP:
                    break;
                case Gravity.BOTTOM:
                    break;
            }

            switch (gravity_h) {//TODO 解决gravity 复合的情况
                case Gravity.CENTER_HORIZONTAL:
                    if (mDrawable == srcLeftDrawable) {
                        dx = widht / 2;
                    } else if (mDrawable == srcRightDrawable) {
                        dx = -(getMeasuredWidth() - (getMeasuredWidth() - widht) / 2 - getMeasuredWidth() / 2);
                    }
                    break;
                case Gravity.LEFT:
                    if (mDrawable == srcLeftDrawable) {
                        dx = widht;
                    } else if (mDrawable == srcRightDrawable) {
                        dx = 0;
                    }
                    break;
                case Gravity.RIGHT:
                    if (mDrawable == srcLeftDrawable) {
                        dx = 0;
                    } else if (mDrawable == srcRightDrawable) {
                        dx = -widht;
                    }
                    break;
            }

            canvas.translate(dx, dy);
            super.onDraw(canvas);
            canvas.translate(-dx, -dy);

            //QDLogger.e("dx=" + dx+",widht="+widht+",textWidth="+textWidth+",getMeasuredWidth="+getMeasuredWidth());
            Bitmap bitmap = QDBitmapUtil.getBitmapByDrawable(mDrawable);
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            int left_1 = left;
            int top_1 = top;
            int right_1 = left + widht;
            int bottom_1 = top + height;
            if (mDrawable == srcLeftDrawable) {
                left_1 = getPaddingLeft();
                right_1 = left_1 + widht;
            } else if (mDrawable == srcTopDrawable) {
                top_1 = top_1 + getPaddingTop();
                bottom_1 = top_1 + height;
            } else if (mDrawable == srcRightDrawable) {
                right_1 = right_1 - getPaddingRight();
                left_1 = right_1 - widht;
            } else if (mDrawable == srcBottomDrawable) {
                bottom_1 = top_1 + height;
                top_1 = bottom_1 - height;
            }
            Rect rect1 = new Rect(left_1, top_1, right_1, bottom_1);
            /*float marginX = 0,marginY = 0;
            if (mDrawable==srcLeftDrawable){
                marginX = getPaddingLeft();
            }else if (mDrawable==srcTopDrawable){
                marginY = getPaddingTop();
            }else if (mDrawable==srcRightDrawable){
                marginX = getPaddingRight();
            }else if (mDrawable==srcBottomDrawable){
                marginY = getPaddingBottom();
            }
            canvas.translate(marginX, marginY);*/
            canvas.drawBitmap(bitmap, rect, rect1, new Paint());
        }else {
            super.onDraw(canvas);
        }
    }


/*
    boolean mChecked;
    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        // postInvalidate();
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
        }
    }

    MyCheckBox.OnCheckedChangeListener mOnCheckedChangeListener;
    public void setOnCheckedChangeListener(MyCheckBox.OnCheckedChangeListener onChangeListener) {
        mOnCheckedChangeListener = onChangeListener;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }*/
}
