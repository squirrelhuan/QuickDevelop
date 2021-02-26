package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;

public class ServiceHelper {
    public static void dissmissWindow(Class serviceClass) {
        Service service = getServiceByKey(serviceClass.getName());
        if (service == null) {
            return;
        }
        if (service instanceof QdFloatingServiceInterFace) {
            if (((QdFloatingServiceInterFace) service).isShowing()) {
                ((QdFloatingServiceInterFace) service).onDismiss();
            }
        }
        Class clazz = service.getClass();
        service.stopService(new Intent(service.getApplicationContext(), clazz));
    }

    public static void showWindow(Context context, Class clazz) {
        context.startService(new Intent(context.getApplicationContext(), clazz));
    }

    public static Service getServiceByKey(String serviceName) {
        return serviceMap.get(serviceName);
    }

    public static void removeService(Service qdFloatingService) {
        if (serviceMap.containsKey(qdFloatingService.getClass().getName())) {
            serviceMap.remove(qdFloatingService.getClass().getName());
        }
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

    public static interface ServiceListener {
        void onCreateService();

        void onDestroyService();
    }
}
