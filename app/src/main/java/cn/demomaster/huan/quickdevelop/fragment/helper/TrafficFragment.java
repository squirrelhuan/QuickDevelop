package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.service.TrafficFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager2;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * 流量监控
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Traffic", preViewClass = TextView.class, resType = ResType.Custome)
public class TrafficFragment extends QDFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_start)
    QDButton btn_start;
    @BindView(R.id.btn_stop)
    QDButton btn_stop;

    View mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_traffic, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("socket");
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionManager2.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.READ_PHONE_STATE,Manifest.permission.PACKAGE_USAGE_STATS}, new PermissionManager.OnCheckPermissionListener() {

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

                        TrafficFloatingService.showWindow(getContext(), TrafficFloatingService.class);
                    }

                    @Override
                    public void onNoPassed() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrafficFloatingService.dissmissWindow(getContext(), TrafficFloatingService.class);
            }
        });
        //QDTcpClient.setStateListener();
    }
}