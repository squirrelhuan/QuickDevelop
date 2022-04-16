package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.BatteryOptimizationsHelper;
import cn.demomaster.huan.quickdeveloplibrary.model.EventMessage;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.DebugFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingMenuService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FpsFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.LifecycleFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.ServiceHelper;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.quickevent_library.core.QuickEvent;
import cn.demomaster.quickevent_library.core.Subscribe;
import cn.demomaster.quickevent_library.core.ThreadMode;
import cn.demomaster.quickpermission_library.PermissionHelper;
import cn.demomaster.quickpermission_library.model.PermissionModel;
import cn.demomaster.quickpermission_library.model.PermissionRequest;

/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "悬浮窗", preViewClass = TextView.class, resType = ResType.Custome)
public class FloatingFragment extends QuickFragment {

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
        QuickEvent.getDefault().register(this);

        btn_floating_01.setOnClickListener(v -> PermissionHelper.requestPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                boolean exist = ServiceHelper.serverIsRunning(mContext,FpsFloatingService.class.getName());
                if (exist) {
                    ServiceHelper.stopService(mContext,FpsFloatingService.class);
                } else {
                    ServiceHelper.startService(mContext, FpsFloatingService.class);
                }
            }

            @Override
            public void onRefused() {
                Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
            }
        }));

        btn_floating_02.setOnClickListener(v -> PermissionHelper.requestPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                boolean exist = ServiceHelper.serverIsRunning(mContext,FloatingMenuService.class.getName());
                if (exist) {
                    ServiceHelper.stopService(mContext,FloatingMenuService.class);
                } else {
                    ServiceHelper.startService(mContext, FloatingMenuService.class);
                }
            }

            @Override
            public void onRefused() {
                Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean handRefused(Context context, PermissionRequest request) {
                //return super.handRefused(context, request);
                PermissionModel permissionModel = request.getPermissionModelList().get(request.getIndex());
                switch (permissionModel.getName()) {
                    case Manifest.permission.SYSTEM_ALERT_WINDOW:
                        return true;
                }
                return false;//返回此权限是否必须，若是必要权限可以返回true，重复请求
            }
        }));

        btn_floating_03.setOnClickListener(v -> PermissionHelper.requestPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                boolean exist = ServiceHelper.serverIsRunning(mContext,DebugFloatingService.class.getName());
                if (exist) {
                    ServiceHelper.stopService(mContext,DebugFloatingService.class);
                } else {
                    ServiceHelper.startService(mContext, DebugFloatingService.class);
                }
            }

            @Override
            public void onRefused() {
                Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
            }
        }));
        btn_floating_04.setOnClickListener(v -> PermissionHelper.requestPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                boolean exist = ServiceHelper.serverIsRunning(mContext,LifecycleFloatingService.class.getName());
                if (exist) {
                    ServiceHelper.stopService(mContext,LifecycleFloatingService.class);
                } else {
                    ServiceHelper.startService(mContext, LifecycleFloatingService.class);
                }
            }

            @Override
            public void onRefused() {
                Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
            }
        }));
        btn_floating_05.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                BatteryOptimizationsHelper.requestIgnoreBatteryOptimizations(getContext());
            }
        });
        btn_floating_06.setOnClickListener(v -> BatteryOptimizationsHelper.requestAutoStartService(getContext()));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnServiceChanged(EventMessage eventMessage){
        if(eventMessage!=null) {
            //QDLogger.println(eventMessage.toString());
            if (!TextUtils.isEmpty(eventMessage.key) && eventMessage.key.equals("QDFloatingService2-onCreate")) {
                if (eventMessage.data[0] == FpsFloatingService.class) {
                    btn_floating_01.setText(((int) eventMessage.data[1]) == 0 ? "关闭悬浮" : "打开悬浮");
                }
                if (eventMessage.data[0] == FloatingMenuService.class) {
                    btn_floating_02.setText(((int) eventMessage.data[1]) == 0 ? "关闭悬浮" : "打开悬浮");
                }
                if (eventMessage.data[0] == DebugFloatingService.class) {
                    btn_floating_03.setText(((int) eventMessage.data[1]) == 0 ? "关闭控制台" : "开启控制台");
                }
                if (eventMessage.data[0] == LifecycleFloatingService.class) {
                    btn_floating_04.setText(((int) eventMessage.data[1]) == 0 ? "关闭生命周期监控器" : "开启生命周期监控器");
                }
            }
        }
    }
/*
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
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuickEvent.getDefault().unRegister(this);
    }
}