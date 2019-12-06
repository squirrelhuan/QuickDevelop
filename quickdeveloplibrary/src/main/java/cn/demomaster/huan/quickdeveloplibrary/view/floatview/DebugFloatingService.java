package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * Created
 */
public class DebugFloatingService extends QDFloatingService {
    @Override
    public View setContentView(final Context context) {
        LinearLayout linearLayout = new LinearLayout(context.getApplicationContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.circle);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(120,120));
        imageView.setAlpha(.5f);
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dissmissWindow(context,DebugFloatingService.this.getClass());
            }
        });*/
        linearLayout.addView(imageView);
        return linearLayout;
    }

    @Override
    public PointF getOriginPoint() {
        return new PointF(100,100);
    }

    @Override
    public void init() {

    }

}
