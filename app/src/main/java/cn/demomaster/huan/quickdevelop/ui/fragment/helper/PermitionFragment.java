package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
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
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.quickpermission_library.PermissionHelper;


/**
 * 权限管理
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "权限管理", preViewClass = TextView.class, resType = ResType.Resource,iconRes = R.mipmap.ic_permission_manager)
public class PermitionFragment extends QuickFragment {

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
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_permition, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("动态权限");
        btn_write_external_storage.setOnClickListener(v -> PermissionHelper.requestPermission(mContext, new String[]{
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
        }));
        btn_write_external_storage2.setOnClickListener(v -> PermissionHelper.requestPermission(mContext, new String[]{
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

        }));

        btn_group.setOnClickListener(v -> PermissionHelper.requestPermission(mContext, new String[]{
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
        }));
    }


    /*public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

    }*/
}