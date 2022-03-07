package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.view.View;

public class ActionButton {
    private int id = View.NO_ID;
    private String text;
    private int textColor;
    private int visible = View.VISIBLE;
    private OnClickActionListener onClickListener;

    public ActionButton() {
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
