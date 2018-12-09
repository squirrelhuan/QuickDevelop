package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.AnimationUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

import static cn.demomaster.huan.quickdeveloplibrary.ApplicationParent.TAG;
import static cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS;

/**
 * Created by Squirrel桓 on 2018/11/9.
 */
public class ActionBarLayout {

    private Activity context;
    private ImageTextView it_actionbar_title;
    private ImageTextView it_actionbar_common_left;
    private ImageTextView it_actionbar_common_right;
    private View.OnClickListener leftOnClickListener;
    private View.OnClickListener rightOnClickListener;

    /**
     * 获取中间视图
     *
     * @return
     */
    public ImageTextView getCenterView() {
        return it_actionbar_title;
    }

    /**
     * 获取左侧控件
     *
     * @return
     */
    public ImageTextView getLeftView() {
        return it_actionbar_common_left;
    }

    /**
     * 获取右侧侧视图控件
     *
     * @return
     */
    public ImageTextView getRightView() {
        return it_actionbar_common_right;
    }

    /**
     * 获取导航栏视图用来更改背景或其他操作
     *
     * @return
     */
    public ViewGroup getHeadView() {
        return headView;
    }


    private int contentLayoutResID;
    private int headLayoutResID;
    /**
     * 构造方法
     *
     * @param context
     * @param actionBarModel
     * @param headView
     * @param contentView
     */
    ;

    public ActionBarLayout(final Activity context, ACTIONBAR_TYPE actionBarModel, int headLayoutResID, int contentLayoutResID) {
        this.context = context;
        this.headLayoutResID = headLayoutResID;
        this.contentLayoutResID = contentLayoutResID;
        initLayout();

        this.actionBarModel = actionBarModel;
        it_actionbar_title = headView.findViewById(R.id.it_actionbar_common_title);
        if (context.getTitle() != null) {
            it_actionbar_title.setText(context.getTitle().toString());
        }
        it_actionbar_common_left = headView.findViewById(R.id.it_actionbar_common_left);
        it_actionbar_common_right = headView.findViewById(R.id.it_actionbar_common_right);
        if (leftOnClickListener == null && it_actionbar_common_left != null) {
            it_actionbar_common_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) context).finish();
                }
            });
        }
        if (rightOnClickListener == null && it_actionbar_common_right != null) {
            it_actionbar_common_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ScreenShotUitl.shot((Activity) context);
                }
            });
        }
        AnimationUtil.addScaleAnimition(it_actionbar_common_left, null);
        AnimationUtil.addScaleAnimition(it_actionbar_common_right, null);

    }

    private FrameLayout.LayoutParams layoutParams_header;
    private FrameLayout.LayoutParams layoutParams_content;

    private void initLayout() {

        LayoutInflater mInflater = LayoutInflater.from(context);
        rootLayout = new FrameLayout(context);
        //contentView宽高
        mInflater.inflate(contentLayoutResID, rootLayout);
        contentView = (ViewGroup) rootLayout.getChildAt(0);
        //header宽高
        mInflater.inflate(headLayoutResID, rootLayout);
        headView = (ViewGroup) rootLayout.getChildAt(1);

        //记录背景drawable
        headerBackgroundDrawable = headView.getBackground();
        if (headerBackgroundDrawable instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) headerBackgroundDrawable;
            headerBackgroundColor = colordDrawable.getColor();
        }
        //记录原始paddingTop
        headerPaddingTop = headView.getPaddingTop();

        //记录原始paddingTop
        contentPaddingTop = contentView.getPaddingTop();

        //记录状态栏高度
        statusBar_Height = DisplayUtil.getStatusBarHeight(context);

        //记录导航栏高度
        actionBar_Height = QMUIDisplayHelper.getActionBarHeight(context);
        if (actionBar_Height > 0) {
            actionBar_Height = actionBar_Height - DisplayUtil.dp2px(context, 8);
        }


        layoutParams_header = (FrameLayout.LayoutParams) this.headView.getLayoutParams();
        if (layoutParams_header == null) {
            layoutParams_header = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBar_Height + actionBar_Height);
        } else {
            layoutParams_header.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams_header.height = statusBar_Height + actionBar_Height;
        }
        headView.setLayoutParams(layoutParams_header);

        layoutParams_content = (FrameLayout.LayoutParams) this.contentView.getLayoutParams();
        if (layoutParams_content == null) {
            layoutParams_content = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams_content.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams_content.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        contentView.setLayoutParams(layoutParams_content);
    }

    /**
     * 设置title
     *
     * @param text
     */
    public void setTitle(String text) {
        if (it_actionbar_title != null && text != null) {
            it_actionbar_title.setText(text);
        }
    }

    /**
     * 设置导航栏颜色（对外）
     *
     * @param color
     */
    public void setBackGroundColor(int color) {
        headerBackgroundColor = color;
        headView.setBackgroundColor(color);
        setStateBarColor();
    }

    /**
     * 设置导航栏颜色（对内）
     *
     * @param color
     */
    private void setActionBarBackGroundColor(int color) {
        if (color == -1) {
            return;
        }
        headView.setBackgroundColor(color);
    }

    private void setActionBarBackGroundDrawable(Drawable headerBackgroundDrawable) {
        if (headerBackgroundDrawable != null) {
            headView.setBackground(headerBackgroundDrawable);
        }
    }

    /**
     * 设置左边按钮点击事件
     *
     * @param leftOnClickListener
     */
    public void setLeftOnClickListener(View.OnClickListener leftOnClickListener) {
        this.leftOnClickListener = leftOnClickListener;
        it_actionbar_common_left.setOnClickListener(leftOnClickListener);
    }

    /**
     * 设置右边按钮点击事件
     *
     * @param rightOnClickListener
     */
    public void setRightOnClickListener(View.OnClickListener rightOnClickListener) {
        this.rightOnClickListener = rightOnClickListener;
        it_actionbar_common_right.setOnClickListener(rightOnClickListener);
    }

    /**
     * 状态栏颜色刷新需要在view加载完成后操作
     *
     * @param ll_layout
     */
    public void changeChildView(final LinearLayout ll_layout) {
        ll_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setStateBarColor();
            }
        });
    }

    /**
     * 导航栏样式三种
     */
    public enum ACTIONBAR_TYPE {
        //无导航栏
        NO_ACTION_BAR,
        //无导航栏并且内容可填充到状态栏
        NO_ACTION_BAR_NO_STATUS,
        //有导航栏
        NORMAL,
        //层叠
        ACTION_STACK,
        //层叠并且内容可填充到状态栏
        ACTION_STACK_NO_STATUS,
        //透明导航栏
        ACTION_TRANSPARENT
    }

    private ViewGroup headView;
    private ViewGroup contentView;
    private int statusBar_Height = 0;
    private int actionBar_Height = 0;

    private ACTIONBAR_TYPE actionBarModel = ACTIONBAR_TYPE.NO_ACTION_BAR;//1,上下顺序排列（普通样式），2层叠排列（actionbar背景透明），3只显示内容

    /**
     * 导航栏构建者
     */
    public static class Builder {
        private ActionBarLayout activityLayout;
        private ACTIONBAR_TYPE actionBarModel = ACTIONBAR_TYPE.NORMAL;//1,上下顺序排列（普通样式），2层叠排列（actionbar背景透明），3只显示内容
        private Activity context;
        private int contentLayoutResID;
        private int headLayoutResID;

        public Builder(Activity context) {
            this.context = context;
        }

        public ActionBarLayout create() {
            activityLayout = new ActionBarLayout(context, actionBarModel, headLayoutResID, contentLayoutResID);
            return activityLayout;
        }

        public void setContentView(int layoutResID) {
            this.contentLayoutResID = layoutResID;
        }

        public void setHeadView(int layoutResID) {
            this.headLayoutResID = layoutResID;
        }
    }

    public void setActionBarModel(ACTIONBAR_TYPE actionBarModel) {
        this.actionBarModel = actionBarModel;
        refresh();
    }

    private void setHeaderView(ViewGroup headView) {
        this.headView = headView;
    }

    private void setContentView(ViewGroup contentView) {
        this.contentView = contentView;
    }

    long startTime;

    private void refresh() {
        startTime = System.currentTimeMillis();
        getFinalView();
        long consumingTime = System.currentTimeMillis() - startTime;
        Log.d(TAG, "time=" + consumingTime);
    }

    ViewGroup rootLayout;
    private int headerBackgroundColor = -1;
    private Drawable headerBackgroundDrawable;
    private static int headerPaddingTop = -1;
    private static int contentPaddingTop = -1;

    /**
     * 生成布局用来setContenView
     *
     * @return
     */
    public ViewGroup getFinalView() {

        // FrameLayout.LayoutParams layoutParams_header = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBar_Height + actionBar_Height);
        //FrameLayout.LayoutParams layoutParams_content = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        switch (actionBarModel) {
            case NO_ACTION_BAR:
                //contentView.layout(0,statusBar_Height ,0,0);
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                //headView.setPadding(0,headerPaddingTop+statusBar_Height,0,0);
                contentView.setPadding(0, contentPaddingTop + statusBar_Height, 0, 0);
                //setActionBarBackGroundDrawable(headerBackgroundDrawable);
                //setActionBarBackGroundColor(headerBackgroundColor);
                headView.setVisibility(View.GONE);
                break;
            case NO_ACTION_BAR_NO_STATUS:
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                contentView.setPadding(0, contentPaddingTop, 0, 0);
                //setActionBarBackGroundDrawable(headerBackgroundDrawable);
                //setActionBarBackGroundColor(headerBackgroundColor);
                headView.setVisibility(View.GONE);
                break;
            case NORMAL:
                layoutParams_content.topMargin = statusBar_Height + actionBar_Height;
                layoutParams_header.topMargin = 0;
                headView.setPadding(0, headerPaddingTop + statusBar_Height, 0, 0);
                contentView.setPadding(0, contentPaddingTop, 0, 0);
                setActionBarBackGroundDrawable(headerBackgroundDrawable);
                setActionBarBackGroundColor(headerBackgroundColor);
                headView.setVisibility(View.VISIBLE);
                break;
            case ACTION_STACK:
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                headView.setPadding(0, headerPaddingTop + statusBar_Height, 0, 0);
                contentView.setPadding(0, contentPaddingTop + statusBar_Height, 0, 0);
                setActionBarBackGroundColor(context.getResources().getColor(R.color.transparent));
                headView.setVisibility(View.VISIBLE);
                break;
            case ACTION_STACK_NO_STATUS:
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                contentView.setPadding(0, contentPaddingTop, 0, 0);
                setActionBarBackGroundColor(context.getResources().getColor(R.color.transparent));
                headView.setVisibility(View.VISIBLE);
                break;
            case ACTION_TRANSPARENT:
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                contentView.setPadding(0, contentPaddingTop + actionBar_Height + statusBar_Height, 0, 0);
                setActionBarBackGroundColor(context.getResources().getColor(R.color.transparent));
                headView.setVisibility(View.VISIBLE);
                break;
            default:
                return null;
        }

        this.contentView.setLayoutParams(layoutParams_content);
        this.headView.setLayoutParams(layoutParams_header);
        //rootLayout.addView(contentView, layoutParams_content);
        //rootLayout.addView(headView, layoutParams_header);
        //状态栏颜色
        if (stateBarColorAuto) {
            //view加载完成时回调
            rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setStateBarColor();
                }
            });
        }
        //setActionBarThemeColors(Color.WHITE, Color.BLACK);
        return rootLayout;
    }


    public void refreshStateBarColor() {
        refresh();
    }

    private boolean stateBarColorAuto;

    /**
     * 设置状态栏颜色自动取色
     *
     * @param stateBarColorAuto
     */
    public void setStateBarColorAuto(boolean stateBarColorAuto) {
        this.stateBarColorAuto = stateBarColorAuto;
        refresh();
    }

    int themeColorType = -1;//状态栏文字颜色

    /**
     * 设置状态栏字体颜色
     */
    private void setStateBarColor() {
        long consumingTime = System.currentTimeMillis() - startTime;
        Log.d(TAG, "setStateBarColor=" + consumingTime);
        //截图取色
        Bitmap bitmap = ScreenShotUitl.getCacheBitmapFromViewTop(rootLayout, statusBar_Height);
        if(bitmap==null){
            return;
        }
        boolean isDart;
        isDart = getBitmapMainColor(bitmap);
        if (themeColorType != (isDart ? 1 : 0)) {
            themeColorType = (isDart ? 1 : 0);
            StatusBarUtil.setStatusBarMode((Activity) context, !isDart);
            //TODO 这里并不严谨isDart是对状态栏颜色的判定， 导航栏应该获取状态栏以下部分的颜色
            setActionBarColorType(isDart);
        }
        long consumingTime2 = System.currentTimeMillis() - startTime;
        Log.d(TAG, "setStateBarColor2=" + consumingTime2);
    }

    private int[] themeColors = {Color.WHITE, Color.BLACK};

    public void setActionBarThemeColors(int lightColor, int dartColor) {
        themeColors[0] = lightColor;
        themeColors[1] = dartColor;
        setActionBarColorType(themeColorType == 1);
    }

    private void setActionBarColorType(boolean isDart) {
        int color = isDart ? themeColors[0] : themeColors[1];
        setActionBarColor(color);
    }

    /**
     * 设置导航栏主题颜色
     */
    private void setActionBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTintAll(headView, color);
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
                Log.i(TAG, "color=" + color + ",red=" + red + ",green=" + green + ",blue=" + blue);
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


}
