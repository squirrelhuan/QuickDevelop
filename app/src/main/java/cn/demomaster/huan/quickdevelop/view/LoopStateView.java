package cn.demomaster.huan.quickdevelop.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.animation.QDValueAnimator;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;

/**
 * 汇入动画
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoopStateView extends View {

    public LoopStateView(Context context) {
        super(context);
    }

    public LoopStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoopStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int centerX, centerY, width, height;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        centerX = width / 2;
        centerY = height/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        drawView(canvas);
    }

    int color1 = Color.parseColor("#6495ED");
    int color2 = Color.parseColor("#00CD00");
    private void drawView(Canvas canvas) {
       // QDLogger.e("progress=" + progress + ","+progress*360);
        int padding = DisplayUtil.dip2px(getContext(),5);
        int radius1 = DisplayUtil.dip2px(getContext(),0);
        RectF rectF = new RectF(padding,padding,width,height);
        Paint mPaint = new Paint();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        float[] hsv = new float[]{0, 1, 1f};
        RadialGradient mRadialGradient = new RadialGradient(this.getWidth()/2f, this.getHeight()/2f, (getWidth()+getHeight())/2f, new int[]{getResources().getColor(R.color.transparent_dark_99), getResources().getColor(R.color.transparent_dark_55)},null,Shader.TileMode.CLAMP);
        mPaint.setShader(mRadialGradient);
        canvas.drawRoundRect(rectF, radius1, radius1, mPaint);

        Drawable vectorDrawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            vectorDrawable = getContext().getDrawable(R.drawable.ic_baseline_loop_24);
            vectorDrawable.setTint(Color.WHITE);
            Bitmap bitmap = QDBitmapUtil.getBitmapByDrawable(vectorDrawable);
            //bitmap = QDBitmapUtil.setBitmapLight(bitmap, (int) (100+progress2*150));

            int bitmap_width = getWidth()/2;
            int bitmap_height = getWidth()/2;
            Rect mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect mDestRect = new Rect(centerX-bitmap_width/2, centerY-bitmap_height/2, centerX+bitmap_width/2, centerY+bitmap_height/2);

            canvas.rotate(progress*360,centerX,centerY);
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mPaint);
        }
    }

    private float progress;
    QDValueAnimator animator;
    public void startAnimation() {
        if (animator!=null&&(animator.isPaused())) {
            animator.resume();
            return;
        }

        final float start = 1f;
        final float end = 0f;
        animator = QDValueAnimator.ofFloat(start, end);
        animator.setDuration(1000);
        animator.setAnimationListener(new QDValueAnimator.AnimationListener() {
            @Override
            public void onStartOpen(Object value) {

            }

            @Override
            public void onOpening(Object value) {
                if (getVisibility() == VISIBLE) {
                    progress = (float) value;
                    //QDLogger.d( "progress=" + progress);
                    invalidate();
                }
            }

            @Override
            public void onEndOpen(Object value) {

            }

            @Override
            public void onStartClose(Object value) {

            }

            @Override
            public void onClosing(Object value) {
                if (getVisibility() == VISIBLE) {
                    progress = Float.valueOf((int) value);
                    //QDLogger.d( "progress=" + progress+",value="+value);
                    invalidate();
                }
            }

            @Override
            public void onEndClose(Object value) {

            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(-1);//ValueAnimator.INFINITE
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void pause(){
        if (animator!=null){
            animator.pause();
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                animator.cancel();
            }
        }
    }

}
