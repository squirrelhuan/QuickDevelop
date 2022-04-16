package cn.demomaster.huan.quickdeveloplibrary.exception;

public class QDException extends Exception {

    public Object errorCode;

    public QDException(String message) {
        super(message);
    }

    public QDException(Object errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "QDException{" +
                "code=" + errorCode +
                ",msg=" + getMessage() +
                '}';
    }
}
