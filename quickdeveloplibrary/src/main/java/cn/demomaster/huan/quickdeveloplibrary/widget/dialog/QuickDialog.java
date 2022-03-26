package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatDialog;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.widget.base.Gravity;
import cn.demomaster.huan.quickdeveloplibrary.widget.layout.VisibleLayout;
import cn.demomaster.qdrouter_library.quickview.ViewInfo;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Squirrel桓
 */
public class QuickDialog extends AppCompatDialog {

    // private Builder builder;
    private int width = ViewGroup.LayoutParams.MATCH_PARENT;
    private Gravity gravity = Gravity.CENTER;
    private boolean isFullScreen = false;
    private int margin = 0;//当isFullScreen=true时生效
    private int contentViewLayoutID = Resources.ID_NULL;

    private int animationStyleID = R.style.qd_dialog_animation_center_scale;
    public Map<Integer,ActionButton> bindViewsMap;

    public QuickDialog(Context context) {
        super(context);
    }

    View contentLayout;

    public QuickDialog(Context context, Builder builder) {
        super(context);
        //this.builder = builder;
        width = builder.width;
        contentLayout = builder.contentView;
        contentViewLayoutID = builder.layoutResID;
        animationStyleID = builder.animationStyleID;
        this.bindViewsMap = builder.bindViewsMap;
        isFullScreen = builder.isFullScreen;
        margin = builder.margin;
        setCancelable(builder.mCancelable);
        init();
    }

    private void init() {
        Window win = getWindow();
        if (animationStyleID != -1) {
            win.setWindowAnimations(animationStyleID);
        }

        if (isFullScreen) {//全屏显示
            win.getDecorView().setPadding(0, 0, 0, 0);
            win.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
            win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // 在底部，宽度撑满
            WindowManager.LayoutParams params = win.getAttributes();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.gravity = gravity.value();
            win.setAttributes(params);
        }

        getWindow().setWindowAnimations(animationStyleID);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER.value();
        if (contentViewLayoutID != Resources.ID_NULL) {
            contentLayout = LayoutInflater.from(getContext()).inflate(contentViewLayoutID, null, false);
        }
        if (contentLayout != null) {
       /* contentLayout.setOnClickListener(v -> {
            if (canceledOnTouchOutside && cancelable) {
                dismiss();
            }
        });*/
            ViewGroup layout = new RelativeLayout(getContext());
            layout.addView(contentLayout, layoutParams);
            setContentView(layout);
            if (bindViewsMap != null) {
                for (Map.Entry entry: bindViewsMap.entrySet()) {
                    final ActionButton actionButton = (ActionButton) entry.getValue();
                    if (actionButton.getId() != View.NO_ID) {
                        View button = contentLayout.findViewById(actionButton.getId());
                        if(button.getVisibility()==GONE) {
                            button.setVisibility(actionButton.getVisible());
                        }
                        if (button instanceof TextView) {
                            ((TextView) button).setText(actionButton.getText());
                            button.setOnClickListener(view -> {
                                if (actionButton.getOnClickListener() != null) {
                                    actionButton.getOnClickListener().onClick(QuickDialog.this, view, null);
                                } else {
                                    dismiss();
                                }
                            });
                        }
                        button.setVisibility(actionButton.getVisible());
                    }
                }
            }
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    boolean canceledOnTouchOutside = true;

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        canceledOnTouchOutside = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }


   /* @Override
    public boolean isHasPadding() {
        return true;
    }*/

    public enum ShowType {
        normal, noHeader, noFoot, onlyBody, contentView, contentLayout
    }

    public enum DataType {
        radio, checkbox, text, editor
    }

    public static class Builder implements Serializable {
        public Context context;
        public int width = ViewGroup.LayoutParams.MATCH_PARENT;
        public boolean isFullScreen = false;
        public int margin = 0;//当isFullScreen=true时生效
        public View contentView;
        public  int layoutResID=R.layout.quick_dialog_layout;
        public boolean mCancelable;
        public int animationStyleID = R.style.qd_dialog_animation_center_scale;

        public Map<Integer,ActionButton> bindViewsMap;

        public Builder(Context context) {
            this.context = context;
            bindViewsMap = new HashMap<>();
        }

        public Builder setCancelable(boolean mCancelable) {
            this.mCancelable = mCancelable;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setContentView(int layoutResID) {
            this.layoutResID = layoutResID;
            return this;
        }

        public Builder setAnimationStyleID(int animationStyleID) {
            this.animationStyleID = animationStyleID;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setFullScreen(boolean fullScreen) {
            isFullScreen = fullScreen;
            return this;
        }

        public Builder setMargin(int margin) {
            this.margin = margin;
            return this;
        }
        @IntDef({VISIBLE, INVISIBLE, GONE})
        @Retention(RetentionPolicy.SOURCE)
        public static  @interface Visibility {}
        public Builder hideView(@IdRes Integer... ids) {
           return setViewVisibe(GONE,ids);
        }
        public Builder setViewVisibe(@Visibility int visiable,@IdRes Integer[] ids) {
            //hideViews.addAll(Arrays.asList(ids));
            if(ids!=null) {
                for (Integer id : ids) {
                    ActionButton actionButton = findViewInfoByID(id);
                    actionButton.setVisible(visiable);
                    bindViewsMap.put(id, actionButton);
                }
            }
            return this;
        }

        private ActionButton findViewInfoByID(@IdRes Integer id) {
            if(bindViewsMap.containsKey(id)){
               return bindViewsMap.get(id);
            }
            ActionButton actionButton = new ActionButton();
            actionButton.setId(id);
            return actionButton;
        }

        public Builder bindView(@IdRes int viewResId, String text) {
            return bindView(viewResId, text, null);
        }
        public Builder bindView(@IdRes int viewResId, OnClickActionListener onClickListener) {
            return bindView(viewResId, null, onClickListener);
        }
        public Builder bindView(@IdRes int viewResId, String text, OnClickActionListener onClickListener) {
            ActionButton actionButton = findViewInfoByID(viewResId);
            actionButton.setId(viewResId);
            if (text != null) {
                actionButton.setText(text);
            }
            if (onClickListener != null) {
                actionButton.setOnClickListener(onClickListener);
            }
            this.bindViewsMap.put(viewResId,actionButton);
            return this;
        }

        public QuickDialog create() {
            return new QuickDialog(context, this);
        }
    }
    public static interface OnBindViewListener{
        void onBind(Dialog dialog, View view);
    }
}
