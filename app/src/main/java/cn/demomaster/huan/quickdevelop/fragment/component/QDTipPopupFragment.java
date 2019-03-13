package cn.demomaster.huan.quickdevelop.fragment.component;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.service.MessageService;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.jni.JNITest;
import cn.demomaster.huan.quickdeveloplibrary.jni.ServiceHelper;
import cn.demomaster.huan.quickdeveloplibrary.jni.ServiceToken;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.popup.QDTipPopup;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "QDTipPopupFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class QDTipPopupFragment extends BaseFragment {
    //Components
    ViewGroup mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_popup_tip, null);
        }
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarLayout actionBarLayout) {
        Button btn_01 = rootView.findViewById(R.id.btn_01);
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"111",Toast.LENGTH_LONG).show();
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage("普通提示窗").create();
                //qdTipPopup.showAsDropDown(v,0,0);
                qdTipPopup.showAsDropDown(v);
            }
        });
    }

}