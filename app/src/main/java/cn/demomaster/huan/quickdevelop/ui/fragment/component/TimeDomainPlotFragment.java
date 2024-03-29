package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.linechart.LinePoint;
import cn.demomaster.huan.quickdeveloplibrary.widget.linechart.TimeDomainPlotView;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * 时域图
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "时域图", preViewClass = TextView.class, resType = ResType.Custome)
public class TimeDomainPlotFragment extends QuickFragment {

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_timedominplot, null);
        return mView;
    }

    public void initView(View rootView) {
        TimeDomainPlotView timeDomainPlotView = rootView.findViewById(R.id.timeDomainPlotView);
        List<LinePoint> linePoints = new ArrayList<>();
        for (int i = 0; i < 20000; i++) {
            linePoints.add(new LinePoint(i * 20, ((i % 2) == 1) ? Math.round((float) (Math.random() * 100) * 100) / 100f : Math.round(-(float) (Math.random() * 100) * 100) / 100f));
        }
        timeDomainPlotView.setLinePoints(linePoints);
        //timeDomainPlotView.setBaselineY(0);

        RadioGroup rg_transitionType = rootView.findViewById(R.id.rg_transitionType);
        rg_transitionType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_transitionType_01:
                    timeDomainPlotView.setTransitionType(TimeDomainPlotView.TransitionType.horizontal);
                    break;
                case R.id.rb_transitionType_02:
                    timeDomainPlotView.setTransitionType(TimeDomainPlotView.TransitionType.vertical);
                    break;
                case R.id.rb_transitionType_03:
                    timeDomainPlotView.setTransitionType(TimeDomainPlotView.TransitionType.transitionXY);
                    break;
                case R.id.rb_transitionType_04:
                    timeDomainPlotView.setTransitionType(TimeDomainPlotView.TransitionType.none);
                    break;
            }
        });
        RadioGroup rg_scaleType = rootView.findViewById(R.id.rg_scaleType);
        rg_scaleType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rg_scaleType_01:
                    timeDomainPlotView.setScaleType(TimeDomainPlotView.ScaleType.scaleX);
                    break;
                case R.id.rg_scaleType_02:
                    timeDomainPlotView.setScaleType(TimeDomainPlotView.ScaleType.scaleY);
                    break;
                case R.id.rg_scaleType_03:
                    timeDomainPlotView.setScaleType(TimeDomainPlotView.ScaleType.scaleXY);
                    break;
                case R.id.rg_scaleType_04:
                    timeDomainPlotView.setScaleType(TimeDomainPlotView.ScaleType.none);
                    break;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        //ServiceHelper.unbindFromService(mToken);
    }
}