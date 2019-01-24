package cn.demomaster.huan.quickdeveloplibrary.jni;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */
public class Watcher {
    static {
        System.loadLibrary("native-lib");
    }

    public native void  createWatcher(String  userId,String  packageName,String serviceName);

    public native void  connectMonitor();
}
