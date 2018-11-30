package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * @author squirrel桓
 * @date 2018/11/29.
 * description：
 */
public class SimplePictureGallery extends RelativeLayout {
    private Context context;
    public SimplePictureGallery(Context context) {
        super(context);
        init(context);
    }

    public SimplePictureGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimplePictureGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context){
        this.context = context;

        RecyclerView recyclerView = new RecyclerView(context);

        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(layoutParams);
        this.addView(recyclerView);

    }
}
