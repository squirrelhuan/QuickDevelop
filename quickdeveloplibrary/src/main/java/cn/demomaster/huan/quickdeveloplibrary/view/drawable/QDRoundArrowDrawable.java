package cn.demomaster.huan.quickdeveloplibrary.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.popup.QDTipPopup;

/**
 * Created by Squirrel桓 on 2019/1/6.
 */
public class QDRoundArrowDrawable extends QDividerDrawable {

    PointF mPointF;
    private  GuiderView.Gravity mGravity;
    private View mAnchor;//相对view
    private ViewGroup mDrawView;//指定背景view

    /**
     *
     * @param anchor 相对view
     * @param gravity 相对位置
     * @param drawView 要设置背景的view
     */
    public QDRoundArrowDrawable(View anchor, GuiderView.Gravity gravity, ViewGroup drawView) {
        mDrawView = drawView;
        mAnchor = anchor;
        mGravity = gravity;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getBackGroundColor());
        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setColor(getBackGroundColor());
        mArrow = new Path();
    }

    Path mArrow;
    Paint mPaint,pathPaint;
    Rect mRect;
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect = bounds;

        /*************   设置三角形   ************/
        PointF centerPointF = new PointF();
        mArrow.reset();
        int value = arrowHeight;//DisplayUtil.dip2px(mAnchor.getContext(),10);//(int) (bounds.height() * 0.2f);
        float r = .8f;
        switch (mGravity){
            case TOP:
                if(bounds.width()>mAnchor.getWidth()){
                    // 设置好参数之后再show
                    int[] location = new int[2];
                    mDrawView.getLocationOnScreen(location);
                    int[] location2 = new int[2];
                    mAnchor.getLocationOnScreen(location2);
                    centerPointF = new PointF(-location[0]+location2[0]+mAnchor.getWidth()/2,0);
                }else {
                    centerPointF = new PointF(bounds.width()/2,0);
                }
                mArrow.moveTo(centerPointF.x, mRect.bottom);
                mArrow.lineTo(centerPointF.x-value/2, mRect.bottom-value*r);
                mArrow.lineTo(centerPointF.x+value/2, mRect.bottom-value*r);
                break;
            case BOTTOM:
                if(bounds.width()>mAnchor.getWidth()){
                    // 设置好参数之后再show
                    int[] location = new int[2];
                    mDrawView.getLocationOnScreen(location);
                    int[] location2 = new int[2];
                    mAnchor.getLocationOnScreen(location2);
                    centerPointF = new PointF(-location[0]+location2[0]+mAnchor.getWidth()/2,0);
                }else {
                    centerPointF = new PointF(bounds.width()/2,0);
                }
                //centerPointF = new PointF(bounds.width()/2,mAnchor.getHeight());
                mArrow.moveTo(centerPointF.x, mRect.top);
                mArrow.lineTo(centerPointF.x-value/2, mRect.top+value*r);
                mArrow.lineTo(centerPointF.x+value/2, mRect.top+value*r);
                break;
            case LEFT:
                centerPointF = new PointF(bounds.width(),mAnchor.getHeight()/2);
                mArrow.moveTo(centerPointF.x, centerPointF.y);
                mArrow.lineTo(centerPointF.x-value*r, centerPointF.y-value/2);
                mArrow.lineTo(centerPointF.x-value*r, centerPointF.y+value/2);
                break;
            case RIGHT:
                centerPointF = new PointF(0,mAnchor.getHeight()/2);
                mArrow.moveTo(centerPointF.x, centerPointF.y);
                mArrow.lineTo(centerPointF.x+value*r, centerPointF.y-value/2);
                mArrow.lineTo(centerPointF.x+value*r, centerPointF.y+value/2);
                break;
        }
        mArrow.close();

        value = (int) (value*r-1);
        /*************   设置padding   ************/
        switch (mGravity){
            case TOP:
                mRect.bottom=mRect.bottom-value;
                mDrawView.setPadding(0,0,0,value);
                break;
            case LEFT:
                mRect.right=mRect.right-value;
                mDrawView.setPadding(0,0,value,0);
                break;
            case RIGHT:
                mRect.left=mRect.left+value;
                mDrawView.setPadding(value,0,0,0);
                break;
            case BOTTOM:
                mRect.top=mRect.top+value;
                mDrawView.setPadding(0,value,0,0);
                break;
        }

        invalidateSelf();
    }

    private boolean withArrow;
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        pathPaint.setColor(getBackGroundColor());
        canvas.drawPath(mArrow, pathPaint);
        /*get
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        RectF rectf  = new RectF(0,0,100,100);
        canvas.drawRoundRect(rectf,20,20,paint);
        if(withArrow){

        }*/
    }
    private int arrowHeight;

    public void setArrowHeight(int arrowHeight) {
        this.arrowHeight = arrowHeight;
        invalidateSelf();
    }

}
