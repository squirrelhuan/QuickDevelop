package cn.demomaster.huan.quickdeveloplibrary.helper.toast;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import cn.demomaster.huan.quickdeveloplibrary.R;


/**
 * Created by Squirrel桓 on 2018/10/29.
 */
public class CPopupWindow extends PopupWindow {

    private CPopupWindow(PopBuilder builder) {
        this.setContentView(builder.contentView);
        this.setWidth(builder.width);
        this.setHeight(builder.height);
        this.setFocusable(builder.focusable);
        this.setAnimationStyle(R.style.pop_shot);

        setClippingEnabled(false);
    }

    public static class PopBuilder {
        private View contentView;
        private int width, height;
        private boolean focusable;

        public PopBuilder() {

        }

        public PopBuilder setContentView(View view, int width, int height, boolean focusable) {
            this.contentView = view;
            this.height = height;
            this.width = width;
            this.focusable = focusable;
            return this;
        }

        public CPopupWindow build() {
            return new CPopupWindow(this);
        }
    }

}
