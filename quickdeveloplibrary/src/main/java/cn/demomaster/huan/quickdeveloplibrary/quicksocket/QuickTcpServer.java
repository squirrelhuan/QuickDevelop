package cn.demomaster.huan.quickdeveloplibrary.quicksocket;

//import com.alibaba.fastjson.JSON;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDUserInfo;
import cn.demomaster.qdlogger_library.QDLogger;

public class QuickTcpServer {

    public static final String SERVICE_IP = "127.0.0.1";
    private String serverIP = SERVICE_IP;
    private int port = 10101;
    Map<Long, Socket> socketMap = new HashMap<>();

    public QuickTcpServer() {
        autoPort = true;
    }

    public QuickTcpServer(String ip, int port) {
        this.serverIP = ip;
        this.port = port;
        autoPort = false;
    }

    boolean autoPort = true;//是否使用自动端口

    public void setAutoPort(boolean autoPort) {
        this.autoPort = autoPort;
    }

    private void initConnect() {
        try {
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            server = new ServerSocket(port, 10, serverAddress);
            QDLogger.println("QDTcpServer 初始化成功，端口号：" + server.getLocalPort());
        } catch (BindException e) {
            QDLogger.e(e);
            portError();
        } catch (UnknownHostException e) {
            QDLogger.e(e);
        } catch (IOException e) {
            QDLogger.e(e);
        }
    }

    /**
     * 端口
     */
    private void portError() {
        if (autoPort) {
            //随机生成端口
            QDLogger.println("QDTcpServer 端口号占用：" + port);
            if (port < 65535) {
                port += 1;
            } else {
                port = 0;
            }
            initConnect();
        }
    }

    private ServerSocket server;

    public void start() {
        if (server == null) {
            initConnect();
            QdThreadHelper.runOnSubThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            QDLogger.println("QDTcpServer 等待连接");
                            // 阻塞式的等待连接
                            Socket client = server.accept();
                            QDLogger.println("新连接");
                            addClient(client);
                            //checkClient();
                        }
                        //server.close();
                    } catch (UnknownHostException e) {
                        QDLogger.e(e);
                    } catch (IOException e) {
                        QDLogger.e(e);
                    } catch (Exception e) {
                        QDLogger.e(e);
                    }
                }
            });
        }
    }

    public int getPort() {
        if (server != null) {
            server.getLocalPort();
        }
        return port;
    }

    private void addClient(Socket client) {
        if (onReceiveListener != null) {
            onReceiveListener.onClientConnect(client);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                QDLogger.println("客户端" + "连接");
                //3.获得输入流
                InputStream is = null;
                try {
                    is = client.getInputStream();
                    //4.读取用户输入信息
                    byte[] tt = new byte[1024 * 10];
                    int b;
                    while ((b = is.read(tt)) != -1) {
                        byte[] bytes1 = new byte[b];
                        for (int i = 0; i < b; i++) {
                            bytes1[i] = tt[i];
                        }
                        System.out.println("收到：" + new String(bytes1) + Arrays.toString(bytes1));
                        if (onReceiveListener != null) {
                                    onReceiveListener.onReceiveMessage(client, bytes1);
                        }
                    }
                } catch (IOException e) {
                    QDLogger.e(e);
                }
            }
        }).start();
    }

    OnReceiveListener onReceiveListener;

    public void setOnReceiveMessageListener(OnReceiveListener onReceiveMessageListener) {
        this.onReceiveListener = onReceiveMessageListener;
    }

    public interface OnReceiveListener {
        void onClientConnect(Socket socket);

        void onReceiveMessage(Socket socket, byte[] bytes);
    }
  /*  public interface OnReceiveListenerInterface {
        void onClientConnect(Socket socket);

        void onReceiveMessage(Socket socket, byte[] bytes);
    }
    public abstract class OnReceiveListener implements OnReceiveListenerInterface {

        public void clientConnect(Socket socket) {
            QdThreadHelper.runOnUiThread(
            onClientConnect(socket);
            );
        }

        public void receiveMessage(Socket socket, byte[] bytes) {
           onReceiveMessage(socket,bytes);
        }
    }
*/
    public static final char END_CHAR = '\n';

    private void replyLogin(Socket client, boolean isSuccess, long time) {
        //获得输出流
        OutputStream os = null;
        try {
            os = client.getOutputStream();
            //给客户一个响应
            QDMessage qdMessage = new QDMessage();
            qdMessage.setStatus(isSuccess ? 1 : 0);
            qdMessage.setTime(time);
            qdMessage.setMsg(isSuccess ? "login success" : "login fail");

            Gson gson = new Gson();
            String reply = gson.toJson(qdMessage) + END_CHAR;
            os.write(reply.getBytes());
        } catch (IOException e) {
            QDLogger.e(e);
        }
    }

    public void sendMessage(long clientId, long time, String msg) {
        OutputStream os = null;
        try {
            os = getSocketById(clientId).getOutputStream();
            //给客户一个响应
            QDMessage qdMessage = new QDMessage();
            qdMessage.setTime(time);
            qdMessage.setMsg(msg);
            Gson gson = new Gson();
            String reply = gson.toJson(qdMessage) + END_CHAR;
            os.write(reply.getBytes());
        } catch (IOException e) {
            QDLogger.e(e);
        }
    }

    private Socket getSocketById(long clientId) {
        return socketMap.get(clientId);
    }

    public synchronized void checkClient() {
        List<Long> socketIds = new ArrayList<>();
        for (Map.Entry entry : socketMap.entrySet()) {
            boolean b = isServerClose((Socket) entry.getValue());
            if (b) {
                System.out.println(entry.getKey() + "离线");
                socketIds.add((Long) entry.getKey());
            }
        }
        for (Long id : socketIds) {
            socketMap.remove(id);
        }
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     *
     * @param socket
     * @return
     */
    public Boolean isServerClose(Socket socket) {
        try {
            socket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {
            return true;
        }
    }

}
