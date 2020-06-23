package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.webview.QDWebCromeClient;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoadingDownView extends View {

    int color;
    public LoadingDownView(Context context) {
        super(context);
    }

    public LoadingDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingDownView);
        color = ta.getColor(R.styleable.LoadingDownView_color, Color.parseColor("#87CEFF"));
        ta.recycle();  //注意回收
    }

    public LoadingDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingDownView);
        color = ta.getColor(R.styleable.LoadingDownView_color, Color.parseColor("#87CEFF"));
        ta.recycle();  //注意回收
        //QDLogger.v("color = "+color);
    }

    private int center_x, center_y, mwidth, width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        center_x = width / 2;
    }

    private boolean isPlaying = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
        if (!isPlaying) {
            startAnimation();
        }
    }

    boolean isDrawed;
    private int lineWidth = 30;
    private int arrowHeight = 30;
    private void drawView(Canvas canvas) {
        //QDLogger.e("progress="+progress+"");
        // canvas.rotate(progress, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        //canvas.drawColor(Color.BLACK);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        arrowHeight = height/2;
        //float currentY = progress*(height-arrowHeight);
       // canvas.translate(0,currentY);

        //开始高度，开始位置
        int startHeight = height/3*2;
        int endHeight = height/2;
        float currentHeight=startHeight-(startHeight-endHeight)*progress ;
        //结束高度，结束位置
        int startY = 0;
        int endY = height-endHeight;
        int top = (int) (startY+(endY-startY)*progress);
        if (!isDrawed) {
            mPaint.setColor(color);
            int lineWidthc = (int) (lineWidth/5*4+lineWidth/5*progress);
            int arrowWidth = lineWidthc*5/4;
            //int arrowWidthc = (int) (arrowWidth/3*2+arrowWidth/3*progress);
            int arrowHeight = lineWidthc*3/2;
            int arrowHeightc = (int) (arrowHeight/2+arrowHeight/2*(1-progress));
            Path path = new Path();
            path.moveTo(center_x-lineWidthc/2,top);
            path.lineTo(center_x+lineWidthc/2,top);
            path.lineTo(center_x+lineWidthc/2,top+currentHeight-arrowHeightc);
            path.lineTo(center_x+arrowWidth,top+currentHeight-arrowHeightc);
            path.lineTo(center_x,top+currentHeight);
            path.lineTo(center_x-arrowWidth,top+currentHeight-arrowHeightc);
            path.lineTo(center_x-lineWidthc/2,top+currentHeight-arrowHeightc);
            path.lineTo(center_x-lineWidthc/2,top);
            canvas.drawPath(path,mPaint);
        }
    }

    public static interface OnProgressChanged {
        void onProgress(float progress);
    }
    OnProgressChanged onProgressChanged;
    public void setOnProgressChanged(OnProgressChanged onProgressChanged) {
        this.onProgressChanged = onProgressChanged;
    }

    private float progress;
    private boolean isForward = true;
    ValueAnimator animator;
    public void startAnimation() {
        isPlaying = true;
        final float end = 1f;
        animator = ValueAnimator.ofFloat(0f, end);
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                progress = value;
                if(onProgressChanged!=null){
                    onProgressChanged.onProgress(progress);
                }
                //Log.d(TAG, "progress=" + progress);
                if (progress >= end) {
                    isForward = !isForward;
                    //Log.d(TAG, "isForward=" + isForward);
                } else {
                    //postInvalidate();
                    invalidate();
                }
            }
        });
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null) {
            animator.cancel();
        }
        if(onProgressChanged!=null){
            onProgressChanged = null;
        }
    }
}
