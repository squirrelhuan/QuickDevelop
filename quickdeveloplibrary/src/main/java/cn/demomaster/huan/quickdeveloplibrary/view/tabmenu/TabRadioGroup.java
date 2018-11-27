package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Squirrel桓 on 2018/11/24.
 */
public class TabRadioGroup extends LinearLayout {
    public TabRadioGroup(Context context) {
        super(context);
    }

    public TabRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private List<TabRadioButton> tabRadioButtons = new ArrayList<>();

    public void addTabButton(TabRadioButton child) {
        tabRadioButtons.add(child);
        addView(child);
        if(tabDividerView!=null){
            if(getChildCount()>1){
               // addView(tabDividerView);
            }
        }
    }

    private View tabDividerView;//分割符

    public void addTabDividerView(View child) {
        this.tabDividerView = child;
    }

    public void selectTabButton() {
        for (int i = 0; i < getChildCount(); i++) {

        }
    }

    public void setOnCheckedChangeListener(final OnCheckedChangeListener onCheckedChangeListener) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setTag(i);
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCheckedChangeListener.onCheckedChanged(view, (int) (view.getTag()));
                    for (TabRadioButton button : tabRadioButtons) {
                        button.setState(false);
                    }
                    ((TabRadioButton) view).setState(true);
                }
            });
        }
    }

    public void setCurrentTab(int tabIndex){
        getChildAt(tabIndex).performClick();
    }

    public void resume() {
        for (TabRadioButton button : tabRadioButtons) {
            button.setState(false);
        }
    }


    public interface OnCheckedChangeListener {
        void onCheckedChanged(View var1, int var2);
    }

    public interface TabRadioButtonInterface {
        void setState(Boolean state);

        void setTabName(String tabName);

        void initView(Context context);
    }

    public static abstract class TabRadioButton extends LinearLayout implements TabRadioButtonInterface {

        public TabRadioButton(Context context) {
            super(context);
            initView(context);
        }

        public TabRadioButton(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TabRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


    }

}
