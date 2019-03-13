package cn.demomaster.huan.quickdeveloplibrary.view.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class QDTipContainerView extends FrameLayout {
    public QDTipContainerView( Context context) {
        super(context);
    }

    public QDTipContainerView( Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public QDTipContainerView( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QDTipContainerView( Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float arrowWidth;
    private boolean withArrow;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        RectF rectf  = new RectF(0,0,getWidth(),getHeight());
        canvas.drawRoundRect(rectf,50,50,paint);
        if(withArrow){

        }
    }
}
