package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.NetworkHelper;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.util.QDAndroidDeviceUtil.isRootSystem;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "关于手机", preViewClass = TextView.class, resType = ResType.Custome)
public class AboutMobileFragment extends BaseFragment {

    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.tv_root_state)
    TextView tv_root_state;
    @BindView(R.id.tv_cpu_info)
    TextView tv_cpu_info;

    View mView;
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_about_mobile, null);
        }
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, mView);
        String ip = null;
        if(NetworkHelper.isNetworkConnected(getContext())){
            ip = NetworkHelper.getLocalIpAddress(getContext());
        }
        tv_root_state.setText("root:"+isRootSystem());
        tv_info.setText("ip:"+ip);

        //获取屏幕分辨率
        DisplayMetrics metric=new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(metric);

        int width=metric.widthPixels; // 宽度（PX）
        int height=metric.heightPixels; // 高度（PX）

        float density=metric.density; // 密度（0.75 / 1.0 / 1.5）
        int densityDpi=metric.densityDpi; // 密度DPI（120 / 160 / 240）

        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width/density);//屏幕宽度(dp)
        int screenHeight = (int)(height/density);//屏幕高度(dp)

        Configuration config = getResources().getConfiguration();

        String a = "\n宽度:" + width + "\n高度:" + height + "\n密度:" + density + "\n密度DPI:" + densityDpi
                + "\r\n屏幕dp宽度：" + screenWidth + "\n屏幕dp高度：" + screenHeight+"\n最小限定宽度："+config.smallestScreenWidthDp;
        QDLogger.i(a);
        tv_info.append(a);

        float v0 = DisplayUtil.dip2px(mContext,45);
        float v1 =  getResources().getDimension(cn.demomaster.huan.quickdeveloplibrary.R.dimen.dp_45);
        QDLogger.i("v0="+v0+",v1="+v1);
        tv_cpu_info.setText("cpu架构:"+getCPUABI());
    }

    public static String getCPUABI() {
        String CPUABI = "unKnow";
            try {
                String os_cpuabi = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine();
                if (os_cpuabi.contains("x86")) {
                    CPUABI = "x86";
                } else if (os_cpuabi.contains("armeabi-v7a") || os_cpuabi.contains("arm64-v8a")) {
                    CPUABI = "armeabi-v7a";
                } else {
                    CPUABI = "armeabi";
                }
                QDLogger.i("os_cpuabi="+os_cpuabi);
            } catch (Exception e) {
                CPUABI = "armeabi";
            }
        return CPUABI;
    }


}