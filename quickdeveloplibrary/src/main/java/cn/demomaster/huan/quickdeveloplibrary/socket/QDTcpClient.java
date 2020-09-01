package cn.demomaster.huan.quickdeveloplibrary.socket;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import cn.demomaster.huan.quickdeveloplibrary.exception.QDException;
import cn.demomaster.qdlogger_library.QDLogger;

public class QDTcpClient {

    private static QDTcpClient instance;
    private String serverIP;
    private int serverPort;

    public static QDTcpClient getInstance() {
        if (instance == null) {
            instance = new QDTcpClient();
        }
        return instance;
    }

    private QDTcpClient() {
        receiveListenerManager = MessageListenerManager.getInstance();
        receiveListenerManager.setConnectTime(connectTime);
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    private String userName = "admin";
    private String passWord = "admin";
    public void connect(String serverIP, int serverPort) {
        flag = true;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isServerClose(client)) {
                        throw new QDException("client已连接，请勿重新初始化");
                    }
                    client = new Socket(serverIP, serverPort);
                    isConnected = true;
                    System.out.println("socket连接成功");
                    //client.setKeepAlive(true);//开启保持活动状态的套接字
                    //client.setSoTimeout(5 * 60 * 1000);//设置超时时间
                    waitMessage();//开启消息接收
                    //qdlogin();//用户登录
                } catch (Exception e) {
                    System.err.println("socket连接失败");
                    QDLogger.e(e);
                }
            }
        });
        thread.start();
    }

    boolean flag = true;
    private void waitMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        System.out.println("等待接收 ：" + flag);
                        InputStream is = client.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        //接收服务器的相应
                        String reply = null;
                        boolean hasRead = false;
                        while (!((reply = br.readLine()) == null)) {
                            hasRead = true;
                            QDMessage qdMessage = JSON.parseObject(reply, QDMessage.class);
                            System.out.println("接收信息：" +qdMessage.getTime()+","+ reply);
                            if (qdMessage != null) {
                                isConnected = true;
                                if (receiveListenerManager.containsKey(qdMessage.getTime())) {
                                    MessageReceiveListener listener = receiveListenerManager.getListener(qdMessage.getTime());
                                    if(listener!=null){
                                        listener.onReceived(qdMessage);
                                    }
                                    receiveListenerManager.removeListenerById(qdMessage.getTime());
                                } else {
                                    if (onMessageReceiveListener != null) {
                                        onMessageReceiveListener.onReceived(qdMessage);
                                    }
                                }
                            }
                        }
                        if(!hasRead){
                            throw new Exception("maybe disconnect");
                        }
                    } catch (Exception e) {
                        QDLogger.e(e);
                        closeConnect();
                    }
                }
            }
        }).start();
    }

    private void closeConnect() {
        System.err.println("关闭socket 链接");
        try {
            isConnected = false;
            flag = false;
            out.close();
            if (client != null) {
                client.shutdownOutput();
                client.shutdownInput();
                client.close();
                client = null;
            }
        } catch (Exception e) {
            QDLogger.e(e);
        }
    }

    private boolean isConnected;
    private long connectTime=30000;//超过30秒会连接超时

    /**
     * 设置超时时间
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
        send(qdMessage, new MessageReceiveListener() {
            @Override
            public void onReceived(QDMessage qdMessage) {
                System.out.println("收到登录回复" + JSON.toJSONString(qdMessage));
            }

            @Override
            public void onError(String err) {
                System.out.println("登录超时" + JSON.toJSONString(qdMessage));
            }
        });
    }

    private Socket client;
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
    public synchronized void send(String data, MessageReceiveListener listener) {
        QDMessage qdMessage = new QDMessage();
        qdMessage.setData(data);
        send(qdMessage, listener);
    }


    private MessageListenerManager receiveListenerManager;
    public void send(QDMessage qdMessage, MessageReceiveListener listener) {
        if(qdMessage!=null){
            qdMessage.setTime(System.currentTimeMillis());
        }
        if (listener != null) {
            receiveListenerManager.addListener(qdMessage.getTime(),listener);
        }
        //这里比较重要，需要给请求信息添加终止符，否则服务端会在解析数据时，一直等待
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (validConnect()) {
                    try {
                        String msg1 = JSON.toJSONString(qdMessage) + END_CHAR;
                        out = client.getOutputStream();
                        out.write(msg1.getBytes());
                        System.out.println("发送请求："+qdMessage.getTime()+","+ JSON.toJSONString(qdMessage));
                    } catch (IOException e) {
                        QDLogger.e(e);
                        reConnect();
                    }
                } else {
                    reConnect();
                }

            }
        });
        thread.start();
    }

    private boolean validConnect() {
       return client != null && client.isConnected();
    }

    /**
     * 重新链接
     */
    private void reConnect() {
        System.out.println("重新连接。。。");
        try {
            if (client != null) {
                client.shutdownInput();
                client.shutdownOutput();
                client = null;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        connect(serverIP, serverPort);
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
            if (socket == null || socket.isClosed() || !client.isConnected()) {
                return true;
            }
            socket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {
            return true;
        }
    }

    MessageReceiveListener onMessageReceiveListener;
    public void setOnMessageReceiveListener(MessageReceiveListener onMessageReceiveListener) {
        this.onMessageReceiveListener = onMessageReceiveListener;
    }
}
