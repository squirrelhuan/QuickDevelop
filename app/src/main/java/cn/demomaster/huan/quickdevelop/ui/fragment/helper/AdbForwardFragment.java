package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ADBHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.qdrouter_library.view.StateView;


/**
 * Squirrel桓
 * 2018/8/25 QDTerminal
 */
@ActivityPager(name = "PC和App通讯", preViewClass = StateView.class, resType = ResType.Custome)
public class AdbForwardFragment extends QuickFragment {

    @BindView(R.id.et_port)
    EditText et_port;
    @BindView(R.id.btn_start_service)
    QDButton btn_start_service;
    @BindView(R.id.tv_console)
    TextView tv_console;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_adbforward, null);
        return mView;
    }
    int port =9000;
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("PC和App通讯");
        et_port.setText(port+"");
        btn_start_service.setOnClickListener(v -> {
            mServer = new ServerThread();
            mServer.start();
        });
        getSerialNumber();
    }
    private static String getSerialNumber() {
        String serial = null;
        try {
            ADBHelper.getInstance().execute("getprop | grep serial", result -> QdToast.show("serial="+result.getResult()));
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialnocustom");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
    ServerThread mServer;

    /**
     * Created by sgll on 2018/12/10.
     */
    public class ServerThread extends Thread {
        private ServerSocket serverSocket;
        private Socket socket;
        boolean isLoop = true;

        public void setIsLoop(boolean isLoop) {
            this.isLoop = isLoop;
        }

        public Socket getSocket() {
            return socket;
        }

        @Override
        public void run() {
            super.run();
            try {
                //设置Android端口为9000
                serverSocket = new ServerSocket(port);
                while (isLoop) {
                    try {
                        //从连接队列中取出一个连接，如果没有则等待
                        socket = serverSocket.accept();
                        writeLog("连接成功");
//                    new ProcessClientRequestThread(socket).start();
                        while (true) {
                            /**
                             * isClosed()、isConnected()、isInputStreamShutdown()、isOutputStreamShutdown()
                             * 这些方法无法判断服务端是否断开，只能判断本地的状态
                             */
                            // 发送心跳包，单线程中使用，判断socket是否断开
                            socket.sendUrgentData(0xFF);
                            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//                        InputStreamReader reader = new InputStreamReader(inputStream);
//                        InputStream inputStream = socket.getInputStream();
                            byte[] buffer = new byte[1024];
//                        char[] buffer = new char[1024];
                            int bt;
                            while ((bt = inputStream.read(buffer)) != -1) {
//                            stringBuffer.append(buffer, 0, bt);
//                            String aaa = new String(buffer, 0, bt).trim();
                                writeLog(new String(buffer, 0, bt).trim());
                                /*String tag = "-end-";
                                if (text.endsWith(tag)) {
                                    text = text.substring(0, text.lastIndexOf(tag));
                                    QDLogger.v("读取结束，内容为：" + text);
                                    writeLog(text.trim());
                                    break;
                                }*/
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeLog(String str) {
        QDLogger.i("str: " + str);
        QdThreadHelper.runOnUiThread(() -> tv_console.append(str + "\n"));
    }
}