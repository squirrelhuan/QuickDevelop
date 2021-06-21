package cn.demomaster.huan.quickdeveloplibrary.operatguid;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

/**
 * Created by Squirrel桓 on 2019/1/7.
 */
public class GuiderActionDialog extends Dialog {

    // private Builder builder;
    private boolean hasStateBar = true;
    private Activity context;
    private String message;
    private GuiderModel guiderModel;
    private int backgroundColor = Color.WHITE;
    private float[] backgroundRadius = new float[8];

    public GuiderActionDialog(Activity context, Builder builder) {
        super(context);
        this.context = context;
        message = builder.message;
        guiderModel = builder.guiderModel;
        backgroundColor = builder.backgroundColor;
        backgroundRadius = builder.backgroundRadius;
        //去除遮罩
        getWindow().setDimAmount(0f);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            hasStateBar = false;
        }

        //getWindow().setWindowAnimations(R.style.FadeInPopWin);  //添加动画
        getWindow().setWindowAnimations(-1);  //添加动画
        initData();
    }

    private LinearLayout contentView;

    private void initData() {
        setCancelable(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        contentView = new LinearLayout(context);
        //contentView.setBackgroundColor(Color.WHITE);
        onActionFinishListener = new OnActionFinishListener() {
            @Override
            public void onFinish() {
                dismiss();
            }
        };
        GuiderView guiderSurfaceView = new GuiderView(context, guiderModel, hasStateBar, onActionFinishListener);
        contentView.addView(guiderSurfaceView, layoutParams);
        //contentView.setBackgroundResource(R.color.red);
        ViewGroup layout = new RelativeLayout(getContext());
        layout.addView(contentView, layoutParams);

        setContentView(layout, layoutParams);
    }

    private OnActionFinishListener onActionFinishListener;

    public interface OnActionFinishListener {
        void onFinish();
    }

    @Override
    public void show() {
        super.show();

    }

    public static enum StateType {
        COMPLETE, WARNING, ERROR, LOADING, IMAGE, TEXT, TOPIMAGE, LEFTIMAGE
    }

    public static class Builder {
        private Activity context;
        private String message;
        private GuiderModel guiderModel;
        private int backgroundColor = Color.WHITE;
        private float[] backgroundRadius = new float[8];

        public Builder(Activity context, GuiderModel guiderModel, View view) {
            this.context = context;
            this.guiderModel = guiderModel;
        }

        public Builder setContext(Activity context) {
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
            for (int i = 0; i < backgroundRadius.length; i++) {
                this.backgroundRadius[i] = backgroundRadiu;
            }
            return this;
        }

        public Builder setBackgroundRadius(float[] backgroundRadius) {
            this.backgroundRadius = backgroundRadius;
            return this;
        }

        public GuiderActionDialog create() {
            return new GuiderActionDialog(context, this);
        }
    }
}
