package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.helper.cache.QuickCache;

import static cn.demomaster.huan.quickdeveloplibrary.view.banner.BannerAdapter.basePosition;
import static cn.demomaster.huan.quickdeveloplibrary.view.banner.BannerAdapter.getRealPosition;

public class Banner extends FrameLayout {
    public Banner(@NonNull Context context) {
        super(context);
        init();
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    ViewPager2 mViewPager;
    BannerIndicator mBannerCursorView;

    /**
     * 指示器的样式
     */
    public enum IndicatorStyle {
        None,
        Circle,
        Rectangle,
        Custom
    }


    int direction = LinearLayout.HORIZONTAL;//LinearLayout.VERTICAL;

    /**
     * 设置banner 方向和滚动动画
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
        setOrientation(direction);
        if (direction == LinearLayout.HORIZONTAL) {
            //setPageTransformer(true,new ZoomOutPageTransformer2());
        } else {
            setPageTransformer(true, new ZoomOutPageTransformer());
        }
    }

    /**
     * 设置滚动器样式
     *
     * @param indicatorStyle
     */
    public void setIndicatorStyle(IndicatorStyle indicatorStyle) {
        switch (indicatorStyle) {
            case None:
                setIndicatorView(null);
                break;
            case Circle:
                setIndicatorView(new BannerCursorView(getContext()));
                break;
            case Rectangle:
                setIndicatorView(new BannerCursorView2(getContext()));
                break;
            case Custom:
                setIndicatorView(mBannerCursorView);
                break;
        }
    }

    int selectIndex = 0;
    int indicatorCount = 0;

    /**
     * 设置轮播滚动器
     *
     * @param bannerCursorView
     */
    public void setIndicatorView(BannerIndicator bannerCursorView) {
        if (mBannerCursorView == bannerCursorView) {
            return;
        }
        if (mBannerCursorView != null && ((View) mBannerCursorView).getParent() != null) {
            removeView((View) mBannerCursorView);
            mBannerCursorView = null;
        }
        if (bannerCursorView != null && ((View) bannerCursorView).getParent() == null) {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            addView((View) bannerCursorView, 1, layoutParams);
            mBannerCursorView = bannerCursorView;
        }
        if (mBannerCursorView != null) {
            mBannerCursorView.selecte(selectIndex);
            mBannerCursorView.setIndicatorCount(indicatorCount);
            //((View)mBannerCursorView).setBackgroundColor(Color.YELLOW);
        }
    }

    BannerAdapter adsAdapter;
    int currentPosition;

    private void init() {
        mViewPager = new ViewPager2(getContext());
        addView(mViewPager);

        mBannerCursorView = new BannerCursorView2(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        addView((View) mBannerCursorView, layoutParams);

        adsResourceList = new ArrayList<>();
        indicatorCount = adsResourceList.size();
        mBannerCursorView.setIndicatorCount(indicatorCount);
        if (adsAdapter == null) {
            adsAdapter = new BannerAdapter((FragmentActivity) getContext(), adsResourceList, mViewPager, () -> setCurrentItem(mViewPager.getCurrentItem() + 1));
        }
        //mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        //mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
        mViewPager.setAdapter(adsAdapter);
        setCurrentItem(basePosition, false);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                //QDLogger.i("mViewPager","当前页面："+position);
                currentPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                //QDLogger.i("mViewPager","onPageSelected="+position);
                super.onPageSelected(position);
                int p = getRealPosition(position);

                if ((position < basePosition) || (position >= basePosition + adsAdapter.getItemCount2())) {
                    if (p < 0) {
                        p = (adsAdapter.getItemCount2() + p);
                    }
                    p = p % adsAdapter.getItemCount2();
                }
                selectIndex = p;
                if (mBannerCursorView != null) {
                    //QDLogger.i("selectIndex","selectIndex="+selectIndex);
                    mBannerCursorView.selecte(selectIndex);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //QDLogger.i("mViewPager","onPageScrollStateChanged="+state);
                super.onPageScrollStateChanged(state);
                if (state == 0) {
                    //QDLogger.i("mViewPager","fragment切换完成："+currentPosition);
                    int p = getRealPosition(currentPosition);
                    if ((currentPosition < basePosition) || (currentPosition >= basePosition + adsAdapter.getItemCount2())) {
                        if (p < 0) {
                            p = (adsAdapter.getItemCount2() + p);
                        }
                        p = p % adsAdapter.getItemCount2();

                        //QDLogger.i("onPageSelected p=" + p);
                        int index = p + basePosition;
                        ((RecyclerView) mViewPager.getChildAt(0)).scrollToPosition(index);
                        adsAdapter.setCurrentItem(index);
                        //QDLogger.i("mViewPager","切换完成2："+index);
                    } else {
                        adsAdapter.setCurrentItem(currentPosition);
                    }
                }
                postDelayedToNext(loopTime);
            }
        });
        postDelayedToNext(loopTime);
        mViewPager.setClipChildren(false); //VP的内容可以不在限制内绘制
        initScroller();
    }

    public void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
        adsAdapter.setCurrentItem(position);
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        mViewPager.setCurrentItem(position, smoothScroll);
        adsAdapter.setCurrentItem(position);
    }

    List<AdsResource> adsResourceList = new ArrayList<>();

    public void setData(List<AdsResource> list) {
        indicatorCount = list.size();
        if (mBannerCursorView != null) {
            mBannerCursorView.setIndicatorCount(indicatorCount <= 1 ? 0 : indicatorCount);
        }
        adsResourceList.clear();
        adsResourceList.addAll(list);
        if (adsAdapter != null) {
            setOffscreenPageLimit(list.size() > 1 ? 3 : 1);
            adsAdapter.setData(list);
            setCurrentItem(basePosition, false);
        }
        postDelayedToNext(loopTime);
    }

    public void initScroller() {
        RecyclerView recyclerView = (RecyclerView) mViewPager.getChildAt(0);
        recyclerView.setOnTouchListener(null);
        recyclerView.setOnClickListener(null);
        recyclerView.setClickable(false);
        recyclerView.setEnabled(false);

        mViewPager.setOnTouchListener(null);
        mViewPager.setOnClickListener(null);
        mViewPager.setClickable(false);
        mViewPager.setEnabled(false);
    }

    boolean canDrag = true;

    /**
     * 设置是否可以触摸拖动
     *
     * @param canDrag
     */
    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
        //setOnTouchListener(null);
        //setOnClickListener(null);
        setClickable(canDrag);
        setEnabled(canDrag);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!canDrag || adsResourceList == null || adsResourceList.size() <= 1) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    /**
     * 延迟滚动到下一个
     *
     * @param time
     */
    public void postDelayedToNext(long time) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, Math.min(time, loopTime));
    }

    /**
     * 根据广告类型动态调整
     * 1video播放完成时切换下一个
     * 2video播放超时后切换下一个
     * <p>
     * 图片
     * 根据固定时长切换
     */
    long loopTime = 10000;

    public void setLoopTime(long loopTime) {
        this.loopTime = loopTime;
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (adsResourceList == null || adsResourceList.size() <= 1) {
                return;
            }

            handler.removeCallbacks(runnable);
            int index = mViewPager.getCurrentItem();
            //System.out.println(Banner.this.hashCode()+"]当前：" + index +",basePosition="+basePosition);
            if (index >= basePosition) {
                index = index - basePosition;
            }
            index = index % adsResourceList.size();

            int index1 = mViewPager.getCurrentItem() + 1;
            // System.out.println("index1=" + index1);
            // System.out.println(Banner.this.hashCode()+"]当前2：" + index);
            AdsResource adsResource = adsResourceList.get(index);
            String url = adsResource.getUrl();
            if (QuickCache.containsUrl(url)) {
                String fileType = QuickCache.getCacheInfoByUrl(adsResource.getUrl()).getFileType();
                if (!TextUtils.isEmpty(fileType) && fileType.startsWith("video/")) {
                    //System.out.println("urlTypMap " + fileType);
                } else {
                    setCurrentItem(index1);
                    handler.postDelayed(runnable, loopTime);
                }
            } else {
                if (BannerContentType.getEnum(adsResource.getType()) == BannerContentType.video) {

                } else {
                    setCurrentItem(index1);
                    handler.postDelayed(runnable, loopTime);
                }
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(runnable);
    }

    /**
     * 设置切换效果
     *
     * @param b
     * @param transformer
     */
    public void setPageTransformer(boolean b, ViewPager2.PageTransformer transformer) {
        mViewPager.setPageTransformer(transformer);
    }

    /**
     * 设置画廊内边距
     *
     * @param
     */
    public void setGalleryPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        RecyclerView recyclerView = (RecyclerView) mViewPager.getChildAt(0);
        recyclerView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        recyclerView.setClipToPadding(false);
    }

    public void setOffscreenPageLimit(int i) {
        mViewPager.setOffscreenPageLimit(i);
    }

    int mBannerRadius = 0;

    public void setRadius(int radius) {
        this.mBannerRadius = radius;
        //postInvalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mBannerRadius > 0) {
            Path path = new Path();
            path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()),
                    mBannerRadius, mBannerRadius, Path.Direction.CW);
            canvas.clipPath(path);
        }
        super.dispatchDraw(canvas);
    }

    public void setOrientation(int orientation) {
        mViewPager.setOrientation(orientation);//ViewPager2.ORIENTATION_HORIZONTAL
    }

    public interface BannerIndicator {
        void selecte(int position);

        void setIndicatorCount(int count);
    }
}
