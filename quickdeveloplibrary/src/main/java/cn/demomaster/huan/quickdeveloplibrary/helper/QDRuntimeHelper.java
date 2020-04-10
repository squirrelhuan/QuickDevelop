package cn.demomaster.huan.quickdeveloplibrary.helper;


import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

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
            //runtimeThread = new RuntimeThread(process);
            //runtimeThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exeCommand(String command) {
        exeCommand(command, null);
        return true;
    }

    /**
     * 测试有bug
     * @param command
     * @param callBack
     */
    public void exeCommand2(String command, final CallBack callBack) {
        addCommand(command, callBack);
        excute();
       /* new Thread(new Runnable() {
            @Override
            public void run() {

        try {
            String cmd = "adb version";
            Process p = runtime.exec("/system/bin/sh", null, new File("/system/bin"));
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            System.out.println("【version】" );
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("【line】-->" + line);
            }
            p.waitFor();
            is.close();
            reader.close();
            p.destroy();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

            }
        }).start();*/
    }

    //synchronized
    private void excute() {
        System.out.println("excute...");
        if (isIdle) {
            isIdle = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("rThread size=" + map.size());
                    try {
                        Map.Entry entry = getFirstEntry();
                        if (entry != null && entry.getKey() != null) {
                            System.out.println("【sendCmd】-->");
                            CallBack callBack = (CallBack) entry.getValue();
                            String cmd = callBack.data;
                            Process p = Runtime.getRuntime().exec("/system/bin/sh", null, new File("/system/bin"));
                            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(p.getOutputStream())), true);
                            System.out.println("【准备发送】-->" + cmd);
                            printWriter.println(cmd);
                            //Runtime.getRuntime().exec(cmd);
                            System.out.println("【发送】-->" + cmd);
                            InputStream is = p.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                            StringBuffer buffer = new StringBuffer();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                buffer.append(line + "\n");
                                QDLogger.e("【line】==>"+line);
                            }
                            p.waitFor();
                            System.out.println("【callBack1】-->" + cmd);
                            callBack((Long) entry.getKey());
                            System.out.println("【callBack2】-->" + cmd);
                            /*
                            if (callBack != null) {
                                callBack.onReceive(buffer.toString());
                            }
                            QDLogger.e(buffer.toString());
                            buffer = null;
                            is.close();
                            reader.close();
                            p.destroy();*/
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        //callBack();
                    }
                }
            }).start();
        }
    }

    boolean isIdle = true;
    public void addCommand(String command, CallBack callBack) {
        System.out.println("addCommand:" + command);
        if (callBack == null) {
            callBack = new CallBack() {
                @Override
                public void onReceive(String data) {
                }
            };
        }
        callBack.setData(command);
        map.put(System.currentTimeMillis(), callBack);
    }

    public void callBack(Long key) {
        isIdle = true;
        System.out.println("rThread size1=" + map.size());
        removeByKey(key);
        System.out.println("rThread size2=" + map.size());
        excute();
    }

    private void removeByKey(Long key) {
        if (map.containsValue(key)) {
            map.remove(key);
        }
    }

    private void removeFirst() {
        Map.Entry entry = getFirstEntry();
        if (entry != null) {
            map.remove(entry.getKey());
        }
    }


    private Map.Entry getFirstEntry() {
        for (Map.Entry<Long, CallBack> entry : map.entrySet()) {
            return entry;
        }
        return null;
    }

    LinkedHashMap<Long, CallBack> map = new LinkedHashMap();

    /**
     * 发送命令
     * @param command
     * @param callBack
     * @return
     */
    public boolean exeCommand(String command, CallBack callBack) {
        if (callBack != null) {
            callBack.data = command;
        }
        PrintWriter out;
        try {
            if (process == null) {
                process = Runtime.getRuntime().exec("/system/bin/sh", null, new File("/system/bin")); // android中使用
                // proc = Runtime.getRuntime().exec("/bin/bash", null, new File("/bin")); 			//Linux中使用
                // 至于windows，由于没有bash，和sh 所以这种方式可能行不通
                Log.d("", "process...");
            }
            if (runtimeThread != null) {
                runtimeThread.addReceiver(callBack);
            }
            if (process != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);

                System.out.println("【准备发送】-->" + command);
                printWriter.println(command);
                runtimeThread.receive();
                //out.println("exit");
                // proc.waitFor(); //上面读这个流食阻塞的，所以waitfor 没太大必要性
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                runtimeThread.end();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public void addReceiver(RuntimeReceiver receiver) {
        runtimeThread.addReceiver(receiver);
    }

    public static interface BCallBack {
        void onReceive(String data);
    }

    public static abstract class CallBack implements BCallBack {
        private String data;

        public void setData(String data) {
            this.data = data;
        }
    }

    public static interface RuntimeReceiver {
        void onReceive(String data);
    }

    RuntimeThread runtimeThread = new RuntimeThread();

    public class RuntimeThread extends Thread {
        Process mProcess;

        BufferedReader bufferedReader;

        public RuntimeThread(Process process) {
            mProcess = process;
        }

        RuntimeReceiver runtimeReceiver;
        CallBack callBack;
        StringBuffer stringBuffer;

        public RuntimeThread() {

        }

        @Override
        public void run() {
            receive();
        }

        public void receive() {
            try {
                if (bufferedReader == null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    stringBuffer = new StringBuffer();
                    while (((line = bufferedReader.readLine()) != null)) {
                        if (stringBuffer == null) {
                            stringBuffer = new StringBuffer();
                        }
                        stringBuffer.append(line.getBytes());
                        if (runtimeReceiver != null) {
                            runtimeReceiver.onReceive(line + "");
                        } else {
                            System.out.println(line + "");
                        }
                    }
                    stringBuffer = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                bufferedReader = null;
            }
        }

        public void addReceiver(RuntimeReceiver receiver) {
            runtimeReceiver = receiver;
        }

        public void addReceiver(CallBack callBack) {
            this.callBack = callBack;
        }

        public void end() {
            if (callBack != null && stringBuffer != null) {
                QDLogger.e("==================================" + stringBuffer.toString());
                callBack.onReceive(stringBuffer.toString());
            }
            stringBuffer = null;
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