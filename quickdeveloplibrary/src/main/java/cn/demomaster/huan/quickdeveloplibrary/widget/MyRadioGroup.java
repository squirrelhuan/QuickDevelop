package cn.demomaster.huan.quickdeveloplibrary.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * flow RadioGroup
 */
public class MyRadioGroup extends RadioGroup {

    public MyRadioGroup(Context context) {
        super(context);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    // 存储所有子View
    private List<List<View>> mAllChildViews = new ArrayList<>();
    // 每一行的高度
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        int lineWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//单行宽度
        //QDLogger.e("单行宽度=" + lineWidth);
        mAllChildViews = new ArrayList<>();//所有view按行分割成多个list
        List<View> lineViewList = new ArrayList<>();//单行
        for (int i = 0; i < count; i++) {
            View chid = getChildAt(i);
            // QDLogger.println("text=" + textView.getText() + ",s=" + lineViewList.size());
            if (lineViewList.size() == 0) {//空行
                lineViewList.add(chid);
                MarginLayoutParams layoutParams = (MarginLayoutParams) chid.getLayoutParams();
                int viewWith = chid.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                if (viewWith > lineWidth) {//单个元素占用一行的情况
                    //QDLogger.e("换行=" + viewWith);
                    //QDLogger.println("单个元素占用一行：" + lineViewList.size());
                    List<View> viewList = new ArrayList<>(lineViewList);
                    mAllChildViews.add(viewList);
                    lineViewList.clear();
                }
            } else {//判断新加的view会不会撑满整个父窗体
                int currentWidth = 0;
                for (int j = 0; j < lineViewList.size(); j++) {
                    View v = lineViewList.get(j);
                    MarginLayoutParams layoutParams = (MarginLayoutParams) v.getLayoutParams();
                    currentWidth += v.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                }

                MarginLayoutParams layoutParams = (MarginLayoutParams) chid.getLayoutParams();
                int viewWith = chid.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                if (currentWidth + viewWith > lineWidth) {//再追加一个元素会超出最大宽度
                    //QDLogger.println("换行追加=" + lineViewList.size());
                    List<View> viewList = new ArrayList<>(lineViewList);
                    mAllChildViews.add(viewList);
                    lineViewList.clear();
                } 
                lineViewList.add(chid);
            }
        }
        if (lineViewList.size() > 0) {
            mAllChildViews.add(lineViewList);
        }

        //QDLogger.println("mAllChildViews=" + mAllChildViews.size());
        mLineHeight = new ArrayList<>();
        for (int i = 0; i < mAllChildViews.size(); i++) {
            List<View> lineViews = mAllChildViews.get(i);
            int maxLineHeight = 0;
            for (int j = 0; j < lineViews.size(); j++) {
                View v = lineViews.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) v.getLayoutParams();
                maxLineHeight = Math.max(maxLineHeight, v.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
            }
            mLineHeight.add(maxLineHeight);
        }

        int layoutHeight = 0;
        for (int i = 0; i < mLineHeight.size(); i++) {
            layoutHeight += mLineHeight.get(i);
            // QDLogger.println("h" + i + "=" + mLineHeight.get(i));
        }
        layoutHeight += getPaddingTop() + getPaddingBottom();

        //高度度
        int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //QDLogger.println("specModeHeight=" + specModeHeight + ",layoutHeight=" + layoutHeight + ",count=" + count);
        switch (specModeHeight) {
            case MeasureSpec.UNSPECIFIED:
                setMeasuredDimension(widthMeasureSpec, layoutHeight);
                break;
            case MeasureSpec.AT_MOST://AT_MOST 自适应模式，根据设置的行数动态申请高度
                setMeasuredDimension(widthMeasureSpec, layoutHeight);
                //super.onMeasure(widthMeasureSpec, layoutHeight);
                break;
            case MeasureSpec.EXACTLY:
                break;
        }
        //QDLogger.println("specModeHeight=" + specModeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 获取行数
        int lineCount = mAllChildViews.size();
        for (int i = 0; i < lineCount; i++) {
            // 当前行的views和高度
            List<View> lineViews = mAllChildViews.get(i);//单行
            int top_last = getPaddingTop();
            for (int j = 0; j < i; j++) {
                top_last += mLineHeight.get(j);
            }
            // 设置子View的位置
            int left_last = getPaddingLeft();
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                // 判断是否显示
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                int left = left_last + layoutParams.leftMargin;
                int top = top_last + layoutParams.topMargin;
                // 进行子View进行布局
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                //QDLogger.println("进行子View进行布局:" + left + "," + top + "," + (left + child.getMeasuredWidth()) + "," + (top + child.getMeasuredHeight()));
                left_last += layoutParams.leftMargin + child.getMeasuredWidth() + layoutParams.rightMargin;
            }
        }
    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(),attrs);// new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAllChildViews.clear();
        mLineHeight.clear();
    }
}
