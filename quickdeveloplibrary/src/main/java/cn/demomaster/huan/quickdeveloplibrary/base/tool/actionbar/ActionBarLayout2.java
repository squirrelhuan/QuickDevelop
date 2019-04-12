package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.AnimationUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

import static cn.demomaster.huan.quickdeveloplibrary.ApplicationParent.TAG;

/**
 * Created by Squirrel桓 on 2018/11/9.
 */
public class ActionBarLayout2 implements ActionBarInterface{

    private Activity context;
    private ImageTextView it_actionbar_title;
    private ImageTextView it_actionbar_common_left;
    private ImageTextView it_actionbar_common_right;
    private View.OnClickListener leftOnClickListener;
    private View.OnClickListener rightOnClickListener;

    @Override
    public void onClickBack() {
    }

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

    @Override
    public void setActionBarType(ActionBarInterface.ACTIONBAR_TYPE noActionBarNoStatus) {

    }

    @Override
    public View generateView() {
        return null;
    }

    @Override
    public void setFullScreen(boolean b) {

    }

    @Override
    public View getActionBarLayoutHeaderView() {
        return null;
    }

    @Override
    public void setActionBarTipType(ActionBarTip.ACTIONBARTIP_TYPE actionbartip_type) {

    }

    @Override
    public void setHasContainBackground(boolean isChecked) {

    }

    private int contentLayoutResID;
    private int headLayoutResID;
    public ActionBarLayout2(final Activity context, ACTIONBAR_TYPE actionBarModel, int headLayoutResID, int contentLayoutResID) {
        initActionBarLayout(context,actionBarModel, headLayoutResID, contentLayoutResID,null);
    }
    public ActionBarLayout2(final Activity context, ACTIONBAR_TYPE actionBarModel, int headLayoutResID, int contentLayoutResID, ViewGroup contentView){
        initActionBarLayout(context,actionBarModel, headLayoutResID, contentLayoutResID,contentView);
    }
    public int getFragmentContentViewId(){
        return R.id.qd_fragment_content_view;
    }
    private ContentType contextType = ContentType.ActivityModel;
    //private ActionBarLayoutInterface actionBarLayoutInterface;
    public void initActionBarLayout(final Activity context, ACTIONBAR_TYPE actionBarModel, int headLayoutResID, int contentLayoutResID,ViewGroup relContentView) {
        this.context = context;
        this.headLayoutResID = headLayoutResID;
        this.contentLayoutResID = contentLayoutResID;
        this.contentView = relContentView;//fragment传递过来真实的contentView
        if(contentLayoutResID==-1){
            contextType=ContentType.FragmentModel;
            //actionBarLayoutInterface = FragmentActivityHelper.getInstance().getActionBarLayoutInterface();
        }else {
            contextType=ContentType.ActivityModel;
        }
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
                    if(contextType==ContentType.FragmentModel){
                        //actionBarLayoutInterface.onBack((AppCompatActivity) context);
                    }else {
                        ((Activity) context).finish();
                    }
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


    private ActionBarTip actionBarTip;
    public ActionBarTip getActionBarTip() {
        return actionBarTip;
    }

    private FrameLayout.LayoutParams layoutParams_header;//顶部导航
    private FrameLayout.LayoutParams layoutParams_content;//内容区域
    private FrameLayout.LayoutParams layoutParams_tip;//提示部分

    private void initLayout() {
        LayoutInflater mInflater = LayoutInflater.from(context);
        rootLayout = new FrameLayout(context);
        //contentView宽高
        if(contextType==ContentType.ActivityModel){
            mInflater.inflate(contentLayoutResID, rootLayout, true);
            contentView = (ViewGroup) rootLayout.getChildAt(0);
        }else if(contextType==ContentType.FragmentModel){
            rootLayout.addView(contentView);
        }

        actionBarTip = new ActionBarTip(context);
        actionBarTip.setContentView(R.layout.quickdevelop_activity_actionbar_tip);
        layoutParams_tip = (FrameLayout.LayoutParams) this.actionBarTip.getLayoutParams();
        if (layoutParams_tip == null) {
            layoutParams_tip = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        actionBarTip.setLayoutParams(layoutParams_tip);
        rootLayout.addView(actionBarTip);
        //header宽高
        mInflater.inflate(headLayoutResID, rootLayout, true);
        headView = (ViewGroup) rootLayout.getChildAt(2);

        //fragment view容器
        View FragmentLayout = new FrameLayout(context);
        FragmentLayout.setId(getFragmentContentViewId());
        rootLayout.addView(FragmentLayout);

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

        actionBarTip.setActionBarHeight(statusBar_Height + actionBar_Height);
        layoutParams_header = (FrameLayout.LayoutParams) this.headView.getLayoutParams();
        if (layoutParams_header == null) {
            layoutParams_header = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBar_Height + actionBar_Height);
        } else {
            layoutParams_header.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams_header.height = statusBar_Height + actionBar_Height;
        }
        headView.setLayoutParams(layoutParams_header);
        //view加载完成时回调
       /* headView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if(onHeadLayoutListener!=null){
                    onHeadLayoutListener.onLayoutComplete();
                }
            }
        });*/

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

    @Override
    public void setStateBarColorAuto(Boolean barColorAuto) {

    }

    /**
     * 设置导航栏颜色（对外）
     *
     * @param color
     */
    public void setBackGroundColor(int color) {
        headerBackgroundColor = color;
        headView.setBackgroundColor(color);
        refreshStateBarColor();
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
                refreshStateBarColor();
            }
        });
    }

/*    *//**
     * 导航栏样式三种
     *//*
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
    }*/

    private ViewGroup headView;
    private ViewGroup contentView;
    private int statusBar_Height = 0;
    private int actionBar_Height = 0;

    private ACTIONBAR_TYPE actionBarModel = ACTIONBAR_TYPE.NO_ACTION_BAR;//1,上下顺序排列（普通样式），2层叠排列（actionbar背景透明），3只显示内容

    /**
     * 导航栏构建者
     */
    public static class Builder {
        private ActionBarLayout2 activityLayout;
        private ACTIONBAR_TYPE actionBarModel = ACTIONBAR_TYPE.NORMAL;//1,上下顺序排列（普通样式），2层叠排列（actionbar背景透明），3只显示内容
        private Activity context;
        private int contentLayoutResID = -1;
        private ViewGroup contentView;
        private int headLayoutResID;

        public Builder(Activity context) {
            this.context = context;
        }

        public ActionBarLayout2 create() {
            activityLayout = new ActionBarLayout2(context, actionBarModel, headLayoutResID, contentLayoutResID,contentView);

            return activityLayout;
        }

        public void setContentView(int layoutResID) {
            this.contentLayoutResID = layoutResID;
        }

        public void setContentView(ViewGroup contentView) {
            this.contentView = contentView;
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
        QDLogger.d(TAG, "time=" + consumingTime);
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
        switch (actionBarModel) {
            case NO_ACTION_BAR:
                //contentView.layout(0,statusBar_Height ,0,0);
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                //headView.setPadding(0,headerPaddingTop+statusBar_Height,0,0);
                contentView.setPadding(contentView.getPaddingLeft(), contentPaddingTop + statusBar_Height, contentView.getPaddingRight(), contentView.getPaddingBottom());
                //setActionBarBackGroundDrawable(headerBackgroundDrawable);
                //setActionBarBackGroundColor(headerBackgroundColor);
                headView.setVisibility(View.GONE);
                break;
            case NO_ACTION_BAR_NO_STATUS:
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                contentView.setPadding(contentView.getPaddingLeft(), contentPaddingTop, contentView.getPaddingRight(), contentView.getPaddingBottom());

                //setActionBarBackGroundDrawable(headerBackgroundDrawable);
                //setActionBarBackGroundColor(headerBackgroundColor);
                headView.setVisibility(View.GONE);
                break;
            case NORMAL:
                layoutParams_content.topMargin = statusBar_Height + actionBar_Height;
                layoutParams_header.topMargin = 0;
                headView.setPadding(0, headerPaddingTop + statusBar_Height, 0, 0);
                contentView.setPadding(contentView.getPaddingLeft(), contentPaddingTop, contentView.getPaddingRight(), contentView.getPaddingBottom());
                setActionBarBackGroundDrawable(headerBackgroundDrawable);
                setActionBarBackGroundColor(headerBackgroundColor);
                headView.setVisibility(View.VISIBLE);
                break;
            case ACTION_STACK:
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                headView.setPadding(0, headerPaddingTop + statusBar_Height, 0, 0);
                contentView.setPadding(contentView.getPaddingLeft(), contentPaddingTop + statusBar_Height, contentView.getPaddingRight(), contentView.getPaddingBottom());
                setActionBarBackGroundColor(context.getResources().getColor(R.color.transparent));
                headView.setVisibility(View.VISIBLE);
                break;
            case ACTION_STACK_NO_STATUS:
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                contentView.setPadding(contentView.getPaddingLeft(), contentPaddingTop, contentView.getPaddingRight(), contentView.getPaddingBottom());

                setActionBarBackGroundColor(context.getResources().getColor(R.color.transparent));
                headView.setVisibility(View.VISIBLE);
                break;
            case ACTION_TRANSPARENT:
                layoutParams_content.topMargin = 0;
                layoutParams_header.topMargin = 0;
                contentView.setPadding(contentView.getPaddingLeft(), contentPaddingTop + actionBar_Height + statusBar_Height, contentView.getPaddingRight(), contentView.getPaddingBottom());

                setActionBarBackGroundColor(context.getResources().getColor(R.color.transparent));
                headView.setVisibility(View.VISIBLE);
                break;
            default:
                return null;
        }
        QDLogger.i(TAG, "contentPaddingTop=" + contentPaddingTop + ",actionBar_Height=" + actionBar_Height + ",statusBar_Height=" + statusBar_Height);
        this.contentView.setLayoutParams(layoutParams_content);
        this.headView.setLayoutParams(layoutParams_header);
        //状态栏颜色
        if (stateBarColorAuto) {
            //view加载完成时回调
            rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    refreshStateBarColor();
                }
            });
        }
        //setActionBarThemeColors(Color.WHITE, Color.BLACK);
        return rootLayout;
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
    public void refreshStateBarColor() {
        long consumingTime = System.currentTimeMillis() - startTime;
        QDLogger.d(TAG, "setStateBarColor=" + consumingTime);
        //截图取色
        Bitmap bitmap = ScreenShotUitl.getCacheBitmapFromViewTop(rootLayout, statusBar_Height);
        if (bitmap == null) {
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
        QDLogger.d(TAG, "setStateBarColor2=" + consumingTime2);
    }


    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
        if(headView!=null){
            headView.setBackgroundColor(headerBackgroundColor);
        }
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


}