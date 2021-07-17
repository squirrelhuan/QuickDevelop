package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.colorpicker.ColorPicker;

@ActivityPager(name = "取色器", preViewClass = TextView.class, resType = ResType.Custome)
public class ColorPickerFragment extends BaseFragment {
    TextView tv_color;
    ColorPicker cp_color;

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_colorpicker, null);
        return view;
    }

    @Override
    public void initView(View rootView) {
        cp_color = rootView.findViewById(R.id.cp_color);
        tv_color = rootView.findViewById(R.id.tv_color);
        handler.postDelayed(runnable, 1000);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tv_color.setText("" + cp_color.getColor());
            handler.postDelayed(runnable, 1000);
        }
    };

}