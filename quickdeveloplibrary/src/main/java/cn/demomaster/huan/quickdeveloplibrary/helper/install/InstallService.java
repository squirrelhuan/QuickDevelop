package cn.demomaster.huan.quickdeveloplibrary.helper.install;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashMap;

public class InstallService extends AccessibilityService {
    HashMap<Integer, Boolean> hashMap = new HashMap<>();

    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            boolean hander = interNoderInfo(nodeInfo);
            if (hander) {
                hashMap.put(event.getWindowId(), true);
            }

        }
    }

    private boolean interNoderInfo(AccessibilityNodeInfo nodeInfo) {
        int count = nodeInfo.getChildCount();
        if (nodeInfo.getClassName().equals("android.widget.Button")) {
            String nodeContent = nodeInfo.getText().toString();
            if (nodeContent.equals("确定") || nodeContent.equals("安装") || nodeContent.equals("完成") || nodeContent.equals("继续安装")) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else if (nodeInfo.getClassName().equals("android.widget.ScrollView")) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo nodeInfo1 = nodeInfo.getChild(i);
            if (nodeInfo1 != null) {
                if (interNoderInfo(nodeInfo1)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onInterrupt() {

    }
}
