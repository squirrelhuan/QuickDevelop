package cn.demomaster.huan.quickdevelop.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by moon.zhong.
 */
public class SlidingTabLayout extends HorizontalScrollView {
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS_TB = 15;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 14;
    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;

    private ViewPager mViewPager;
    private final SlidingTabView mTabStrip;

    private int mWidth ;
    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setHorizontalScrollBarEnabled(false);

        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabView(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        DisplayMetrics displayMetrics = new DisplayMetrics() ;
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = (int) (displayMetrics.widthPixels) ;
    }
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();
        populateTabStrip();
    }
    private void populateTabStrip() {
        List<String> names =new ArrayList<>();
        for(int i=0;i<50;i++){
            names.add("第几"+i+"个");
        }
        final OnClickListener tabClickListener = new TabClickListener();
        /**/
        /*通过 viewPager 的 item 来确定tab 的个数*/

        for (int i = 0; i < names.size(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
                /*创建textView*/
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            tabTitleView.setText(names.get(i));
            tabView.setOnClickListener(tabClickListener);
            tabView.setBackgroundResource(R.drawable.ic_launcher_background);
            /*把 textView 放入到自定义的 tab 栏中*/
            mTabStrip.addView(tabView);
        }
    }
    /*这里就是创建 textView，没什么可讲的*/
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            textView.setAllCaps(true);
        }

        int paddingTB = (int) (TAB_VIEW_PADDING_DIPS_TB * getResources().getDisplayMetrics().density);
        textView.setPadding(0, paddingTB, 0, paddingTB);
        textView.setTextColor(0xff666666);
        int width = (int) (100 * getResources().getDisplayMetrics().density);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT) ;
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * 这个方法是关键
     * 滚动 scrollview
     * @param tabIndex
     * @param positionOffset
     */
    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        /*获取当前选中的 item*/
        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            /*获取当前 item 的偏移量*/
            int targetScrollX = selectedChild.getLeft() + positionOffset;
            /*item 的宽度*/
            int width = selectedChild.getWidth();
            /*item 距离正中间的偏移量*/
            mTitleOffset = (int) ((mWidth-width)/2.0f);

            if (tabIndex > 0 || positionOffset > 0) {
                /*计算出正在的偏移量*/
                targetScrollX -= mTitleOffset;
            }
            QDLogger.v("zgy","==================mWidth======="+mWidth) ;
            /*这个时候偏移的量就是屏幕的正中间*/
            scrollTo(targetScrollX, 0);
        }
    }


    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.viewPagerChange(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);


        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.viewPagerChange(position, 0f);
                scrollToTab(position, 0);
            }

        }

    }
    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    return;
                }
            }
        }
    }
}