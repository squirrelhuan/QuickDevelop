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

import android.content.Context;
import android.database.Observable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.view.adapter.TabAdapter;
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

    private final LinearLayout mContainer;

    private final ArrayList<View> mTabs = new ArrayList<>();

    public ScrollableTabView(Context context) {
        this(context, null);
    }

    public ScrollableTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ScrollableTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);

        mContainer = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.setLayoutParams(params);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);

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
                selectTab(position);
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
        mTabs.clear();
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
                mTabs.add(tab);
                tab.setOnClickListener(v -> {
                    if (mPager.getCurrentItem() == index) {
                        selectTab(index);
                    } else {
                        mPager.setCurrentItem(index, true);
                    }
                });
            }
            selectTab(mPager.getCurrentItem());
        }else if (mPager2 != null) {
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
                mTabs.add(tab);
                tab.setOnClickListener(v -> {
                    if (mPager2.getCurrentItem() == index) {
                        selectTab(index);
                    } else {
                        mPager2.setCurrentItem(index, true);
                    }
                });
            }
            selectTab(mPager2.getCurrentItem());
        }else {
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
                mTabs.add(tab);
                tab.setOnClickListener(v -> {
                    QDLogger.println("onItemClick index="+index);
                    //mTabAdapter.onItemClick();
                    mTabAdapter.onSelectedChange(index,v,true);
                    selectTab(index);
                });
            }
            //selectTab(mPager.getCurrentItem());
        }
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
        selectTab(position);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed && mPager != null)
            selectTab(mPager.getCurrentItem());
    }

    private void selectTab(int position) {
        for (int i = 0, pos = 0; i < mContainer.getChildCount(); i++, pos++) {
            View tab = mContainer.getChildAt(i);
            tab.setSelected(pos == position);
        }

        View selectedTab = mContainer.getChildAt(position);
        final int w = selectedTab.getMeasuredWidth();
        final int l = selectedTab.getLeft();
        final int x = l - this.getWidth() / 2 + w / 2;
        smoothScrollTo(x, this.getScrollY());
    }

    public void setCurrentItem(int position) {
        selectTab(position);
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
}
