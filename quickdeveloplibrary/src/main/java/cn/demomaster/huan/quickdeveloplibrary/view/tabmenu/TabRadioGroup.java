package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Squirrel桓 on 2018/11/24.
 */
public class TabRadioGroup extends LinearLayout {
    public TabRadioGroup(Context context) {
        super(context);
        mInflater = LayoutInflater.from(getContext());
        setGravity(Gravity.CENTER);
    }


    private List<TabRadioButton> tabRadioButtons = new ArrayList<>();

    LayoutInflater mInflater;

    public void setTabRadioButtons(List<TabRadioButton> tabRadioButtons) {
        removeAllViews();
        this.tabRadioButtons = tabRadioButtons;
        for (int i = 0; i < tabRadioButtons.size(); i++) {
            addView(tabRadioButtons.get(i));
            if (tabDividerResId != -1 && i < tabRadioButtons.size() - 1) {
                View view = mInflater.inflate(tabDividerResId, null);
                view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                addView(view);
            }
        }
    }

    private int tabDividerResId = -1;//分割符

    public void setTabDividerResId(int layoutId) {
        this.tabDividerResId = layoutId;
        if (tabRadioButtons != null) {
            setTabRadioButtons(tabRadioButtons);
        }
    }

    public void setOnCheckedChangeListener(final OnCheckedChangeListener onCheckedChangeListener) {
        for (int i = 0; i < tabRadioButtons.size(); i++) {
            tabRadioButtons.get(i).setTag(i);
            tabRadioButtons.get(i).setOnClickListener(view -> {
                onCheckedChangeListener.onCheckedChanged(view, (int) (view.getTag()));
                for (TabRadioButton button : tabRadioButtons) {
                    button.setState(false);
                }
                ((TabRadioButton) view).setState(true);
            });
        }
    }

    public void setCurrentTab(int tabIndex) {
        tabRadioButtons.get(tabIndex).performClick();
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
