package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoadingTengxuntvView extends View {

    public LoadingTengxuntvView(Context context) {
        super(context);
    }

    public LoadingTengxuntvView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingTengxuntvView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //QDLogger.e("dispatchDraw LoadingTengxuntvView");
    }

    private float maxHeight=.4f;
    private float maxWidth = .16f;
    private int childCount =3;
    private int[] colors ={Color.GREEN,Color.YELLOW,Color.RED};
    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        for(int i=0;i<childCount;i++){
            float progress_c = Math.abs(progress-(float)(i+1)/(childCount));
            progress_c = progress_c>1?1-(progress_c-1):progress_c;
            float left = width/(childCount+1)*(i+1) - width*maxWidth*progress_c/2f;
            float right = left + progress_c*maxWidth*width;
            float top = (float) (height*(0.5-maxHeight*progress_c/2f));
            float bottom = top+height*maxHeight*progress_c;
            RectF rectF = new RectF(left,top,right,bottom);
            float x = (left+right)/2;
            float y = (top+bottom)/2;
            mPaint.setColor(colors[i]);
            
            canvas.save();
            canvas.rotate(15,x,y);
            canvas.drawRoundRect(rectF,5,5,mPaint);
            canvas.restore();
            //canvas.drawCircle(0,0,59,mPaint);
        }
    }
    ValueAnimator animator;
    private float progress;
    public void startAnimation() {
        isPlaying = true;
        float end = 2f;
        animator = ValueAnimator.ofFloat(0f, end);
        animator.setDuration(1200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                progress = value;
                //QDLogger.d(TAG, "progress=" + progress);
                //postInvalidate();
                invalidate();
            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        //animator.setInterpolator(new AccelerateInterpolator());
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null)
            animator.cancel();
    }
}
