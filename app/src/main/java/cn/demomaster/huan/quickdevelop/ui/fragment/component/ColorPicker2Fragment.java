package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.GroundGlassUtil;

@ActivityPager(name = "颜色锥", preViewClass = TextView.class, resType = ResType.Custome)
public class ColorPicker2Fragment extends BaseFragment {
    ImageView iv_image1,iv_image2,iv_image3,iv_image4;
    TextView tv_lable;

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_colorpicker2, null);
        return view;
    }

    GroundGlassUtil glassUtil = null;
    @Override
    public void initView(View rootView) {
        iv_image1 = rootView.findViewById(R.id.iv_image1);
        iv_image2 = rootView.findViewById(R.id.iv_image2);
        iv_image3 = rootView.findViewById(R.id.iv_image3);
        iv_image4 = rootView.findViewById(R.id.iv_image4);
        tv_lable = rootView.findViewById(R.id.tv_lable);
        iv_image3.post(new Runnable() {
            @Override
            public void run() {
                // glassUtil.setBackgroundView(rootView, false);
            }
        });

        glassUtil = new GroundGlassUtil(getContext());
        glassUtil.setTargetView(tv_lable);
        GroundGlassUtil glassUtil2 = new GroundGlassUtil(getContext());
        glassUtil2.setTargetView(iv_image4);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(runnable, 1000);
        }
    };

}