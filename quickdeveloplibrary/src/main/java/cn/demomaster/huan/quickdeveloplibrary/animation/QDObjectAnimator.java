package cn.demomaster.huan.quickdeveloplibrary.animation;

import android.animation.ValueAnimator;
import android.view.View;

import cn.demomaster.qdlogger_library.QDLogger;

public class QDObjectAnimator extends QDValueAnimator{
    public  String propertyName;
    public  View targetView;

    public QDObjectAnimator(Class dataType) {
        super(dataType);
        //this.dataType = dataType;
        if (listener1 == null) {
            listener1 = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if(targetView!=null){
                        switch (propertyName){
                            case "rotation":
                                QDLogger.i("rotation .....");
                                targetView.setRotation((Float) animation.getAnimatedValue());
                                break;
                        }
                    }
                }
            };
            addUpdateListener(listener1);
        }
    }

    public static QDObjectAnimator ofFloat(View view, String property, float start, float end) {
        QDObjectAnimator objectAnimator = ofFloat(start,end);
        objectAnimator.propertyName = property;
        objectAnimator.targetView = view;
       return objectAnimator;
    }

    public static QDObjectAnimator ofFloat(float... values) {
        QDObjectAnimator anim = new QDObjectAnimator(Float.class);
        anim.setFloatValues(values);
        return anim;
    }
    ValueAnimator.AnimatorUpdateListener listener1 = null;
    @Override
    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener listener) {
        QDLogger.i("rotation .....  addUpdateListener");
        super.addUpdateListener(listener);
    }

    @Override
    public void cancel() {
        super.cancel();
        targetView = null;
    }
}
