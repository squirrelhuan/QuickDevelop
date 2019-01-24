package cn.demomaster.huan.quickdeveloplibrary.jni;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */
public class JNITest {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    //创建一个 native 方法
    public native static String get();


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();
}
