package cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;

public class QdVideo extends VideoView {
    public QdVideo(Context context) {
        super(context);
        init();
    }

    public QdVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QdVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public QdVideo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
/*

        mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
*/

        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

}
