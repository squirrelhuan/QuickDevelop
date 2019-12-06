package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.service.TrafficFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager2;
import cn.demomaster.huan.quickdeveloplibrary.socket.MessageReceiveListener;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

import static cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager2.isGrantedUsagePremission;
import static com.umeng.socialize.utils.ContextUtil.getPackageName;


/**
 * 流量监控
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Traffic", preViewClass = StateView.class, resType = ResType.Custome)
public class TrafficFragment extends QDBaseFragment {

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
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
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