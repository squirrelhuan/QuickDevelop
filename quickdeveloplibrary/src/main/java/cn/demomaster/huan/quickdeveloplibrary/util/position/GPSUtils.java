package cn.demomaster.huan.quickdeveloplibrary.util.position;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Administrator on 2018/4/17.
 * 获取用户的地理位置
 */
public class GPSUtils {

    private static GPSUtils instance;
    private final Context mContext;
    private LocationManager locationManager;

    private GPSUtils(Context context) {
        this.mContext = context;
    }

    public static GPSUtils getInstance(Context context) {
        if (instance == null) {
            instance = new GPSUtils(context);
        }
        return instance;
    }

    /**
     * 获取经纬度
     * @return
     */
    public void getLngAndLat(LocationResultListener onLocationResultListener) {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if(mOnLocationListener!=null) {
            locationManager.removeUpdates(mOnLocationListener);
        }
        mOnLocationListener = onLocationResultListener;

        String locationProvider = null;
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
            QDLogger.i("GPS定位");
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
            QDLogger.i("网路定位");
        } else {
            QDLogger.i("no gps no network");
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(i);
            return ;
        }
        Location location = null;
        //获取Location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                QDLogger.i("no permission");
            } else {
                location = locationManager.getLastKnownLocation(locationProvider);
            }
        } else {
            location = locationManager.getLastKnownLocation(locationProvider);
        }
        if (location != null) {
            //不为空,显示地理位置经纬度
            if (mOnLocationListener != null) {
                mOnLocationListener.onLocationResult(location);
            }
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 1000, 0, mOnLocationListener);
    }

    /*public LocationListener locationListener = new LocationListener() {
        
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (mOnLocationListener != null) {
                mOnLocationListener.OnLocationChange(location);
            }
        }
    };*/

    public void removeListener() {
        if(mOnLocationListener!=null) {
            locationManager.removeUpdates(mOnLocationListener);
        }
    }

    private LocationResultListener mOnLocationListener;

    public abstract static class LocationResultListener implements LocationListener{
       protected abstract void onLocationResult(Location location);

        @Override
        public void onLocationChanged(@NonNull Location location) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @NonNull
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

}