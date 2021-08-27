package cn.demomaster.huan.quickdeveloplibrary.view.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;

/**
 * Created by Squirrel桓 on 2019/1/6.
 */
public class QDRoundArrowDrawable extends QDividerDrawable {

    PointF mPointF;
    private GuiderView.Gravity mGravity;
    private View mAnchor;//相对view
    private ViewGroup mDrawView;//指定背景view

    /**
     * @param anchor   相对view
     * @param gravity  相对位置
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
    Paint mPaint, pathPaint;
    Rect mRect;

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect = bounds;

        /*************   设置三角形   ************/
        PointF centerPointF = new PointF();
        mArrow.reset();
        int valueH = arrowHeight;
        int valueW = arrowWidth;//DisplayUtil.dip2px(mAnchor.getContext(),10);//(int) (bounds.height() * 0.2f);
        float r = .8f;
        switch (mGravity) {
            case TOP:
                if (bounds.width() > mAnchor.getWidth()) {
                    // 设置好参数之后再show
                    int[] location = new int[2];
                    mDrawView.getLocationOnScreen(location);
                    int[] location2 = new int[2];
                    mAnchor.getLocationOnScreen(location2);
                    centerPointF = new PointF(-location[0] + location2[0] + mAnchor.getWidth() / 2f, 0);
                } else {
                    centerPointF = new PointF(bounds.width() / 2f, 0);
                }
                mArrow.moveTo(centerPointF.x, mRect.bottom);
                mArrow.lineTo(centerPointF.x - valueW / 2f, mRect.bottom - valueH * r);
                mArrow.lineTo(centerPointF.x + valueW / 2f, mRect.bottom - valueH * r);
                break;
            case BOTTOM:
                if (bounds.width() > mAnchor.getWidth()) {
                    // 设置好参数之后再show
                    int[] location = new int[2];
                    mDrawView.getLocationOnScreen(location);
                    int[] location2 = new int[2];
                    mAnchor.getLocationOnScreen(location2);
                    centerPointF = new PointF(-location[0] + location2[0] + mAnchor.getWidth() / 2f, 0);
                } else {
                    centerPointF = new PointF(bounds.width() / 2f, 0);
                }
                //centerPointF = new PointF(bounds.width()/2,mAnchor.getHeight());
                mArrow.moveTo(centerPointF.x, mRect.top);
                mArrow.lineTo(centerPointF.x - valueW / 2f, mRect.top + valueH * r);
                mArrow.lineTo(centerPointF.x + valueW / 2f, mRect.top + valueH * r);
                break;
            case LEFT:
                centerPointF = new PointF(bounds.width(), mAnchor.getHeight() / 2f);
                mArrow.moveTo(centerPointF.x, centerPointF.y);
                mArrow.lineTo(centerPointF.x - valueW * r, centerPointF.y - valueH / 2f);
                mArrow.lineTo(centerPointF.x - valueW * r, centerPointF.y + valueH / 2f);
                break;
            case RIGHT:
                centerPointF = new PointF(0, mAnchor.getHeight() / 2f);
                mArrow.moveTo(centerPointF.x, centerPointF.y);
                mArrow.lineTo(centerPointF.x + valueW * r, centerPointF.y - valueH / 2f);
                mArrow.lineTo(centerPointF.x + valueW * r, centerPointF.y + valueH / 2f);
                break;
        }
        mArrow.close();

        valueH = (int) (valueH * r - 1);
        valueW = (int) (valueW * r - 1);
        /*************   设置padding   ************/
        switch (mGravity) {
            case TOP:
                mRect.bottom = mRect.bottom - valueH;
                mDrawView.setPadding(0, 0, 0, valueH);
                break;
            case LEFT:
                mRect.right = mRect.right - valueW;
                mDrawView.setPadding(0, 0, valueW, 0);
                break;
            case RIGHT:
                mRect.left = mRect.left + valueW;
                mDrawView.setPadding(valueW, 0, 0, 0);
                break;
            case BOTTOM:
                mRect.top = mRect.top + valueH;
                mDrawView.setPadding(0, valueH, 0, 0);
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
    private int arrowWidth;

    public void setArrowHeight(int arrowHeight) {
        this.arrowHeight = arrowHeight;
        invalidateSelf();
    }

    public void setArrowWidth(int arrowWidth) {
        this.arrowWidth = arrowWidth;
        invalidateSelf();
    }

    public void setArrowSize(int arrowWidth, int arrowHeight) {
        this.arrowWidth = arrowWidth;
        this.arrowHeight = arrowHeight;
        invalidateSelf();
    }
}
