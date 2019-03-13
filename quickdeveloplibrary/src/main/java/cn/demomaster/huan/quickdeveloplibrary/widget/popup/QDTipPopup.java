package cn.demomaster.huan.quickdeveloplibrary.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundButtonDrawable;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundDrawable;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDTipContainerView;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;

public class QDTipPopup extends QDPopup {
    private Builder builder;
    private Context context;

    public QDTipPopup(Context context, Builder builder) {
        super(context);
        this.builder = builder;
        this.context = context;
        init();
    }


    private void init() {
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        QDTipContainerView frameLayout = new QDTipContainerView(context);
        TextView textView = new TextView(context);
        textView.setText(builder.message);
        textView.setTextSize(20);
        textView.setTextColor(Color.WHITE);
        frameLayout.addView(textView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //新建一个Drawable对象
        QDividerDrawable drawable_bg=new QDividerDrawable(QDividerDrawable.Gravity.NONE);
        drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(builder.backgroundColor);
        frameLayout.setBackground(drawable_bg);
        //QDRoundButtonDrawable bg = new QDRoundButtonDrawable();
        //bg.setBgData(builder.backgroundColor);
      //  bg.setStrokeData(borderWidth, colorBorder);
       // bg.setCornerRadii(builder.backgroundRadius);

        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(frameLayout);
    }

    public static enum Direction{
        top,left,right,bottom
    }
    public static class Builder {
        private Context context;
        private String message;
        private int backgroundColor = Color.YELLOW;
        private float[] backgroundRadius=new float[8];
        private Direction direction;

        public Builder(Context context) {
            this.context = context;
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

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public QDTipPopup create() {
            return new QDTipPopup(context, this);
        }
    }
}
