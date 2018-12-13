package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**自适应宽度的RecyclerView（仅供单列）
 * @author squirrel桓
 * @date 2018/12/13.
 * description：
 */
public class WrapListView extends ListView {
    public WrapListView(@NonNull Context context) {
        super(context);
    }

    public WrapListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = meathureWidthByChilds() + getPaddingLeft() + getPaddingRight();
        super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth,MeasureSpec.UNSPECIFIED),heightMeasureSpec);//注意，这个地方一定是MeasureSpec.UNSPECIFIED
    }
    public int meathureWidthByChilds() {
        int maxWidth = 0;
        View view = null;
        for (int i = 0; i < getAdapter().getCount(); i++) {
            view = getAdapter().getView(i, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (view.getMeasuredWidth() > maxWidth){
                maxWidth = view.getMeasuredWidth();
            }
            view = null;
        }
        return maxWidth;
    }

}
