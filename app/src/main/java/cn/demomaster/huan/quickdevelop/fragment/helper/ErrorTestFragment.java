package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnDoubleClickListener;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnMultClickListener;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "ErrorTest",preViewClass = StateView.class,resType = ResType.Custome)
public class ErrorTestFragment extends QDBaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_error_01)
    QDButton btn_error_01;
    @BindView(R.id.btn_error_buggly)
    QDButton btn_error_buggly;

    @BindView(R.id.btn_double_click)
    QDButton btn_double_click;
    @BindView(R.id.btn_three_click)
    QDButton btn_three_click;

    View mView;
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_errortest, null);
        }
        ButterKnife.bind(this,mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("异常捕获");
        btn_error_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a =0;
                int b = 1;
                int c = b/a;
            }
        });
        btn_error_buggly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrashReport.testJavaCrash();
            }
        });

        btn_double_click.setOnClickListener(new OnDoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                Toast.makeText(mContext,"double click",Toast.LENGTH_SHORT).show();
            }
        });
        btn_three_click.setOnClickListener(new OnMultClickListener(3,300) {
            @Override
            public void onClickEvent(View v) {
                Toast.makeText(mContext,"three click",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

    }
}