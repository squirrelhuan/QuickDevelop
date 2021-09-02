package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.DividerGravity;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;

/**
 * Created by Squirrel桓 on 2019/1/6.
 */
public class QDDialog extends QDDialog2 {

    // private Builder builder;
    public int actionButtonPadding;
    private String title;
    private String message;
    private int icon;
    private ShowType showType = ShowType.normal;
    private DataType dataType = DataType.text;
    private int width = ViewGroup.LayoutParams.MATCH_PARENT;
    private int gravity_header = Gravity.LEFT;
    private int gravity_body = Gravity.LEFT;
    private int gravity_foot = Gravity.CENTER;
    private int gravity = Gravity.CENTER;
    private boolean isFullScreen = false;
    private int margin = 0;//当isFullScreen=true时生效
    private int padding = -1;
    private int padding_header;
    private int padding_body;
    private int padding_foot;
    private int minHeight_header;
    private int minHeight_body;
    private int minHeight_foot;
    private int color_header = Color.TRANSPARENT;
    private int color_body = Color.TRANSPARENT;
    private int color_foot = Color.TRANSPARENT;
    private int text_color_header = Color.BLACK;
    private int text_color_body = Color.BLACK;
    private int text_color_foot = Color.BLACK;
    private int text_size_header = 18;
    private int text_size_body = 16;
    private int text_size_foot = 16;
    private int contentViewLayoutID;

    private int backgroundColor = Color.WHITE;
    private int lineColor = Color.GRAY;
    private float[] backgroundRadius = new float[8];
    private int animationStyleID = R.style.qd_dialog_animation_center_scale;
    private List<ActionButton> actionButtons = new ArrayList<>();
    public Map<Integer, OnClickActionListener> clickListenerMap = new HashMap<>();
    public QDDialog(Context context) {
        super(context);
    }
    View contentLayout;
    public QDDialog(Context context, Builder builder) {
        super(context);
        //this.builder = builder;
        actionButtonPadding = builder.actionButtonPadding;
        title = builder.title;
        message = builder.message;
        icon = builder.icon;
        showType = builder.showType;
        dataType = builder.dataType;
        width = builder.width;
        gravity_header = builder.gravity_header;
        gravity_body = builder.gravity_body;
        gravity_foot = builder.gravity_foot;
        padding_header = builder.padding_header;
        padding_body = builder.padding_body;
        padding_foot = builder.padding_foot;
        minHeight_header = builder.minHeight_header;
        minHeight_body = builder.minHeight_body;
        minHeight_foot = builder.minHeight_foot;
        color_header = builder.color_header;
        color_body = builder.color_body;
        color_foot = builder.color_foot;
        text_color_header = builder.text_color_header;
        text_color_body = builder.text_color_body;
        text_color_foot = builder.text_color_foot;
        text_size_header = builder.text_size_header;
        text_size_body = builder.text_size_body;
        text_size_foot = builder.text_size_foot;
        contentLayout = builder.contentView;
        contentViewLayoutID = builder.contentViewLayoutID;

        backgroundColor = builder.backgroundColor;
        lineColor = builder.lineColor;
        backgroundRadius = builder.backgroundRadius;
        animationStyleID = builder.animationStyleID;
        actionButtons = builder.actionButtons;

        padding = builder.padding;
        isFullScreen = builder.isFullScreen;
        margin = builder.margin;
        
        if(builder.clickListenerMap!=null) {
            clickListenerMap.putAll(builder.clickListenerMap);
        }
        init();
    }
    
    private LinearLayout contentLinearView;
    private LinearLayout headerView;
    private LinearLayout bodyView;
    private LinearLayout footView;

    private void init() {
        Window win = getWindow();
        if (animationStyleID != -1) {
            win.setWindowAnimations(animationStyleID);
        }

        if (padding != -1) {
            win.getDecorView().setPadding(0, 0, 0, 0);
        }
        if (isFullScreen) {//全屏显示
            win.getDecorView().setPadding(0, 0, 0, 0);
            win.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
            win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // 在底部，宽度撑满
            WindowManager.LayoutParams params = win.getAttributes();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.gravity = gravity;
            win.setAttributes(params);
        }

        getWindow().setWindowAnimations(animationStyleID);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        contentLinearView = new LinearLayout(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            contentLinearView.setId(View.generateViewId());
        }
        contentLinearView.setOrientation(LinearLayout.VERTICAL);
        //新建一个Drawable对象
        QDividerDrawable drawable_bg = new QDividerDrawable(DividerGravity.NONE);
        drawable_bg.setCornerRadii(backgroundRadius);
        drawable_bg.setBackGroundColor(backgroundColor);
        contentLinearView.setBackground(drawable_bg);
        //contentView.setPadding((int)builder.backgroundRadius[0],(int)builder.backgroundRadius[2],(int)builder.backgroundRadius[4],(int)builder.backgroundRadius[6]);
        if (title == null && message != null && actionButtons.size() == 0) {
            showType = ShowType.onlyBody;
        }
        if (title != null && message != null && actionButtons.size() == 0) {
            showType = ShowType.noFoot;
        }
/*
        int padding_header = padding_header;
        int padding_body = padding_body;*/
        switch (showType) {
            case normal:
                headerView = new LinearLayout(getContext());
                bodyView = new LinearLayout(getContext());
                footView = new LinearLayout(getContext());
                headerView.setPadding(padding_header, padding_header, padding_header, padding_header);
                bodyView.setPadding(padding_body, 0, padding_body, padding_body);
                break;
            case noHeader:
                bodyView = new LinearLayout(getContext());
                footView = new LinearLayout(getContext());
                bodyView.setPadding(padding_body, padding_body, padding_body, padding_body);
                break;
            case onlyBody:
                bodyView = new LinearLayout(getContext());
                bodyView.setPadding(padding_body, padding_body, padding_body, padding_body);
                break;
            case noFoot:
                headerView = new LinearLayout(getContext());
                bodyView = new LinearLayout(getContext());
                headerView.setPadding(padding_header, padding_header, padding_header, padding_header);
                bodyView.setPadding(padding_body, 0, padding_body, padding_body);
                break;
            case contentView:
                ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentLinearView.addView(contentLayout, layoutParams1);
                break;
            case contentLayout:
                ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                View view = LayoutInflater.from(getContext()).inflate(contentViewLayoutID, null, false);
                contentLinearView.addView(view, layoutParams2);
                break;
        }
        if (headerView != null) {
            contentLinearView.addView(headerView);
            headerView.setMinimumHeight(minHeight_header);
            headerView.setBackgroundColor(color_header);
            headerView.setGravity(gravity_header);
            headerView.setTag(gravity_header);
            addTextView(headerView, title, text_color_header, text_size_header);
        }
        if (bodyView != null) {
            contentLinearView.addView(bodyView);
            bodyView.setMinimumHeight(minHeight_body);
            bodyView.setBackgroundColor(color_body);
                        bodyView.setGravity(gravity_body);
            bodyView.setTag(gravity_body);
            addBodyTextView(bodyView, message, text_color_body, text_size_body);
        }
        int actionPadding = actionButtonPadding;//DisplayUtil.dip2px(getContext(), 10);
        if (footView != null) {
            contentLinearView.addView(footView);
            footView.setMinimumHeight(minHeight_foot);
            footView.setGravity(gravity_foot);
            footView.setBackgroundColor(color_foot);
            footView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams_button;
            if (gravity_foot == Gravity.CENTER) {
                layoutParams_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                //新建一个Drawable对象
                QDividerDrawable drawable = new QDividerDrawable(DividerGravity.TOP);
                drawable.setmStrokeColors(this.lineColor);
                footView.setBackground(drawable);
            } else {
                layoutParams_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            for (int i = 0; i < actionButtons.size(); i++) {
                final ActionButton actionButton = actionButtons.get(i);
                TextView button = new TextView(getContext());
                button.setText(actionButton.getText());
                button.setTextSize(text_size_foot);
                button.setTextColor(text_color_foot);
                button.setPadding(actionPadding * 3, actionPadding * 2, actionPadding * 3, actionPadding * 2);
                button.setGravity(Gravity.CENTER);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //获取selectableItemBackground中对应的attrId
                    TypedValue typedValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

                    int[] attribute = new int[]{android.R.attr.selectableItemBackground};
                    TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
                    button.setForeground(typedArray.getDrawable(0));
                    typedArray.recycle();
                }
                button.setOnClickListener(view -> {
                    if (actionButton.getOnClickListener() != null) {
                        actionButton.getOnClickListener().onClick(QDDialog.this, view,null);
                    } else {
                        dismiss();
                    }
                });

                //button.setBackgroundDrawable(null);
                footView.addView(button, layoutParams_button);
                if (i != actionButtons.size() - 1 && gravity_foot == Gravity.CENTER) {
                    View centerLineView = new View(getContext());
                    LinearLayout.LayoutParams layoutParams_line = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                    centerLineView.setLayoutParams(layoutParams_line);
                    centerLineView.setBackgroundColor(lineColor);
                    footView.addView(centerLineView);
                }
            }
        }

        ViewGroup layout = new RelativeLayout(getContext());
        layout.addView(contentLinearView, layoutParams);
        contentLinearView.setOnClickListener(v -> {

        });
        layout.setPadding(margin, margin, margin, margin);
        if (margin > 0) {
            layout.setOnClickListener(v -> {
                if (canceledOnTouchOutside && cancelable) {
                    dismiss();
                }
            });
        }
        setContentView(layout, layoutParams);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        for(Map.Entry entry: clickListenerMap.entrySet()){
            View view1 = view.findViewById((Integer) entry.getKey());
            if(view1!=null){
                view1.setOnClickListener(v -> ((OnClickActionListener) entry.getValue()).onClick(QDDialog.this,v,null));
            }
        }
    }
    
    boolean canceledOnTouchOutside = true;

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        canceledOnTouchOutside = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }

    public LinearLayout getContentLinearView() {
        return contentLinearView;
    }

    private void addBodyTextView(LinearLayout viewGroup, String title, int color, int textSize) {
        addTextView(viewGroup, title, color, textSize);
    }

    @Override
    public boolean isHasPadding() {
        return true;
    }

    private void addTextView(LinearLayout viewGroup, String title, int color, int textSize) {
        if (title != null) {
            TextView textView = new TextView(getContext());
            textView.setText(title);
            textView.setTextColor(color);
            textView.setTextSize(textSize);
            //textView.setPadding(padding, padding, padding, padding);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            switch ((int) viewGroup.getTag()) {
                case Gravity.LEFT:
                    layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    break;
                case Gravity.CENTER:
                    layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                    break;
                case Gravity.RIGHT:
                    layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    break;
            }
            textView.setLayoutParams(layoutParams);
            //
            textView.setGravity((int) viewGroup.getTag());
            viewGroup.addView(textView);
        }
    }

    public enum ShowType {
        normal, noHeader, noFoot, onlyBody, contentView, contentLayout
    }

    public enum DataType {
        radio, checkbox, text, editor
    }

    public static class Builder implements Serializable {
        public int actionButtonPadding;
        public Context context;
        public String title;
        public String message;
        public int icon;
        public ShowType showType = ShowType.normal;
        public DataType dataType = DataType.text;
        public int width = ViewGroup.LayoutParams.MATCH_PARENT;
        public int gravity_header = Gravity.LEFT;
        public int gravity_body = Gravity.LEFT;
        public int gravity_foot = Gravity.CENTER;
        public boolean isFullScreen = false;
        public int margin = 0;//当isFullScreen=true时生效
        public int padding = -1;
        public int padding_header;
        public int padding_body;
        public int padding_foot;
        public int minHeight_header;
        public int minHeight_body;
        public int minHeight_foot;
        public int color_header = Color.TRANSPARENT;
        public int color_body = Color.TRANSPARENT;
        public int color_foot = Color.TRANSPARENT;
        public int text_color_header = Color.BLACK;
        public int text_color_body = Color.BLACK;
        public int text_color_foot = Color.BLACK;
        public int text_size_header = 18;
        public int text_size_body = 16;
        public int text_size_foot = 16;
        public View contentView;
        public int contentViewLayoutID;

        public int backgroundColor = Color.WHITE;
        public int lineColor = Color.GRAY;
        public float[] backgroundRadius = new float[8];
        public int animationStyleID = R.style.qd_dialog_animation_center_scale;
        public List<ActionButton> actionButtons = new ArrayList<>();
        public Map<Integer, OnClickActionListener> clickListenerMap = new HashMap<>();
        
        public Builder(Context context) {
            this.context = context;
            this.actionButtonPadding = DisplayUtil.dip2px(context, 8);
            this.padding_header = DisplayUtil.dip2px(context, 10);
            this.padding_body = DisplayUtil.dip2px(context, 10);
            this.padding_foot = DisplayUtil.dip2px(context, 10);
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public Builder setShowType(ShowType showType) {
            this.showType = showType;
            return this;
        }

        public Builder setDataType(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder setGravity_header(int gravity_header) {
            this.gravity_header = gravity_header;
            return this;
        }

        public Builder setGravity_body(int gravity_body) {
            this.gravity_body = gravity_body;
            return this;
        }

        public Builder setGravity_foot(int gravity_foot) {
            this.gravity_foot = gravity_foot;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }


        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            this.showType = ShowType.contentView;
            return this;
        }

        public Builder setContentViewLayout(int contentViewLayout) {
            this.contentViewLayoutID = contentViewLayout;
            this.showType = ShowType.contentLayout;
            return this;
        }

        public Builder setColor_header(int color_header) {
            this.color_header = color_header;
            return this;
        }

        public Builder setColor_body(int color_body) {
            this.color_body = color_body;
            return this;
        }

        public Builder setColor_foot(int color_foot) {
            this.color_foot = color_foot;
            this.actionButtonPadding = color_foot;
            return this;
        }

        public Builder setActionButtonPadding(int actionButtonPadding) {
            this.actionButtonPadding = actionButtonPadding;
            return this;
        }

        public Builder setLineColor(int lineColor) {
            this.lineColor = lineColor;
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

        public Builder setPadding_header(int padding_header) {
            this.padding_header = padding_header;
            return this;
        }

        public Builder setPadding_body(int padding_body) {
            this.padding_body = padding_body;
            return this;
        }

        public Builder setPadding_foot(int padding_foot) {
            this.padding_foot = padding_foot;
            return this;
        }

        public Builder setMinHeight_header(int minHeight_header) {
            this.minHeight_header = minHeight_header;
            return this;
        }

        public Builder setMinHeight_body(int minHeight_body) {
            this.minHeight_body = minHeight_body;
            return this;
        }

        public Builder setMinHeight_foot(int minHeight_foot) {
            this.minHeight_foot = minHeight_foot;
            return this;
        }

        public Builder setText_color_header(int text_color_header) {
            this.text_color_header = text_color_header;
            return this;
        }

        public Builder setText_color_body(int text_color_body) {
            this.text_color_body = text_color_body;
            return this;
        }

        public Builder setText_color_foot(int text_color_foot) {
            this.text_color_foot = text_color_foot;
            return this;
        }

        public Builder setText_size_header(int text_size_header) {
            this.text_size_header = text_size_header;
            return this;
        }

        public Builder setText_size_body(int text_size_body) {
            this.text_size_body = text_size_body;
            return this;
        }

        public Builder setText_size_foot(int text_size_foot) {
            this.text_size_foot = text_size_foot;
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

        public Builder setPadding(int padding) {
            this.padding = padding;
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

        public Builder addAction(String text) {
            return addAction(text, null);
        }
        
        public Builder addAction(String text, OnClickActionListener onClickListener) {
            ActionButton actionButton = new ActionButton();
            if (text != null) {
                actionButton.setText(text);
            }
            if (onClickListener != null) {
                actionButton.setOnClickListener(onClickListener);
            }
            this.actionButtons.add(actionButton);
            return this;
        }

        public Builder addAction(int viewID, OnClickActionListener onClickListener) {
            this.clickListenerMap.put(viewID,onClickListener);
            return this;
        }

        public QDDialog create() {
            return new QDDialog(context, this);
        }
        
    }

}
