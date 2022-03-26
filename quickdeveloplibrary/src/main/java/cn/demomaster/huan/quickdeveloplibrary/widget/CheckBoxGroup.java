package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.IdRes;

/**
 * 支持多选（多选数量）和单选
 */
public class CheckBoxGroup extends FlowLayout {

    public static enum CheckType {
        //notLimit数量不限 mandatoryLimit数量必选 rangeLimit数量范围
        notLimit,
        mandatoryLimit,
        rangeLimit
    }

    private boolean isSingle;//是否是单选
    private int fixedSelectCount = 1;//固定可选数量 适用于mandatoryLimit模式
    private int minSelectCount = 0;//可选最小数量 适用于rangeLimit模式
    private int maxSelectCount = Integer.MAX_VALUE;//可选最大数量 适用于rangeLimit模式
    private CheckType checkType = CheckType.notLimit;

    public CheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }

    public void setMinSelectCount(int minSelectCount) {
        this.minSelectCount = minSelectCount;
    }

    public int getMinSelectCount() {
        return minSelectCount;
    }

    public int getMaxSelectCount() {
        return maxSelectCount;
    }

    public void setMaxSelectCount(int maxSelectCount) {
        this.maxSelectCount = maxSelectCount;
    }

    public boolean isSingle() {
        boolean b = false;
        switch (checkType){
            case notLimit:
                b = isSingle;
                break;
            case rangeLimit:
                b = (maxSelectCount==minSelectCount&&minSelectCount==1)||(minSelectCount==0&&maxSelectCount==1);
                break;
            case mandatoryLimit:
                b = fixedSelectCount==1;
                break;
        }
        return b;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public int getFixedSelectCount() {
        return fixedSelectCount;
    }

    public void setFixedSelectCount(int fixedSelectCount) {
        this.fixedSelectCount = fixedSelectCount;
    }

    //获取当前已选项数量
    public int getCurrentSelectCount() {
        int currentSelectCount = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (childView instanceof CheckBox) {
                if (((CheckBox) childView).isChecked()) {
                    currentSelectCount = currentSelectCount + 1;
                }
            }
        }
        return currentSelectCount;
    }

    public CheckBoxGroup(Context context) {
        super(context);
        init();
    }

    public CheckBoxGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckBoxGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOnHierarchyChangeListener(onHierarchyChangeListener);
    }

    private OnHierarchyChangeListener onHierarchyChangeListener = new OnHierarchyChangeListener() {
        @Override
        public void onChildViewAdded(View parent, View child) {
            //QDLogger.d("Added:"+child.getId());
            initView();
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            //QDLogger.d("Removed");
            if (child instanceof CheckBox) {
                ((CheckBox) child).setOnCheckedChangeListener(null);
            }
        }
    };

    void initView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (childView instanceof CheckBox) {
                ((CheckBox) childView).setOnCheckedChangeListener(onChildCheckedChangeListener);
            }
        }
    }

    CompoundButton.OnCheckedChangeListener onChildCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onChildChecked(buttonView, isChecked);
        }
    };

    private void onChildChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (isSingle()
                    || (checkType == CheckType.mandatoryLimit && fixedSelectCount == 1)
                    || (checkType == CheckType.rangeLimit && (minSelectCount == 1 && maxSelectCount == 1))) {
                //处理单选的情况
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    View childView = getChildAt(i);
                    if (childView instanceof CheckBox) {
                        if (childView.getId() != buttonView.getId()) {
                            ((CheckBox) childView).setChecked(false);
                        }
                    }
                }
            }
        }

        if (checkType == CheckType.mandatoryLimit) {
            if (fixedSelectCount < getCurrentSelectCount()) {
                buttonView.setChecked(false);
                outOfMaxSelectCount(fixedSelectCount);
                return;
            }
        } else if (checkType == CheckType.rangeLimit) {
            if (maxSelectCount < getCurrentSelectCount()) {
                buttonView.setChecked(false);
                outOfMaxSelectCount(maxSelectCount);
                return;
            }
        }
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, isChecked, buttonView.getId());
        }
    }

    public void outOfMaxSelectCount(int maxSelectCount) {
    }

    OnCheckedChangeListener mOnCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group         the group in which the checked radio button has changed
         * @param isChecked     the unique identifier of the newly checked radio button
         * @param changedViewId the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(CheckBoxGroup group, boolean isChecked, @IdRes int changedViewId);
    }
}
