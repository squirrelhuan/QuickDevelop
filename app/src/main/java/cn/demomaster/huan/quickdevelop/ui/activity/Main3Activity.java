package cn.demomaster.huan.quickdevelop.ui.activity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;

public class Main3Activity extends QDActivity {
    SensorManager sensorManager;
    Sensor gyroscopeSensor;
    SensorEventListener gyroscopeSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //获取取传感器管理器
        sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        //获取陀螺仪传感器
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //添加传感器监听
        gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
               //QDLogger.println("onSensorChanged="+Arrays.toString(sensorEvent.values));//sensorEvent.values[0] 代表x轴角速度，sensorEvent.values[1] 代表y轴，sensorEvent.values[2] 代表z轴，
                // 对接收的数据处理
                if (sensorEvent.values[2] > 0.5f) { // anticlockwise
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if (sensorEvent.values[2] < -0.5f) { // clockwise
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }

            @Override
             public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        //注册监听器，
        sensorManager.registerListener(gyroscopeSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            sensorManager.unregisterListener(gyroscopeSensorListener);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
