package cn.demomaster.huan.quickdeveloplibrary.socket;

public interface RequestListener {
    void onReceived(QDMessage qdMessage,byte[] bytes);

    void onError(String err);
}