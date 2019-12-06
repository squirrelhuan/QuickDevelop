package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnDoubleClickListener;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnMultClickListener;
import cn.demomaster.huan.quickdeveloplibrary.socket.MessageReceiveListener;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Socket",preViewClass = StateView.class,resType = ResType.Custome)
public class SocketFragment extends QDBaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_send_tcp)
    QDButton btn_send_tcp;
    @BindView(R.id.btn_send_connect)
    QDButton btn_send_connect;

    View mView;
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_socket, null);
        }
        ButterKnife.bind(this,mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("socket");
        btn_send_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTcpClient.getInstance().connect("118.25.63.138",10101);
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

    public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

    }
}