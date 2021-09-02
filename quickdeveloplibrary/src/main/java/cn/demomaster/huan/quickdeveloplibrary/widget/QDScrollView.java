package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 带回弹效果的NestedScrollView
 */
public class QDScrollView extends androidx.core.widget.NestedScrollView {
    public QDScrollView(Context context) {
        super(context);
        init();
    }

    public QDScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QDScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public QDScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }*/

    private void init() {
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    Rect originalRect = new Rect();
    //记录初始位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getChildlayout();
        //QDLogger.println("onLayout l="+l+",t="+t+",r="+r+",b="+b);
        /*if(getChildCount()>0) {
            View convertView = getChildAt(0);
            if (convertView != null) {
                //用rect记录 scrollview的子控件的上下左右
                originalRect.set(convertView.getLeft(), convertView.getTop(), convertView.getRight(), convertView.getBottom());
                QDLogger.println("onLayout left="+convertView.getLeft()+",top="+convertView.getTop()+",right="+convertView.getRight()+",bottom="+convertView.getBottom());
            }
        }*/
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
         boolean b = super.onInterceptTouchEvent(ev);
        //QDLogger.println("父视图 onInterceptTouchEvent2="+b);
        return b;
    }

    float startY = 0, startX = 0;
    int currentTop;
    //  事件分发
    int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                break;
        }
        boolean b = super.dispatchTouchEvent(ev);
        //QDLogger.println("父视图 dispatchTouchEvent ="+b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //QDLogger.i("onTouchEvent y="+ev.getY());
        if(getChildCount()>0) {
            View convertView = getChildAt(0);
            if (convertView != null) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = ev.getY();
                        startX = ev.getX();
                        if(childAnimator!=null&&childAnimator.isRunning()){
                            childAnimator.cancel();
                            currentTop = convertView.getTop()-originalRect.top;
                        }else {
                            currentTop = 0;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        //还原位置，回弹动画， 可以自己定于需要的动画
                        /*TranslateAnimation animation = new TranslateAnimation(0, 0, convertView.getTop(), originalRect.top);
                        animation.setDuration(200);
                        animation.setRepeatCount(0);
                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                        //convertView.setAnimation(animation);
                        convertView.startAnimation(animation);*/
                        //getChildlayout();
                        getChildlayout();
                        if(childAnimator!=null&&childAnimator.isRunning()){
                            childAnimator.cancel();
                        }
                        childAnimator = ValueAnimator.ofInt(convertView.getTop(), originalRect.top);
                        childAnimator.setDuration(200);
                        childAnimator.addUpdateListener(animation -> {
                                convertView.layout(originalRect.left,
                                        (Integer) animation.getAnimatedValue(),
                                originalRect.right,
                                        (Integer) animation.getAnimatedValue()+originalRect.height());}
                        );
                        childAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                        childAnimator.start();
                        //convertView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int detalY = (int) (ev.getY() - startY);
                        int detalX = (int) (ev.getX() - startX);
                        //QDLogger.i("detalY="+detalY+",startY="+startY+",ev.getY()="+ev.getY());
                        if (2 < Math.abs(detalY)) {
                            //detalY 乘以0.2 使得很难的效果
                            //getChildlayout();
                            int top = (int) (originalRect.top + detalY * 0.2)+currentTop;
                            convertView.layout(originalRect.left,top,
                                    originalRect.right, top+convertView.getMeasuredHeight());
                        }
                        break;
                }
            }
        }
        boolean b = super.onTouchEvent(ev);
        //QDLogger.i("父视图 onTouchEvent="+b);
        return b;
    }

    ValueAnimator childAnimator;
    private void getChildlayout() {
        if (getChildCount() > 0) {
            View convertView = getChildAt(0);
            final int width = convertView.getMeasuredWidth();
            final int height = convertView.getMeasuredHeight();
            final LayoutParams lp = (LayoutParams) convertView.getLayoutParams();
            int gravity = lp.gravity;
            if (gravity == -1) {
                gravity = DEFAULT_CHILD_GRAVITY;
            }

            int layoutDirection = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutDirection = getLayoutDirection();
            }
            final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
            final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
            int childLeft = 0;
            int childTop = 0;
            switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = getPaddingLeft() + lp.leftMargin + (getMeasuredWidth() - getPaddingLeft() - lp.leftMargin - getPaddingRight() - lp.rightMargin) / 2 - width / 2;
                    break;
                case Gravity.RIGHT:
                    // if (!forceLeftGravity) {
                    childLeft = getMeasuredWidth() - getPaddingRight() - width - lp.rightMargin;
                    break;
                // }
                case Gravity.LEFT:
                default:
                    childLeft = getPaddingLeft() + lp.leftMargin;
            }

            switch (verticalGravity) {
                case Gravity.TOP:
                    childTop = getPaddingTop() + lp.topMargin;
                    break;
                case Gravity.CENTER_VERTICAL:
                    childTop = lp.topMargin + getPaddingTop()
                            + (getMeasuredHeight() - lp.topMargin - getPaddingTop() - lp.bottomMargin - getPaddingBottom()) / 2 - height / 2;
                    break;
                case Gravity.BOTTOM:
                    childTop = getMeasuredHeight() - getPaddingBottom() - lp.bottomMargin - height;
                    break;
                default:
                    childTop = getPaddingTop() + lp.topMargin;
            }
            originalRect.set(childLeft, childTop, childLeft + width, childTop + height);
        }
    }
}
