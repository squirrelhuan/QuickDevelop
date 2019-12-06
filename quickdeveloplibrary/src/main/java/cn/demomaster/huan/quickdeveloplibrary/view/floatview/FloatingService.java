package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created
 */
public class FloatingService extends QDFloatingService {
    @Override
    public View setContentView(final Context context) {
        LinearLayout linearLayout = new LinearLayout(context.getApplicationContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        Button button = new Button(context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmissWindow(context,FloatingService.this.getClass());
            }
        });
        //linearLayout.addView(button);
        return button;
    }

    @Override
    public PointF getOriginPoint() {
        return new PointF(100,100);
    }

    @Override
    public void init() {

    }

}
