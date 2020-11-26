package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.service.QDAccessibilityService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingMenuService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.ServiceHelper;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "无障碍服务", preViewClass = TextView.class, resType = ResType.Custome)
public class AccessibilityServiceFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @BindView(R.id.btn_01)
    Button btn_01;
    @BindView(R.id.btn_floating_02)
    Button btn_floating_02;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_accessibilityservice, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //QDAccessibilityService.addPackage("cn.demomaster.huan.quickdevelop");
                if (!QDAccessibilityService.isAccessibilityServiceRunning(getContext(), QDAccessibilityService.class.getName())) {
                    //跳转系统自带界面 辅助功能界面
                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    return;
                }
                if (QDAccessibilityService.isAccessibilityServiceRunning(mContext, QDAccessibilityService.class.getName())) {
                    QdToast.show(mContext, "true");
                }
            }
        });
        btn_floating_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionManager.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionManager.PermissionListener() {
                    @Override
                    public void onPassed() {
                        Toast.makeText(getContext(), "开启", Toast.LENGTH_SHORT).show();
                        if(ServiceHelper.getServiceByKey(FloatingMenuService.class.getName())!=null){
                            if(ServiceHelper.getServiceByKey(FloatingMenuService.class.getName()).isShowing){
                                ServiceHelper.dissmissWindow(FloatingMenuService.class);
                                btn_floating_02.setText("开启悬浮");
                            }else {
                                ServiceHelper.showWindow(getContext(),FloatingMenuService.class);
                                btn_floating_02.setText("关闭悬浮");
                            }
                        }else {
                            ServiceHelper.showWindow(getContext(),FloatingMenuService.class);
                            btn_floating_02.setText("关闭悬浮");
                        }

                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

 /*if (!QDAccessibilityService.isAccessibilityServiceRunning(getApplicationContext(),QDAccessibilityService.class.getName())) {
                        //跳转系统自带界面 辅助功能界面
                        QDAccessibilityService.startSettintActivity(mContext);
                        return;
                    }*/

    }

}