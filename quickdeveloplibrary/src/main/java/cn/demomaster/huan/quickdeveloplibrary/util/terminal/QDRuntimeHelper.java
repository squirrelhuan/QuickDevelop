package cn.demomaster.huan.quickdeveloplibrary.util.terminal;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * javaDB helper
 * Created by huan on 2017/11/30.
 */
public class QDRuntimeHelper {
    public static final int SUCCESS = 0;            // 表示程序执行成功
    public static final String SUCCESS_MESSAGE = "执行成功：";
    public static final String ERROR_MESSAGE = "出错了：";
    public static String Tag = "QDRuntimeHelper";
    private static QDRuntimeHelper instance;
    boolean isRunning = true;
    OnAdbReceiceListener onAdbReceiceListener;

    /*public Process getProcess() {
        if (process == null) {
            try {
                process = Runtime.getRuntime().exec("/system/bin/sh", null, new File("/system/bin")); // android中使用
            } catch (IOException e) {
            QDLogger.e(e);
            }
        }
        return process;
    }*/

    private QDRuntimeHelper() {
        /*try {
            Runtime.getRuntime().exec("su");//执行这一句，superuser.apk就会弹出授权对话框
        } catch (IOException e) {
            QDLogger.e(e);
        }*/
    }

    public static QDRuntimeHelper getInstance() {
        if (instance == null) {
            instance = new QDRuntimeHelper();
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

    public void execute(String path, String cmd) {
        execute(path, cmd, null);
    }

    public void execute(String cmd) {
        execute(null, cmd, null);
    }

    public void execute(String path, String cmd, OnReceiveListener onReceiveListener) {
        execute(path, new String[]{cmd}, onReceiveListener);
    }

    public void execute(String cmd, OnReceiveListener onReceiveListener) {
        execute(null, new String[]{cmd}, onReceiveListener);
    }

    public void execute(String[] cmds) {
        execute(null, cmds, null);
    }

    public void execute(String path, String[] cmds) {
        execute(path, cmds, null);
    }

    public void execute(String path, String[] cmds, OnReceiveListener onReceiveListener) {
        try {
            // Process process = Runtime.getRuntime().exec(cmd);
          /*  Process process = getProcess();// Runtime.getRuntime().exec(cmds[0], null, new File("/system/bin")); // android中使用
            //String[] cmd1 = { "/system/bin", "-c", cmd };
            // 打印程序输出
            //readProcessOutput(process);
            if (process == null) {
                return;
            }*/
            ProcessResult p = null;
            for (String cmd : cmds) {
                p = executeChild(path, cmd);
                if (p.getCode() != 0) {
                    if (onReceiveListener != null) {
                        onReceiveListener.onReceive(p);
                    }
                    return;
                }
            }
            if (onReceiveListener != null) {
                onReceiveListener.onReceive(p);
            }
        } catch (Exception e) {
            QDLogger.e(Tag, "Cause:" + e.getCause() + ",Message:" + e.getMessage());
            if (onReceiveListener != null) {
                ProcessResult processResult = new ProcessResult();
                processResult.setCode(-1);
                processResult.setError("Cause:" + e.getCause() + ",Message:" + e.getMessage());
                onReceiveListener.onReceive(processResult);
            }
            QDLogger.e(e);
        }
    }

    private ProcessResult executeChild(String path, String cmd) {
        QDLogger.println(Tag, "executeChild cmd:" + cmd + ",path:" + path);
        Process process = null;
        DataOutputStream os =null;
        try {
            try {
                process = Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                QDLogger.e(e);
            }
            if (process == null) {
                process = Runtime.getRuntime().exec(cmd, null, new File(TextUtils.isEmpty(path) ? "/" : path)); // android中使用
            }
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("cd "+path+"\n");
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            QDLogger.e(e);
        }

        ProcessResult processResult = getProcessResult(process);
        // 等待程序执行结束并输出状态
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
            process.destroy();
            os.close();
            processResult.setCode(exitCode);
            if (exitCode == SUCCESS) {
                QDLogger.i(Tag, SUCCESS_MESSAGE + cmd + ",Result=" + processResult.getResult());
                return processResult;
            } else {
                QDLogger.e(Tag, ERROR_MESSAGE + cmd + ",exitCode=" + exitCode + ",processIsEnd=" + processResult.getCode() + ",error=" + processResult.getError() + "\n\r");
                switch (exitCode) {
                    case 255:
                        QDLogger.e("端口ADB被占用");
                        break;
                    case 1:
                        QDLogger.e(Tag, "Operation not permitted");
                        if (TextUtils.isEmpty(processResult.getError())) {
                            processResult.setError("Permission denied");
                        }
                    case 2:
                        QDLogger.e(Tag, "error2");
                        if (TextUtils.isEmpty(processResult.getError())) {
                            processResult.setError("error2");
                        }
                        break;
                }
                return processResult;
            }
        } catch (Exception e) {
            QDLogger.e(Tag, "[" + cmd + "],Cause:" + e.getCause() + ",Message:" + e.getMessage());
            processResult.setCode(-1);
            processResult.setError("" + "[" + cmd + "],Cause:" + e.getCause() + ",Message:" + e.getMessage());
            return processResult;
        }
    }

    private ProcessResult getProcessResult(Process process) {
        ProcessResult result = new ProcessResult();
        result.setResult(getResultString(process.getInputStream()));//, System.out
        result.setError(getResultString(process.getErrorStream()));//, System.err
        return result;
    }

    private String getResultString(InputStream inputStream) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n\r");
            }
            QDLogger.e(Tag, "getResultString end :" + new String(stringBuffer));
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


    public void setOnAdbReceiceListener(OnAdbReceiceListener onAdbReceiceListener) {
        this.onAdbReceiceListener = onAdbReceiceListener;
    }

    public static interface OnReceiveListener {
        void onReceive(ProcessResult result);
    }

    public static interface OnAdbReceiceListener {
        void onFindDevices(List<DeviceModel> deviceModels);
    }

}
