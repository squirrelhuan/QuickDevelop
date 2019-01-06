package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.drawable.QDividerDrawable;

import static cn.demomaster.huan.quickdeveloplibrary.ApplicationParent.TAG;

/**
 * Created by Squirrel桓 on 2019/1/6.
 */
public class QDDialog extends Dialog {

    private Builder builder;

    public QDDialog(Context context, Builder builder) {
        super(context);
        this.builder = builder;
        init();
    }

    private LinearLayout contentView;
    private LinearLayout headerView;
    private LinearLayout bodyView;
    private LinearLayout footView;

    private void init() {

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView = new LinearLayout(builder.context);
        contentView.setOrientation(LinearLayout.VERTICAL);
        if (builder.title == null && builder.message != null && builder.actionButtons.size() == 0) {
            builder.showType = ShowType.onlyBody;
        }
        if (builder.title != null && builder.message != null && builder.actionButtons.size() == 0) {
            builder.showType = ShowType.noFoot;
        }

        int p = DisplayUtil.dip2px(getContext(), 10);
        switch (builder.showType) {
            case normal:
                headerView = new LinearLayout(builder.context);
                bodyView = new LinearLayout(builder.context);
                footView = new LinearLayout(builder.context);
                headerView.setPadding(p, p, p, p);
                bodyView.setPadding(p, 0, p, p);
                break;
            case onlyBody:
                bodyView = new LinearLayout(builder.context);
                bodyView.setPadding(p, p, p, p);
                break;
            case noFoot:
                headerView = new LinearLayout(builder.context);
                bodyView = new LinearLayout(builder.context);
                headerView.setPadding(p, p, p, p);
                bodyView.setPadding(p, 0, p, p);
                break;
        }
        if (headerView != null) {
            contentView.addView(headerView);
            //headerView.setBackgroundColor(builder.color_header);
            headerView.setBackgroundColor(builder.color_header);
            headerView.setGravity(builder.gravity_header);
            headerView.setTag(builder.gravity_header);
            addTextView(headerView, builder.title, getContext().getResources().getColor(R.color.black), 18);
        }
        if (bodyView != null) {
            contentView.addView(bodyView);
            bodyView.setBackgroundColor(builder.color_body);
            //新建一个Drawable对象
            QDividerDrawable drawable=new QDividerDrawable(Gravity.BOTTOM);

            bodyView.setBackground(drawable);
            bodyView.setGravity(builder.gravity_body);
            bodyView.setTag(builder.gravity_body);
            addTextView(bodyView, builder.message);
        }
        if (footView != null) {
            contentView.addView(footView);
            footView.setGravity(builder.gravity_foot);
            footView.setBackgroundColor(builder.color_foot);
            footView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams_button;
            if (builder.gravity_foot == Gravity.CENTER) {
                layoutParams_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            } else {
                layoutParams_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            for (int i = 0; i < builder.actionButtons.size(); i++) {
                final ActionButton actionButton = builder.actionButtons.get(i);
                TextView button = new TextView(getContext());
                button.setText(actionButton.getText());
                button.setTextSize(16);
                button.setTextColor(getContext().getResources().getColor(R.color.black));
                button.setPadding(p * 2, (int) (p * 2), p * 2, (int) (p * 2));
                button.setGravity(Gravity.CENTER);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (actionButton.onClickListener != null) {
                            actionButton.onClickListener.onClick(QDDialog.this);
                        }else {
                            dismiss();
                        }
                    }
                });

                //button.setBackgroundDrawable(null);
                footView.addView(button, layoutParams_button);
            }
        }

        ViewGroup layout = new RelativeLayout(getContext());
        layout.addView(contentView, layoutParams);
        setContentView(layout, layoutParams);
    }

    private void addTextView(LinearLayout viewGroup, String title) {
        addTextView(viewGroup, title, getContext().getResources().getColor(R.color.black), 14);
    }

    private void addTextView(LinearLayout viewGroup, String title, int color, int textSize) {
        if (title != null) {
            TextView textView = new TextView(getContext());
            textView.setText(title);
            textView.setTextColor(color);
            textView.setTextSize(textSize);
            int p = DisplayUtil.dip2px(getContext(), 5);
            textView.setPadding(p, p, p, p);
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
        private Context context;
        private String title;
        private String message;
        private int icon;
        private ShowType showType = ShowType.normal;
        private DataType dataType = DataType.text;
        private int gravity_header = Gravity.LEFT;
        private int gravity_body = Gravity.LEFT;
        private int gravity_foot = Gravity.CENTER;
        private int color_header = Color.TRANSPARENT;
        private int color_body = Color.TRANSPARENT;
        private int color_foot = Color.TRANSPARENT;
        private List<ActionButton> actionButtons = new ArrayList<>();

        public Builder(Context context) {
            this.context = context;
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

        public QDDialog create() {
            return new QDDialog(context, this);
        }
    }

    public static interface OnClickActionListener {
        void onClick(QDDialog dialog);
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
