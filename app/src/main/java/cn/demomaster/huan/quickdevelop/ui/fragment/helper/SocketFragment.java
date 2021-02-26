package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

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
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.socket.MessageReceiveListener;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Socket", preViewClass = TextView.class, resType = ResType.Custome)
public class SocketFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @BindView(R.id.btn_send_tcp)
    QDButton btn_send_tcp;
    @BindView(R.id.btn_send_connect)
    QDButton btn_send_connect;
    View mView;

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_socket, null);
        }
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, mView);
        getActionBarTool().setTitle("socket");
        btn_send_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTcpClient.getInstance().connect("118.25.63.138", 10101);
                //QDTcpClient.getInstance().connect("10.0.2.2",10101);
            }
        });
        btn_send_tcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTcpClient.getInstance().send("你好", new MessageReceiveListener() {
                    @Override
                    public void onReceived(QDMessage qdMessage) {

                    }

                    @Override
                    public void onError(String err) {

                    }
                });
            }
        });
        //QDTcpClient.setStateListener();
    }

   /* public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

    }*/
}