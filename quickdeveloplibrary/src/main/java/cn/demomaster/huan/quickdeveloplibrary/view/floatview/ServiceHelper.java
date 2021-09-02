package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;

import static android.content.Context.ACTIVITY_SERVICE;

public class ServiceHelper {
    public static void dissmissWindow(Class serviceClass) {
        Service service = getServiceByKey(serviceClass.getName());
        if (service == null) {
            return;
        }
        if (serverIsRunning(service.getApplicationContext(), serviceClass.getName())) {
            QDLogger.println("Service在运行：" + serviceClass.getName());
            if (service instanceof QdFloatingServiceInterFace) {
                //if (((QdFloatingServiceInterFace) service).isShowing()) {
                    ((QdFloatingServiceInterFace) service).onDismiss();
                //}
            }
            Class clazz = service.getClass();
            service.stopService(new Intent(service.getApplicationContext(), clazz));
        } else {
            QDLogger.println("Service未在运行：" + serviceClass.getName());
        }
    }

    public static void showWindow(Context context, Class clazz) {
        context.startService(new Intent(context.getApplicationContext(), clazz));
    }

    public static Service getServiceByKey(String serviceName) {
        return serviceMap.get(serviceName);
    }

    public static void removeService(Service qdFloatingService) {
        serviceMap.remove(qdFloatingService.getClass().getName());
        if (serviceListenerMap.containsKey(qdFloatingService.getClass().getName())) {
            ServiceListener serviceListener = serviceListenerMap.get(qdFloatingService.getClass().getName());
            if (serviceListener != null) {
                serviceListener.onDestroyService();
            }
            // serviceListenerMap.remove(qdFloatingService.getClass().getName());
        }
    }

    static Map<String, Service> serviceMap = new HashMap<>();
    static Map<String, ServiceListener> serviceListenerMap = new HashMap<>();

    public static void addService(Service qdFloatingService) {
        serviceMap.put(qdFloatingService.getClass().getName(), qdFloatingService);
        if (serviceListenerMap.containsKey(qdFloatingService.getClass().getName())) {
            ServiceListener serviceListener = serviceListenerMap.get(qdFloatingService.getClass().getName());
            if (serviceListener != null) {
                serviceListener.onCreateService();
            }
        }
    }

    public static void addServiceListener(Class<? extends Service> clazz, ServiceListener serviceListener) {
        serviceListenerMap.put(clazz.getName(), serviceListener);
    }

    public static void onCreateService(Service qdFloatingService) {
        addService(qdFloatingService);
    }

    public static void onDestroyService(Service qdFloatingService) {
        removeService(qdFloatingService);
    }

    public interface ServiceListener {
        void onCreateService();

        void onDestroyService();
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

        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            if (componentName.equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
