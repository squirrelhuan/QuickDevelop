package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.graphics.Color;
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
import cn.demomaster.huan.quickdevelop.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.socket.MessageReceiveListener;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.util.NetworkUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "关于手机", preViewClass = TextView.class, resType = ResType.Custome)
public class AboutMobileFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @BindView(R.id.btn_send_tcp)
    QDButton btn_send_tcp;
    @BindView(R.id.tv_ip)
    TextView tv_ip;

    View mView;

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_about_mobile, null);
        }
        return (ViewGroup) mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, mView);

        if(NetworkUtil.isNetworkConnected(getContext())){
            String ip = NetworkUtil.getLocalIpAddress(getContext());
            tv_ip.setText("ip:"+ip);
        }
        btn_send_tcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

}