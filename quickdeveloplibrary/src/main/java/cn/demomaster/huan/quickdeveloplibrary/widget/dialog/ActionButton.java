package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

public class ActionButton {
    private String text;
    private int textColor;
    private OnClickActionListener onClickListener;

    public ActionButton() {
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
