package cn.demomaster.huan.quickdeveloplibrary.util.terminal;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * javaDB helper
 * Created by huan on 2017/11/30.
 */
public class ADBHelper {
    public static final int SUCCESS = 0;            // 表示程序执行成功

    public static final String COMMAND = "java.exe -version";    // 要执行的语句
    public static final String SUCCESS_MESSAGE = "执行成功：";
    public static final String ERROR_MESSAGE = "出错了：";
    public static final String Tag = "ADB";
    public static String Path_Temp;
    public static String Path_Def;
    private static ADBHelper instance;
    OnScreenChangeListener onScreenChangeListener;
    boolean isRunning = true;
    public String currentPath = "/";
    OnAdbReceiceListener onAdbReceiceListener;
    final List<DeviceModel> deviceModels = new ArrayList<>();
    private String directory_path;
    private final Map<String, DeviceModel> deviceMap = new HashMap<>();
    private DeviceModel currentDevice;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                String devName = "192.168.199.180:5555";
                if (currentDevice != null) {
                    devName = currentDevice.getName();
                }
                execute("adb -s " + devName + " shell screencap -p /sdcard/tmp.png");
                execute("adb -s " + devName + " pull /sdcard/tmp.png " + directory_path + "/resource/screen_android/");
                //execute(" remote-screencap > screen.png");
                onScreenChangeListener.onRefresh(directory_path + "/resource/screen_android/tmp.png");
            }
        }
    };

    private ADBHelper() {
        init();
    }

    public static ADBHelper getInstance() {
        if (instance == null) {
            instance = new ADBHelper();
        }
        return instance;
    }

    /**
     * 打印进程输出
     *
     * @param process 进程
     */
    private static void readProcessOutput(final Process process) {
        // 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
        read(process.getInputStream(), System.out);
        read(process.getErrorStream(), System.err);
    }

    // 读取输入流
    private static void read(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException e) {
            QDLogger.e(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                QDLogger.e(e);
            }
        }
    }

    private void init() {
       /* File directory = new File("adb");//设定为当前文件夹
        try {
           QDLogger.println("获取标准的路径=" + directory.getCanonicalPath());//获取标准的路径
        } catch (IOException e) {
            QDLogger.e(e);
        }
       QDLogger.println("获取绝对路径=" + directory.getAbsolutePath());//获取绝对路径
       QDLogger.println("获取class路径=" + getClass().getClass()
                .getResource("/")
                .toString());//获取绝对路径
        if (!directory.exists()) {
           QDLogger.println("未找到 adb可执行文件");//获取绝对路径
            return;
        } else {
            Path_Temp = directory.getAbsolutePath();
            directory_path = new File("").getAbsolutePath();
            Path_Temp = Path_Temp.substring(0, Path_Temp.lastIndexOf(File.separator));
            Path_Def = Path_Temp + File.separator + "res/screen_android/def.png";
           QDLogger.println("获取绝对路径" + Path_Temp);//获取绝对路径
        }*/
    }

    public void changeDeviece(String devName) {
        currentDevice = deviceMap.get(devName);
    }

    public void startScreen(OnScreenChangeListener onScreenChangeListener) {
        this.onScreenChangeListener = onScreenChangeListener;
        isRunning = true;
        execute(" adb kill-server ");
        execute(" adb start-server ");
        execute(" adb connect 192.168.199.180:5555");
        execute(" adb devices ");
        //execute("alias remote-screencap='adb shell screencap -p | sed 's/\r$//'");//设置别名
        String f = directory_path + "/resource/screen_android/";
        QDLogger.println(f);
        File file = new File(f);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                QDLogger.e(e);
            }
        }
        new Thread(runnable).start();
    }


    public void execute(String cmd) {
        execute(currentPath, cmd, null);
    }

    public void execute(String path, String cmd) {
        currentPath = path;
        execute(currentPath, cmd, null);
    }

    public void execute(String cmd, OnReceiveListener onReceiveListener) {
        execute(currentPath, cmd, onReceiveListener);
    }

    public void execute(String path, String cmd, OnReceiveListener onReceiveListener) {
        try {
            // QDLogger.e(Tag, "path=" + path);
            // Process process = Runtime.getRuntime().exec(cmd);
            if (!TextUtils.isEmpty(path)) {
                setCurrentPath(path);
            }
            if (cmd.contains("cd ") || cmd.trim().equals("\\")) {
                String pathstr = getPathInCmd(currentPath, cmd);
                QDLogger.e(Tag, cmd + "包含【cd】命令," + pathstr + "，路径：" + currentPath);
                File file = new File(pathstr);
                if (file == null || !file.exists() || !file.isDirectory()) {
                    if (onReceiveListener != null) {
                        ProcessResult processResult = new ProcessResult();
                        processResult.setCode(-1);
                        processResult.setError("fail");
                        onReceiveListener.onReceive(processResult);
                    }
                } else {
                    setCurrentPath(pathstr);
                    ProcessResult processResult = new ProcessResult();
                    processResult.setCode(0);
                    processResult.setResult("" + currentPath);
                    if (onReceiveListener != null) {
                        onReceiveListener.onReceive(processResult);
                    }
                }
                QDLogger.i(Tag, "currentPath=" + currentPath);
                return;
            }
            QDLogger.i(Tag, "currentPath=" + currentPath);
            Process process = Runtime.getRuntime().exec(cmd, null, new File(currentPath)); // android中使用
            //String[] cmd1 = { "/system/bin", "-c", cmd };
            // 打印程序输出
            //readProcessOutput(process);
            ProcessResult processResult = getProcessResult(process);
            // 等待程序执行结束并输出状态
            int exitCode = process.waitFor();
            if (exitCode == SUCCESS) {
                QDLogger.i(Tag, SUCCESS_MESSAGE + cmd + ",Result=" + processResult.getResult());
            } else {
                QDLogger.e(Tag, ERROR_MESSAGE + cmd + ",exitCode=" + exitCode + ",processIsEnd=" + processResult.getCode() + ",error=" + processResult.getError());
                switch (exitCode) {
                    case 255:
                        QDLogger.e("端口ADB被占用");
                        break;
                    case 1:
                        QDLogger.e(Tag, "Operation not permitted");
                        processResult.setError("Permission denied:"+processResult.getError());
                    case 2:
                        QDLogger.e(Tag, "error2");
                        processResult.setError("error2:"+processResult.getError());
                        break;
                }
            }
            if (onReceiveListener != null) {
                onReceiveListener.onReceive(processResult);
            }
            process.destroy();
        } catch (Exception e) {
            QDLogger.e(Tag, "[" + cmd + "],Cause:" + e.getCause() + ",Message:" + e.getMessage());
            if (onReceiveListener != null) {
                ProcessResult processResult = new ProcessResult();
                processResult.setCode(-1);
                processResult.setError("" + "[" + cmd + "],Cause:" + e.getCause() + ",Message:" + e.getMessage());
                onReceiveListener.onReceive(processResult);
            }
            QDLogger.e(e);
        }
    }

    private void setCurrentPath(String path) {
        currentPath = path;
    }

    /**
     * 获取cmd中的路径
     *
     * @param currentPath
     * @param cmd
     * @return
     */
    private String getPathInCmd(String currentPath, String cmd) {

        if (cmd.trim().startsWith("cd")) {
            String part1 = currentPath.trim();
            QDLogger.e(Tag, "当前路径=" + part1);
            int a = cmd.indexOf("cd") + 2;
            QDLogger.e(Tag, "[cd]出现的位置=" + a);
            String part2 = cmd.substring(a);
            QDLogger.e(Tag, "截取后的路径=" + part2);

            if (part2.trim().startsWith(".")) {
                int i = part2.indexOf("/");
                int index = Math.max(0, i - 1);
                int count = (index / 2);
                splitLastSeparatorFormPath(part1, count);
                QDLogger.i(Tag, "[part2],i=" + i + ",l=" + part2.length());
                part2 = part2.substring(i);
                return part1 + (part1.endsWith(File.separator) ? "" : File.separator) + (part2.startsWith(File.separator) ? part2.substring(1, part2.length() - 1) : part2);
            } else if (part2.trim().startsWith(File.separator)) {
                //return (part1.trim().endsWith(File.separator) ?part1.replaceFirst(File.separator,""):part1)+part2;
                return part2.trim();
            } else {
                return part1 + (part1.endsWith(File.separator) ? "" : File.separator) + part2.trim();
            }
        }
        return null;
    }

    private String splitLastSeparatorFormPath(String part1, int count) {
        if (count <= 0) {
            return part1;
        } else {

        }
        //part1 + (part1.endsWith(File.separator) ? "" : File.separator)
        return part1;
    }

    private ProcessResult getProcessResult(Process process) {
        ProcessResult result = new ProcessResult();
        result.setResult(getResultString(process.getInputStream()));
        result.setError(getResultString(process.getErrorStream()));//, System.out
        return result;
    }

    private String getResultString(InputStream inputStream) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
                //out.println(line);
            }
        } catch (IOException e) {
            QDLogger.e(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                QDLogger.e(e);
            }
        }
        return new String(stringBuffer);
    }

    public void startAdbServer() {
        execute(" adb start-server ");
    }

    public void stopAdbServer() {
        execute(" adb kill-server ");
    }

    public void setOnAdbReceiceListener(OnAdbReceiceListener onAdbReceiceListener) {
        this.onAdbReceiceListener = onAdbReceiceListener;
    }

    public void findDeviceList() {
        execute(" adb start-server ");
        execute(" adb devices ", result -> {
            if (result.getCode() == 0) {
                String res = result.getResult();
                if (res.startsWith("List of devices attached")) {
                    String[] list = res.replace("List of devices attached\n\r", "").split("\n\r");
                    deviceModels.clear();
                    deviceMap.clear();
                    for (String s : list) {
                        if (s.contains("\t")) {
                            DeviceModel deviceModel = new DeviceModel(s.split("\t")[0], s.split("\t")[1]);
                            deviceModels.add(deviceModel);
                            deviceMap.put(deviceModel.getName(), deviceModel);
                        }
                    }
                    //System.out.println("设备列表：" + Arrays.asList(deviceModels));
                    if (onAdbReceiceListener != null) {
                        onAdbReceiceListener.onFindDevices(deviceModels);
                    }
                }
            }
        });//List of devices attached
    }

    public void stopScreen() {
        isRunning = false;
    }

    public void connect(String ip) {
        execute("adb " + ip + " 5555");
        execute(" adb connect " + ip);
    }

    public interface OnScreenChangeListener {
        void onRefresh(String path);
    }

    public interface OnReceiveListener {
        void onReceive(ProcessResult result);
    }

    public interface OnAdbReceiceListener {
        void onFindDevices(List<DeviceModel> deviceModels);
    }

}
