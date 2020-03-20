package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager2;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingMenuService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.FloatingService;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Floating", preViewClass = TextView.class, resType = ResType.Custome)
public class FloatingFragment extends QDBaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @BindView(R.id.btn_floating_01)
    Button btn_floating_01;

    @BindView(R.id.btn_floating_02)
    Button btn_floating_02;
    View mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_floating, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        if(!FloatingService.isShowing()){
            btn_floating_01.setText("打开悬浮");
        }else {
            btn_floating_01.setText("关闭悬浮");
        }
        btn_floating_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionManager2.getInstance().chekPermission(mContext, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionManager.OnCheckPermissionListener() {

                    @Override
                    public void onPassed() {
                        Toast.makeText(getContext(),"开启",Toast.LENGTH_SHORT).show();
                        if(FloatingService.isShowing()){
                            FloatingService.dissmissWindow(mContext,FloatingService.class);
                        }else {
                            FloatingService.showWindow(mContext,FloatingService.class);
                            btn_floating_01.setText("关闭悬浮");
                        }
                    }

                    @Override
                    public void onNoPassed() {
                        Toast.makeText(getContext(),"拒绝",Toast.LENGTH_SHORT).show();
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


        FloatingService.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                    btn_floating_01.setText("打开悬浮");
            }
        });

    }


}