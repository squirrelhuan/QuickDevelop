package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caizhiming
 * @created on 2015-4-13
 */
public class FlowLayout extends ViewGroup {

    private Context mContext;
    // 存储所有子View
    private List<List<View>> mAllChildViews = new ArrayList<>();
    // 每一行的高度
    private List<Integer> mLineHeight = new ArrayList<>();

    public FlowLayout(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub

        // 父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // 如果当前ViewGroup的宽高为wrap_content的情况
        int width = 0;// 自己测量的 宽度
        int height = 0;// 自己测量的高度
        // 记录每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        // 获取子view的个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childWidth;
            int childHeight;
            // 得到LayoutParams
            childWidth = child.getMeasuredWidth() ;
            // 子View占据的高度
            childHeight = child.getMeasuredHeight() ;
            if(child.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                // 子View占据的宽度
                childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                // 子View占据的高度
                childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
            // 换行时候
            if (lineWidth + childWidth > sizeWidth) {
                // 对比得到最大的宽度
                width = Math.max(width, lineWidth);
                // 重置lineWidth
                lineWidth = childWidth;
                // 记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {// 不换行情况
                // 叠加行宽
                lineWidth += childWidth;
                // 得到最大行高
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 处理最后一个子View的情况
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        // wrap_content
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        mAllChildViews.clear();
        mLineHeight.clear();
        // 获取当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;
        // 记录当前行的view
        List<View> lineViews = new ArrayList<View>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
           int margin_left=0 ,margin_right=0,margin_top=0,margin_bottom=0;
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                margin_left = lp.leftMargin;
                margin_right = lp.rightMargin;
                margin_top = lp.topMargin;
                margin_bottom = lp.bottomMargin;
            }
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果需要换行
            if (childWidth + lineWidth + margin_left + margin_right > width) {
                // 记录LineHeight
                mLineHeight.add(lineHeight);
                // 记录当前行的Views
                mAllChildViews.add(lineViews);
                // 重置行的宽高
                lineWidth = 0;
                lineHeight = childHeight + margin_top + margin_bottom;
                // 重置view的集合
                lineViews = new ArrayList();
            }
            lineWidth += childWidth + margin_left+ margin_right;
            lineHeight = Math.max(lineHeight, childHeight + margin_top + margin_bottom);
            lineViews.add(child);
        }
        // 处理最后一行
        mLineHeight.add(lineHeight);
        mAllChildViews.add(lineViews);

        // 设置子View的位置
        int left = 0;
        int top = 0;
        // 获取行数
        int lineCount = mAllChildViews.size();
        //lineCount = lineCount >= 3 ? 3 : 0;
        for (int i = 0; i < lineCount; i++) {
            // 当前行的views和高度
            lineViews = mAllChildViews.get(i);
            lineHeight = mLineHeight.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                // 判断是否显示
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                int cLeft = left ;
                int cTop = top ;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                if (child.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                    cLeft = left + lp.leftMargin;
                    cTop = top + lp.topMargin;
                    cRight = cLeft + child.getMeasuredWidth();
                    cBottom = cTop + child.getMeasuredHeight();

                    left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                }

                // 进行子View进行布局
                child.layout(cLeft, cTop, cRight, cBottom);
                left += child.getMeasuredWidth();
            }
            left = 0;
            top += lineHeight;
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        // TODO Auto-generated method stub

        return new MarginLayoutParams(getContext(), attrs);
    }

    private static OnClickListener onItemClickedListener;

    public void setOnItemClickListener(final OnClickListener myListener) {
        onItemClickedListener = myListener;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setOnClickListener(myListener);
        }
    }

    private View convertView;

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setAdapter(FlowAdapter adapter) {
        if (adapter == null) {
            return;
        }
        if (getChildCount() > 0) {
            removeAllViews();
        }

        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i);
            if (view.getParent() == null) {
                addView(view);
            }
        }


       /* WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 7;
        lp.rightMargin = 7;
        lp.topMargin = 7;
        lp.bottomMargin = 7;
        int ii = 0;
        for (Tag value : asList) {
            if (value.getTag().length() <= 6) {
                TextView view = new TextView(mContext);
                //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.tag_text_drawable, null));
                view.setBackground(getResources().getDrawable(R.drawable.tag_text_drawable));
                view.setPadding(width / 50, width / 150, width / 50, width / 150);
                view.setText(value.getTag());
                view.setTextSize(11);
                // exceptions_list.get(i).getSimpleName()
                view.setTextColor(Color.WHITE);
//                Random random = new Random();
//                int c = (random.nextInt(4));
                int c = switchColor(ii);
                view.setBackgroundResource(c);
                //if(tags_c!=null&&tags_c.contains(value)){view.setBackgroundResource(R.drawable.check_bg);
                //view.setTextColor(getResources().getColor(R.color.white));}
                addView(view, lp);
                ii++;
                if (ii > 5) ii = 0;
            }
        }*/
        setOnItemClickListener(onItemClickedListener);
        postInvalidate();
    }


    public int setCheckItem(View v) {
        for (int i = 0; i < getChildCount(); i++) {
            if (((TextView) v).getText().equals(((TextView) getChildAt(i)).getText())) {
                return i;
            }
        }
        return 0;
    }


    public static interface FlowAdapter {

        int getCount();

        Object getItem(int position);

        long getItemId(int position);

        View getView(int position);

    }

    public static interface ViewHolder {

        View getItemView(int position);

    }


}