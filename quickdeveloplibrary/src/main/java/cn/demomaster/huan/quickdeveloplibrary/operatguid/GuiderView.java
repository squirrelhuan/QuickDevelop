package cn.demomaster.huan.quickdeveloplibrary.operatguid;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;

import static cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderModel.TouchType.TargetView;

/**
 * Created by Squirrel桓 on 2019/1/12.
 */
public class GuiderView extends View {

    private boolean hasStateBar;
    private GuiderModel guiderModel;
    private GuiderActionDialog.OnActionFinishListener onActionFinishListener;

    float screenHeight;
    float screenWidth;

    public GuiderView(Context context, GuiderModel guiderModel, boolean hasStateBar, GuiderActionDialog.OnActionFinishListener onActionFinishListener) {
        super(context);
        this.guiderModel = guiderModel;
        this.hasStateBar = hasStateBar;
        this.onActionFinishListener = onActionFinishListener;

        screenHeight = getWidth();
        screenWidth = getHeight();
    }

    private ViewGroup windowView;

    public GuiderView(Context context, GuiderModel guiderModel, ViewGroup windowView, GuiderActionDialog.OnActionFinishListener onActionFinishListener) {
        super(context);
        this.guiderModel = guiderModel;
        this.windowView = windowView;
        this.onActionFinishListener = onActionFinishListener;

        screenHeight = QMUIDisplayHelper.getScreenHeight(getContext());
        screenWidth = QMUIDisplayHelper.getScreenWidth(getContext());

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    public GuiderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GuiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public GuiderModel getGuiderModel() {
        return guiderModel;
    }

    public void setGuiderModel(GuiderModel guiderModel) {

        if(animator.isRunning()){
            animator.cancel();
        }
        isPlaying = false;
        this.guiderModel = guiderModel;
        this.rectF_view =null;
        this.rectF_message =null;
        postInvalidate();
    }

    private boolean isPlaying = false;

    private int duration = 1200;
    private float durations[] = {.0f, .4f, .65f, .95f, 1f};//%百分比叠加
    private int animationIndex = 0;
    private float progress = 0;
    private int backgroundColor =0xccffffff;
    private ValueAnimator animator;

    public void startAnimation() {
        isPlaying = true;
        //value值0-1
        animator = ValueAnimator.ofFloat(0f, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                W:
                for (int i = durations.length - 1; i >= 0; i--) {
                    if (value >= durations[i]) {
                        float next = 0;
                        if ((i - 1) < 0) {
                            next = 0;
                        } else {
                            next = durations[i - 1];
                        }
                        progress = (value - durations[i]) / (durations[i] - next);
                        animationIndex = i;
                        break W;
                    }
                }
                postInvalidate();
            }
        });
        //animator.setRepeatMode(ValueAnimator.RESTART);
        //animator.setRepeatCount(ValueAnimator.INFINITE);
        //new AccelerateInterpolator()
        //animator.setInterpolator(new AccelerateInterpolator());
        animator.setInterpolator(new LinearInterpolator());
        //animator.setInterpolator(new CycleInterpolator());
        animator.start();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (guiderModel != null) {
            drawGuider(canvas);
        }
        if (!isPlaying) {
            startAnimation();
        }
    }

    private GuiderRectF rectF_view;
    private GuiderRectF rectF_message;
    private RectF rectF_background;

    private void drawGuider(Canvas canvas) {

        if(guiderModel.getTargetView()==null){
            return;
        }
        //初始化数据
        if (rectF_view == null) {
            rectF_view = getViewRectF(guiderModel.getTargetView().get());
        }
        if (rectF_message == null) {
            Paint paint = getMessagePaint();
            rectF_message = getMessageRectF(rectF_view, paint, guiderModel.getMessage());
        }


        //step1透明度
        drawBackground(canvas);

        //drawTargetView
        drawTargetView(canvas);

        //drawLine
        drawLinePath(canvas);

        drawMessageBox(canvas);

        //写字
        if (animationIndex > 3) {
            //paint.setTextSize(guiderModel.getTextSize());
            //paint.setColor(guiderModel.getTextColor());
            //canvas.drawText(guiderModel.getMessage(), messageContentRectf.left, messageContentRectf.bottom, paint);

            TextPaint textPaint = new TextPaint();
            textPaint.setColor(guiderModel.getTextColor());
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(guiderModel.getTextSize());
            String message = guiderModel.getMessage();
            StaticLayout myStaticLayout = new StaticLayout(message, textPaint, (int) (canvas.getWidth() * .8f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            int heightText = myStaticLayout.getHeight();
            canvas.save();
            canvas.translate((int) (canvas.getWidth() * .1f), messageContentRectf.top);
            myStaticLayout.draw(canvas);
            canvas.restore();
            initTouch();
        }
    }

    /**
     * step4 描边
     *
     * @param canvas
     */
    private void drawMessageBox(Canvas canvas) {
        float progress_c = progress;
        if (animationIndex < 3) {
            return;
        } else if (animationIndex > 3) {
            progress_c = 1;
        }
        Paint paint = new Paint();
        paint.setStrokeWidth(guiderModel.getLineWidth());

        //canvas.drawRect(rectF_message, paint);
        //使用离屏绘制
        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(null);
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        if (guiderModel.getShape() == GuiderModel.SHAPE.oval) {//椭圆
            canvas.drawArc(rectF_message, 0, 360, true, paint);
        } else if (guiderModel.getShape() == GuiderModel.SHAPE.rectangle) {//矩形
            canvas.drawRect(rectF_message, paint);
        } else {
            int radio = (int) (Math.min(rectF_message.width(), rectF_message.height()) / 2);
            canvas.drawRoundRect(rectF_message, radio, radio, paint);
        }

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(guiderModel.getLineColor());
        paint.setTextSize(guiderModel.getTextSize());
        float min = Math.min(rectF_message.width(), rectF_message.height()) / 2;
        RectF rectF1 = new RectF(rectF_message.left - min, rectF_message.top - min, rectF_message.right + min, rectF_message.bottom + min);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int startAngle = getStartAngle(1);

        canvas.drawArc(rectF1, startAngle, progress_c * 360, true, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);

        canvas.restoreToCount(layerID);


    }

    /**
     * 获取文字画笔
     *
     * @return
     */
    private Paint getMessagePaint() {
        Paint paint = new Paint();
        paint.setTextSize(guiderModel.getTextSize());
        return paint;
    }

    /**
     * 绘制背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {

        Paint paint = new Paint();
        //使用离屏绘制
        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        rectF_background = new RectF(0, 0, screenWidth, screenHeight);
        canvas.drawRect(rectF_background, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(rectF_view, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        //使用CLEAR作为PorterDuffXfermode绘制的矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectF_view, paint);

        canvas.restoreToCount(layerID);
    }

    private void drawTargetView(Canvas canvas) {
        float progress2 = progress;
        if(animationIndex==0){
            return;
        }if (animationIndex > 1) {
            progress2 = 1;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(null);
        paint.setStrokeWidth(guiderModel.getLineWidth());
        //使用离屏绘制
        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);



        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectF_view, paint);

        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
       // paint.setXfermode(null);
       // canvas.drawRect(rectF_view, paint);

        float min = Math.min(rectF_message.width(), rectF_message.height()) / 2;
        RectF rectF1 = new RectF(rectF_view.left - min, rectF_view.top - min, rectF_view.right + min, rectF_view.bottom + min);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(guiderModel.getLineColor());
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int startAngle = getStartAngle(0);

        canvas.drawArc(rectF1, startAngle, progress2 * 360, true, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);

        //使用离屏绘制
        canvas.restoreToCount(layerID);

    }


    private int getStartAngle(int type) {
        if (type == 0) {//目标视图初始角度
            switch (gravity) {
                case TOP:
                    return -90;
                case BOTTOM:
                    return 90;
                case LEFT:
                    return 180;
                case RIGHT:
                    return 0;
            }
        } else if (type == 1) {//提示内容视图初始角度
            switch (gravity) {
                case TOP:
                    return 90;
                case BOTTOM:
                    return -90;
                case LEFT:
                    return 0;
                case RIGHT:
                    return 180;
            }
        }
        return 0;
    }

    RectF rectF_touch = new RectF();

    private void initTouch() {
        if (guiderModel.getTouchType() == null) {
            guiderModel.setTouchType(TargetView);
        }
        switch (guiderModel.getTouchType()) {
            case TargetView:
                rectF_touch = rectF_view;
                break;
            case TipView:
                rectF_touch = rectF_message;
                break;
            case Other:
                rectF_touch = rectF_background;
                break;
        }

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (guiderModel.getComplateType()) {
                    case CLICK:
                        if (motionEvent.getX() > rectF_touch.left && motionEvent.getX() < rectF_touch.right && motionEvent.getY() > rectF_touch.top && motionEvent.getY() < rectF_touch.bottom) {
                            if (onActionFinishListener != null) {
                                onActionFinishListener.onFinish();
                               // windowView.removeView(GuiderView.this);
                                if (animator.isRunning()) {
                                    animator.cancel();
                                }
                            }
                        }
                        break;
                }
                return false;
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    private void drawLinePath(Canvas canvas) {
        float progress_c = progress;
        if (animationIndex < 2) {
            return;
        } else if (animationIndex > 2) {
            progress_c = 1;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(guiderModel.getLineWidth());
        //使用离屏绘制
        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);

        PointF pointF_start = new PointF(rectF_view.getCenterX(), rectF_view.getCenterY());
        PointF pointF_end = new PointF(rectF_message.getCenterX(), rectF_message.getCenterY());
        switch (gravity) {
            case TOP:
                pointF_start = new PointF(rectF_view.centerX, rectF_view.top);
                pointF_end = new PointF(rectF_message.centerX, rectF_message.bottom);
                break;
            case LEFT:
                pointF_start = new PointF(rectF_view.left, rectF_view.centerY);
                pointF_end = new PointF(rectF_message.right, rectF_message.centerY);
                break;
            case RIGHT:
                pointF_start = new PointF(rectF_view.right, rectF_view.centerY);
                pointF_end = new PointF(rectF_message.left, rectF_message.centerY);
                break;
            case BOTTOM:
                pointF_start = new PointF(rectF_view.centerX, rectF_view.bottom);
                pointF_end = new PointF(rectF_message.centerX, rectF_message.top);
                break;
        }

        Path path = new Path();
        path.moveTo(pointF_start.x, pointF_start.y);
        //曲线有两个阶段,一个中间过度点
        PointF pointF_center = new PointF((pointF_start.x + pointF_end.x) / 2, (pointF_start.y + pointF_end.y) / 2);
        //path.quadTo(pointF_start.x, pointF_center.y, pointF_center.x, pointF_center.y);
        if (pointF_start.x == pointF_center.x || pointF_start.y == pointF_center.y) {
            path.lineTo(pointF_end.x, pointF_end.y);
        } else {
            path.quadTo(pointF_start.x, pointF_center.y, pointF_center.x, pointF_center.y);
            path.quadTo(pointF_end.x, pointF_center.y, (pointF_end.x - pointF_start.x) + pointF_start.x, pointF_end.y);
        }
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        paint.setColor(guiderModel.getLineColor());
        int l = Math.min((int) pointF_start.x, (int) pointF_end.x);
        int t = Math.min((int) pointF_start.y, (int) pointF_end.y);
        int r = Math.max((int) pointF_start.x, (int) pointF_end.x);
        int b = Math.max((int) pointF_start.y, (int) pointF_end.y);
        if (pointF_start.x > pointF_end.x)
        {
            l = (int) (r - Math.abs(r - l) * progress_c);
        }else if(pointF_start.x < pointF_end.x){
            r = (int) (l + Math.abs(l - r) * progress_c);
        }else{
            l = l - getWidth()/2;
            r = r + getWidth()/2;
        }
        if (pointF_start.y > pointF_end.y){
            t = (int) (b - Math.abs(t - b) * progress_c);
        } else if(pointF_start.y < pointF_end.y){
            b = (int) (t + Math.abs(t - b) * progress_c);
        }
        canvas.clipRect(l, t, r, b);
        //canvas.drawRect(l, t, r, b,paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(guiderModel.getLineColor());
        canvas.drawPath(path, paint);
        canvas.restoreToCount(layerID);
    }

    private Gravity gravity;

    private GuiderRectF messageContentRectf;

    private GuiderRectF getMessageRectF(GuiderRectF rectF_view, Paint paint, String text) {
        gravity = rectF_view.getGavity();

        // 文字宽
        float textWidth = paint.measureText(text);
        // 文字baseline在y轴方向的位置
        float baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2;
        // 文字baseline在y轴方向的位置
        float descent = Math.abs(paint.descent());

        float heightText = (baseLineY + descent) * 2;
        float maxwidth = getWidth() * 0.8f;
        if (textWidth >= maxwidth) {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(guiderModel.getTextColor());
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(guiderModel.getTextSize());
            String message = guiderModel.getMessage();
            StaticLayout myStaticLayout = new StaticLayout(message, textPaint, (int) (getWidth() * .8f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            heightText = myStaticLayout.getHeight();
            textWidth = getWidth() * .8f;
        }

        float l = screenWidth / 2 - textWidth / 2;
        float t = screenHeight / 2 - heightText;
        float r = screenWidth / 2 + textWidth / 2;
        float b = screenHeight / 2 + heightText;
        float dest_x = 0;
        switch (gravity) {
            case TOP:
                dest_x = rectF_view.getToTop() * .2f;
                b = rectF_view.top - dest_x;
                t = b - heightText;
                break;
            case BOTTOM:
                dest_x = rectF_view.getToBottom() * .2f;
                t = rectF_view.bottom + dest_x;
                b = t + heightText;
                break;
            case LEFT:
                dest_x = rectF_view.getToLeft() * .2f;
                r = rectF_view.left - dest_x;
                l = r - textWidth / 2;
                break;
            case RIGHT:
                dest_x = rectF_view.getToRight() * .2f;
                l = rectF_view.left + dest_x;
                r = l + textWidth / 2;
                break;
        }

        int pading = 50;
        messageContentRectf = new GuiderRectF(l, t, r, b, screenWidth, screenHeight);
        return new GuiderRectF(l - pading, t - pading, r + pading, b + pading, screenWidth, screenHeight);
    }

    public enum Gravity {
        LEFTTOP, LEFTBOTTOM, RIGHTTOP, RIGHTBOTTOM, TOP, LEFT, RIGHT, BOTTOM
    }


    private GuiderRectF getViewRectF(View view) {
        int[] location;
        location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        System.out.println("view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);

        float l = location[0];
        float t = location[1];
        float r = location[0] + view.getWidth();
        float b = location[1] + view.getHeight();
        if (hasStateBar) {
            float stateBarHeight = QMUIDisplayHelper.getStatusBarHeight(getContext());
            t = t - stateBarHeight;
            b = b - stateBarHeight;
        }
        return new GuiderRectF(l, t, r, b, screenWidth, screenHeight);
    }

    public class GuiderRectF extends RectF {
        private float centerX;
        private float centerY;
        private float toTop;
        private float toBottom;
        private float toLeft;
        private float toRight;

        float screenHeight;
        float screenWidth;

        public GuiderRectF(float l, float t, float r, float b, float screenWidth, float screenHeight) {
            left = l;
            right = r;
            top = t;
            bottom = b;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            getCenterX();
            getCenterY();
        }

        public float getCenterX() {
            centerX = (left + right) / 2;
            return centerX;
        }

        public float getCenterY() {
            centerY = (top + bottom) / 2;
            return centerY;
        }

        public float getToTop() {
            toTop = top;
            return toTop;
        }

        public float getToBottom() {
            toBottom = (screenHeight - bottom);
            return toBottom;
        }

        public float getToLeft() {
            toLeft = left;
            return toLeft;
        }

        public float getToRight() {
            toRight = (screenWidth - right);
            return toRight;
        }

        public Gravity getGavity() {
            if (getToLeft() > getToRight() && getToLeft() > getToTop() && getToLeft() > getToBottom()) {
                return Gravity.LEFT;
            }
            if (getToRight() > getToLeft() && getToRight() > getToTop() && getToRight() > getToBottom()) {
                return Gravity.RIGHT;
            }
            if (getToTop() > getToBottom() && getToTop() > getToLeft() && getToTop() > getToRight()) {
                return Gravity.TOP;
            }
            if (getToBottom() > getToTop() && getToBottom() > getToLeft() && getToBottom() > getToRight()) {
                return Gravity.BOTTOM;
            }
            return Gravity.BOTTOM;
        }
    }
}
