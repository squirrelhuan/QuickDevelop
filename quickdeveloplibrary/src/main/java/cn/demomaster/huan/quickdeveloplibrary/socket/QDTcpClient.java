package cn.demomaster.huan.quickdeveloplibrary.socket;

//import com.alibaba.fastjson.JSON;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import cn.demomaster.huan.quickdeveloplibrary.exception.QDException;
import cn.demomaster.qdlogger_library.QDLogger;

public class QDTcpClient {
    private String serverIP;
    private int serverPort;
    public QDTcpClient(String serverIP,int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        receiveListenerManager = MessageListenerManager.getInstance();
        receiveListenerManager.setConnectTime(connectTime);
    }
    
    private String userName = "admin";
    private String passWord = "admin";
    
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
                String reply = null;
                boolean hasRead = false;
                System.out.println("等待接收 ：" + flag);
                while (!((reply = br.readLine()) == null)) {
                    hasRead = true;
                    Gson gson = new Gson();
                    QDMessage qdMessage = gson.fromJson(reply, QDMessage.class);
                    //System.out.println("接收信息：" + qdMessage.getTime() + "," + reply);
                    if (qdMessage != null) {
                        isConnected = true;
                        if (receiveListenerManager.containsKey(qdMessage.getTime())) {
                            RequestListener listener = receiveListenerManager.getListener(qdMessage.getTime());
                            if (listener != null) {
                                listener.onReceived(qdMessage, (qdMessage.getData() + "").getBytes());
                            }
                            receiveListenerManager.removeListenerById(qdMessage.getTime());
                        } else {
                            if (receiveListener != null) {
                                receiveListener.onReceived(qdMessage);
                            }
                        }
                    }
                }
                System.out.println("over" );
                if (!hasRead) {
                    // System.out.println("\"maybe disconnect\"");
                    // throw new Exception("maybe disconnect");
                }
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
        receiveListenerManager.setConnectTime(connectTime);
    }

    /**
     * 消息登录验证身份
     */
    private void qdlogin() {
        QDMessage qdMessage = new QDMessage();
        QDUserInfo userInfo = new QDUserInfo();
        userInfo.setUserName(userName);
        userInfo.setPassWord(passWord);
        qdMessage.setData(userInfo);
        qdMessage.setTime(System.currentTimeMillis());
        sendObj(qdMessage, new RequestListener() {
            @Override
            public void onReceived(QDMessage qdMessage, byte[] bytes) {
                Gson gson = new Gson();
                System.out.println("收到登录回复" + gson.toJson(qdMessage));
            }

            @Override
            public void onError(String err) {
                Gson gson = new Gson();
                System.out.println("登录超时" + gson.toJson(qdMessage));
            }
        });
    }

    private Socket clientSocket;
    private OutputStream out;
    public static final char END_CHAR = '\n';

    /**
     * @param data
     */
    public synchronized void send(String data) {
        send(data, null);
    }

    /**
     * @param data
     */
    public synchronized void send(String data, RequestListener listener) {
        QDMessage qdMessage = new QDMessage();
        qdMessage.setData(data);
        sendObj(qdMessage, listener);
    }

    /**
     * @param data
     */
    public synchronized void request(String data, RequestListener listener) {
        sendObj(data, listener);
    }

    private MessageListenerManager receiveListenerManager;

    //public void send(QDMessage qdMessage, RequestListener listener) {
    public void sendObj(Object msg, RequestListener listener) {
        if (msg == null) {
            return;
        }
        String msg1;
        QDMessage qdMessage = null;
        if (msg instanceof QDMessage) {
            qdMessage = (QDMessage) msg;
            qdMessage.setTime(System.currentTimeMillis());
            Gson gson = new Gson();
            msg1 = gson.toJson(qdMessage) + END_CHAR;
        } else {
            msg1 = msg.toString();// + END_CHAR;
        }
        if (listener != null && qdMessage != null) {
            receiveListenerManager.addListener(qdMessage.getTime(), listener);
        }
        sendBytes(msg1.getBytes());
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
