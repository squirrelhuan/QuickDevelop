package cn.demomaster.huan.quickdeveloplibrary.jni;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import cn.demomaster.huan.quickdeveloplibrary.jni.aidl.IBaseService;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */

public class ServiceBinder implements ServiceConnection {
    private final ServiceConnection mCallback;

    public ServiceBinder(ServiceConnection callback) {
        mCallback = callback;
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        ServiceHelper.mService = IBaseService.Stub.asInterface(service);
        if (mCallback != null)
            mCallback.onServiceConnected(className, service);
    }

    @Override
    public void onServiceDisconnected(ComponentName className) {
        if (mCallback != null)
            mCallback.onServiceDisconnected(className);
            ServiceHelper.mService = null;
    }
}
