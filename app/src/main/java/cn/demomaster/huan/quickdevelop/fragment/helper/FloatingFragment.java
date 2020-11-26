package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
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
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingMenuService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.DebugFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.LifecycleFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.QDFloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.ServiceHelper;

/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Floating", preViewClass = TextView.class, resType = ResType.Custome)
public class FloatingFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @BindView(R.id.btn_floating_01)
    Button btn_floating_01;
    @BindView(R.id.btn_floating_02)
    Button btn_floating_02;
    @BindView(R.id.btn_floating_03)
    Button btn_floating_03;
    @BindView(R.id.btn_floating_04)
    Button btn_floating_04;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_floating, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);


        ServiceHelper.addServiceListener(FloatingService.class, new ServiceHelper.ServiceListener() {
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
                PermissionManager.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionManager.PermissionListener() {
                    @Override
                    public void onPassed() {
                        QDFloatingService service = ServiceHelper.getServiceByKey(FloatingService.class.getName());
                        if (service != null && service.isShowing) {
                            ServiceHelper.dissmissWindow(FloatingService.class);
                        } else {
                            ServiceHelper.showWindow(mContext, FloatingService.class);
                        }
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
                /*if (!Settings.canDrawOverlays(mContext)) {
                    Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    if(FloatingService.isShowing()){
                        FloatingService.dissmissWindow(mContext,FloatingService.class);
                    }else {
                        FloatingService.showWindow(mContext,FloatingService.class);
                        btn_floating_01.setText("关闭悬浮");
                    }
                }*/
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
                PermissionManager.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionManager.PermissionListener() {
                    @Override
                    public void onPassed() {
                        QDFloatingService service = ServiceHelper.getServiceByKey(FloatingMenuService.class.getName());
                        if (service != null && service.isShowing) {
                            ServiceHelper.dissmissWindow(FloatingMenuService.class);
                        } else {
                            ServiceHelper.showWindow(mContext, FloatingMenuService.class);
                        }
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(getContext(), "拒绝", Toast.LENGTH_SHORT).show();
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
                PermissionManager.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionManager.PermissionListener() {
                    @Override
                    public void onPassed() {
                        QDFloatingService service = ServiceHelper.getServiceByKey(DebugFloatingService.class.getName());
                        if (service != null && service.isShowing) {
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
                PermissionManager.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionManager.PermissionListener() {
                    @Override
                    public void onPassed() {
                        QDFloatingService service = ServiceHelper.getServiceByKey(LifecycleFloatingService.class.getName());
                        if (service != null && service.isShowing) {
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
    }
}