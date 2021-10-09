package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

import static android.content.Context.ACTIVITY_SERVICE;

public class ServiceHelper {
    public static void dissmissWindow(Context context,Class serviceClass) {
        if (serverIsRunning(context.getApplicationContext(), serviceClass.getName())) {
            QDLogger.println("Service在运行：" + serviceClass.getName());
            context.stopService(new Intent(context, serviceClass));
            //serviceMap.remove(clazz.getName());
           // serviceListenerMap.remove(clazz.getName());
        }
    }

    public static void showWindow(Context context, Class clazz) {
        context.startService(new Intent(context.getApplicationContext(), clazz));
    }

    /**
     * @return boolean 返回该服务是否在运行中；
     * @params componentName 查询指定service的组件名；
     * e.g. com.hr.life.trnfa.service.services.MqttConnectService
     */
    public static boolean serverIsRunning(Context context, String componentName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices
                = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices.size() <= 0) {
            return false;
        }
        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            //QDLogger.e("componentName:"+serviceInfo.service.getClassName()+",pid="+serviceInfo.pid+",pid2="+pid);
            if ( serviceInfo.pid==pid&& componentName.equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

 /*   public static void addServiceListener(Class serviceClass,ServiceListener serviceListener) {
        serviceListenerMap.put(serviceClass.getName(),serviceListener);
    }*/

   /* public interface ServiceListener {
        void onCreateService();
        void onDestroyService();
    }*/
}
