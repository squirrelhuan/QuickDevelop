package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIViewHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.drawable.QDRoundButtonDrawable;

/**
 * Created by Squirrel桓 on 2019/1/5.
 */
public class QDButton extends AppCompatButton {

    public QDButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QDButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.QDButtonStyle);
        init(context, attrs, R.attr.QDButtonStyle);
    }

    public QDButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        QDRoundButtonDrawable bg = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QMUIViewHelper.setBackgroundKeepingPadding(this, bg);
    }

}
