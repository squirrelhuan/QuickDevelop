package cn.demomaster.huan.quickdeveloplibrary.socket;

import java.util.LinkedHashMap;
import java.util.Map;

public class MessageListenerManager {
    private static MessageListenerManager instance;

    public static MessageListenerManager getInstance() {
        if (instance == null) {
            instance = new MessageListenerManager();
        }
        return instance;
    }

    private boolean isRunning;

    private MessageListenerManager() {
        timerRunnable = new TimerRunnable() {
            @Override
            public void run() {
                try {
                    isRunning = true;
                    Thread.sleep(getDelyTime());
                    dealTimeOut();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void dealTimeOut() {
        //处理当前listener
        if (getFirstTime() != null) {
            long time1 = getFirstTime().getKey() + connectTime*1000;
            if (time1 - System.currentTimeMillis() <= 0) {
                getFirstTime().getValue().onError("请求超时");
                receiveListenerMap.remove(getFirstTime().getKey());
            }
        }
        dealNext();
    }

    private void dealNext() {
        //准备下一个计时
        if (receiveListenerMap.size() > 0) {
            long time1 = getFirstTime().getKey() + connectTime*1000;
            long offsetTime = Math.max(time1 - System.currentTimeMillis(), 10);
            if (offsetTime > 0) {
                timerRunnable.setDelyTime(offsetTime);
                timerThread = new Thread(timerRunnable);
                timerThread.start();
            }
        } else {
            isRunning = false;
        }
    }

    public abstract class TimerRunnable implements Runnable {
        private long delyTime;

        public long getDelyTime() {
            return delyTime;
        }

        public void setDelyTime(long delyTime) {
            this.delyTime = delyTime;
        }

    }

    TimerRunnable timerRunnable;
    Thread timerThread;

    private void startTimer() {
        if (!isRunning) {
            dealNext();
        }
    }

    public Map.Entry<Long, MessageReceiveListener> getFirstTime() {
        for (Map.Entry<Long, MessageReceiveListener> entry : receiveListenerMap.entrySet()) {
            return entry;
        }
        return null;
    }

    LinkedHashMap<Long, MessageReceiveListener> receiveListenerMap = new LinkedHashMap<>();

    /**
     * @param targetTime 目标时间
     * @param listener   监听器
     */
    public void addListener(long targetTime, MessageReceiveListener listener) {
        receiveListenerMap.put(targetTime, listener);
        startTimer();
    }

    public MessageReceiveListener getListener(long time) {
        return receiveListenerMap.get(time);
    }

    public void removeListenerById(long time) {
        receiveListenerMap.remove(time);
    }

    public boolean containsKey(long time) {
        return receiveListenerMap.containsKey(time);
    }

    private long connectTime;

    /**
     * 设置超时时间
     *
     * @param connectTime
     */
    public void setConnectTime(long connectTime) {
        this.connectTime = connectTime;
    }
}
