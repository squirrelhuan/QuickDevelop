package cn.demomaster.huan.quickdeveloplibrary.widget.linechart;

import android.content.Context;
import android.util.AttributeSet;

import cn.demomaster.huan.quickdeveloplibrary.widget.FlowLayout;

public class LineChartLayout extends FlowLayout {

    private TimeDomainPlotView timeDomainPlotView;

    public LineChartLayout(Context context) {
        super(context);
        initView(context);
    }

    public LineChartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LineChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        if (timeDomainPlotView == null) {
            timeDomainPlotView = new TimeDomainPlotView(context);
        }
    }

}
