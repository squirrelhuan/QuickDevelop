package cn.demomaster.huan.quickdeveloplibrary.lifecycle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LifecycleTimerData {
    /*LifecycleTimerData instance;

    public LifecycleTimerData getInstance() {
        if(instance==null){
            instance = new LifecycleTimerData();
        }
        return instance;
    }*/

    int maxLength = 1000;

    public LifecycleTimerData(int maxLength) {
        this.maxLength = maxLength;
    }

    public LifecycleTimerData() {

    }


    LinkedHashMap<LifeCycleClassInfo, List<LifeCycleEvent>> linePointsMap = new LinkedHashMap();

    public void addLifecycleEvent(Object object, LifeCycleEvent lifeCycleEvent) {
        if (linePointsMap.size() > maxLength) {
            linePointsMap.clear();
        }
        List<LifeCycleEvent> lifeCycleEventList = new ArrayList<>();

        LifeCycleClassInfo cycleClassInfo = new LifeCycleClassInfo();
        cycleClassInfo.setClazz(object.getClass());
        cycleClassInfo.setClazzHashCode(object.hashCode());
        cycleClassInfo.setCreatTime(System.currentTimeMillis());
        if (linePointsMap.containsKey(cycleClassInfo)) {
            lifeCycleEventList = linePointsMap.get(cycleClassInfo);
        }
        lifeCycleEventList.add(lifeCycleEvent);
        linePointsMap.put(cycleClassInfo, lifeCycleEventList);
    }

    public LinkedHashMap<LifeCycleClassInfo, List<LifeCycleEvent>> getLinePointsMap() {
        return linePointsMap;
    }
}
