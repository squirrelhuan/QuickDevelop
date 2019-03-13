package cn.demomaster.huan.quickdevelop.fragment.component;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.service.GuardService;
import cn.demomaster.huan.quickdevelop.service.MessageService;
import cn.demomaster.huan.quickdevelop.service.SimpleService;
import cn.demomaster.huan.quickdevelop.service.UploadFilesIntentService;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.jni.BaseService;
import cn.demomaster.huan.quickdeveloplibrary.jni.JNITest;
import cn.demomaster.huan.quickdeveloplibrary.jni.ServiceHelper;
import cn.demomaster.huan.quickdeveloplibrary.jni.ServiceToken;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "NdkTestFragment",preViewClass = StateView.class,resType = ResType.Custome)
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

    //Components
    ViewGroup mView;


    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_ndk_test, null);
        }

        Bundle bundle = getArguments();
        String title = "空界面";
        // Example of a call to a native method
        TextView tv = (TextView) mView.findViewById(R.id.sample_text);
        tv.setText(JNITest.stringFromJNI());

        Button btn_start = mView.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bind to Service
                mToken = ServiceHelper.bindToService(getActivity(),MessageService.class, serviceConnection);
                //getActivity().startService(new Intent(getContext(),BaseService.class));

                //Intent mIntent = new Intent();
               // mIntent.setClass(mContext, MessageService.class);
                //mContext.startService(mIntent);
            }
        });

        //BaseService.baseBinder.getService();

        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarLayout actionBarLayout) {

    }

    @Override
    public void onStop() {
        super.onStop();
        //ServiceHelper.unbindFromService(mToken);
    }
}