package cn.demomaster.huan.quickdeveloplibrary.widget.pushcardlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Squirrel桓 on 2018/9/1.
 */
public class Saleng extends View {

    private Context context;
    public Saleng(Context context) {
        super(context);
        this.context = context;
    }

    public Saleng(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public Saleng(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    private int center_x, center_y, mwidth, width, height;
    private int pointX = 20;
    private int pointY = 20;
    private int pointRadius = (int)(getResources().getDisplayMetrics().density*8);
    private int bigRadius = (int)(getResources().getDisplayMetrics().density*35);
    private boolean isPlaying = false;
    private int paddingTop =0;
    private int paddingTopAbs =0;
    private float offet;

    public void setPaddingTop(int paddingTopAbs) {
        this.paddingTopAbs = paddingTopAbs;
        this.paddingTop = (int)(paddingTopAbs-height*.4f-offet* Math.sqrt(3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        offet =bigRadius/3;
        //绘制背景
        drawBackground(canvas);
        if (!isPlaying) {
            //startAnimation();
        }
    }

    private float y1,y2,y3;

    private void drawBackground(Canvas canvas) {
        paddingTop=(int)(paddingTopAbs*(1-percent));
        //float height = this.height<dp2px(context,100)?dp2px(context,100):this.height;
        Log.i("getMeasuredHeight","Saleng 高度:"+height) ;
        //绘制圆
        Paint p = new Paint();
        //p.setColor(0x77000000);
        p.setColor(Color.RED);
        p.setAntiAlias(true);
        if(percent!=1){
            animator.cancel();
        }
        if (percent < .5f) {//处理产生
            float step1 = 0.2f;
            float step2 = 0.3f;
            float step3 = 0.4f;

            //第一个点动态
            if (percent < step1) {
                canvas.drawCircle(width / 2, -pointRadius * 2 + height * percent+paddingTop, pointRadius, p);
            } else { //第一个点到达终点
                float c1 = 1;
            /*
                从0到step1大小不变
                从step1到setp1+2radiu变小
                从step2到step2+2radio变小
            */
                //大小控制，产生第一个点变小一圈
                if (percent > step1 && percent < (step1 + pointRadius * 2 / height)) {
                    c1 = (float) (1 - 0.15 * ((step1 + pointRadius * 2 / height) - percent));//在step1与step2之间产生在step2时达到最终状态
                } else if (percent > step1) {
                    c1 = 0.85f;
                }
                if (percent < (step1 + pointRadius * 2 / height) && percent > step2) {
                    c1 = (float) (1 - 0.4);//在step1与step2之间产生在step2时达到最终状态
                } else if (percent > step2) {
                    c1 = 0.6f;
                }
                y1 = -pointRadius * 2 + height * step1;
                canvas.drawCircle(width / 2, y1+paddingTop, pointRadius * c1, p);
            }
            //p.setColor(Color.GREEN);
            //第二个产生的点动态（实际排在最下边）
            if (percent < step3 && percent > step1) {
                float c2 = .6f;//在step1与step2之间产生在step2时达到最终状态
                canvas.drawCircle(width / 2, -pointRadius * 2 + height * percent+paddingTop, pointRadius * c2, p);
            } else if (percent > step3) {//第二个点到达终点
                float c2 = .6f;//在step1与step2之间产生在step2时达到最终状态
                y3 = -pointRadius * 2 + height * step3;
                canvas.drawCircle(width / 2, y3+paddingTop, pointRadius * c2, p);
            }

            //p.setColor(Color.YELLOW);
            //第三个产生的点动态(实际是中间那个)
            if (percent < step3 && percent > step2) {
                float c3 = .6f;//在step1与step2之间产生在step2时达到最终状态
                canvas.drawCircle(width / 2, -pointRadius * 2 + height * (percent - (step2 - step1))+paddingTop, pointRadius * c3, p);
            } else if (percent > step3) {//第二个点到达终点
                float c3 = .6f;//在step1与step2之间产生在step2时达到最终状态
                y2 = -pointRadius * 2 + height * step2;
                canvas.drawCircle(width / 2, y2+paddingTop, pointRadius * c3, p);
            }
           /* canvas.drawCircle(width/2,height/5,pointRadius,p);
            canvas.drawCircle(width/2,height/5*2,pointRadius,p);*/
        }else if (percent>.5f&&percent<.7f){//处理成圆

            if(percent>.5f&&percent<=.6f){//最下面的球右移，上面两个球下来
                float c0=.6f;
                /*float y = y1;
                y1 = y2;
                y2 =y3;
                y3 = */
                canvas.drawCircle(width / 2, y1+(y2-y1)*(percent-.5f)/.1f+paddingTop, pointRadius * c0, p);
                canvas.drawCircle(width / 2, y2+(y3-y2)*(percent-.5f)/.1f+paddingTop, pointRadius * c0, p);
                float s1 = 1;
                float s2 = (float) Math.sqrt(3);
                canvas.drawCircle(width / 2+(percent-.5f)/.1f*offet*s1, y3+offet*(percent-.5f)/.1f*s2+paddingTop, pointRadius * c0, p);
            }
            if(percent>.6f&&percent<=.7f){//最下面的球右移，上面两个球下来
                float c0=.6f;
                float s1 = 1;
                float s2 = (float) Math.sqrt(3);
                canvas.drawCircle(width / 2, y2+(y3-y2)*(percent-.6f)/.1f+paddingTop, pointRadius * c0, p);
                canvas.drawCircle(width / 2+(percent-.6f)/.1f*offet*s1, y3+offet*s2*(percent-.6f)/.1f+paddingTop, pointRadius * c0, p);
                canvas.drawCircle(width / 2+offet-offet*2*s1*(percent-.6f)/.1f, y3+offet*s2+paddingTop, pointRadius * c0, p);
            }
        }else {//处理自转
            if(percent!=1) {
                float c0 = .6f;
                float s1 = 1;
                float s2 = (float) Math.sqrt(3);
                canvas.rotate((percent - .7f) / .3f * 2 * 360, width / 2, y3 + s2 * offet - offet / s2 + paddingTop);
                canvas.drawCircle(width / 2, y3 + paddingTop, pointRadius * c0, p);
                canvas.drawCircle(width / 2 + offet * s1, y3 + offet * s2 + paddingTop, pointRadius * c0, p);
                canvas.drawCircle(width / 2 - offet * s1, y3 + offet * s2 + paddingTop, pointRadius * c0, p);
            }else {

                float c0 = .6f;
                float s1 = 1;
                float s2 = (float) Math.sqrt(3);
                canvas.rotate(rotationX * 360, width / 2, y3 + s2 * offet - offet / s2 + paddingTop);
                canvas.drawCircle(width / 2, y3 + paddingTop, pointRadius * c0, p);
                canvas.drawCircle(width / 2 + offet * s1, y3 + offet * s2 + paddingTop, pointRadius * c0, p);
                canvas.drawCircle(width / 2 - offet * s1, y3 + offet * s2 + paddingTop, pointRadius * c0, p);

            }
        }


    }

    private float percent = 0;

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
        postInvalidate();
    }

    private float rotationX;
    final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
    public void startAnimation() {
        isPlaying = true;
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //percent = value;
                rotationX = value;
                postInvalidate();
                //invalidate();


            }
        });
        //animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);

        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void refreshAnimation(){
        if(!animator.isRunning()){
            percent=1;
            startAnimation();
        }
    }



    /**
     * dp转换成px
     * @param context Context
     * @param dp      dp
     * @return px值
     */
    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
