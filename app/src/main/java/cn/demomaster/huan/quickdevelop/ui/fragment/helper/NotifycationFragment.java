package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.BatteryOptimizationsHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.NotificationHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "消息通知", preViewClass = TextView.class, resType = ResType.Custome)
public class NotifycationFragment extends BaseFragment {

    @BindView(R.id.btn_01)
    QDButton btn_01;
    @BindView(R.id.btn_02)
    QDButton btn_02;
    @BindView(R.id.btn_03)
    QDButton btn_03;
    @BindView(R.id.btn_04)
    QDButton btn_04;
    @BindView(R.id.btn_05)
    QDButton btn_05;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_notifycation, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("消息通知");
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BatteryOptimizationsHelper.request(getContext());
            }
        });
        btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.Builer builer = new NotificationHelper.Builer(mContext);
                builer.setTitle("消息通知（系统默认）").setContentText(""+((TextView)v).getText()).send();
            }
        });
        btn_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.Builer builer = new NotificationHelper.Builer(mContext);
                String soundResourceName = "beep";
                String soundResourceName2 = "audio/beep.ogg";
                String path = "file:///android_asset/"+ soundResourceName;
                Uri soundUri = null;
                //访问Resource
                //soundUri = NotificationHelper2.getUriFromResource(mContext,soundResourceName);
                //访问Assets
                soundUri = NotificationHelper.getUriFromAssets(mContext,soundResourceName2);
                builer.setTitle("消息通知（自定义音频）").setContentText(""+((TextView)v).getText()).setSoundUri(soundUri).send();
            }
        });
        btn_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.Builer builer = new NotificationHelper.Builer(mContext);
                builer.setTitle("消息通知（静音+震动）").setContentText(""+((TextView)v).getText()).setEnableSound(false).send();
            }
        });

        btn_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper.Builer builer = new NotificationHelper.Builer(mContext);
                builer.setTitle("消息通知（静音不震动）").setContentText(""+((TextView)v).getText()).setEnableVibration(false).setEnableSound(false).send();
            }
        });
    }

}