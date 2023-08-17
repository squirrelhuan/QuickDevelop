/*
 * Copyright (C) 2011 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.demomaster.huan.quickdeveloplibrary.widget;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING;
import static com.google.android.material.animation.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Observable;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.internal.ViewUtils;
import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifecycleView;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.TabAdapter;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuLayout;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * I'm using a custom tab view in place of ActionBarTabs entirely for the theme
 * engine.
 */
public class ScrollableTabView extends HorizontalScrollView implements
        ViewPager.OnPageChangeListener {
    private final RecyclerViewDataObserver mObserver = new RecyclerViewDataObserver();
    private ViewPager mPager = null;
    private ViewPager2 mPager2 = null;

    private TabAdapter mTabAdapter = null;

    private final HLinearLayout mContainer;

    //private final ArrayList<View> mTabs = new ArrayList<>();

    public ScrollableTabView(Context context) {
        this(context, null);
    }

    public ScrollableTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    SlidingTabIndicator slidingTabIndicator;

    public ScrollableTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);

        mContainer = new HLinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.setTag("hContainer");
        mContainer.setLayoutParams(params);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);

        //Add the TabStrip
        slidingTabIndicator = new SlidingTabIndicator(context);
        slidingTabIndicator.setMinimumHeight(DisplayUtil.dip2px(getContext(),5));
        slidingTabIndicator.setMinimumWidth(200);
        slidingTabIndicator.setBackgroundColor(Color.RED);
        TextView textView = new TextView(getContext());
        textView.setText("123");
        slidingTabIndicator.addView(textView);
        mContainer.setIndicatorView(slidingTabIndicator);
        this.addView(mContainer);
    }

    public void setTabsAdapter(TabAdapter adapter) {
        if (mTabAdapter != null) {
            mTabAdapter.unregisterAdapterDataObserver(mObserver);
            //mTabAdapter.onDetachedFromRecyclerView(this);
        }
        this.mTabAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
            //adapter.onAttachedToRecyclerView(this);
        }
        initTabs();
    }

    public void setViewPager(ViewPager pager) {
        this.mPager = pager;
        mPager.removeOnPageChangeListener(this);
        mPager.addOnPageChangeListener(this);
        //mPager.setOnPageChangeListener(this);
        if (mPager != null && mTabAdapter != null)
            initTabs();
    }

    public void setViewPager(ViewPager2 pager) {
        this.mPager2 = pager;
        mPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updataPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        if (mPager2 != null && mTabAdapter != null)
            initTabs();
    }

    private void initTabs() {
        mContainer.removeAllViews();
        //mTabs.clear();
        if (mTabAdapter == null) {
            return;
        }
        if (mPager != null) {
            for (int i = 0; i < mPager.getAdapter().getCount(); i++) {
                final int index = i;
                View tab = mTabAdapter.getView(i, mContainer);
                if (tab == null) {
                    return;
                }
                if (tab.getParent() == null) {
                    mContainer.addView(tab);
                }
                tab.setFocusable(true);
                //mTabs.add(tab);
                tab.setOnClickListener(v -> {
                    if (mPager.getCurrentItem() == index) {
                        setCurrentPosition(index, true);
                    } else {
                        mPager.setCurrentItem(index, true);
                    }
                });
            }
            updataPosition(mPager.getCurrentItem());
        } else if (mPager2 != null) {
            for (int i = 0; i < mPager2.getAdapter().getItemCount(); i++) {
                final int index = i;
                View tab = mTabAdapter.getView(i, mContainer);
                if (tab == null) {
                    return;
                }
                if (tab.getParent() == null) {
                    mContainer.addView(tab);
                }
                tab.setFocusable(true);
                //mTabs.add(tab);
                tab.setOnClickListener(v -> {
                    if (mPager2.getCurrentItem() == index) {
                        setCurrentPosition(index, true);
                    } else {
                        mPager2.setCurrentItem(index, true);
                    }
                });
            }
            updataPosition(mPager2.getCurrentItem());
        } else {
            for (int i = 0; i < mTabAdapter.getViewCount(); i++) {
                final int index = i;
                View tab = mTabAdapter.getView(i, mContainer);
                if (tab == null) {
                    return;
                }
                if (tab.getParent() == null) {
                    mContainer.addView(tab);
                }
                tab.setFocusable(true);
                //mTabs.add(tab);
                tab.setOnClickListener(v -> {
                    //QDLogger.println("onItemClick index="+index);
                    //mTabAdapter.onItemClick();
                    setCurrentPosition(index, true);
                });
                if (i == selectIndex) {
                    updataPosition(index);
                }
            }
            //selectTab(mPager.getCurrentItem());
        }
    }

    boolean hasOnGlobalLayout = false;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        QDLogger.println("onFinishInflate");
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                hasOnGlobalLayout = true;
                updataPosition(selectIndex);
            }
        });
    }

    //viewpaper1 的方法
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    //viewpaper1 的方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    //viewpaper1 的方法
    @Override
    public void onPageSelected(int position) {
        updataPosition(position);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && mPager != null)
            updataPosition(mPager.getCurrentItem());
    }

    public ArrayList<View> getChildViews() {
        int count = mContainer.getChildCount();
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            views.add(mContainer.getChildAt(i));
        }
        return views;
    }

    private void scrollToIndex(int position) {
        View selectedTab = mContainer.getChildAt(position);
        if (selectedTab != null) {
            final int w = selectedTab.getMeasuredWidth();
            final int l = selectedTab.getLeft();
            final int x = l - this.getMeasuredWidth() / 2 + w / 2;
            //QDLogger.e("scrollTo w1="+this.getMeasuredWidth()+",w2="+w);
            smoothScrollTo(x, this.getScrollY());
        }
    }

    int selectIndex;

    /**
     * 只更新UI 不会触发change事件
     *
     * @param position
     */
    public void updataPosition(int position) {
        setCurrentPosition(position, false);
    }

    /**
     * 会触发change事件
     *
     * @param position
     */
    public void setCurrentPosition(int position) {
        setCurrentPosition(position, true);
    }

    /**
     * 该方法会刷新视图 但不会执行onchange方法
     *
     * @param position
     */
    public void setCurrentPosition(int position, boolean changed) {
        selectIndex = position;
        if (hasOnGlobalLayout) {
            for (int i = 0, pos = 0; i < mContainer.getChildCount(); i++, pos++) {
                View tab = mContainer.getChildAt(i);
                tab.setSelected(pos == position);
            }
            if (changed) {
                if (mTabAdapter != null) {
                    View view = mContainer.getChildAt(position);
                    mTabAdapter.onSelectedChange(getChildViews(), position, view, true);
                }
            }
            scrollToIndex(position);
        }
    }

    public View getTabView(int i) {
        return mContainer.getChildAt(i);
    }
    ViewPager viewPager;
    ViewPager.OnPageChangeListener onPageChangeListener;
    public void setupWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        if(onPageChangeListener==null){
            onPageChangeListener = new TabLayoutOnPageChangeListener(this);
        }

        viewPager.setOnPageChangeListener(onPageChangeListener);
    }
    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @NonNull
        private final WeakReference<ScrollableTabView> tabLayoutRef;
        private int previousScrollState;
        private int scrollState;

        public TabLayoutOnPageChangeListener(ScrollableTabView tabLayout) {
            tabLayoutRef = new WeakReference<>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            previousScrollState = scrollState;
            scrollState = state;
        }

        @Override
        public void onPageScrolled(
                final int position, final float positionOffset, final int positionOffsetPixels) {
            final ScrollableTabView tabLayout = tabLayoutRef.get();
            if (tabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                final boolean updateText =
                        scrollState != SCROLL_STATE_SETTLING || previousScrollState == SCROLL_STATE_DRAGGING;
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                final boolean updateIndicator =
                        !(scrollState == SCROLL_STATE_SETTLING && previousScrollState == SCROLL_STATE_IDLE);
                tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator);
            }
        }

        @Override
        public void onPageSelected(final int position) {
            final ScrollableTabView tabLayout = tabLayoutRef.get();
            if (tabLayout != null
                    && tabLayout.getSelectedTabPosition() != position
                    && position < tabLayout.getTabCount()) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                final boolean updateIndicator =
                        scrollState == SCROLL_STATE_IDLE
                                || (scrollState == SCROLL_STATE_SETTLING
                                && previousScrollState == SCROLL_STATE_IDLE);
                //tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator);
                tabLayout.setCurrentPosition(position);
            }
        }

        void reset() {
            previousScrollState = scrollState = SCROLL_STATE_IDLE;
        }
    }

    private void setScrollPosition(int position, float positionOffset, boolean updateText, boolean updateIndicator) {
    }

    private int getTabCount() {
        return mContainer==null?0:mContainer.getChildCount();
    }

    private int getSelectedTabPosition() {
        return selectIndex;
    }

    public static class AdapterDataObservable extends Observable<TabAdapter.AdapterDataObserver> {
        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        public void notifyChanged() {
            // since onChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount, null);
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount,
                                           @Nullable Object payload) {
            // since onItemRangeChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeChanged(positionStart, itemCount, payload);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            // since onItemRangeInserted() is implemented by the app, it could do anything,
            // including removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            // since onItemRangeRemoved() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeMoved(fromPosition, toPosition, 1);
            }
        }
    }

    private class RecyclerViewDataObserver extends TabAdapter.AdapterDataObserver {
        RecyclerViewDataObserver() {
        }

        @Override
        public void onChanged() {
            QDLogger.println("onChanged");
            initTabs();
            /*assertNotInLayoutOrScroll(null);
            mState.mStructureChanged = true;

            processDataSetCompletelyChanged(true);
            if (!mAdapterHelper.hasPendingUpdates()) {
                requestLayout();
            }*/
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            QDLogger.println("onItemRangeChanged");
            /*assertNotInLayoutOrScroll(null);
            if (mAdapterHelper.onItemRangeChanged(positionStart, itemCount, payload)) {
                triggerUpdateProcessor();
            }*/
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            QDLogger.println("onItemRangeInserted");
            /*assertNotInLayoutOrScroll(null);
            if (mAdapterHelper.onItemRangeInserted(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }*/
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            QDLogger.println("onItemRangeRemoved");
            /*assertNotInLayoutOrScroll(null);
            if (mAdapterHelper.onItemRangeRemoved(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }*/
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            QDLogger.println("onItemRangeMoved");
            /*assertNotInLayoutOrScroll(null);
            if (mAdapterHelper.onItemRangeMoved(fromPosition, toPosition, itemCount)) {
                triggerUpdateProcessor();
            }*/
        }

        void triggerUpdateProcessor() {
            QDLogger.println("triggerUpdateProcessor");
            /*if (POST_UPDATES_ON_ANIMATION && mHasFixedSize && mIsAttached) {
                ViewCompat.postOnAnimation(RecyclerView.this, mUpdateChildViewsRunnable);
            } else {
                mAdapterUpdateDuringMeasure = true;
                requestLayout();
            }*/
        }
    }

    public static final int GRAVITY_CENTER = 1;
    @TabLayout.TabGravity
    int tabGravity;
    int INDICATOR_GRAVITY_BOTTOM;
    int indicatorTop = 0;
    int indicatorBottom = 0;
    int indicatorHeight;
    @NonNull
    Drawable tabSelectedIndicator = new GradientDrawable();
    private int tabSelectedIndicatorColor = Color.TRANSPARENT;

   public static class SlidingTabIndicator extends LinearLayout {
        ValueAnimator indicatorAnimator;
        int selectedPosition = -1;
        float selectionOffset;

        private int layoutDirection = -1;

        SlidingTabIndicator(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        void animateIndicatorToPosition(final int position, int duration) {
            if (indicatorAnimator != null && indicatorAnimator.isRunning()) {
                indicatorAnimator.cancel();
            }

            updateOrRecreateIndicatorAnimation(/* recreateAnimation= */ true, position, duration);
        }

        private void updateOrRecreateIndicatorAnimation(
                boolean recreateAnimation, final int position, int duration) {
            final View currentView = getChildAt(selectedPosition);
            final View targetView = getChildAt(position);
            if (targetView == null) {
                // If we don't have a view, just update the position now and return

                return;
            }

            // Create the update listener with the new target indicator positions. If we're not recreating
            // then animationStartLeft/Right will be the same as when the previous animator was created.
            ValueAnimator.AnimatorUpdateListener updateListener =
                    new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                          //  tweenIndicatorPosition(currentView, targetView, valueAnimator.getAnimatedFraction());
                        }
                    };

            if (recreateAnimation) {
                // Create & start a new indicatorAnimator.
                ValueAnimator animator = indicatorAnimator = new ValueAnimator();
                //animator.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
                animator.setDuration(duration);
                animator.setFloatValues(0F, 1F);
                animator.addUpdateListener(updateListener);
                animator.addListener(
                        new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                selectedPosition = position;
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                selectedPosition = position;
                            }
                        });
                animator.start();
            } else {
                // Reuse the existing animator. Updating the listener only modifies the target positions.
                indicatorAnimator.removeAllUpdateListeners();
                indicatorAnimator.addUpdateListener(updateListener);
            }
        }
    }
}
