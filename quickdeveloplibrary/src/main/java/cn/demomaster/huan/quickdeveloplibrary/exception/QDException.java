package cn.demomaster.huan.quickdeveloplibrary.exception;

public class QDException extends Exception {

    public int errorCode;
    public QDException(String message) {
        super(message);
    }
    public QDException(int errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
