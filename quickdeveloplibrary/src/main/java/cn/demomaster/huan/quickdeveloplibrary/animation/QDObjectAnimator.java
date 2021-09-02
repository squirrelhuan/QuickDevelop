package cn.demomaster.huan.quickdeveloplibrary.animation;

import android.view.View;

import cn.demomaster.qdlogger_library.QDLogger;

public class QDObjectAnimator extends QDValueAnimator {
    public String propertyName;
    public View targetView;

    public QDObjectAnimator(Class dataType) {
        super(dataType);
        addUpdateListener(animation -> {
            //print("QDObjectAnimator doo");
            if (targetView != null) {
                switch (propertyName) {
                    case "rotation":
                        //print("rotation =" + (Float) animation.getAnimatedValue());
                        targetView.setRotation((Float) animation.getAnimatedValue());
                        break;
                }
            } else {
                print("targetView = null");
            }
        });
    }

    private void print(String s) {
        QDLogger.println(s);
    }

    public static QDObjectAnimator ofFloat(View view, String property, float start, float end) {
        QDObjectAnimator objectAnimator = ofFloat(start, end);
        objectAnimator.propertyName = property;
        objectAnimator.targetView = view;
        return objectAnimator;
    }

    public static QDObjectAnimator ofFloat(float... values) {
        QDObjectAnimator anim = new QDObjectAnimator(Float.class);
        anim.setFloatValues(values);
        return anim;
    }

    @Override
    public void cancel() {
        super.cancel();
        targetView = null;
    }
}
