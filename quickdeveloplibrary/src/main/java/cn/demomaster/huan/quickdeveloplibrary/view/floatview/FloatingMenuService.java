package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences;
import cn.demomaster.huan.quickdeveloplibrary.widget.square.SquareImageMenuView;

import static cn.demomaster.huan.quickdeveloplibrary.widget.square.SquareImageMenuView.SquareImageMenuView_X_SP;
import static cn.demomaster.huan.quickdeveloplibrary.widget.square.SquareImageMenuView.SquareImageMenuView_Y_SP;

/**
 * Created
 */
public class FloatingMenuService extends QDFloatingService2 {
    private static SquareImageMenuView menuView;
    View view;

    @Override
    public void onCreateView(Context context, WindowManager windowManager) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_floating_menu, null);
        //view.setLayoutParams(new LinearLayout.LayoutParams(w,w));
        /*
        QDRoundDrawable qdRoundDrawable = new QDRoundDrawable();
        //qdRoundDrawable.setCornerRadius(cl_menu.getWidth()/2);
        qdRoundDrawable.setBackGroundColor(getResources().getColor(R.color.transparent_dark_77));
        qdRoundDrawable.setCornerRadius(DisplayUtil.dip2px(context,5));
        qdRoundDrawable.setRadiusAuto(true);
        //cl_menu.setBackground(qdRoundDrawable);*/
        menuView = view.findViewById(R.id.ib_menu_01);
        buttonEnable = true;
        menuView.setEnabled(buttonEnable);
        menuView.setWindowManager(windowManager);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//TYPE_APPLICATION_OVERLAY;
        } else {
            //layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (!getTouchAble()) {
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

        PointF pointF = getOriginPoint();
        if (pointF != null) {
            layoutParams.x = (int) pointF.x;
            layoutParams.y = (int) pointF.y;
        }
        layoutParams.width = (int) getResources().getDimension(R.dimen.dp_45);//ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = layoutParams.width;//ViewGroup.LayoutParams.WRAP_CONTENT;
        this.windowManager = windowManager;
        windowManager.addView(view, layoutParams);
        floatingOnTouchListener = new QDFloatingService.FloatingOnTouchListener(view);
        view.setOnTouchListener(floatingOnTouchListener);
    }
    QDFloatingService.FloatingOnTouchListener floatingOnTouchListener;

    static boolean buttonEnable = true;

    public static void setMenuEnable(boolean enable) {
        buttonEnable = enable;
        if (menuView != null) {
            menuView.setEnabled(buttonEnable);
        }
    }

    @Override
    public PointF getOriginPoint() {
        int x = QDSharedPreferences.getInstance().getInt(SquareImageMenuView_X_SP, 0);
        int y = QDSharedPreferences.getInstance().getInt(SquareImageMenuView_Y_SP, 200);
        return new PointF(x, y);
    }

    @Override
    public void onDestroy() {
        if(view!=null) {
            view.setOnTouchListener(null);
        }
        if(floatingOnTouchListener!=null){
            floatingOnTouchListener.onRelease();
        }
        if(menuView!=null){
            menuView.setOnTouchListener(null);
            menuView.onRelease();
        }
        menuView = null;
        removeView(view);
        super.onDestroy();
    }
}
