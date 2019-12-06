package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.linechart.LinePoint;
import cn.demomaster.huan.quickdeveloplibrary.widget.linechart.TimeDomainPlotView;


/**
 * 时域图
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "TimeDomainPlot",preViewClass = StateView.class,resType = ResType.Custome)
public class TimeDomainPlotFragment extends QDBaseFragment {
    //Components
    ViewGroup mView;

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_timedominplot, null);
        }

        Bundle bundle = getArguments();
        String title = "空界面";
        // Example of a call to a native method

        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {
        TimeDomainPlotView timeDomainPlotView = rootView.findViewById(R.id.timeDomainPlotView);
        List<LinePoint> linePoints = new ArrayList<>();
        for(int i =0;i<50000;i++){
            linePoints.add(new LinePoint(i*20, ((i%2)==1)?Math.round( (float) (Math.random() * 100) *100)/100: Math.round(-(float) (Math.random() * 100)*100)/100));
        }
        timeDomainPlotView.setLinePoints(linePoints);
        //timeDomainPlotView.setBaselineY(0);

        RadioGroup rg_transitionType = rootView.findViewById(R.id.rg_transitionType);
        rg_transitionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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
            }
        });
        RadioGroup rg_scaleType = rootView.findViewById(R.id.rg_scaleType);
        rg_scaleType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        //ServiceHelper.unbindFromService(mToken);
    }
}