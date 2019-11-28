package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;

/**
 * Created by Squirrel桓 on 2019/1/6.
 */
public class QDInputDialog extends Dialog {

    private Builder builder;

    public QDInputDialog(Context context, Builder builder) {
        super(context);
        this.builder = builder;
        init();
    }

    private LinearLayout contentView;
    private LinearLayout headerView;
    private LinearLayout bodyView;
    private LinearLayout footView;
    private void init() {
        getWindow().setWindowAnimations(builder.animationStyleID);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(builder.width, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView = new LinearLayout(builder.context);
        contentView.setOrientation(LinearLayout.VERTICAL);
        //新建一个Drawable对象
        QDividerDrawable drawable_bg = new QDividerDrawable(QDividerDrawable.Gravity.NONE);
        drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(builder.backgroundColor);
        contentView.setBackground(drawable_bg);
        //contentView.setPadding((int)builder.backgroundRadius[0],(int)builder.backgroundRadius[2],(int)builder.backgroundRadius[4],(int)builder.backgroundRadius[6]);
        if (builder.title == null && builder.message != null && builder.actionButtons.size() == 0) {
            builder.showType = ShowType.onlyBody;
        }
        if (builder.title != null && builder.message != null && builder.actionButtons.size() == 0) {
            builder.showType = ShowType.noFoot;
        }

        int padding_header = builder.padding_header;
        int padding_body = builder.padding_body;
        switch (builder.showType) {
            case normal:
                headerView = new LinearLayout(builder.context);
                bodyView = new LinearLayout(builder.context);
                footView = new LinearLayout(builder.context);
                headerView.setPadding(padding_header, padding_header, padding_header, padding_header);
                bodyView.setPadding(padding_body, 0, padding_body, padding_body);
                break;
            case noHeader:
                bodyView = new LinearLayout(builder.context);
                footView = new LinearLayout(builder.context);
                bodyView.setPadding(padding_body, padding_body, padding_body, padding_body);
                break;
            case onlyBody:
                bodyView = new LinearLayout(builder.context);
                bodyView.setPadding(padding_body, padding_body, padding_body, padding_body);
                break;
            case noFoot:
                headerView = new LinearLayout(builder.context);
                bodyView = new LinearLayout(builder.context);
                headerView.setPadding(padding_header, padding_header, padding_header, padding_header);
                bodyView.setPadding(padding_body, 0, padding_body, padding_body);
                break;
        }
        if (headerView != null) {
            contentView.addView(headerView);
            headerView.setMinimumHeight(builder.minHeight_header);
            headerView.setBackgroundColor(builder.color_header);
            headerView.setGravity(builder.gravity_header);
            headerView.setTag(builder.gravity_header);
            addTextView(headerView, builder.title,builder.text_color_header, builder.text_size_header);
        }
        if (bodyView != null) {
            contentView.addView(bodyView);
            bodyView.setMinimumHeight(builder.minHeight_body);
            bodyView.setBackgroundColor(builder.color_body);

            bodyView.setGravity(builder.gravity_body);
            bodyView.setTag(builder.gravity_body);
            addBodyTextView(bodyView, builder.message,builder.text_color_body,builder.text_size_body);
        }
        int actionPadding = builder.actionButtonPadding;//DisplayUtil.dip2px(getContext(), 10);
        if (footView != null) {
            contentView.addView(footView);
            footView.setMinimumHeight(builder.minHeight_foot);
            footView.setGravity(builder.gravity_foot);
            footView.setBackgroundColor(builder.color_foot);
            footView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams_button;
            if (builder.gravity_foot == Gravity.CENTER) {
                layoutParams_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                //新建一个Drawable对象
                QDividerDrawable drawable = new QDividerDrawable(QDividerDrawable.Gravity.TOP);
                drawable.setmStrokeColors(this.builder.lineColor);
                footView.setBackground(drawable);
            } else {
                layoutParams_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            for (int i = 0; i < builder.actionButtons.size(); i++) {
                final ActionButton actionButton = builder.actionButtons.get(i);
                TextView button = new TextView(getContext());
                button.setText(actionButton.getText());
                button.setTextSize(builder.text_size_foot);
                button.setTextColor(builder.text_color_foot);
                button.setPadding(actionPadding * 3, (int) (actionPadding * 2), actionPadding * 3, (int) (actionPadding * 2));
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
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actionButton.onClickListener != null) {
                            actionButton.onClickListener.onClick(QDInputDialog.this, TextUtils.isEmpty(textView.getText())?null:textView.getText().toString());
                        } else {
                            dismiss();
                        }
                    }
                });

                //button.setBackgroundDrawable(null);
                footView.addView(button, layoutParams_button);
                if (i != builder.actionButtons.size() - 1&&builder.gravity_foot == Gravity.CENTER) {
                    View centerLineView = new View(getContext());
                    LinearLayout.LayoutParams layoutParams_line = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                    centerLineView.setLayoutParams(layoutParams_line);
                    centerLineView.setBackgroundColor(builder.lineColor);
                    footView.addView(centerLineView);
                }
            }
        }

        ViewGroup layout = new RelativeLayout(getContext());
        layout.addView(contentView, layoutParams);
        setContentView(layout, layoutParams);
    }

    private void addBodyTextView(LinearLayout viewGroup, String title, int color, int textSize) {
        addEditView(viewGroup, title, color, textSize);
    }
    EditText textView;
    public EditText getEditView() {
        return textView;
    }

    private void addEditView(LinearLayout viewGroup, String title, int color, int textSize) {
        if (title != null) {
            textView = new EditText(getContext());
            //int i = textView.getInputType();
            textView.setInputType(builder.inputType);
            textView.setText(title);
            textView.setHint(builder.hint);
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

    public static enum ShowType {
        normal, noHeader, noFoot, onlyBody
    }

    public static enum DataType {
        radio, checkbox, text, editor
    }

    public static class Builder {
        public int actionButtonPadding;
        private String hint;
        private Context context;
        private String title;
        private String message;
        private int inputType = InputType.TYPE_TEXT_VARIATION_NORMAL;
        private int icon;
        private ShowType showType = ShowType.normal;
        private DataType dataType = DataType.text;
        private int width = ViewGroup.LayoutParams.MATCH_PARENT;
        private int gravity_header = Gravity.LEFT;
        private int gravity_body = Gravity.LEFT;
        private int gravity_foot = Gravity.CENTER;
        private int padding_header ;
        private int padding_body ;
        private int padding_foot ;
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
        private int backgroundColor = Color.WHITE;
        private int lineColor = Color.GRAY;
        private float[] backgroundRadius = new float[8];
        private int animationStyleID = R.style.qd_dialog_animation_center_scale;
        private List<ActionButton> actionButtons = new ArrayList<>();

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

        public QDInputDialog create() {
            return new QDInputDialog(context, this);
        }

        public Builder setInputType(int typeNumberFlagSigned) {
            this.inputType = typeNumberFlagSigned;
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }
    }

    public static interface OnClickActionListener {
        void onClick(QDInputDialog dialog,String value);
    }

    public static class ActionButton {
        private String text;
        private int textColor;
        private OnClickActionListener onClickListener;

        ActionButton() {
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public OnClickActionListener getOnClickListener() {
            return onClickListener;
        }

        public void setOnClickListener(OnClickActionListener onClickListener) {
            this.onClickListener = onClickListener;
        }
    }
}