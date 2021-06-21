package cn.demomaster.huan.quickdeveloplibrary.socket;

public interface MessageReceiveListener {
    void onReceived(QDMessage qdMessage);

    void onError(String err);
}