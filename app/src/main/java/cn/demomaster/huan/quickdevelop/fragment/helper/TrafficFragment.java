package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.BaseFragment;
import cn.demomaster.huan.quickdevelop.service.TrafficFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingMenuService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.QDFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.ServiceHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * 流量监控
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "流量", preViewClass = TextView.class, resType = ResType.Custome)
public class TrafficFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @BindView(R.id.btn_start)
    QDButton btn_start;
    @BindView(R.id.btn_stop)
    QDButton btn_stop;
    View mView;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_traffic, null);
        }
        ButterKnife.bind(this, mView);
        return mView;
    }

    public void initView(View rootView) {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionManager.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PACKAGE_USAGE_STATS}, new PermissionManager.PermissionListener() {
                    @Override
                    public void onPassed() {
                        /*final AppOpsManager appOps;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            if (isGrantedUsagePremission(getContext())) {
                                Toast.makeText(getContext(),"ok",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(),"refused",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }*/

                        ServiceHelper.dissmissWindow(TrafficFloatingService.class);
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceHelper.dissmissWindow(TrafficFloatingService.class);
            }
        });
        //QDTcpClient.setStateListener();
    }
}