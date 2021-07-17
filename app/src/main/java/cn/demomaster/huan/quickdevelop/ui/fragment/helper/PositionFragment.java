package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.position.GPSUtils;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickpermission_library.PermissionHelper;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "定位", preViewClass = TextView.class, resType = ResType.Custome)
public class PositionFragment extends BaseFragment {

    @BindView(R.id.btn_get_position)
    QDButton btn_get_position;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_position, null);
        return (ViewGroup) mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("获取定位");
        btn_get_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.getInstance().requestPermission(mContext, PERMISSIONS_POSITION, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        GPSUtils.getInstance(mContext).getLngAndLat(new GPSUtils.OnLocationResultListener() {
                            @Override
                            public void onLocationResult(Location location) {
                                QDLogger.i("当前位置：" + location.getLatitude() + "," + location.getLongitude());
                            }

                            @Override
                            public void OnLocationChange(Location location) {
                                QDLogger.i("最新位置：" + location.getLatitude() + "," + location.getLongitude());
                            }
                        });
                    }

                    @Override
                    public void onRefused() {

                    }
                });
            }
        });
    }

    private static String[] PERMISSIONS_POSITION = {
            Manifest.permission.ACCESS_FINE_LOCATION};

}