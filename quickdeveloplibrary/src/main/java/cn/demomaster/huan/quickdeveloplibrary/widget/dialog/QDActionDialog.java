package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.DividerGravity;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadStateType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

/**
 * Created by Squirrel桓 on 2019/1/7.
 */
public class QDActionDialog extends Dialog {

    //private Builder builder;
    public int gravity = Gravity.CENTER;
    private Context context;
    private String message;
    private int messageTextSize = 16;
    private int messageTextColor = Color.WHITE;
    private float dimAmount = .0f;
    private int backgroundColor = Color.TRANSPARENT;
    private int contentbackgroundColor = Color.WHITE;
    private float[] backgroundRadius = new float[8];
    private QDActionStateType stateType = QDActionStateType.TEXT;
    private int leftImage;
    private int topImage;
    private View leftView;
    private View topView;
    private Class leftViewClass;
    private Class topViewClass;
    private int delayMillis = 1500;//默认-1即为不自动关闭。其他值则对应时间后自动dismiss()
    private int imageHeight;
    private int imageWidth;
    private View contentView;
    private int contentViewLayoutID;
    private boolean mCancelable = true;
    private int padding = 15;
    private int animationStyleID = -1;

    public QDActionDialog(Context context, Builder builder) {
        //设置全屏将会覆盖状态栏
        super(context, builder.styleId);
        gravity = builder.gravity;
        this.context = builder.context;
        message = builder.message;
        messageTextSize = builder.messageTextSize;
        messageTextColor = builder.messageTextColor;
        dimAmount = builder.dimAmount;
        backgroundColor = builder.backgroundColor;
        contentbackgroundColor = builder.contentbackgroundColor;
        backgroundRadius = builder.backgroundRadius;
        stateType = builder.stateType;
        leftImage = builder.leftImage;
        topImage = builder.topImage;
        leftView = builder.leftView;
        topView = builder.topView;
        leftViewClass = builder.leftViewClass;
        topViewClass = builder.topViewClass;
        delayMillis = builder.delayMillis;//默认-1即为不自动关闭。其他值则对应时间后自动dismiss()
        imageHeight = builder.imageHeight;
        imageWidth = builder.imageWidth;
        contentView = builder.contentView;
        contentViewLayoutID = builder.contentViewLayoutID;
        mCancelable = builder.cancelable;
        padding = builder.padding;
        animationStyleID = builder.animationStyleID;
        init();
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable = flag;
    }

    private LinearLayout contentLinearView;

    public LinearLayout getContentView() {
        return contentLinearView;
    }

    private void init() {
        Window win = getWindow();
        if (animationStyleID != -1) {
            win.setWindowAnimations(animationStyleID);
        }
        if (backgroundColor != Color.TRANSPARENT) {
            win.getDecorView().setPadding(0, 0, 0, 0);
            win.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
            win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // 在底部，宽度撑满
        WindowManager.LayoutParams params = win.getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = gravity;
        win.setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //新建一个Drawable对象
        QDividerDrawable drawable_bg = new QDividerDrawable(DividerGravity.NONE);
        //drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(backgroundColor);

        win.setBackgroundDrawable(drawable_bg);
        win.setDimAmount(dimAmount);
        setCancelable(mCancelable);
        int p = DisplayUtil.dip2px(getContext(), padding);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        contentLinearView = new LinearLayout(context);
        contentLinearView.setOrientation(LinearLayout.VERTICAL);

        //新建一个Drawable对象
        QDividerDrawable drawable_content_bg = new QDividerDrawable(DividerGravity.NONE);
        drawable_content_bg.setCornerRadii(backgroundRadius);
        drawable_content_bg.setBackGroundColor(contentbackgroundColor);
        contentLinearView.setBackground(drawable_content_bg);

        contentLinearView.setGravity(Gravity.CENTER);
        int w = QMUIDisplayHelper.getScreenWidth(context) / 3;
        contentLinearView.setMinimumHeight(w / 2);
        contentLinearView.setMinimumWidth(w);
        int l = w / 3 * 2;
        if (stateType == QDActionStateType.COMPLETE) {
            contentLinearView.setOrientation(LinearLayout.VERTICAL);
            StateView stateView = new StateView(context);
            stateView.setLayoutParams(new LinearLayout.LayoutParams(l, l * 3 / 4));
            LoadStateType stateType1 = LoadStateType.NONE;
            if (stateType == QDActionStateType.COMPLETE) {
                stateType1 = LoadStateType.COMPLETE;
            }
            if (stateType == QDActionStateType.ERROR) {
                stateType1 = LoadStateType.ERROR;
            }
            if (stateType == QDActionStateType.LOADING) {
                stateType1 = LoadStateType.LOADING;
            }
            if (stateType == QDActionStateType.WARNING) {
                stateType1 = LoadStateType.WARNING;
            }
            stateView.setStateType(stateType1);
            //LoadingCircleView loadingCircleView = new LoadingCircleView(builder.context);
            //loadingCircleView.setLayoutParams(new LinearLayout.LayoutParams(l,l*3/4));
            //contentView.addView(loadingCircleView);

            stateView.setDrawCricleBackground(false);
            contentLinearView.addView(stateView);
        } else {
            if (stateType == QDActionStateType.TOPIMAGE) {
                contentLinearView.setOrientation(LinearLayout.VERTICAL);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(topImage);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxHeight(l);
                imageView.setMaxWidth(l);
                contentLinearView.addView(imageView);
            }
            if (stateType == QDActionStateType.LEFTIMAGE) {
                contentLinearView.setOrientation(LinearLayout.HORIZONTAL);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(leftImage);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxHeight(l / 2);
                imageView.setMaxWidth(l / 2);
                imageView.setPadding(0, 0, DisplayUtil.dip2px(context, 5), 0);
                contentLinearView.addView(imageView);
            }
            if (stateType == QDActionStateType.LEFTVIEWCLASS) {
                contentLinearView.setOrientation(LinearLayout.HORIZONTAL);
                View imageView = null;
                if (View.class.isAssignableFrom(leftViewClass)) {
                    try {
                        //无参数 imageView = (View) builder.leftViewClass.newInstance();
                        //有参数需要使用Constructor类对象
                        //这种方式和下面这种方式都行，注意这里的参数类型是 new Class[]
                        Constructor ct = leftViewClass.getDeclaredConstructor(new Class[]{Context.class});
                        ct.setAccessible(true);
                        imageView = (View) ct.newInstance(new Object[]{context});
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(l / 2, l / 2);
                        layoutParams1.rightMargin = l / 8;
                        contentLinearView.addView(imageView, layoutParams1);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (stateType == QDActionStateType.TOPVIEWCLASS) {
                contentLinearView.setOrientation(LinearLayout.VERTICAL);
                View imageView = null;
                if (View.class.isAssignableFrom(topViewClass)) {
                    try {
                        //无参数 imageView = (View) builder.leftViewClass.newInstance();
                        //有参数需要使用Constructor类对象
                        //这种方式和下面这种方式都行，注意这里的参数类型是 new Class[]
                        Constructor ct = topViewClass.getDeclaredConstructor(new Class[]{Context.class});
                        ct.setAccessible(true);
                        imageView = (View) ct.newInstance(new Object[]{context});
                        ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(l, l);
                        if (imageHeight != 0) {
                            layoutParams1 = new LinearLayout.LayoutParams(layoutParams1.width, imageHeight);
                        }
                        if (imageWidth != 0) {
                            layoutParams1 = new LinearLayout.LayoutParams(imageWidth, layoutParams1.height);
                        }
                        contentLinearView.addView(imageView, layoutParams1);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (stateType == QDActionStateType.CONTENTVIEW) {
                ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentLinearView.addView(contentView, layoutParams1);
            }
            if (stateType == QDActionStateType.CONTENTVIEWID) {
                ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                View view = LayoutInflater.from(context).inflate(contentViewLayoutID, null, false);
                contentLinearView.addView(view, layoutParams1);
                contentLinearView.setBackgroundColor(Color.TRANSPARENT);
                padding = 0;
            }

        }
        if (message != null) {
            TextView textView = new TextView(context);
            textView.setText(message);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(messageTextColor);
            textView.setTextSize(messageTextSize);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentLinearView.addView(textView, layoutParams1);
        }
        contentLinearView.setPadding(p, p, p, p);

        ViewGroup layout = new RelativeLayout(getContext());
        ((RelativeLayout) layout).setGravity(gravity);
        layout.addView(contentLinearView, layoutParams);
        layout.setBackgroundColor(backgroundColor);
        if (mCancelable) {
            layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dismiss();
                    return false;
                }
            });
        }
        setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));




       /* Window window = getWindow();
        View decorView = window.getDecorView();
        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(backgroundColor);
        }
        //设置导航栏颜
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setNavigationBarColor(backgroundColor);
        }
        //内容扩展到导航栏
        window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);*/
    }

    //private int delayMillis = 1500;

    @Override
    public void show() {
        super.show();
        if (delayMillis != -1) {
            contentLinearView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, delayMillis);
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
        if (isShowing()) {
            Context context = ((ContextWrapper) getContext()).getBaseContext();
            if (context instanceof Activity) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        dismiss();
                    }
                } else {
                    if (!((Activity) context).isFinishing()) {
                        dismiss();
                    }
                }
            } else {
                dismiss();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    public static class Builder {
        public int styleId = R.style.Dialog_Fullscreen;
        public int gravity = Gravity.CENTER;
        private Context context;
        private String message;
        private int messageTextSize = 16;
        private int messageTextColor = Color.WHITE;
        private float dimAmount = .0f;
        private int backgroundColor = Color.TRANSPARENT;
        private int contentbackgroundColor = Color.WHITE;
        private float[] backgroundRadius = new float[8];
        private QDActionStateType stateType = QDActionStateType.TEXT;
        private int leftImage;
        private int topImage;
        private View leftView;
        private View topView;
        private Class leftViewClass;
        private Class topViewClass;
        private int delayMillis = -1;//默认-1即为不自动关闭。其他值则对应时间后自动dismiss()
        private int imageHeight;
        private int imageWidth;
        private View contentView;
        private int contentViewLayoutID;
        private boolean cancelable = true;
        private int padding = 15;
        private int animationStyleID = -1;
        // private int animationStyleID = R.style.qd_dialog_animation_center_scale;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessageTextColor(int messageTextColor) {
            this.messageTextColor = messageTextColor;
            return this;
        }

        public Builder setPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            this.stateType = QDActionStateType.CONTENTVIEW;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            this.stateType = QDActionStateType.CONTENTVIEW;
            return this;
        }

        public Builder setContentViewLayout(int contentViewLayout) {
            this.contentViewLayoutID = contentViewLayout;
            this.stateType = QDActionStateType.CONTENTVIEWID;
            return this;
        }

        public Builder setDimAmount(float dimAmount) {
            this.dimAmount = dimAmount;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setContentbackgroundColor(int contentbackgroundColor) {
            this.contentbackgroundColor = contentbackgroundColor;
            return this;
        }

        public Builder setBackgroundRadius(float backgroundRadiu) {
            for (int i = 0; i < backgroundRadius.length; i++) {
                this.backgroundRadius[i] = backgroundRadiu;
            }
            return this;
        }

        public Builder setBackgroundRadius(float[] backgroundRadius) {
            this.backgroundRadius = backgroundRadius;
            return this;
        }

        public Builder setLeftView(View leftView) {
            this.leftView = leftView;
            this.stateType = QDActionStateType.LEFTVIEW;
            return this;
        }

        public Builder setTopView(View topView) {
            this.topView = topView;
            this.stateType = QDActionStateType.TOPVIEW;
            return this;
        }

        public Builder setLeftViewClass(Class leftViewClass) {
            this.leftViewClass = leftViewClass;
            this.stateType = QDActionStateType.LEFTVIEWCLASS;
            return this;
        }

        public Builder setTopViewClass(Class topViewClass) {
            this.topViewClass = topViewClass;
            this.stateType = QDActionStateType.TOPVIEWCLASS;
            return this;
        }

        public View getLeftView() {
            return leftView;
        }

        public View getTopView() {
            return topView;
        }

        public Builder setStateType(QDActionStateType stateType) {
            this.stateType = stateType;
            return this;
        }

        public Builder setLeftImage(int leftImage) {
            this.leftImage = leftImage;
            this.stateType = QDActionStateType.LEFTIMAGE;
            return this;
        }

        public Builder setTopImage(int topImage) {
            this.topImage = topImage;
            this.stateType = QDActionStateType.TOPIMAGE;
            return this;
        }

        public Builder setDelayMillis(int delayMillis) {
            this.delayMillis = delayMillis;
            return this;
        }

        public Builder setMessageTextSize(int messageTextSize) {
            this.messageTextSize = messageTextSize;
            return this;
        }

        public Builder setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
            return this;
        }

        public Builder setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
            return this;
        }


        public Builder setAnimationStyleID(int animationStyleID) {
            this.animationStyleID = animationStyleID;
            return this;
        }

        public QDActionDialog create() {
            return new QDActionDialog(context, this);
        }
    }
}
