package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * @author squirrel桓
 * @date 2018/12/14.
 * description：Android横向滚动居中的HorizontalScrollView
 */
public class AutoCenterHorizontalScrollView extends HorizontalScrollView {
    public AutoCenterHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public AutoCenterHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoCenterHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        init();
    }

    /**
     * 选中改变时触发回调
     */
    private OnSelectChangeListener onSelectChangeListener;

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.onSelectChangeListener = onSelectChangeListener;
    }

    public static interface OnSelectChangeListener {
        void onSelectChange(int position);
    }

    /**
     * itemView适配器很随意
     */
    private HAdapter adapter;

    public static interface HAdapter {
        int getCount();//获取子view个数

        View getItemView(int position);//获取指定index的view
    }

    //自己组装itemView
    public void setAdapter(HAdapter adapter) {
        this.adapter = adapter;
        removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getContext());
        for (int i = 0; i < adapter.getCount(); i++) {
            LinearLayout linearLayout1 = new LinearLayout(getContext());
            linearLayout1.addView(adapter.getItemView(i));
            linearLayout1.setTag(i);
            linearLayout1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();
                    if (onSelectChangeListener != null) {
                        onSelectChangeListener.onSelectChange(index);//触发选中事件回调
                    }
                    smoothScrollTo(getChildCenterPosition(index), 0);//点击某个item滚动到指定位置
                }
            });
            linearLayout.addView(linearLayout1);
        }
        addView(linearLayout);
        init();
    }

    /**
     * 获取item的X位置
     *
     * @param index
     * @return
     */
    private int getChildCenterPosition(int index) {
        currentIndex = index;
        offset_current = super.computeHorizontalScrollOffset();
        if (getChildCount() <= 0) {
            return 0;
        }
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return 0;
        }
        int offset_tmp = 0;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int child_width = child.getWidth();
            offset_tmp = offset_tmp + child_width;
            if (i == index) {
                offset_target = offset_tmp - child_width / 2 - viewGroup.getChildAt(0).getWidth() / 2;
                currentIndex = i;
                return offset_target;
            }
        }
        return 0;
    }

    private int paddingLeft = 0;//左侧内边距
    private int paddingRight = 0;//右侧内边距
    private float touchDown_X;//判断是否是点击还是滑动来用

    void init() {
        //添加触摸事件，滑动事件会触发
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {//按下事件记录x坐标
                    touchDown_X = motionEvent.getX();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {//抬起事件判断是否是滑动事件
                    if (touchDown_X != motionEvent.getX()) {//抬起事件则，触发
                        touchDown_X = motionEvent.getX();
                        handler.removeCallbacks(scrollerTask);
                        handler.postDelayed(scrollerTask, delayMillis);
                    }
                }
                return false;
            }
        });

        if (getChildCount() <= 0) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return;
        }
        //一下代码是设置padding,实现第一个itemview和最后一个能够居中
        View first = viewGroup.getChildAt(0);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        first.measure(w, h);
        int first_width = first.getMeasuredWidth();
        View last = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        last.measure(w, h);
        int last_width = last.getMeasuredWidth();
        paddingLeft = getScreenWidth(getContext()) / 2 - first_width / 2;
        paddingRight = getScreenWidth(getContext()) / 2 - last_width / 2;
        setPadding(paddingLeft, getPaddingTop(), paddingRight, getBottom());
    }

    /**
     * Runnable延迟执行的时间
     */
    private long delayMillis = 100;

    /**
     * 上次滑动left，即x
     */
    private long lastScrollLeft = -1;
    private long nowScrollLeft = -1;
    private Runnable scrollerTask = new Runnable() {
        @Override
        public void run() {
            if ((nowScrollLeft == lastScrollLeft)) {
                lastScrollLeft = nowScrollLeft;
                nowScrollLeft = -1;
                int index = getCurrentIndex();
                if (offset_target != offset_current) {
                    Log.d(tag, "offset_target=" + offset_target + ",offset_current=" + offset_current);
                    smoothScrollTo(offset_target, 0);
                }
                if (onSelectChangeListener != null) {
                    onSelectChangeListener.onSelectChange(index);
                }
            } else {
                lastScrollLeft = nowScrollLeft;
                postDelayed(this, delayMillis);
            }
        }
    };

    /**
     * 用来判断滚动是否滑动
     */
    private Handler handler = new Handler();
    String tag = "AutoCenter";

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i(tag, "left=" + l);
        // 更新ScrollView的滑动位置
        nowScrollLeft = l;
    }

    @Override
    protected int computeHorizontalScrollRange() {
        Log.i(tag, "横向总宽度 computeHorizontalScrollRange:" + super.computeHorizontalScrollRange());
        Log.i(tag, "computeHorizontalScrollRange2:" + (super.computeHorizontalScrollRange() + getScreenWidth(getContext())));
        return super.computeHorizontalScrollRange() + paddingLeft + paddingRight;
    }

    @Override
    protected int computeHorizontalScrollOffset() {
        Log.i(tag, "当前位置 computeHorizontalScrollOffset:" + super.computeHorizontalScrollOffset());
        return super.computeHorizontalScrollOffset() + paddingLeft;
    }

    private int offset_target;//目标位置
    private int offset_current;//当前位置
    private int currentIndex = 0;//当前选中的item的position

    //获取当前选中的item的position
    public int getCurrentIndex() {
        currentIndex = 0;
        offset_current = super.computeHorizontalScrollOffset();
        if (getChildCount() <= 0) {
            return 0;
        }
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return 0;
        }
        int offset_tmp = 0;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int child_width = child.getWidth();

            offset_tmp = offset_tmp + child_width;
            if (offset_tmp > offset_current) {
                offset_target = offset_tmp - child_width / 2 - viewGroup.getChildAt(0).getWidth() / 2;
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取 DisplayMetrics
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }


}
