package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.content.Context;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundDrawable;
import cn.demomaster.huan.quickdeveloplibrary.widget.FlowLayout;
import cn.demomaster.huan.quickdeveloplibrary.widget.square.SquareImageMenuView;

/**
 * Created
 */
public class FloatingMenuService extends QDFloatingService {
    private boolean isExpanded = false;
    private static SquareImageMenuView button01, button02, button03;
    private static ConstraintLayout cl_menu;
    @Override
    public View setContentView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_floating_menu, null);
        //int w =DisplayUtil.dip2px(context,100);
        //view.setLayoutParams(new LinearLayout.LayoutParams(w,w));

        // dissmissWindow(context, FloatingMenuService.this.getClass());
        cl_menu = view.findViewById(R.id.cl_menu);
        QDRoundDrawable qdRoundDrawable = new QDRoundDrawable();
        //qdRoundDrawable.setCornerRadius(cl_menu.getWidth()/2);
        qdRoundDrawable.setBackGroundColor(getResources().getColor(R.color.transparent_dark_77));
        qdRoundDrawable.setCornerRadius(DisplayUtil.dip2px(context,5));
        qdRoundDrawable.setRadiusAuto(true);
        //cl_menu.setBackground(qdRoundDrawable);
        cl_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                //changeState();
            }
        });
        button01 = view.findViewById(R.id.ib_menu_01);
        buttonEnable = true;
        button01.setEnabled(buttonEnable);
        /*button02 = view.findViewById(R.id.ib_menu_02);
        button03 = view.findViewById(R.id.ib_menu_03);*/
        //changeState();
        /*button01.setOnTouchListener(new FloatingOnTouchListener(this, view));
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button01.stopAnimation();
            }
        });*/
        //linearLayout.addView(button);
        return view;
    }

    private void changeState() {
        if (isExpanded) {
            hideMenus();
        } else {
            enlarge();
        }
    }
    static boolean buttonEnable = true;
    public static void setMenuEnable(boolean enable){
        buttonEnable = enable;
        if(button01!=null){
            button01.setEnabled(buttonEnable);
        }
    }

    /**
     * 放大
     */
    private void enlarge() {
       /* button01.setVisibility(View.VISIBLE);
        button02.setVisibility(View.VISIBLE);
        button03.setVisibility(View.VISIBLE);*/
    }

    /**
     * hide
     */
    private void hideMenus() {
       /* button01.setVisibility(View.GONE);
        button02.setVisibility(View.GONE);
        button03.setVisibility(View.GONE);*/
    }

    @Override
    public PointF getOriginPoint() {
        return new PointF(0, 200);
    }

    @Override
    public void init() {

    }

}
