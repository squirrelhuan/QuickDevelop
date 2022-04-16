package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

import static android.content.Context.SENSOR_SERVICE;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "陀螺仪", preViewClass = TextView.class, resType = ResType.Custome)
public class LmSensorFragment extends QuickFragment implements SensorEventListener {

    @BindView(R.id.btn_start)
    Button btn_start;
    private SensorManager LmSensorManager;
    private Sensor LmSensor;
    //屏幕当前位置
    int Lwidth;
    int Lheight;
    float LPointX;
    float LPointY;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_lmsensor, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("陀螺仪");
        getActionBarTool().getActionBarLayout().getActionBarView().setBackgroundColor(Color.RED);

        LmSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        LmSensor = LmSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        LmSensorManager.registerListener(this, LmSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        WindowManager Lwm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics Ldm = new DisplayMetrics();

        Lwm.getDefaultDisplay().getMetrics(Ldm);
        Lwidth = Ldm.widthPixels;         // 设备屏幕宽度
        Lheight = Ldm.heightPixels;       // 设备屏幕高度
        LPointX = Lwidth / 2f;              //默认设置当前 "焦点位置"在屏幕的中心位置
        LPointY = Lheight / 2f;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }
        LPointX = LPointY = 0;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            /**
             * 当设备在正前方90度时 ，X :10 Y:0 Z:0
             * 当设备在正前方90度时，左右抬起数据变化幅度很小
             * 当设备在正前下方0度时， 0 ，0 ，10
             * 当设备在正下方0度时，然后向左抬起90度，0，10，0  Z从10减到0
             * 当设备在正下方0度时，然后向右抬起90度，0，-10，0 Z从10减到0
             * 当设备在正前上方180度时，0，0，-10
             * 当设备在正前上方180度时，然后向左抬起90度，0，-10，0  Z从负数增加到0
             * 当设备在正前上方180度时，然后向右抬起90度，0，10，0 Z从负数增加到0
             */

            //通过1和3参数得到Y轴位置
            if (x >= 0.1 && (z >= 0.1 || z <= -0.1)) {
                LPointY = Lheight / 2f + Lheight / 20f * z;
            }
            //通过2参数得到X轴坐标
            if ((y >= 0.1 || y <= -0.1) && z >= 0.1) {
                LPointX = Lwidth / 2f + Lwidth / 20f * -y;
            } else if ((y >= 0.1 || y <= -0.1) && z <= -0.1) {
                LPointX = Lwidth / 2f + Lwidth / 20f * -y;
            }
            if (0 == LPointX) //这里做了保护措施，如果超过了设定范围自动回到X轴中心位置
                LPointX = Lwidth / 2f;
            if (0 == LPointY)//如果超过了设定范围自动回到Y轴中心位置
                LPointY = Lheight / 2f;

            SetScreenPoint(LPointX, LPointY);
        }
    }

    /**
     * 设置当前 "焦点位置"
     */
    void SetScreenPoint(float x, float y) {
        //这个函数用来记录计算后的屏幕坐标，可以用到不同的场景
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LmSensorManager != null) {
            LmSensorManager.unregisterListener(this);
        }
    }
}