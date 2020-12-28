package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.quickpermission_library.PermissionHelper;
import cn.demomaster.quickpermission_library.model.PermissionModel;
import cn.demomaster.quickpermission_library.model.PermissionRequest;


/**
 * 权限管理
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "权限管理", preViewClass = TextView.class, resType = ResType.Custome)
public class PermitionFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_write_external_storage)
    QDButton btn_write_external_storage;
    @BindView(R.id.btn_write_external_storage2)
    QDButton btn_write_external_storage2;

    @BindView(R.id.btn_group)
    QDButton btn_group;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_permition, null);
        return (ViewGroup) mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("动态权限");
        btn_write_external_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.getInstance().requestPermission(mContext, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionHelper.PermissionListener() {

                    @Override
                    public void onPassed() {
                        Toast.makeText(mContext, "通过", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(mContext, "拒绝", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        btn_write_external_storage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.getInstance().requestPermission(mContext, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_SETTINGS,
                        Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        Toast.makeText(mContext, "通过", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(mContext, "拒绝", Toast.LENGTH_LONG).show();
                    }

                });
            }
        });

        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.getInstance().requestPermission(mContext, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE}, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        Toast.makeText(mContext, "通过", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(mContext, "拒绝", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    /*public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

    }*/
}