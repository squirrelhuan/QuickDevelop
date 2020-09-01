package cn.demomaster.huan.quickdeveloplibrary.view.floator;

import android.app.Activity;
import android.os.Build;
import android.view.View;

public class FloatViewImp {
    int id;
    public View view;
    private FloatView floatView;

    public FloatViewImp(FloatView view) {
        floatView = view;
    }

    Activity mActivity;

    /**
     * 生成子view 不要重复创建
     * @param activity
     * @return
     */
    public View genateView(Activity activity) {
        mActivity = activity;
        view = floatView.onCreateView(activity);
        if (view.getId() == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (id == 0)
                    id = View.generateViewId();
                view.setId(id);
            }
        }
        return view;
    }

    public void onResume(Activity activity, View windowView) {
        floatView.onResume(activity);
    }
}
