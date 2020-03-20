package cn.demomaster.huan.quickdeveloplibrary.helper;


import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class QDRuntimeHelper {
    Runtime runtime;
    Process process;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    private static QDRuntimeHelper instance;

    public static QDRuntimeHelper getInstance() {
        if (instance == null) {
            instance = new QDRuntimeHelper();
        }
        return instance;
    }

    private QDRuntimeHelper() {
        init();
    }

    private void init() {
        runtime = Runtime.getRuntime();
        try {
            process = runtime.exec("/system/bin/sh", null, new File("/system/bin")); // android中使用
            printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
            printWriter.println("su");
            runtimeThread = new RuntimeThread(process);
            runtimeThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exeCommand(String command) {
        try {
            if (process == null) {
                process = Runtime.getRuntime().exec("/system/bin/sh", null, new File("/system/bin")); // android中使用
                // proc = Runtime.getRuntime().exec("/bin/bash", null, new File("/bin")); 			//Linux中使用
                // 至于windows，由于没有bash，和sh 所以这种方式可能行不通
                Log.d("", "process...");
            }
            if (process != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
                out.println(command);
                //out.println("exit");
                // proc.waitFor(); //上面读这个流食阻塞的，所以waitfor 没太大必要性
                System.out.println("【发送】-->"+command);
                runtimeThread.receive();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public void addReceiver(RuntimeReceiver receiver){
        runtimeThread.addReceiver(receiver);
    }

    public static interface RuntimeReceiver {
       void onReceive(String data);
    }

    RuntimeThread runtimeThread;
    public class RuntimeThread extends Thread {
        Process mProcess;

        BufferedReader bufferedReader;
        public RuntimeThread(Process process) {
            mProcess = process;
        }

        RuntimeReceiver runtimeReceiver;
        @Override
        public void run() {
            receive();
        }

        public void receive() {
            try {
                if(bufferedReader==null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while (((line = bufferedReader.readLine()) != null)) {
                        if(runtimeReceiver!=null) {
                            runtimeReceiver.onReceive(new String(line.getBytes()));
                        }else {
                            System.out.println(line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                bufferedReader = null;
            }
        }

        public void addReceiver(RuntimeReceiver receiver) {
            runtimeReceiver = receiver;
        }
    }



    public void destory() {
        try {
            bufferedReader.close();
            printWriter.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRootSystem() {
        if (isRootSystem1() || isRootSystem2()) {
            //TODO 可加其他判断 如是否装了权限管理的apk，大多数root 权限 申请需要app配合，也有不需要的，这个需要改su源码。因为管理su权限的app太多，无法列举所有的app，特别是国外的，暂时不做判断是否有root权限管理app
            //多数只要su可执行就是root成功了，但是成功后用户如果删掉了权限管理的app，就会造成第三方app无法申请root权限，此时是用户删root权限管理app造成的。
            //市场上常用的的权限管理app的包名   com.qihoo.permmgr  com.noshufou.android.su  eu.chainfire.supersu   com.kingroot.kinguser  com.kingouser.com  com.koushikdutta.superuser
            //com.dianxinos.superuser  com.lbe.security.shuame com.geohot.towelroot 。。。。。。
            return true;
        } else {
            return false;
        }
    }

    private static boolean isRootSystem1() {
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static boolean isRootSystem2() {
        List<String> pros = getPath();
        File f = null;
        try {
            for (int i = 0; i < pros.size(); i++) {
                f = new File(pros.get(i), "su");
                System.out.println("f.getAbsolutePath():" + f.getAbsolutePath());
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }
}