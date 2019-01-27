package cn.demomaster.huan.quickdeveloplibrary.jni;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;

import java.util.HashMap;

import cn.demomaster.huan.quickdeveloplibrary.jni.aidl.IBaseService;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */
public class ServiceHelper {
    public static IBaseService mService = null;
    private static HashMap<Context, BaseServiceConnection> sConnectionMap = new HashMap<Context, BaseServiceConnection>();

    /**
     * @return
     */
    public static ServiceToken bindToService(Activity realActivity, Class service,ServiceConnection callback){
        ContextWrapper cw = new ContextWrapper(realActivity);
        //cw.startService(new Intent(cw, service));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cw.startForegroundService(new Intent(cw, service));
        } else {
            cw.startService(new Intent(cw, service));
        }
        BaseServiceConnection serviceBinder = new BaseServiceConnection(callback);
        if (cw.bindService((new Intent()).setClass(cw, service), serviceBinder, 0)) {
            sConnectionMap.put(cw, serviceBinder);
            return new ServiceToken(cw);
        }
        return null;
    }

    /**
     * @param context
     * @return
     */
    public static ServiceToken bindToService(Activity context,Class service) {
        return bindToService(context,service, null);
    }


    /**
     * @param token
     */
    public static void unbindFromService(ServiceToken token) {
        if (token == null) {
            return;
        }
        ContextWrapper cw = token.mWrappedContext;
        BaseServiceConnection sb = sConnectionMap.remove(cw);
        if (sb == null) {
            return;
        }
        cw.unbindService(sb);
        if (sConnectionMap.isEmpty()) {
            mService = null;
        }
    }
}
