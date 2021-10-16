package cn.demomaster.huan.quickdeveloplibrary.socket;

public interface ReceiveListener {
    void onConnect();
    void onReceived(QDMessage qdMessage);
    void onLostConnect(int failCode,String msg);//
    //void onError(String err);
}