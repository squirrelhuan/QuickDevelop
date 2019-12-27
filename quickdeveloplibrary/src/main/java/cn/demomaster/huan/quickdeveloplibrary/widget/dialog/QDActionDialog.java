package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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

import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

/**
 * Created by Squirrel桓 on 2019/1/7.
 */
public class QDActionDialog extends Dialog {

    private Builder builder;

    public QDActionDialog(Context context, Builder builder) {
        super(context);
        this.builder = builder;
        init();
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        builder.mCancelable = flag;
    }

    private LinearLayout contentView;

    public LinearLayout getContentView() {
        return contentView;
    }

    private void init() {
        Window win = getWindow();
        if (builder.backgroundColor != Color.TRANSPARENT) {
            win.getDecorView().setPadding(0, 0, 0, 0);
            win.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
            win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        // 在底部，宽度撑满
        WindowManager.LayoutParams params = win.getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = builder.gravity;
        win.setAttributes(params);
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //新建一个Drawable对象
        QDividerDrawable drawable_bg = new QDividerDrawable(QDividerDrawable.Gravity.NONE);
        //drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(builder.backgroundColor);

        win.setBackgroundDrawable(drawable_bg);
        win.setDimAmount(builder.dimAmount);
        setCancelable(builder.mCancelable);
        int p = DisplayUtil.dip2px(getContext(), builder.padding);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        contentView = new LinearLayout(builder.context);
        contentView.setOrientation(LinearLayout.VERTICAL);
        delayMillis = builder.delayMillis;

        //新建一个Drawable对象
        QDividerDrawable drawable_content_bg = new QDividerDrawable(QDividerDrawable.Gravity.NONE);
        drawable_content_bg.setCornerRadii(builder.backgroundRadius);
        drawable_content_bg.setBackGroundColor(builder.contentbackgroundColor);
        contentView.setBackground(drawable_content_bg);

        WindowManager.LayoutParams lp = win.getAttributes();
        contentView.setGravity(Gravity.CENTER);
        int w = QMUIDisplayHelper.getScreenWidth(builder.context) / 3;
        contentView.setMinimumHeight(w / 2);
        contentView.setMinimumWidth(w);
        int w2 = QMUIDisplayHelper.getScreenWidth(builder.context) / 2;
        int l = w / 3 * 2;
        if (builder.stateType == StateType.COMPLETE || builder.stateType == StateType.LOADING || builder.stateType == StateType.ERROR || builder.stateType == StateType.WARNING) {
            contentView.setOrientation(LinearLayout.VERTICAL);

            StateView stateView = new StateView(builder.context);
            stateView.setLayoutParams(new LinearLayout.LayoutParams(l, l * 3 / 4));
            if (builder.stateType == StateType.COMPLETE) {
                stateView.setStateType(StateView.StateType.COMPLETE);
            }
            if (builder.stateType == StateType.LOADING) {
                stateView.setStateType(StateView.StateType.LOADING);
                //LoadingCircleView loadingCircleView = new LoadingCircleView(builder.context);
                //loadingCircleView.setLayoutParams(new LinearLayout.LayoutParams(l,l*3/4));
                //contentView.addView(loadingCircleView);
            }
            if (builder.stateType == StateType.ERROR) {
                stateView.setStateType(StateView.StateType.ERROR);
            }
            if (builder.stateType == StateType.WARNING) {
                stateView.setStateType(StateView.StateType.WARNING);
            }
            stateView.setDrawCricleBackground(false);
            contentView.addView(stateView);
        } else {
            if (builder.stateType == StateType.TOPIMAGE) {
                contentView.setOrientation(LinearLayout.VERTICAL);
                ImageView imageView = new ImageView(builder.context);
                imageView.setImageResource(builder.topImage);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxHeight(l);
                imageView.setMaxWidth(l);
                contentView.addView(imageView);
            }
            if (builder.stateType == StateType.LEFTIMAGE) {
                contentView.setOrientation(LinearLayout.HORIZONTAL);
                ImageView imageView = new ImageView(builder.context);
                imageView.setImageResource(builder.leftImage);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxHeight(l / 2);
                imageView.setMaxWidth(l / 2);
                imageView.setPadding(0, 0, DisplayUtil.dip2px(builder.context, 5), 0);
                contentView.addView(imageView);
            }
            if (builder.stateType == StateType.LEFTVIEWCLASS) {
                contentView.setOrientation(LinearLayout.HORIZONTAL);
                View imageView = null;
                if (View.class.isAssignableFrom(builder.leftViewClass)) {
                    try {
                        //无参数 imageView = (View) builder.leftViewClass.newInstance();
                        //有参数需要使用Constructor类对象
                        //这种方式和下面这种方式都行，注意这里的参数类型是 new Class[]
                        Constructor ct = builder.leftViewClass.getDeclaredConstructor(new Class[]{Context.class});
                        ct.setAccessible(true);
                        imageView = (View) ct.newInstance(new Object[]{builder.context});
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(l / 2, l / 2);
                        layoutParams1.rightMargin = l / 8;
                        contentView.addView(imageView, layoutParams1);
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
            if (builder.stateType == StateType.TOPVIEWCLASS) {
                contentView.setOrientation(LinearLayout.VERTICAL);
                View imageView = null;
                if (View.class.isAssignableFrom(builder.topViewClass)) {
                    try {
                        //无参数 imageView = (View) builder.leftViewClass.newInstance();
                        //有参数需要使用Constructor类对象
                        //这种方式和下面这种方式都行，注意这里的参数类型是 new Class[]
                        Constructor ct = builder.topViewClass.getDeclaredConstructor(new Class[]{Context.class});
                        ct.setAccessible(true);
                        imageView = (View) ct.newInstance(new Object[]{builder.context});
                        ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(l, l);
                        if(builder.imageHeight!=0){
                            layoutParams1 = new LinearLayout.LayoutParams(layoutParams1.width,builder.imageHeight);
                        }
                        if(builder.imageWidth!=0){
                            layoutParams1 = new LinearLayout.LayoutParams(builder.imageWidth,layoutParams1.height);
                        }
                        contentView.addView(imageView, layoutParams1);
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
            if (builder.stateType == StateType.CONTENTVIEW) {
                ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentView.addView(builder.contentView, layoutParams1);
            }
            if (builder.stateType == StateType.CONTENTVIEWID) {
                ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                View view = LayoutInflater.from(builder.context).inflate(builder.contentViewLayoutID, null, false);
                contentView.addView(view, layoutParams1);
            }

        }
        if (builder.message != null) {
            TextView textView = new TextView(builder.context);
            textView.setText(builder.message);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(builder.messageTextSize);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentView.addView(textView, layoutParams1);
        }
        contentView.setPadding(p, p, p, p);

        ViewGroup layout = new RelativeLayout(getContext());
        ((RelativeLayout) layout).setGravity(builder.gravity);
        layout.addView(contentView, layoutParams);
        layout.setBackgroundColor(builder.backgroundColor);
        if (builder.mCancelable) {
            layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dismiss();
                    return false;
                }
            });
        }
        setContentView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private int delayMillis = 1500;

    @Override
    public void show() {
        super.show();
        if (delayMillis != -1) {
            contentView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, delayMillis);
        }
    }

    public static enum StateType {
        COMPLETE, WARNING, ERROR, LOADING, IMAGE, TEXT, TOPIMAGE, LEFTIMAGE, TOPVIEW, LEFTVIEW, TOPVIEWCLASS, LEFTVIEWCLASS, CONTENTVIEW, CONTENTVIEWID
    }

    public static class Builder {
        public int gravity = Gravity.CENTER;
        private Context context;
        private String message;
        private int messageTextSize = 16;
        private float dimAmount = .0f;
        private int backgroundColor = Color.TRANSPARENT;
        private int contentbackgroundColor = Color.WHITE;
        private float[] backgroundRadius = new float[8];
        private StateType stateType = StateType.TEXT;
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
        private boolean mCancelable = true;
        private int padding=15;


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

        public Builder setPadding(int padding) {
            this.padding = padding;
            return this;
        }
        public Builder setmCancelable(boolean mCancelable) {
            this.mCancelable = mCancelable;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            this.stateType = StateType.CONTENTVIEW;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            this.stateType = StateType.CONTENTVIEW;
            return this;
        }

        public Builder setContentViewLayout(int contentViewLayout) {
            this.contentViewLayoutID = contentViewLayout;
            this.stateType = StateType.CONTENTVIEWID;
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
            this.stateType = StateType.LEFTVIEW;
            return this;
        }

        public Builder setTopView(View topView) {
            this.topView = topView;
            this.stateType = StateType.TOPVIEW;
            return this;
        }

        public Builder setLeftViewClass(Class leftViewClass) {
            this.leftViewClass = leftViewClass;
            this.stateType = StateType.LEFTVIEWCLASS;
            return this;
        }

        public Builder setTopViewClass(Class topViewClass) {
            this.topViewClass = topViewClass;
            this.stateType = StateType.TOPVIEWCLASS;
            return this;
        }

        public View getLeftView() {
            return leftView;
        }

        public View getTopView() {
            return topView;
        }

        public Builder setStateType(StateType stateType) {
            this.stateType = stateType;
            return this;
        }

        public Builder setLeftImage(int leftImage) {
            this.leftImage = leftImage;
            this.stateType = StateType.LEFTIMAGE;
            return this;
        }

        public Builder setTopImage(int topImage) {
            this.topImage = topImage;
            this.stateType = StateType.TOPIMAGE;
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

        public QDActionDialog create() {
            return new QDActionDialog(context, this);
        }
    }
}
