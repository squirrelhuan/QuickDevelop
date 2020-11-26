package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "按钮", preViewClass = QDButton.class, resType = ResType.Custome)
public class QdButtonFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_button, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        Button button = rootView.findViewById(R.id.btn_play1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button button2 = rootView.findViewById(R.id.btn_play2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}