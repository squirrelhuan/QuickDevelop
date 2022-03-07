package cn.demomaster.huan.quickdeveloplibrary.quicksocket;

//import com.alibaba.fastjson.JSON;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import cn.demomaster.huan.quickdeveloplibrary.exception.QDException;
import cn.demomaster.huan.quickdeveloplibrary.socket.MessageListenerManager;
import cn.demomaster.huan.quickdeveloplibrary.socket.RequestListener;
import cn.demomaster.qdlogger_library.QDLogger;

public class QuickTcpClient {
    private String serverIP;
    private int serverPort;

    public QuickTcpClient(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void connect() {
        flag = true;
        Thread thread = new Thread(() -> {
            try {
                if (!isServerClose(clientSocket)) {
                    throw new QDException("client已连接，请勿重新初始化");
                }
                clientSocket = new Socket(serverIP, serverPort);
                //client.setKeepAlive(true);//开启保持活动状态的套接字
                //client.setSoTimeout(5 * 60 * 1000);//设置超时时间
                newConnect(clientSocket);//开启消息接收
                //qdlogin();//用户登录
            } catch (Exception e) {
                QDLogger.e(e);
            }
        });
        thread.start();
    }

    boolean flag = true;

    private void newConnect(Socket clientSocket) {
        isConnected = true;
        if (receiveListener != null) {
            receiveListener.onConnect();
        }
        System.out.println("socket连接成功");
        while (flag) {
            try {
                InputStream is = this.clientSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                //接收服务器的相应
                System.out.println("等待接收 ：" + flag);
                byte[] tt = new byte[1024 * 10];
                int b;
                while ((b = is.read(tt)) != -1) {
                    byte[] bytes1 = new byte[b];
                    for (int i = 0; i < b; i++) {
                        bytes1[i] = tt[i];
                    }
                    //System.out.println("接收信息：" + qdMessage.getTime() + "," + reply);
                    isConnected = true;
                    if (receiveListener != null) {
                        receiveListener.onReceived(bytes1);
                    }
                }
                System.out.println("over");
            } catch (Exception e) {
                QDLogger.e(e);
                closeConnect();
            }
        }
    }

    private void closeConnect() {
        System.err.println("关闭socket 链接");
        try {
            isConnected = false;
            flag = false;
            out.close();
            if (clientSocket != null) {
                clientSocket.shutdownOutput();
                clientSocket.shutdownInput();
                clientSocket.close();
                clientSocket = null;
            }
        } catch (Exception e) {
            QDLogger.e(e);
        }
    }

    private boolean isConnected;
    private long connectTime = 30000;//超过30秒会连接超时

    /**
     * 设置超时时间
     *
     * @param connectTime
     */
    public void setConnectTime(long connectTime) {
        this.connectTime = connectTime;
    }

    private Socket clientSocket;
    private OutputStream out;
    public static final char END_CHAR = '\n';

    //public void send(QDMessage qdMessage, RequestListener listener) {
    public void sendObj(byte[] bytes) {
        if (bytes == null) {
            return;
        }
        sendBytes(bytes);
      /*  //这里比较重要，需要给请求信息添加终止符，否则服务端会在解析数据时，一直等待
        Thread thread = new Thread(() -> {
            if (validConnect()) {
                try {
                    String msg1 = JSON.toJSONString(qdMessage) + END_CHAR;
                    out = client.getOutputStream();
                    out.write(msg1.getBytes());
                    System.out.println("发送请求：" + qdMessage.getTime() + "," + JSON.toJSONString(qdMessage));
                } catch (IOException e) {
                    QDLogger.e(e);
                    reConnect();
                }
            } else {
                reConnect();
            }

        });
        thread.start();*/
    }

    public void sendBytes(final byte[] bytes) {
        //这里比较重要，需要给请求信息添加终止符，否则服务端会在解析数据时，一直等待
        Thread thread = new Thread(() -> {
            if (validConnect()) {
                try {
                    out = clientSocket.getOutputStream();
                    out.write(bytes);
                    QDLogger.println("发送数据：" + new String(bytes));
                    // System.out.println("发送请求：");
                } catch (IOException e) {
                    QDLogger.e(e);
                    reConnect();
                }
            } else {
                reConnect();
            }
        });
        thread.start();
    }

    private boolean validConnect() {
        return clientSocket != null && clientSocket.isConnected();
    }

    /**
     * 重新链接
     */
    private void reConnect() {
        System.out.println("重新连接。。。");
        try {
            if (clientSocket != null) {
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
                clientSocket = null;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        connect();
    }


    /**
     * 判断是否断开连接，断开返回true,没有返回false
     *
     * @param socket
     * @return
     */
    public Boolean isServerClose(Socket socket) {
        try {
            if (socket == null) return true;
            if (socket.isClosed() || !clientSocket.isConnected()) {
                return true;
            }
            socket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {
            return true;
        }
    }

    ReceiveListener receiveListener;

    public void setReceiveListener(ReceiveListener receiveListener) {
        this.receiveListener = receiveListener;
    }

    public void setPort(int port) {
        this.serverPort = port;
    }
}
