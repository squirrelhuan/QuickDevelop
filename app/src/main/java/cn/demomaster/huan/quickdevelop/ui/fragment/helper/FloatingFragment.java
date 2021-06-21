package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
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
import cn.demomaster.huan.quickdevelop.ui.dialog.DialogWindowActivity;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.BatteryOptimizationsHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingMenuService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FpsFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.DebugFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.LifecycleFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.ServiceHelper;
import cn.demomaster.quickpermission_library.PermissionHelper;
import cn.demomaster.quickpermission_library.model.PermissionModel;
import cn.demomaster.quickpermission_library.model.PermissionRequest;

/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "悬浮窗", preViewClass = TextView.class, resType = ResType.Custome)
public class FloatingFragment extends BaseFragment {

    @BindView(R.id.btn_floating_01)
    Button btn_floating_01;
    @BindView(R.id.btn_floating_02)
    Button btn_floating_02;
    @BindView(R.id.btn_floating_03)
    Button btn_floating_03;
    @BindView(R.id.btn_floating_04)
    Button btn_floating_04;
    @BindView(R.id.btn_floating_05)
    Button btn_floating_05;
    @BindView(R.id.btn_floating_06)
    Button btn_floating_06;
    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_floating, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        ServiceHelper.addServiceListener(FpsFloatingService.class, new ServiceHelper.ServiceListener() {
            @Override
            public void onCreateService() {
                btn_floating_01.setText("关闭悬浮");
            }

            @Override
            public void onDestroyService() {
                btn_floating_01.setText("打开悬浮");
            }
        });

        btn_floating_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOverlayPermission(mContext);
                PermissionHelper.getInstance().requestPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        Service service = ServiceHelper.getServiceByKey(FpsFloatingService.class.getName());
                        if (service != null) {
                            ServiceHelper.dissmissWindow(FpsFloatingService.class);
                        } else {
                            ServiceHelper.showWindow(mContext, FpsFloatingService.class);
                        }
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ServiceHelper.addServiceListener(FloatingMenuService.class, new ServiceHelper.ServiceListener() {
            @Override
            public void onCreateService() {
                btn_floating_02.setText("关闭悬浮");
            }

            @Override
            public void onDestroyService() {
                btn_floating_02.setText("开启悬浮");
            }
        });
        btn_floating_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.requestPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        Service service = ServiceHelper.getServiceByKey(FloatingMenuService.class.getName());
                        if (service != null) {
                            ServiceHelper.dissmissWindow(FloatingMenuService.class);
                        } else {
                            ServiceHelper.showWindow(mContext, FloatingMenuService.class);
                        }
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public boolean handRefused(Activity context, PermissionRequest request) {
                        //return super.handRefused(context, request);
                        PermissionModel permissionModel = request.getPermissionModelList().get(request.getIndex());
                        switch (permissionModel.getName()) {
                            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                                return true;
                        }
                        return false;//返回此权限是否必须，若是必要权限可以返回true，重复请求
                    }
                });
            }
        });
        ServiceHelper.addServiceListener(DebugFloatingService.class, new ServiceHelper.ServiceListener() {
            @Override
            public void onCreateService() {
                btn_floating_03.setText("关闭控制台");
            }

            @Override
            public void onDestroyService() {
                btn_floating_03.setText("开启控制台");
            }
        });
        btn_floating_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.getInstance().requestPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        Service service = ServiceHelper.getServiceByKey(DebugFloatingService.class.getName());
                        if (service != null) {
                            ServiceHelper.dissmissWindow(DebugFloatingService.class);
                        } else {
                            ServiceHelper.showWindow(mContext, DebugFloatingService.class);
                        }
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ServiceHelper.addServiceListener(LifecycleFloatingService.class, new ServiceHelper.ServiceListener() {
            @Override
            public void onCreateService() {
                btn_floating_04.setText("关闭生命周期监控器");
            }

            @Override
            public void onDestroyService() {
                btn_floating_04.setText("开启生命周期监控器");
            }
        });
        btn_floating_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.requestPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        Service service = ServiceHelper.getServiceByKey(LifecycleFloatingService.class.getName());
                        if (service != null) {
                            ServiceHelper.dissmissWindow(LifecycleFloatingService.class);
                        } else {
                            ServiceHelper.showWindow(mContext, LifecycleFloatingService.class);
                        }
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_floating_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    BatteryOptimizationsHelper.requestIgnoreBatteryOptimizations(getContext());
                }
            }
        });
        btn_floating_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BatteryOptimizationsHelper.requestAutoStartService(getContext());
            }
        });
    }

    //跳转到设置-请求悬浮窗权限
    @TargetApi(Build.VERSION_CODES.M)
    public static void getOverlayPermission(Activity context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent, 123456);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(context, DialogWindowActivity.class);
                context.startActivity(intent1);
                // 参数1：SecondActivity进场动画，参数2：MainActivity出场动画
                context.overridePendingTransition(R.anim.fade_in, 0);
            }
        });
    }
}