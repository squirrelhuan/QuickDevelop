package cn.demomaster.huan.quickdeveloplibrary.quicksocket;

import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;

public interface ReceiveListener {
    void onConnect();
    void onReceived(byte[] bytes);
    void onLostConnect(int failCode,String msg);//
    //void onError(String err);
}