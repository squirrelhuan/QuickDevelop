package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager2;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.service.QDAccessibilityService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingMenuService;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "AccessibilityService", preViewClass = TextView.class, resType = ResType.Custome)
public class AccessibilityServiceFragment extends QDFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @BindView(R.id.btn_01)
    Button btn_01;

    @BindView(R.id.btn_floating_02)
    Button btn_floating_02;
    View mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_accessibilityservice, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayoutOld) {
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //QDAccessibilityService.addPackage("cn.demomaster.huan.quickdevelop");
                if (!QDAccessibilityService.isAccessibilityServiceRunning(getContext(),QDAccessibilityService.class.getName())) {
                    //跳转系统自带界面 辅助功能界面
                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    return;
                }
               if(QDAccessibilityService.isAccessibilityServiceRunning(mContext,QDAccessibilityService.class.getName())){
                   QdToast.show(mContext,"true");
                }
            }
        });
        btn_floating_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionManager2.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionManager.OnCheckPermissionListener() {
                    @Override
                    public void onPassed() {
                        Toast.makeText(getContext(),"开启",Toast.LENGTH_SHORT).show();
                        if(FloatingMenuService.isShowing()){
                            FloatingMenuService.dissmissWindow(mContext,FloatingMenuService.class);
                        }else {
                            FloatingMenuService.showWindow(mContext,FloatingMenuService.class);
                            btn_floating_02.setText("关闭悬浮");
                        }
                    }

                    @Override
                    public void onNoPassed() {
                        Toast.makeText(getContext(),"拒绝",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


}