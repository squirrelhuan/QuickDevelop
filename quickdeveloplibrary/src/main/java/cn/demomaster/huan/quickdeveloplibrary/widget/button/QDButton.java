package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.content.Context;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIViewHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundButtonDrawable;

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
        StateListDrawable drawable=new StateListDrawable();
//如果要设置莫项为false，在前面加负号 ，比如android.R.attr.state_focesed标志true，-android.R.attr.state_focesed就标志false

        QDRoundButtonDrawable bg_normal = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QDRoundButtonDrawable bg_focused = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QDRoundButtonDrawable bg_pressed = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QDRoundButtonDrawable bg_selected = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        drawable.addState(new int[]{android.R.attr.state_focused}, bg_focused);
        drawable.addState(new int[]{android.R.attr.state_pressed}, bg_pressed);
        drawable.addState(new int[]{android.R.attr.state_selected}, bg_selected);
        drawable.addState(new int[]{}, bg_normal);//默认
        //btn.setBackgroundDrawable(drawable);

        //QDRoundButtonDrawable bg = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QMUIViewHelper.setBackgroundKeepingPadding(this, drawable);
    }

}
