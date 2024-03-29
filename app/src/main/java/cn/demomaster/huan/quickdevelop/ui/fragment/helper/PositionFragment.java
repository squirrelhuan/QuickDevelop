package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.position.GPSUtils;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.quickpermission_library.PermissionHelper;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "定位", preViewClass = TextView.class, resType = ResType.Custome)
public class PositionFragment extends QuickFragment {

    @BindView(R.id.btn_get_position)
    QDButton btn_get_position;
    @BindView(R.id.btn_get_position2)
    QDButton btn_get_position2;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_position, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("获取定位");
        btn_get_position.setOnClickListener(v -> PermissionHelper.requestPermission(mContext, PERMISSIONS_POSITION, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                getposition();
            }

            @Override
            public void onRefused() {

            }
        }));

        btn_get_position2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,6000);
            }
        });
    }

    private void getposition() {
        GPSUtils.getInstance(mContext).getLngAndLat(new GPSUtils.LocationResultListener() {

            @Override
            protected void onLocationResult(Location location) {
                QDLogger.i("当前位置：" + location.getLatitude() + "," + location.getLongitude());
            }

            @Override
            public void onLocationChanged(@NonNull @NotNull Location location) {
                super.onLocationChanged(location);
                QDLogger.i("onLocationChanged：" +location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                QDLogger.i("onStatusChanged：" );
            }

            @Override
            public void onProviderDisabled(@NonNull @NotNull String provider) {
                super.onProviderDisabled(provider);
            }

            @Override
            public void onProviderEnabled(@NonNull @NotNull String provider) {
                super.onProviderEnabled(provider);
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(runnable);
            getposition();
            handler.postDelayed(runnable,3000);
        }
    };

    private static String[] PERMISSIONS_POSITION = {
            Manifest.permission.ACCESS_FINE_LOCATION};

}