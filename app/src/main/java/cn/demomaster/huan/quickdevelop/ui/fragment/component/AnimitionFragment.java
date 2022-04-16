package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.animation.QDValueAnimator;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "动画测试", preViewClass = QDButton.class, resType = ResType.Custome)
public class AnimitionFragment extends QuickFragment {

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_animation, null);
        return mView;
    }

    QDValueAnimator qdValueAnimator;
    QDValueAnimator qdValueAnimator2;
    @Override
    public void initView(View rootView) {
        qdValueAnimator = QDValueAnimator.ofFloat(0,1);
        qdValueAnimator.setDuration(1000);
        qdValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        qdValueAnimator.setAnimationListener(new QDValueAnimator.AnimationListener() {
            @Override
            public void onStartOpen(Object value) {
                QDLogger.i("onStartOpen="+value+",state="+qdValueAnimator.getState()+",isRunning="+qdValueAnimator.isRunning());
            }

            @Override
            public void onOpening(Object value) {
                QDLogger.i("onOpening="+value+",state="+qdValueAnimator.getState()+",isRunning="+qdValueAnimator.isRunning());
            }

            @Override
            public void onEndOpen(Object value) {
                QDLogger.i("onEndOpen="+value+",state="+qdValueAnimator.getState()+",isRunning="+qdValueAnimator.isRunning());
            }

            @Override
            public void onStartClose(Object value) {
                QDLogger.i("onStartClose="+value+",state="+qdValueAnimator.getState()+",isRunning="+qdValueAnimator.isRunning());
            }

            @Override
            public void onClosing(Object value) {
                QDLogger.i("onClosing="+value+",state="+qdValueAnimator.getState()+",isRunning="+qdValueAnimator.isRunning());
            }

            @Override
            public void onEndClose(Object value) {
                QDLogger.i("onEndClose="+value+",state="+qdValueAnimator.getState()+",isRunning="+qdValueAnimator.isRunning());
            }
        });

        qdValueAnimator2 = QDValueAnimator.ofFloat(10,30);
        qdValueAnimator2.setDuration(1000);
        qdValueAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        qdValueAnimator2.setAnimationListener(new QDValueAnimator.AnimationListener() {
            @Override
            public void onStartOpen(Object value) {
                QDLogger.i("onStartOpen2="+value+",state="+qdValueAnimator2.getState()+",isRunning="+qdValueAnimator2.isRunning());
            }

            @Override
            public void onOpening(Object value) {
                QDLogger.i("onOpening2="+value+",state="+qdValueAnimator2.getState()+",isRunning="+qdValueAnimator2.isRunning());
            }

            @Override
            public void onEndOpen(Object value) {
                QDLogger.i("onEndOpen2="+value+",state="+qdValueAnimator2.getState()+",isRunning="+qdValueAnimator2.isRunning());
            }

            @Override
            public void onStartClose(Object value) {
                QDLogger.i("onStartClose2="+value+",state="+qdValueAnimator2.getState()+",isRunning="+qdValueAnimator2.isRunning());
            }

            @Override
            public void onClosing(Object value) {
                QDLogger.i("onClosing2="+value+",state="+qdValueAnimator2.getState()+",isRunning="+qdValueAnimator2.isRunning());
            }

            @Override
            public void onEndClose(Object value) {
                QDLogger.i("onEndClose2="+value+",state="+qdValueAnimator2.getState()+",isRunning="+qdValueAnimator2.isRunning());
                qdValueAnimator2.start();
            }
        });
        rootView.findViewById(R.id.btn_start).setOnClickListener(v -> {
            QDLogger.e("start "+",state="+qdValueAnimator.getState()+",isRunning="+qdValueAnimator.isRunning());
            qdValueAnimator.start();
        });

        rootView.findViewById(R.id.btn_reverse).setOnClickListener(v -> {
            QDLogger.e("reverse "+",state="+qdValueAnimator.getState()+",isRunning="+qdValueAnimator.isRunning());
            qdValueAnimator.reverse();
        });

        rootView.findViewById(R.id.btn_start2).setOnClickListener(v -> {
            QDLogger.e("start "+",state="+qdValueAnimator2.getState()+",isRunning="+qdValueAnimator2.isRunning());
            qdValueAnimator.start();
            qdValueAnimator2.start();
        });

        rootView.findViewById(R.id.btn_reverse2).setOnClickListener(v -> {
            QDLogger.e("reverse "+",isFrward="+qdValueAnimator2.isFrward()+",isRunning="+qdValueAnimator2.isRunning());
            qdValueAnimator2.backward();
        });

       }

}