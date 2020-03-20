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
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager2;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.setLanguageLocal;


/**
 * 权限管理
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Permition", preViewClass = TextView.class, resType = ResType.Custome)
public class PermitionFragment extends QDBaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_write_external_storage)
    QDButton btn_write_external_storage;
    @BindView(R.id.btn_write_external_storage2)
    QDButton btn_write_external_storage2;
    View mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_permition, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("动态权限");
        btn_write_external_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionManager.chekPermission(mContext, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionManager.OnCheckPermissionListener() {

                    @Override
                    public void onPassed() {
                        Toast.makeText(mContext,"通过",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNoPassed() {
                        Toast.makeText(mContext,"拒绝",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        btn_write_external_storage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionManager2.getInstance().chekPermission(mContext, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_SETTINGS,Manifest.permission.SYSTEM_ALERT_WINDOW}, new PermissionManager.OnCheckPermissionListener() {

                    @Override
                    public void onPassed() {
                        Toast.makeText(mContext,"通过",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNoPassed() {
                        Toast.makeText(mContext,"拒绝",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

    }
}