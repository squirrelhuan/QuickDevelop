package cn.demomaster.huan.quickdeveloplibrary.base.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.helper.NotificationHelper;

/**
 * 弹窗类型的activity,帮助类
 */
public class DialogActivityHelper {
    static Map<Long, Object> dataMap = new HashMap<>();

    public static Object getDataById(long id) {
        if (dataMap.containsKey(id)) {
            return dataMap.get(id);
        }
        return null;
    }

    public static void showDialog(Context context, Class<? extends QdDialogActivity> clazz, Object data) {
        Intent intent = new Intent(context, clazz);
        Bundle bundle = new Bundle();
        long id = System.currentTimeMillis();
        dataMap.put(id, data);
        bundle.putLong("QDDialogId", id);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NotificationHelper.clearAllNotifiication(context);
            NotificationHelper.sendFullScreenNotification(context, "消息", "点击查看", clazz, bundle);
        } else {
            context.startActivity(intent);
        }
    }

    public static void onDialogActivityDismiss(long id) {
        
    }
}
