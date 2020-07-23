package cn.demomaster.huan.quickdeveloplibrary.util.terminal;


import java.io.*;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

public class Test {
    public static final int SUCCESS = 0;            // 表示程序执行成功

    public static final String COMMAND = "java.exe -version";    // 要执行的语句
    public static final String SUCCESS_MESSAGE = "执行成功：";
    public static final String ERROR_MESSAGE = "出错了：";
    public static String Path_Temp;

    public static void main(String[] args) {
        init();

        execute(" adb kill-server ");
        execute(" adb start-server ");
        execute(" adb devices ");
        execute(" adb connect 192.168.199.180:5555");
        execute(" adb connect 192.168.199.180:5555");
        while (true) {
            execute("adb -s 192.168.199.180:5555 shell screencap -p /sdcard/01.png");
            execute("adb -s 192.168.199.180:5555 pull /sdcard/01.png screen");

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                QDLogger.e(e);
            }
        }
    }

    private static void init() {
        File directory = new File("adb");//设定为当前文件夹
        if (!directory.exists()) {
           QDLogger.println("未找到 adb可执行文件");//获取绝对路径
            return;
        } else {
            Path_Temp = directory.getAbsolutePath();
            Path_Temp = Path_Temp.substring(0, Path_Temp.lastIndexOf(File.separator));
           QDLogger.println("获取绝对路径" + Path_Temp);//获取绝对路径
        }
        //QDLogger.println(directory.getCanonicalPath());//获取标准的路径
        //QDLogger.println(directory.getAbsolutePath());//获取绝对路径
    }

    private static void execute(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            // 打印程序输出
            readProcessOutput(process);
            // 等待程序执行结束并输出状态
            int exitCode = process.waitFor();
            if (exitCode == SUCCESS) {
               QDLogger.println(SUCCESS_MESSAGE + cmd);
            } else {
                System.err.println(ERROR_MESSAGE + exitCode + "\n\r" + cmd);
            }
            process.destroy();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            QDLogger.e(e);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            QDLogger.e(e);
        }
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
}
