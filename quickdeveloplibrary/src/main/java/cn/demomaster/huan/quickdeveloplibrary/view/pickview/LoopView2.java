package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

/**
 * Created by Squirrel桓 on 2018/11/13.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class LoopView2 extends View {
    private static final String TAG = LoopView2.class.getSimpleName();
    public static final int MSG_INVALIDATE = 1000;
    public static final int MSG_SCROLL_LOOP = 2000;
    public static final int MSG_SELECTED_ITEM = 3000;
    private ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mScheduledFuture;
    private int mTotalScrollY;
    private LoopScrollListener mLoopListener;
    private GestureDetector mGestureDetector;
    private SimpleOnGestureListener mOnGestureListener;
    private Context mContext;
    private Paint mTextPaint;
    private ArrayList mDataList;
    private int mTextSize;
    private int mMaxTextWidth;
    private int mMaxTextHeight;
    private int normalColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
    private int selectColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
    private float lineSpacingMultiplier;
    private int mCurrentIndex;
    private float lineHeight;
    private int mDrawItemsCount = 5;
    private int mWidgetHeight;
    private int mWidgetWidth;
    public Handler mHandler;

    public LoopView2(Context context) {
        this(context, (AttributeSet) null);
    }

    public LoopView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mHandler = new Handler(new Callback() {
            public boolean handleMessage(Message msg) {
                if (msg.what == 1000) {
                    LoopView2.this.invalidate();
                }
                if (msg.what == 2000) {
                    LoopView2.this.startSmoothScrollTo();
                } else if (msg.what == 3000) {
                    LoopView2.this.itemSelected();
                }
                return false;
            }
        });
        this.initView(context, attrs);
    }

    @TargetApi(21)
    public LoopView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mHandler = new Handler(new Callback() {
            public boolean handleMessage(Message msg) {
                if (msg.what == 1000) {
                    LoopView2.this.invalidate();
                }

                if (msg.what == 2000) {
                    LoopView2.this.startSmoothScrollTo();
                } else if (msg.what == 3000) {
                    LoopView2.this.itemSelected();
                }

                return false;
            }
        });
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoopView);
        if (array != null) {
            this.mTextSize = array.getDimensionPixelSize(R.styleable.LoopView_textSize, this.sp2px(context, 16.0F));
            this.mDrawItemsCount = array.getInt(R.styleable.LoopView_drawItemCount, 5);
            array.recycle();
        }
        this.lineSpacingMultiplier = 2.0F;
        this.mContext = context;
        this.mOnGestureListener = new LoopView2.LoopViewGestureListener();
        this.mTextPaint = new Paint();
        if (VERSION.SDK_INT >= 11) {
            this.setLayerType(1, (Paint) null);
        }

        this.mGestureDetector = new GestureDetector(context, this.mOnGestureListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
    }

    int allItemHeight;
    private void initData() {
        if (this.mDataList == null) {
            throw new IllegalArgumentException("data list must not be null!");
        } else {
            this.mTextPaint.setColor(this.normalColor);
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setTextScaleX(1.05F);
            this.mTextPaint.setTypeface(Typeface.MONOSPACE);
            this.mTextPaint.setTextSize((float) this.mTextSize);
            this.measureTextWidthHeight();
            this.lineHeight=mMaxTextHeight*lineSpacingMultiplier;
            this.allItemHeight=(int)lineHeight*mDataList.size();
            this.mTotalScrollY = (int)((mCurrentIndex)*lineHeight);

            this.invalidate();
        }
    }

    private void measureTextWidthHeight() {
        Rect rect = new Rect();

        for (int i = 0; i < this.mDataList.size(); ++i) {
            String s1 = (String) this.mDataList.get(i);
            this.mTextPaint.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > this.mMaxTextWidth) {
                this.mMaxTextWidth = textWidth;
            }

            int textHeight = rect.height();
            if (textHeight > this.mMaxTextHeight) {
                this.mMaxTextHeight = textHeight;
            }
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidgetWidth = this.getMeasuredWidth();
        this.mWidgetHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.lineHeight = this.lineSpacingMultiplier * (float) this.mMaxTextHeight;
        //高度度
        int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);
        switch (specModeHeight) {
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST://AT_MOST 自适应模式，根据设置的行数动态申请高度
                mWidgetHeight = (int) lineHeight * mDrawItemsCount;
                mWidgetWidth = measureWidth(widthMeasureSpec);
                setMeasuredDimension(mWidgetWidth, mWidgetHeight);
                break;
            case MeasureSpec.EXACTLY:
                break;
        }

/*
        int minimumWidth = getSuggestedMinimumWidth();
        int minimumHeight = getSuggestedMinimumHeight();*/
        /*int width = mMaxTextWidth;
        int height = (int)lineHeight*mDrawItemsCount;
        setMeasuredDimension(width, height);*/
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 75;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 75;//根据自己的需要更改
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDataList == null) {
            super.onDraw(canvas);
        } else {
            super.onDraw(canvas);

            Log.d("CGQ", "mTotalScrollY=" + mTotalScrollY+",allItemHeight="+allItemHeight);
            if(mTotalScrollY<0){
                mTotalScrollY=0;
            }else if(mTotalScrollY>allItemHeight){
                mTotalScrollY=allItemHeight;
            }
            int mChangingItem = (int) ((float) this.mTotalScrollY / this.lineHeight);
            this.mCurrentIndex = mChangingItem % this.mDataList.size();
            // Log.d("CGQ", "mTotalScrollY=" + mTotalScrollY + ",mCurrentIndex" + mCurrentIndex + ",mChangingItem=" + mChangingItem);

            /*String[] data = new String[50];
            for (int i = 0; i < data.length; i++) {
                data[i] = "" + i;
            }*/
            int startIndex = (int) (this.mTotalScrollY / this.lineHeight);
            if (startIndex < 0) {
                startIndex = 0;
            }
            //前后填充空数据
            int num_empty = mDrawItemsCount / 2;
            String[] data1 = new String[mDataList.size() + 2 * num_empty];
            for (int i = 0; i < data1.length; i++) {
                if (i - num_empty >= 0 && i - num_empty < mDataList.size()) {
                    data1[i] = (String) mDataList.get(i - num_empty);
                } else {
                    data1[i] = "";
                }
            }
            //Log.d("CGQ", "startIndex=" + startIndex);
            int count_t = ((data1.length - startIndex-1) > mDrawItemsCount+1 ? mDrawItemsCount +1: (data1.length - startIndex));

            String[] data2 = new String[count_t];
            for (int i = 0; i < data2.length; i++) {
                data2[i] = data1[startIndex + i];
            }

            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setColor(normalColor);
            mTextPaint.setXfermode(null);
            //RectF rectF1 = new RectF(0, 0, this.mWidgetWidth, this.mWidgetHeight);
            // canvas.drawRect(rectF1,mTextPaint);
            for (int i = 0; i < data2.length; i++) {
                int translateY = (int) (lineHeight * (i));
                int y = translateY - (int) ((float) this.mTotalScrollY % this.lineHeight);
                //Log.d("CGQ", "偏移Y=" + this.mTotalScrollY % this.lineHeight);

               /* mTextPaint.setColor(Color.BLUE);
                RectF rectF = new RectF(0, translateY, this.mWidgetWidth, translateY+lineHeight-2);
                mTextPaint.setColor(selectColor);
                canvas.drawRect(rectF,mTextPaint);

                mTextPaint.setColor(normalColor);*/
                String text = data2[i];
                // 文字宽
                float textWidth = mTextPaint.measureText(text);
                // 文字baseline在y轴方向的位置
                float baseLineY = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
                canvas.drawText(text, (float) this.getWidth() / 2 - textWidth / 2, y + lineHeight / 2 + baseLineY, this.mTextPaint);
            }

            RectF rectF = new RectF(0, this.mWidgetHeight / 2 - lineHeight / 2, this.mWidgetWidth, this.mWidgetHeight / 2 + lineHeight / 2);
            //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
            mTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            mTextPaint.setColor(selectColor);
            canvas.drawRect(rectF, mTextPaint);

        }
       /* if (Math.abs(LoopView2.this.mTotalScrollY % LoopView2.this.lineHeight) > 4) {
            int offset = (int) ((float) LoopView2.this.mTotalScrollY / LoopView2.this.lineHeight);
            if (offset_current != offset) {
                offset_current = offset;
                Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
                vibrator.vibrate(8);
                //long[] patter = {10, 10, 20, 5};
                //vibrator.vibrate(patter, 0);
            }
        }*/
    }

    int offset_current = 0;

    public boolean onTouchEvent(MotionEvent motionevent) {
        switch (motionevent.getAction()) {
            case 1:
            default:
                if (!this.mGestureDetector.onTouchEvent(motionevent)) {
                    this.startSmoothScrollTo();
                }

                return true;
        }
    }


    public void setTextColor(int normalColor, int selectColor) {
        this.selectColor = selectColor;
        this.normalColor = normalColor;
    }

    public final void setTextSize(float size) {
        this.mTextSize = this.sp2px(this.mContext, size);
    }

    public void setLoopListener(LoopScrollListener LoopListener) {
        this.mLoopListener = LoopListener;
    }

    public final void setDataList(List<String> list) {
        this.mDataList = (ArrayList) list;
        this.initData();
    }

    public int getCurrentIndex() {
        return mCurrentIndex+mDrawItemsCount/2;
    }

    public void setCurrentIndex(int mCurrentIndex) {
        this.mCurrentIndex = mCurrentIndex;
        this.initData();
    }

    public int getSelectedItem() {
        return this.mCurrentIndex;
    }

    private void itemSelected() {
        if (this.mLoopListener != null) {
            this.postDelayed(new LoopView2.SelectedRunnable(), 200L);
        }

    }

    private void cancelSchedule() {
        if (this.mScheduledFuture != null && !this.mScheduledFuture.isCancelled()) {
            this.mScheduledFuture.cancel(true);
            this.mScheduledFuture = null;
        }
        Log.i("CGQ", "cancelSchedule");
    }

    private void startSmoothScrollTo() {
        Log.i("CGQ", "startSmoothScrollTo----------------------------------");
        int offset = (int) ((float) this.mTotalScrollY % this.lineHeight);
        this.cancelSchedule();
        this.mScheduledFuture = this.mExecutor.scheduleWithFixedDelay(new LoopView2.HalfHeightRunnable(offset), 0L, 10L, TimeUnit.MILLISECONDS);
    }

    private void startSmoothScrollTo(float velocityY) {
        Log.i("CGQ", "startSmoothScrollTo----------------------------------" + velocityY);
        this.cancelSchedule();
        int velocityFling = 20;
        this.mScheduledFuture = this.mExecutor.scheduleWithFixedDelay(new LoopView2.FlingRunnable(velocityY), 0L, (long) velocityFling, TimeUnit.MILLISECONDS);
    }

    public int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }

    class FlingRunnable implements Runnable {
        float velocity;
        final float velocityY;

        FlingRunnable(float velocityY) {
            this.velocityY = velocityY;
            this.velocity = 2.14748365E9F;
        }

        public void run() {
            if (this.velocity == 2.14748365E9F) {
                if (Math.abs(this.velocityY) > 2000.0F) {
                    if (this.velocityY > 0.0F) {
                        this.velocity = 2000.0F;
                    } else {
                        this.velocity = -2000.0F;
                    }
                } else {
                    this.velocity = this.velocityY;
                }
            }

            Log.i(LoopView2.TAG, "velocity->" + this.velocity);
            if (Math.abs(this.velocity) >= 0.0F && Math.abs(this.velocity) <= 20.0F) {
                LoopView2.this.cancelSchedule();
                LoopView2.this.mHandler.sendEmptyMessage(2000);
            } else {
                int i = (int) (this.velocity * 10.0F / 1000.0F);
                LoopView2.this.mTotalScrollY = LoopView2.this.mTotalScrollY - i;
                float itemHeight = LoopView2.this.lineSpacingMultiplier * (float) LoopView2.this.mMaxTextHeight;

                    if (LoopView2.this.mTotalScrollY >= (int) ((float) (LoopView2.this.mDataList.size() - 1) * itemHeight)) {
                        LoopView2.this.mTotalScrollY = (int) ((float) (LoopView2.this.mDataList.size() - 1) * itemHeight);
                        this.velocity = -40.0F;
                    }

                if (this.velocity < 0.0F) {
                    this.velocity += 20.0F;
                } else {
                    this.velocity -= 20.0F;
                }

                LoopView2.this.mHandler.sendEmptyMessage(1000);
            }
        }
    }

    class HalfHeightRunnable implements Runnable {
        int realTotalOffset;
        int realOffset;
        int offset;

        public HalfHeightRunnable(int offset) {
            this.offset = offset;
            this.realTotalOffset = 2147483647;
            this.realOffset = 0;
        }

        public void run() {
            if (this.realTotalOffset == 2147483647) {
                if ((float) this.offset > LoopView2.this.lineHeight / 2.0F) {
                    this.realTotalOffset = (int) (LoopView2.this.lineHeight - (float) this.offset);
                } else {
                    this.realTotalOffset = -this.offset;
                }
            }

            Log.i("CGQ", "HalfHeightRunnable");
            this.realOffset = (int) ((float) this.realTotalOffset * 0.1F);
            if (this.realOffset == 0) {
                if (this.realTotalOffset < 0) {
                    this.realOffset = -1;
                } else {
                    this.realOffset = 1;
                }
            }

            if (Math.abs(this.realTotalOffset) <= 0) {
                LoopView2.this.cancelSchedule();
                LoopView2.this.mHandler.sendEmptyMessage(3000);
            } else {
                LoopView2.this.mTotalScrollY = LoopView2.this.mTotalScrollY + this.realOffset;
                LoopView2.this.mHandler.sendEmptyMessage(1000);
                this.realTotalOffset -= this.realOffset;
            }
        }
    }

    class SelectedRunnable implements Runnable {
        SelectedRunnable() {
        }

        public final void run() {
            LoopScrollListener listener = LoopView2.this.mLoopListener;
            int selectedItem = LoopView2.this.getSelectedItem();
            LoopView2.this.mDataList.get(selectedItem);
            listener.onItemSelect(selectedItem);
            Log.i("CGQ", "SelectedRunnable");
        }
    }

    class LoopViewGestureListener extends SimpleOnGestureListener {
        LoopViewGestureListener() {
        }

        public final boolean onDown(MotionEvent motionevent) {
            LoopView2.this.cancelSchedule();
            Log.i(LoopView2.TAG, "LoopViewGestureListener->onDown");
            return true;
        }

        public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LoopView2.this.startSmoothScrollTo(velocityY);
            Log.i(LoopView2.TAG, "LoopViewGestureListener->onFling");
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(LoopView2.TAG, "LoopViewGestureListener->onScroll");
            int temp_scroll  = (int) ((float) LoopView2.this.mTotalScrollY + distanceY);

            allItemHeight = (int) ((float) (LoopView2.this.mDataList.size() - 1) * LoopView2.this.lineHeight);
            if (temp_scroll >= allItemHeight) {
                LoopView2.this.mTotalScrollY = allItemHeight;
            }
            if (temp_scroll <= 0) {
                LoopView2.this.mTotalScrollY = 0;
            }
            LoopView2.this.mTotalScrollY = temp_scroll;
            LoopView2.this.invalidate();
            return true;
        }
    }


}