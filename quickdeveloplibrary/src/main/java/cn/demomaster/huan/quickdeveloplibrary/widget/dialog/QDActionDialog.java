package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIViewHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleBallView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleView;
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

    private LinearLayout contentView;
    private void init() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setDimAmount(0f);
        setCancelable(true);
        int p = DisplayUtil.dip2px(getContext(), 15);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        contentView = new LinearLayout(builder.context);
        contentView.setOrientation(LinearLayout.VERTICAL);

        //新建一个Drawable对象
        QDividerDrawable drawable_bg=new QDividerDrawable(QDividerDrawable.Gravity.NONE);
        drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(builder.backgroundColor);
        contentView.setBackground(drawable_bg);
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        contentView.setGravity(Gravity.CENTER);
        int w = QMUIDisplayHelper.getScreenWidth(builder.context)/3;
        contentView.setMinimumHeight(w/2);
        contentView.setMinimumWidth(w);
        int w2 = QMUIDisplayHelper.getScreenWidth(builder.context)/2;
        int l=w/3*2;
        if(builder.stateType==StateType.COMPLETE||builder.stateType==StateType.LOADING||builder.stateType==StateType.ERROR||builder.stateType==StateType.WARNING){
            contentView.setOrientation(LinearLayout.VERTICAL);

            StateView stateView = new StateView(builder.context);
            stateView.setLayoutParams(new LinearLayout.LayoutParams(l,l*3/4));
            if(builder.stateType==StateType.COMPLETE){
                stateView.setStateType(StateView.StateType.COMPLETE);
            }
            if(builder.stateType==StateType.LOADING){
                stateView.setStateType(StateView.StateType.LOADING);
                //LoadingCircleView loadingCircleView = new LoadingCircleView(builder.context);
                //loadingCircleView.setLayoutParams(new LinearLayout.LayoutParams(l,l*3/4));
                //contentView.addView(loadingCircleView);
            }
            if(builder.stateType==StateType.ERROR){
                stateView.setStateType(StateView.StateType.ERROR);
            }
            if(builder.stateType==StateType.WARNING){
                stateView.setStateType(StateView.StateType.WARNING);
            }
            stateView.setDrawCricleBackground(false);
            contentView.addView(stateView);
        }else {
            if(builder.stateType==StateType.TOPIMAGE){
                contentView.setOrientation(LinearLayout.VERTICAL);
                ImageView imageView = new ImageView(builder.context);
                imageView.setImageResource(builder.topImage);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxHeight(l);
                imageView.setMaxWidth(l);
                contentView.addView(imageView);
            }
            if(builder.stateType==StateType.LEFTIMAGE){
                contentView.setOrientation(LinearLayout.HORIZONTAL);
                ImageView imageView = new ImageView(builder.context);
                imageView.setImageResource(builder.leftImage);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxHeight(l/2);
                imageView.setMaxWidth(l/2);
                imageView.setPadding(0,0,DisplayUtil.dip2px(builder.context,5),0);
                contentView.addView(imageView);
            }
        }
        if(builder.message!=null) {
            TextView textView = new TextView(builder.context);
            textView.setText(builder.message);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(14);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            contentView.addView(textView,layoutParams1);
        }
        contentView.setPadding(p,p,p,p);

        ViewGroup layout = new RelativeLayout(getContext());
        layout.addView(contentView, layoutParams);
        setContentView(layout, layoutParams);
    }

    @Override
    public void show() {
        super.show();
        contentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },1500);
    }

    public static enum StateType {
        COMPLETE, WARNING, ERROR,LOADING,IMAGE,TEXT,TOPIMAGE,LEFTIMAGE
    }
    public static class Builder {
        private Context context;
        private String message;
        private int backgroundColor = Color.WHITE;
        private float[] backgroundRadius=new float[8];
        private StateType stateType = StateType.TEXT;
        private int leftImage;
        private int topImage;


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


        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setBackgroundRadius(float backgroundRadiu) {
            for(int i=0;i<backgroundRadius.length;i++) {
                this.backgroundRadius[i] = backgroundRadiu;
            }
            return this;
        }
        public Builder setBackgroundRadius(float[] backgroundRadius) {
            this.backgroundRadius = backgroundRadius;
            return this;
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

        public QDActionDialog create() {
            return new QDActionDialog(context, this);
        }
    }
}
