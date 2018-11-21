package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

/**
 * Created by Squirrel桓 on 2018/11/13.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.Vibrator;
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

public class LoopView extends View {
    private static final String TAG = LoopView.class.getSimpleName();
    public static final int MSG_INVALIDATE = 1000;
    public static final int MSG_SCROLL_LOOP = 2000;
    public static final int MSG_SELECTED_ITEM = 3000;
    private ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mScheduledFuture;
    private int mTotalScrollY;
    private LoopScrollListener mLoopListener;
    private GestureDetector mGestureDetector;
    private int mSelectedItem;
    private SimpleOnGestureListener mOnGestureListener;
    private Context mContext;
    private Paint mTopBottomTextPaint;
    private Paint mCenterTextPaint;
    private Paint mCenterLinePaint;
    private ArrayList mDataList;
    private int mTextSize;
    private int mMaxTextWidth;
    private int mMaxTextHeight;
    private int mTopBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
    private int mCenterTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
    private int mCenterLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
    private float lineSpacingMultiplier;
    private boolean mCanLoop;
    private int mTopLineY;
    private int mBottomLineY;
    private int mCurrentIndex;
    private int mInitPosition;
    private int mPaddingLeftRight;
    private int mPaddingTopBottom;
    private float mItemHeight;
    private int mDrawItemsCount;
    private int mCircularDiameter;
    private int mWidgetHeight;
    private int mCircularRadius;
    private int mWidgetWidth;
    public Handler mHandler;

    public LoopView(Context context) {
        this(context, (AttributeSet) null);
    }

    public LoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mHandler = new Handler(new Callback() {
            public boolean handleMessage(Message msg) {
                if (msg.what == 1000) {
                    LoopView.this.invalidate();
                }

                if (msg.what == 2000) {
                    LoopView.this.startSmoothScrollTo();
                } else if (msg.what == 3000) {
                    LoopView.this.itemSelected();
                }

                return false;
            }
        });
        this.initView(context, attrs);
    }

    @TargetApi(21)
    public LoopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mHandler = new Handler(new Callback() {
            public boolean handleMessage(Message msg) {
                if (msg.what == 1000) {
                    LoopView.this.invalidate();
                }

                if (msg.what == 2000) {
                    LoopView.this.startSmoothScrollTo();
                } else if (msg.what == 3000) {
                    LoopView.this.itemSelected();
                }

                return false;
            }
        });
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoopView);
        if (array != null) {
            /*this.mTopBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
            this.mCenterTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
            this.mCenterLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);*/
            this.mCanLoop = array.getBoolean(R.styleable.LoopView_canLoop, true);
            this.mInitPosition = array.getInt(R.styleable.LoopView_initPosition, -1);
            this.mTextSize = array.getDimensionPixelSize(R.styleable.LoopView_textSize, this.sp2px(context, 16.0F));
            this.mDrawItemsCount = array.getInt(R.styleable.LoopView_drawItemCount, 7);
            array.recycle();
        }

        this.lineSpacingMultiplier = 2.0F;
        this.mContext = context;
        this.mOnGestureListener = new LoopView.LoopViewGestureListener();
        this.mTopBottomTextPaint = new Paint();
        this.mCenterTextPaint = new Paint();
        this.mCenterLinePaint = new Paint();
        if (VERSION.SDK_INT >= 11) {
            this.setLayerType(1, (Paint) null);
        }

        this.mGestureDetector = new GestureDetector(context, this.mOnGestureListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
    }

    private void initData() {
        if (this.mDataList == null) {
            throw new IllegalArgumentException("data list must not be null!");
        } else {
            this.mTopBottomTextPaint.setColor(this.mTopBottomTextColor);
            this.mTopBottomTextPaint.setAntiAlias(true);
            this.mTopBottomTextPaint.setTypeface(Typeface.MONOSPACE);
            this.mTopBottomTextPaint.setTextSize((float) this.mTextSize * .8f);
            this.mCenterTextPaint.setColor(this.mCenterTextColor);
            this.mCenterTextPaint.setAntiAlias(true);
            this.mCenterTextPaint.setTextScaleX(1.05F);
            this.mCenterTextPaint.setTypeface(Typeface.MONOSPACE);
            this.mCenterTextPaint.setTextSize((float) this.mTextSize);
            this.mCenterLinePaint.setColor(this.mCenterLineColor);
            this.mCenterLinePaint.setAntiAlias(true);
            this.mCenterLinePaint.setTypeface(Typeface.MONOSPACE);
            this.mCenterLinePaint.setTextSize((float) this.mTextSize);
            this.measureTextWidthHeight();
            int mHalfCircumference = (int) ((float) this.mMaxTextHeight * this.lineSpacingMultiplier * (float) (this.mDrawItemsCount - 1));
            this.mCircularDiameter = (int) ((double) (mHalfCircumference * 2) / 3.141592653589793D);
            this.mCircularRadius = (int) ((double) mHalfCircumference / 3.141592653589793D);
            if (this.mInitPosition == -1) {
                if (this.mCanLoop) {
                    this.mInitPosition = (this.mDataList.size() + 1) / 2;
                } else {
                    this.mInitPosition = 0;
                }
            }

            this.mCurrentIndex = this.mInitPosition;
            this.invalidate();
        }
    }

    private void measureTextWidthHeight() {
        Rect rect = new Rect();

        for (int i = 0; i < this.mDataList.size(); ++i) {
            String s1 = (String) this.mDataList.get(i);
            this.mCenterTextPaint.getTextBounds(s1, 0, s1.length(), rect);
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
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.i(TAG, "onMeasure -> heightMode:" + heightMode);
        this.mItemHeight = this.lineSpacingMultiplier * (float) this.mMaxTextHeight;
        this.mPaddingLeftRight = (this.mWidgetWidth - this.mMaxTextWidth) / 2;
        this.mPaddingTopBottom = (this.mWidgetHeight - this.mCircularDiameter) / 2;
        this.mTopLineY = (int) (((float) this.mCircularDiameter - this.mItemHeight) / 2.0F) + this.mPaddingTopBottom;
        this.mBottomLineY = (int) (((float) this.mCircularDiameter + this.mItemHeight) / 2.0F) + this.mPaddingTopBottom;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDataList == null) {
            super.onDraw(canvas);
        } else {
            super.onDraw(canvas);
            int mChangingItem = (int) ((float) this.mTotalScrollY / this.mItemHeight);
            this.mCurrentIndex = this.mInitPosition + mChangingItem % this.mDataList.size();
            if (!this.mCanLoop) {
                if (this.mCurrentIndex < 0) {
                    this.mCurrentIndex = 0;
                }

                if (this.mCurrentIndex > this.mDataList.size() - 1) {
                    this.mCurrentIndex = this.mDataList.size() - 1;
                }
            } else {
                if (this.mCurrentIndex < 0) {
                    this.mCurrentIndex += this.mDataList.size();
                }

                if (this.mCurrentIndex > this.mDataList.size() - 1) {
                    this.mCurrentIndex -= this.mDataList.size();
                }
            }

            int count = 0;

            String[] itemCount;
            int templateItem;
            for (itemCount = new String[this.mDrawItemsCount]; count < this.mDrawItemsCount; ++count) {
                templateItem = this.mCurrentIndex - (this.mDrawItemsCount / 2 - count);
                if (this.mCanLoop) {
                    if (templateItem < 0) {
                        templateItem += this.mDataList.size();
                    }

                    if (templateItem > this.mDataList.size() - 1) {
                        templateItem -= this.mDataList.size();
                    }

                    itemCount[count] = (String) this.mDataList.get(templateItem);
                } else if (templateItem < 0) {
                    itemCount[count] = "";
                } else if (templateItem > this.mDataList.size() - 1) {
                    itemCount[count] = "";
                } else {
                    itemCount[count] = (String) this.mDataList.get(templateItem);
                }
            }
//去掉标记线
            /*canvas.drawLine(0.0F, (float)this.mTopLineY, (float)this.mWidgetWidth, (float)this.mTopLineY, this.mCenterLinePaint);
            canvas.drawLine(0.0F, (float)this.mBottomLineY, (float)this.mWidgetWidth, (float)this.mBottomLineY, this.mCenterLinePaint);*/
            count = 0;

            for (templateItem = (int) ((float) this.mTotalScrollY % this.mItemHeight); count < this.mDrawItemsCount; ++count) {
                canvas.save();
                float itemHeight = (float) this.mMaxTextHeight * this.lineSpacingMultiplier;
                double radian = (double) ((itemHeight * (float) count - (float) templateItem) / (float) this.mCircularRadius);
                float angle = (float) (radian * 180.0D / 3.141592653589793D);
                if (angle < 180.0F && angle > 0.0F) {
                    int translateY = (int) ((double) this.mCircularRadius - Math.cos(radian) * (double) this.mCircularRadius - Math.sin(radian) * (double) this.mMaxTextHeight / 2.0D) + this.mPaddingTopBottom;
                    canvas.translate(0.0F, (float) translateY);
                    double f = Math.sin(radian);
                    canvas.scale(1.0F, (float) f);
                    mTopBottomTextPaint.setAlpha((int) (f * 250));
                    if (translateY <= this.mTopLineY) {
                        canvas.save();
                        canvas.clipRect(0, 0, this.mWidgetWidth, this.mTopLineY - translateY);

                        // 文字宽
                        float textWidth1 = mTopBottomTextPaint.measureText(itemCount[count]);
                        canvas.drawText(itemCount[count], (float) this.getWidth() / 2 - textWidth1 / 2, (float) this.mMaxTextHeight, this.mTopBottomTextPaint);
                        //canvas.drawText(itemCount[count], (float)this.mPaddingLeftRight, (float)this.mMaxTextHeight, this.mTopBottomTextPaint);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(0, this.mTopLineY - translateY, this.mWidgetWidth, (int) itemHeight);

                        // 文字宽
                        float textWidth = mCenterTextPaint.measureText(itemCount[count]);
                        // 文字baseline在y轴方向的位置
                        float baseLineY = Math.abs(mCenterTextPaint.ascent() + mTopBottomTextPaint.descent()) / 2;
                        canvas.drawText(itemCount[count], (float) this.getWidth() / 2 - textWidth / 2, (float) this.mMaxTextHeight, this.mCenterTextPaint);
                        //canvas.drawText(itemCount[count], (float)this.mPaddingLeftRight, (float)this.mMaxTextHeight, this.mCenterTextPaint);
                        canvas.restore();
                    } else if (this.mMaxTextHeight + translateY >= this.mBottomLineY) {
                        canvas.save();
                        canvas.clipRect(0, 0, this.mWidgetWidth, this.mBottomLineY - translateY);


                        // 文字宽
                        float textWidth1 = mCenterTextPaint.measureText(itemCount[count]);

                        canvas.drawText(itemCount[count], (float) this.getWidth() / 2 - textWidth1 / 2, (float) this.mMaxTextHeight, this.mCenterTextPaint);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(0, this.mBottomLineY - translateY, this.mWidgetWidth, (int) itemHeight);


                        // 文字宽
                        float textWidth = mTopBottomTextPaint.measureText(itemCount[count]);
                        // 文字baseline在y轴方向的位置
                        float baseLineY = Math.abs(mTopBottomTextPaint.ascent() + mTopBottomTextPaint.descent()) / 2;
                        canvas.drawText(itemCount[count], (float) this.getWidth() / 2 - textWidth / 2, (float) this.mMaxTextHeight, this.mTopBottomTextPaint);
                        //canvas.drawText(itemCount[count], (float)this.mPaddingLeftRight, (float)this.mMaxTextHeight, this.mTopBottomTextPaint);
                        canvas.restore();
                    } else if (translateY >= this.mTopLineY && this.mMaxTextHeight + translateY <= this.mBottomLineY) {
                        canvas.clipRect(0, 0, this.mWidgetWidth, (int) itemHeight);

                        // 文字宽
                        float textWidth = mCenterTextPaint.measureText(itemCount[count]);
                        // 文字baseline在y轴方向的位置
                        float baseLineY = Math.abs(mCenterTextPaint.ascent() + mTopBottomTextPaint.descent()) / 2;
                        canvas.drawText(itemCount[count], (float) this.getWidth() / 2 - textWidth / 2, (float) this.mMaxTextHeight, this.mCenterTextPaint);
                        //canvas.drawText(itemCount[count], (float)this.mPaddingLeftRight, (float)this.mMaxTextHeight, this.mCenterTextPaint);
                        this.mSelectedItem = this.mDataList.indexOf(itemCount[count]);
                    }

                    canvas.restore();
                } else {
                    canvas.restore();
                }
            }

        }
        if (Math.abs(LoopView.this.mTotalScrollY % LoopView.this.mItemHeight) > 4) {
            int offset = (int) ((float) LoopView.this.mTotalScrollY / LoopView.this.mItemHeight);
            if (offset_current != offset) {
                offset_current = offset;
                Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
                vibrator.vibrate(8);
                //long[] patter = {10, 10, 20, 5};
                //vibrator.vibrate(patter, 0);
            }
        }
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

    public final void setCanLoop(boolean canLoop) {
        this.mCanLoop = canLoop;
        this.invalidate();
    }

    public void setTextColor(int mTopBottomTextColor, int mCenterTextColor, int mCenterLineColor) {
        this.mTopBottomTextColor = mTopBottomTextColor;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
        this.mCenterTextColor = mCenterTextColor;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
        this.mCenterLineColor = mCenterLineColor;
    }

    public final void setTextSize(float size) {
        if (size > 0.0F) {
            this.mTextSize = this.sp2px(this.mContext, size);
        }

    }

    public void setInitPosition(int initPosition) {
        this.mInitPosition = initPosition;
        this.invalidate();
    }

    public void setLoopListener(LoopScrollListener LoopListener) {
        this.mLoopListener = LoopListener;
    }

    public final void setDataList(List<String> list) {
        this.mDataList = (ArrayList) list;
        this.initData();
    }

    public int getSelectedItem() {
        return this.mSelectedItem;
    }

    private void itemSelected() {
        if (this.mLoopListener != null) {
            this.postDelayed(new LoopView.SelectedRunnable(), 200L);
        }

    }

    private void cancelSchedule() {
        if (this.mScheduledFuture != null && !this.mScheduledFuture.isCancelled()) {
            this.mScheduledFuture.cancel(true);
            this.mScheduledFuture = null;
        }

    }

    private void startSmoothScrollTo() {
        int offset = (int) ((float) this.mTotalScrollY % this.mItemHeight);
        this.cancelSchedule();
        this.mScheduledFuture = this.mExecutor.scheduleWithFixedDelay(new LoopView.HalfHeightRunnable(offset), 0L, 10L, TimeUnit.MILLISECONDS);
    }

    private void startSmoothScrollTo(float velocityY) {
        this.cancelSchedule();
        int velocityFling = 20;
        this.mScheduledFuture = this.mExecutor.scheduleWithFixedDelay(new LoopView.FlingRunnable(velocityY), 0L, (long) velocityFling, TimeUnit.MILLISECONDS);
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

            Log.i(LoopView.TAG, "velocity->" + this.velocity);
            if (Math.abs(this.velocity) >= 0.0F && Math.abs(this.velocity) <= 20.0F) {
                LoopView.this.cancelSchedule();
                LoopView.this.mHandler.sendEmptyMessage(2000);
            } else {
                int i = (int) (this.velocity * 10.0F / 1000.0F);
                LoopView.this.mTotalScrollY = LoopView.this.mTotalScrollY - i;
                if (!LoopView.this.mCanLoop) {
                    float itemHeight = LoopView.this.lineSpacingMultiplier * (float) LoopView.this.mMaxTextHeight;
                    if (LoopView.this.mTotalScrollY <= (int) ((float) (-LoopView.this.mInitPosition) * itemHeight)) {
                        this.velocity = 40.0F;
                        LoopView.this.mTotalScrollY = (int) ((float) (-LoopView.this.mInitPosition) * itemHeight);
                    } else if (LoopView.this.mTotalScrollY >= (int) ((float) (LoopView.this.mDataList.size() - 1 - LoopView.this.mInitPosition) * itemHeight)) {
                        LoopView.this.mTotalScrollY = (int) ((float) (LoopView.this.mDataList.size() - 1 - LoopView.this.mInitPosition) * itemHeight);
                        this.velocity = -40.0F;
                    }
                }

                if (this.velocity < 0.0F) {
                    this.velocity += 20.0F;
                } else {
                    this.velocity -= 20.0F;
                }

                LoopView.this.mHandler.sendEmptyMessage(1000);
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
                if ((float) this.offset > LoopView.this.mItemHeight / 2.0F) {
                    this.realTotalOffset = (int) (LoopView.this.mItemHeight - (float) this.offset);
                } else {
                    this.realTotalOffset = -this.offset;
                }
            }

            this.realOffset = (int) ((float) this.realTotalOffset * 0.1F);
            if (this.realOffset == 0) {
                if (this.realTotalOffset < 0) {
                    this.realOffset = -1;
                } else {
                    this.realOffset = 1;
                }
            }

            if (Math.abs(this.realTotalOffset) <= 0) {
                LoopView.this.cancelSchedule();
                LoopView.this.mHandler.sendEmptyMessage(3000);
            } else {
                LoopView.this.mTotalScrollY = LoopView.this.mTotalScrollY + this.realOffset;
                LoopView.this.mHandler.sendEmptyMessage(1000);
                this.realTotalOffset -= this.realOffset;
            }
        }
    }

    class SelectedRunnable implements Runnable {
        SelectedRunnable() {
        }

        public final void run() {
            LoopScrollListener listener = LoopView.this.mLoopListener;
            int selectedItem = LoopView.this.getSelectedItem();
            LoopView.this.mDataList.get(selectedItem);
            listener.onItemSelect(selectedItem);
        }
    }

    class LoopViewGestureListener extends SimpleOnGestureListener {
        LoopViewGestureListener() {
        }

        public final boolean onDown(MotionEvent motionevent) {
            LoopView.this.cancelSchedule();
            Log.i(LoopView.TAG, "LoopViewGestureListener->onDown");
            return true;
        }

        public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LoopView.this.startSmoothScrollTo(velocityY);
            Log.i(LoopView.TAG, "LoopViewGestureListener->onFling");
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(LoopView.TAG, "LoopViewGestureListener->onScroll");
            LoopView.this.mTotalScrollY = (int) ((float) LoopView.this.mTotalScrollY + distanceY);
            if (!LoopView.this.mCanLoop) {
                int initPositionCircleLength = (int) ((float) LoopView.this.mInitPosition * LoopView.this.mItemHeight);
                int initPositionStartY = -1 * initPositionCircleLength;
                if (LoopView.this.mTotalScrollY < initPositionStartY) {
                    LoopView.this.mTotalScrollY = initPositionStartY;
                }

                int circleLength = (int) ((float) (LoopView.this.mDataList.size() - 1 - LoopView.this.mInitPosition) * LoopView.this.mItemHeight);
                if (LoopView.this.mTotalScrollY >= circleLength) {
                    LoopView.this.mTotalScrollY = circleLength;
                }
            }

            LoopView.this.invalidate();
            return true;
        }
    }
}