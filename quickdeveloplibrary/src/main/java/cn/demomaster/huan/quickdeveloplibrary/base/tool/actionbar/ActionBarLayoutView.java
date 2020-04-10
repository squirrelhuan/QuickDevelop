package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragmentInterface;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Squirrel桓 on 2018/11/9.
 */
public class ActionBarLayoutView extends FrameLayout implements ActionBarInterface {

    private ActionBarLayoutContentView actionBarLayoutContentView;
    private ActionBarLayoutHeaderView actionBarLayoutHeaderView;
    private ActionBarLayoutFragmentView actionBarLayoutFragmentView;
    private boolean hasContainBackground = true;

    private WeakReference<AppCompatActivity> context;
    private int contentResId = -1;
    private int fragmentResId = -1;
    private int headerResId = -1;
    private View contentView;
    private View fragmentView;
    private boolean isFullScreen = true;
    private ACTIONBAR_TYPE actionbarType = ACTIONBAR_TYPE.NORMAL;;
    private WeakReference<Fragment> fragmentWeakReference;
    private ActivityContentType contextType = ActivityContentType.ActivityModel;

    public ActionBarLayoutView(Builder builder) {
        super(builder.contextWeakReference.get());
        context = builder.contextWeakReference;
        contextType = builder.contextType;
        fragmentWeakReference = builder.fragmentWeakReference;
        contentResId = builder.contentResId;
        contentView = builder.contentView;
        headerResId = builder.headerResId;
        isFullScreen = builder.isFullScreen;
        fragmentResId = builder.fragmentResId;
        fragmentView = builder.fragmentView;
        initView();
    }

    public void setHasContainBackground(boolean hasContainBackground) {
        this.hasContainBackground = hasContainBackground;
        if (actionBarLayoutContentView != null) {
            actionBarLayoutContentView.setHasContainBackground(hasContainBackground);
        }
    }


    /**
     * 获取中间视图
     *
     * @return
     */
    public ImageTextView getCenterView() {
        return actionBarLayoutHeaderView.getCenterView();
    }

    /**
     * 获取左侧控件
     *
     * @return
     */
    @Override
    public ImageTextView getLeftView() {
        return actionBarLayoutHeaderView.getLeftView();
    }

    /**
     * 获取右侧侧视图控件
     *
     * @return
     */
    @Override
    public ImageTextView getRightView() {
        return actionBarLayoutHeaderView.getRightView();
    }

    /**
     * 获取导航栏视图用来更改背景或其他操作
     *
     * @return
     */
    public ViewGroup getHeadView() {
        return actionBarLayoutHeaderView;
    }

    @Override
    public void setActionBarType(ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        if(loadFinished){
            initActionBarType();
        }
    }
    private boolean loadFinished;
    public void initActionBarType(){
        if (actionBarLayoutHeaderView != null&&actionBarLayoutContentView != null) {
            actionBarLayoutHeaderView.setActionbarType(actionbarType);
        }else if (actionBarLayoutContentView != null) {
            actionBarLayoutContentView.setActionbarType(actionbarType);
        }
    }

    @Override
    public View generateView() {
        return this;
    }

    @Override
    public void onClickBack() {
        int eventCode = KEYCODE_BACK;
        long now = SystemClock.uptimeMillis();
        KeyEvent down = new KeyEvent(now, now, ACTION_DOWN, eventCode, 0);
        if (contextType == ActivityContentType.FragmentModel) {
            if (fragmentWeakReference.get() instanceof QDBaseFragmentInterface) {
                boolean ret = ((QDBaseFragmentInterface) fragmentWeakReference.get()).onKeyDown(eventCode,down);
                if (ret) {
                    QDLogger.d("fragment 消费了返回事件");
                }
            }
        }
        //context.get().onKeyDown(eventCode,down);
        context.get().onBackPressed();
        //((QDActivityInterface)context.get()).onClickBack();
    }



    public ActionBarLayoutContentView getActionBarLayoutContentView() {
        return actionBarLayoutContentView;
    }

    public void setActionBarLayoutContentView(ActionBarLayoutContentView actionBarLayoutContentView) {
        this.actionBarLayoutContentView = actionBarLayoutContentView;
    }

    public ActionBarLayoutHeaderView getActionBarLayoutHeaderView() {
        return actionBarLayoutHeaderView;
    }

    public void setActionBarLayoutHeaderView(ActionBarLayoutHeaderView actionBarLayoutHeaderView) {
        this.actionBarLayoutHeaderView = actionBarLayoutHeaderView;
    }

    private int headerBackgroundColor = Color.WHITE;

    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
        if (actionBarLayoutHeaderView != null) {
            actionBarLayoutHeaderView.setBackgroundColor(headerBackgroundColor);
        }
    }

    private LayoutInflater mInflater;

    private void initView() {
        mInflater = LayoutInflater.from(getContext());
        initChildView();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                loadFinished = true;
                initActionBarType();
                setFullScreen(isFullScreen);
            }
        });
    }

    /**
     * init内容
     */
    private void initChildView() {
        actionBarLayoutContentView = new ActionBarLayoutContentView(new WeakReference<Context>(getContext()), this);
        actionBarLayoutFragmentView = new ActionBarLayoutFragmentView(getContext());
        //内容区
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(actionBarLayoutContentView, layoutParams);
        addContentBackView(contentResId);
        addContentBackView(contentView);

        //头部导航
        actionBarLayoutHeaderView = new ActionBarLayoutHeaderView(new WeakReference<Context>(getContext()),this);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(actionBarLayoutHeaderView, layoutParams2);
        addHeadView(headerResId);
        actionBarLayoutHeaderView.setBackgroundColor(headerBackgroundColor);
        actionBarLayoutHeaderView.setContentType(contextType);

     /*   ActionBarLayoutHeaderView.GlobalLayoutListener globalLayoutListener = new ActionBarLayoutHeaderView.GlobalLayoutListener() {
            @Override
            public void onLoadFinish() {
                setFullScreen(isFullScreen);
            }
        };
        actionBarLayoutHeaderView.addGlobalLayoutListener(globalLayoutListener);*/

        if (contextType != ActivityContentType.FragmentModel) {
            //Front导航
            FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(actionBarLayoutFragmentView, layoutParams3);
            addContentFragmentView(fragmentResId);
            addContentFragmentView(fragmentView);
            actionBarLayoutContentView.setActionbarType(actionbarType);
            setFullScreen(isFullScreen);
        }
    }

    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
        /*LayoutParams layoutParams_c = (LayoutParams) getLayoutParams();
        if (layoutParams_c == null) {
            layoutParams_c = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (!isFullScreen) {
            int h = context.getActionBarLayout().getActionBarLayoutHeaderView().getHeight();
            QDLogger.d("h="+h);
            layoutParams_c.topMargin = h;
            setLayoutParams(layoutParams_c);
        } else {
            setLayoutParams(layoutParams_c);
        }*/
    }

    public void addContentBackView(int layoutResID) {
        if (layoutResID == -1) return;
        View view = mInflater.inflate(layoutResID, this, false);
        addContentBackView(view);
    }

    private void addContentBackView(View view) {
        if (view == null) return;
        actionBarLayoutContentView.setContentViewBack(view);
    }

    public void addContentFragmentView(int layoutResID) {
        if (layoutResID == -1) {
            return;
        }
        View view = mInflater.inflate(layoutResID, this, false);
        addContentFragmentView(view);
    }

    private void addContentFragmentView(View view) {
        if (view == null) return;
        actionBarLayoutFragmentView.setContentView(view);
    }

    public void addHeadView(int layoutResID) {
        if (layoutResID == -1) return;
        View view = mInflater.inflate(layoutResID, this, false);
        addHeadView(view);
    }

    private void addHeadView(View view) {
        if (view == null) return;
        actionBarLayoutHeaderView.setContentView(view);
    }

    public void setFragmentActionbarType(ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        if (actionBarLayoutContentView != null) {
            actionBarLayoutContentView.setActionbarType(actionbarType);
        }
        if (actionBarLayoutHeaderView != null) {
            actionBarLayoutHeaderView.setActionbarType(actionbarType);
        }
    }

    private ActionBarTip actionBarTip;
    public ActionBarTip getActionBarTip() {
        if (actionBarTip == null) {
            actionBarTip = new ActionBarTip(getContext());
            actionBarTip.setContentView(R.layout.quickdevelop_activity_actionbar_tip);
            FrameLayout.LayoutParams layoutParams_tip = (FrameLayout.LayoutParams) this.actionBarTip.getLayoutParams();
            if (layoutParams_tip == null) {
                layoutParams_tip = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            actionBarTip.setLayoutParams(layoutParams_tip);
            actionBarLayoutContentView.addActionBarTipView(actionBarTip);
        }
        return actionBarTip;
    }


    int themeColorType = -1;//状态栏文字颜色
    /**
     * 设置状态栏字体颜色
     */
    public void refreshStateBarColor() {
        //截图取色
        Bitmap bitmap = ScreenShotUitl.getCacheBitmapFromViewTop(this, actionBarLayoutHeaderView.getStatusBar_Height());
        if (bitmap == null) {
            return;
        }
        boolean isDart;
        isDart = getBitmapMainColor(bitmap);
        if (themeColorType != (isDart ? 1 : 0)) {
            themeColorType = (isDart ? 1 : 0);
            StatusBarUtil.setStatusBarMode((Activity) context.get(), !isDart);
            //TODO 这里并不严谨isDart是对状态栏颜色的判定， 导航栏应该获取状态栏以下部分的颜色
            setActionBarColorType(isDart);
        }
    }

    private int[] themeColors = {Color.WHITE, Color.BLACK};

    /*public void setActionBarThemeColors(int lightColor, int dartColor) {
        themeColors[0] = lightColor;
        themeColors[1] = dartColor;
        setActionBarColorType(themeColorType == 1);
    }*/
    private void setActionBarColorType(boolean isDart) {
        int color = isDart ? themeColors[0] : themeColors[1];
        setActionBarColor(color);
    }

    /**
     * 设置导航栏主题颜色
     */
    private void setActionBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTintAll(actionBarLayoutHeaderView, color);
        }
    }

    /**
     * 遍历view集合，对vector视图tint,对textView视图设置颜色
     *
     * @param view
     * @param color
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setTintAll(View view, int color) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ImageView) {
                    Drawable drawable = (((ImageView) viewGroup.getChildAt(i)).getDrawable());
                    if (drawable != null) {
                        drawable.setTint(color);
                    }
                }
                if (viewGroup.getChildAt(i) instanceof ImageTextView) {
                    ((ImageTextView) viewGroup.getChildAt(i)).setTextColor(color);
                } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    setTintAll(viewGroup.getChildAt(i), color);
                } else if (viewGroup.getChildAt(i) instanceof TextView) {
                    ((TextView) viewGroup.getChildAt(i)).setTextColor(color);
                }
            }
        }
    }
    /*
       获取bitmap主色调
        */
    public boolean getBitmapMainColor(Bitmap oldBitmap) {
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        //int[] mArrayColor = new int[mArrayColorLengh];
        int count_h = 3;//垂直方向三个点
        int count_w = 10;//水平方向10个点
        int distance_h = mBitmapHeight / count_h;//垂直间距
        int distance_w = mBitmapWidth / count_w;//水平间距
        int dart_point_count = 0;
        for (int i = 0; i < count_h; i++) {
            for (int j = 0; j < count_w; j++) {
                //获得Bitmap 图片中每一个点的color颜色值
                //将需要填充的颜色值如果不是
                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
                //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000
                //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了
                int color = mBitmap.getPixel(j * distance_w, i * distance_h);
                int red = (color & 0xff0000) >> 16;
                int green = (color & 0x00ff00) >> 8;
                int blue = (color & 0x0000ff);
                if ((red + green + blue) / 3 > 128) {
                    dart_point_count++;
                }
                //Log.i(TAG, "color=" + color + ",red=" + red + ",green=" + green + ",blue=" + blue);
                if (i * count_w + j > (count_h * count_w) / 2) {
                    if (dart_point_count > (count_h * count_w) / 2) {
                        return false;
                    } else if (i * count_w + j - dart_point_count > (count_h * count_w) / 2) {
                        return true;
                    }
                }
                //将颜色值存在一个数组中 方便后面修改
                //if (color == oldColor) {
                //   mBitmap.setPixel(j, i, newColor);  //将白色替换成透明色
                // }
            }
        }
        mBitmap.recycle();
        return dart_point_count < (count_h * count_w) / 2;
    }
    @Override
    public void setTitle(String title) {
        getActionBarLayoutHeaderView().setTitle(title);
    }

    @Override
    public void setStateBarColorAuto(Boolean barColorAuto) {
        getActionBarLayoutHeaderView().setStateBarColorAuto(barColorAuto);
    }

    @Override
    public void setLeftOnClickListener(OnClickListener leftOnClickListener) {
        getActionBarLayoutHeaderView().setLeftOnClickListener(leftOnClickListener);
    }

    @Override
    public void setRightOnClickListener(OnClickListener rightOnClickListener) {
        getActionBarLayoutHeaderView().setRightOnClickListener(rightOnClickListener);
    }

    @Override
    public void setActionBarThemeColors(int lightColor, int darkColor) {
        getActionBarLayoutHeaderView().setActionBarThemeColors(lightColor, darkColor);
    }

    public void setActionBarTipType(ActionBarTip.ACTIONBARTIP_TYPE actionbartip_type) {
        getActionBarLayoutContentView().setActionbartipType(actionbartip_type);
    }

    /**
     * 导航栏构建者
     */
    public static class Builder {
        private WeakReference<AppCompatActivity> contextWeakReference;
        private int contentResId = -1;
        private int fragmentResId = -1;
        private int headerResId = -1;
        private View contentView;
        private View fragmentView;
        private boolean isFullScreen = true;
        private ACTIONBAR_TYPE actionbarType;
        private WeakReference<Fragment> fragmentWeakReference;
        private ActivityContentType contextType = ActivityContentType.ActivityModel;

        public Builder( AppCompatActivity context) {
            this.contextWeakReference = new WeakReference<>(context);
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setFragmentView(View fragmentView) {
            this.fragmentView = fragmentView;
            return this;
        }

        public Builder setFragmentResId(int fragmentResId) {
            this.fragmentResId = fragmentResId;
            return this;
        }

        public Builder setContentResId(int contentResId) {
            this.contentResId = contentResId;
            return this;
        }

        public Builder setHeaderResId(int headerResId) {
            this.headerResId = headerResId;
            return this;
        }

        /*public Builder setContextType(ContentType contextType) {
            this.contextType = contextType;
            return this;
        }*/

        public Builder setFullScreen(boolean fullScreen) {
            isFullScreen = fullScreen;
            return this;
        }

        public Builder setFragment(Fragment fragment) {
            this.fragmentWeakReference = new WeakReference<>(fragment);
            this.contextType = ActivityContentType.FragmentModel;
            return this;
        }

        public ActionBarLayoutView creat() {
            return new ActionBarLayoutView(this);
        }

    }
}