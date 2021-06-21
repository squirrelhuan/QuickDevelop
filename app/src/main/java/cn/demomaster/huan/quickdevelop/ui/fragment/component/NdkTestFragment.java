package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdevelop.service.SimpleService;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.jni.JNITest;
import cn.demomaster.huan.quickdeveloplibrary.jni.ServiceHelper;
import cn.demomaster.huan.quickdeveloplibrary.jni.ServiceToken;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Ndk测试", preViewClass = TextView.class, resType = ResType.Custome)
public class NdkTestFragment extends BaseFragment {

    private ServiceToken mToken;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
           /*BaseService.BaseBinder baseBinder = (BaseService.BaseBinder) iBinder;
            SimpleService simpleService = (SimpleService) baseBinder.getService();
            simpleService.setText("yes");*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_ndk, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        // Example of a call to a native method
        TextView tv = (TextView) rootView.findViewById(R.id.sample_text);
        tv.setText(JNITest.stringFromJNI());

        Button btn_start = rootView.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bind to Service
                mToken = ServiceHelper.bindToService(getActivity(), SimpleService.class, serviceConnection);
                //getActivity().startService(new Intent(getContext(),BaseService.class));

                //Intent mIntent = new Intent();
                // mIntent.setClass(mContext, MessageService.class);
                //mContext.startService(mIntent);
            }
        });

        //BaseService.baseBinder.getService();
    }

    @Override
    public void onStop() {
        super.onStop();
        //ServiceHelper.unbindFromService(mToken);
    }
}