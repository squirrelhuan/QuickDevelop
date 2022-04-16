package cn.demomaster.huan.quickdeveloplibrary.widget.square;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import cn.demomaster.huan.quickdeveloplibrary.widget.CircleImageView;

/**
 * 正方形的ImageView
 */
public class SquareImageView extends CircleImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void init(AttributeSet attrs) {
        circle_background_radius = 0;
        super.init(attrs);
    }
}