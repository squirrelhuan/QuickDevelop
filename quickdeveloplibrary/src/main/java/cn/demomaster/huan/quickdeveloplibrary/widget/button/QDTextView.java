package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * @author squirrel桓
 * @date 2019/1/7.
 * description：
 */
public class QDTextView extends AppCompatTextView {
    public QDTextView(Context context) {
        super(context);
        init();
    }

    public QDTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QDTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取selectableItemBackground中对应的attrId
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

            int[] attribute = new int[]{android.R.attr.selectableItemBackground};
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
            setForeground(typedArray.getDrawable(0));
            typedArray.recycle();

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
