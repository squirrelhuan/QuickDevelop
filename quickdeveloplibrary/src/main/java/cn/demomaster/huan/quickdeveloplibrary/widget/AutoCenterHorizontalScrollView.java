package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

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

    private HAdapter adapter;
    ViewPager viewPager;
    ViewPager.OnPageChangeListener onPageChangeListener;

    public void setupWithViewPager(ViewPager viewPager) {
        if (viewPager != null) {
            this.viewPager = viewPager;
            if (onPageChangeListener == null) {
                onPageChangeListener = new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        setCurrentIndex(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                };
            }
            viewPager.addOnPageChangeListener(onPageChangeListener);
            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                // Now we'll populate ourselves from the pager adapter, adding an observer if
                // autoRefresh is enabled
                boolean autoRefresh = true;
                setPagerAdapter(adapter, autoRefresh);
                //adapter.setPrimaryItem();
            }
        }
    }

    private DataSetObserver pagerAdapterObserver;
    PagerAdapter pagerAdapter;

    void setPagerAdapter(@Nullable final PagerAdapter adapter, final boolean addObserver) {
        if (pagerAdapter != null && pagerAdapterObserver != null) {
            // If we already have a PagerAdapter, unregister our observer
            pagerAdapter.unregisterDataSetObserver(pagerAdapterObserver);
        }

        pagerAdapter = adapter;

        if (addObserver && adapter != null) {
            // Register our observer on the new adapter
            if (pagerAdapterObserver == null) {
                pagerAdapterObserver = new PagerAdapterObserver();
            }
            adapter.registerDataSetObserver(pagerAdapterObserver);
        }

        setOnSelectChangeListener(position -> viewPager.setCurrentItem(position));

        // Finally make sure we reflect the new adapter
        populateFromPagerAdapter();
    }

    void populateFromPagerAdapter() {
        removeAllTabs();

        if (pagerAdapter != null) {
            final int adapterCount = pagerAdapter.getCount();
            List<String> names = new ArrayList<>();
            for (int i = 0; i < adapterCount; i++) {
                //addTab(newTab().setText(pagerAdapter.getPageTitle(i)), false);
                names.add(pagerAdapter.getPageTitle(i) + "");
            }
            if (adapter != null) {
                adapter.setData(names);
                setAdapter(adapter);
            }
            // Make sure we reflect the currently set ViewPager item
            if (viewPager != null && adapterCount > 0) {
               /* final int curItem = viewPager.getCurrentItem();
                if (curItem != getSelectedTabPosition() && curItem < getTabCount()) {
                    selectTab(getTabAt(curItem));
                }*/
            }
        }
    }

    private void removeAllTabs() {
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() {
        }

        @Override
        public void onChanged() {
            populateFromPagerAdapter();
        }

        @Override
        public void onInvalidated() {
            populateFromPagerAdapter();
        }
    }

    public interface HAdapter {
        void setData(List<String> data);

        int getCount();//获取子view个数

        RecyclerView.ViewHolder getItemView(int position);//获取指定index的view

        void onSelectStateChanged(RecyclerView.ViewHolder itemView, int position, boolean isSelected);//改变选中状态
    }

    List<RecyclerView.ViewHolder> viewHolders = new ArrayList<>();

    //自己组装itemView
    public void setAdapter(final HAdapter adapter) {
        if (adapter == null) {
            return;
        }
        this.adapter = adapter;
        viewHolders.clear();
        removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getContext());
        for (int i = 0; i < adapter.getCount(); i++) {
            viewHolders.add(adapter.getItemView(i));
            LinearLayout linearLayout1 = new LinearLayout(getContext());
            linearLayout1.addView(viewHolders.get(i).itemView);
            linearLayout1.setTag(i);
            linearLayout1.setOnClickListener(view -> {
                int index = (int) view.getTag();
                if (adapter != null) {
                    //处理上一个
                    adapter.onSelectStateChanged(viewHolders.get(lastIndex), lastIndex, false);//触发选中事件回调
                    //处理当前选中的
                    adapter.onSelectStateChanged(viewHolders.get(index), index, true);//触发选中事件回调
                    lastIndex = index;
                }
                smoothScrollTo(getChildCenterPosition(index), 0);//点击某个item滚动到指定位置
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
            if (i == index) {
                offset_target = offset_tmp;
                setCurrent(i);
                return offset_target;
            }
            View child = viewGroup.getChildAt(i);
            int child_width = child.getWidth();
            offset_tmp = offset_tmp + child_width;
        }
        return 0;
    }

    private int paddingLeft = 0;//左侧内边距
    private int paddingRight = 0;//右侧内边距
    private float touchDown_X;//判断是否是点击还是滑动来用

    int gravity = Gravity.CENTER;

    public void setGravity(int gravity) {
        this.gravity = gravity;
        if (gravity == Gravity.CENTER) {
            setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());
        } else if (gravity == Gravity.LEFT) {
            setPadding(0, getPaddingTop(), 0, getPaddingBottom());
        }
    }

    void init() {
        //添加触摸事件，滑动事件会触发
        this.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {//按下事件记录x坐标
                touchDown_X = motionEvent.getX();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {//抬起事件判断是否是滑动事件
                if (touchDown_X != motionEvent.getX()) {//抬起事件则，触发
                    touchDown_X = motionEvent.getX();
                    handler.removeCallbacks(scrollerTask);
                    handler.postDelayed(scrollerTask, delayMillis);
                }
            }
            return false;
        });

        if (getChildCount() <= 0) {
            return;
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

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
                if (gravity == Gravity.CENTER) {
                    paddingLeft = getWidth() / 2 - first_width / 2;
                    paddingRight = getWidth() / 2 - last_width / 2;
                } else if (gravity == Gravity.LEFT) {
                    paddingLeft = 0;
                    paddingRight = 0;
                }
                setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());
                //设置默认位置
                setCurrentIndex(currentIndex);
            }
        });
    }

    /**
     * Runnable延迟执行的时间
     */
    private final long delayMillis = 100;
    /**
     * 上次滑动left，即x
     */
    private long lastScrollLeft = -1;
    private long nowScrollLeft = -1;
    private final Runnable scrollerTask = new Runnable() {
        @Override
        public void run() {
            if ((nowScrollLeft == lastScrollLeft)) {
                lastScrollLeft = nowScrollLeft;
                nowScrollLeft = -1;
                int index = getCurrentIndex();
                if (offset_target != offset_current) {
                    QDLogger.d(tag, "offset_target=" + offset_target + ",offset_current=" + offset_current);
                    smoothScrollTo(offset_target, 0);
                }
                if (adapter != null && adapter.getCount() > 0 && currentIndex < adapter.getCount()) {
                    //处理上一个
                    adapter.onSelectStateChanged(viewHolders.get(lastIndex), lastIndex, false);//触发选中事件回调
                    //处理当前选中的
                    adapter.onSelectStateChanged(viewHolders.get(index), index, true);//触发选中事件回调
                    lastIndex = index;
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
    private final Handler handler = new Handler();
    String tag = "AutoCenter";

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //QDLogger.println(tag, "left=" + l);
        // 更新ScrollView的滑动位置
        nowScrollLeft = l;
    }

    @Override
    protected int computeHorizontalScrollRange() {
        //QDLogger.println(tag, "横向总宽度 computeHorizontalScrollRange:" + super.computeHorizontalScrollRange());
        //QDLogger.println(tag, "computeHorizontalScrollRange2:" + (super.computeHorizontalScrollRange() + getWidth()));
        return super.computeHorizontalScrollRange() + paddingLeft + paddingRight;
    }

    @Override
    protected int computeHorizontalScrollOffset() {
        //QDLogger.println(tag, "当前位置 computeHorizontalScrollOffset:" + super.computeHorizontalScrollOffset());
        return super.computeHorizontalScrollOffset() + paddingLeft;
    }

    private int offset_target;//目标位置
    private int offset_current;//当前位置
    private int currentIndex = 0;//当前选中的item的position
    private int lastIndex = 0;//上一次选中的位置

    private void setCurrent(int currentIndex) {
        this.currentIndex = currentIndex;
        if (adapter != null && adapter.getCount() > 0 && currentIndex < adapter.getCount()) {
            //处理上一个
            adapter.onSelectStateChanged(viewHolders.get(lastIndex), lastIndex, false);//触发选中事件回调
            //处理当前选中的
            adapter.onSelectStateChanged(viewHolders.get(currentIndex), currentIndex, true);//触发选中事件回调
            lastIndex = currentIndex;
            if (onSelectChangeListener != null) {
                onSelectChangeListener.onSelectChange(currentIndex);
            }
        }
    }

    public void setCurrentIndex(int currentIndex) {
        setCurrent(currentIndex);
        if (getChildCount() <= 0) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return;
        }
        int offset_tmp = 0;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            child.measure(w, h);
            int child_width = child.getMeasuredWidth();
            if (i == currentIndex) {
                offset_target = offset_tmp;
                this.post(() -> smoothScrollTo(offset_target, 0));
                return;
            }
            offset_tmp = offset_tmp + child_width;
        }
    }

    //获取当前选中的item的position
    public int getCurrentIndex() {
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
            if (offset_tmp >= offset_current + viewGroup.getChildAt(0).getWidth() / 2) {
                offset_target = offset_tmp - child_width / 2 - viewGroup.getChildAt(0).getWidth() / 2;
                setCurrent(i);
                break;
            }
        }
        return currentIndex;
    }

    /**
     * 选中改变时触发回调
     */
    public OnSelectChangeListener onSelectChangeListener;

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.onSelectChangeListener = onSelectChangeListener;
        setCurrent(currentIndex);
    }

    public interface OnSelectChangeListener {
        void onSelectChange(int position);
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
