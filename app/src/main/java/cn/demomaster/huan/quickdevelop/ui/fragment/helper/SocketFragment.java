package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.network.NetworkHelper;
import cn.demomaster.huan.quickdeveloplibrary.quicksocket.QuickTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.quicksocket.QuickTcpServer;
import cn.demomaster.huan.quickdeveloplibrary.quicksocket.ReceiveListener;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.util.QDAndroidDeviceUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Socket", preViewClass = TextView.class, resType = ResType.Custome)
public class SocketFragment extends BaseFragment implements ReceiveListener {

    @BindView(R.id.btn_send_tcp)
    QDButton btn_send_tcp;
    @BindView(R.id.btn_send_connect)
    QDButton btn_send_connect;
    @BindView(R.id.btn_start_server)
    QDButton btn_start_server;
    @BindView(R.id.tv_local_ip)
    TextView tv_local_ip;
    @BindView(R.id.tv_sn)
    TextView tv_sn;
    @BindView(R.id.tv_server_log)
    TextView tv_server_log;
    @BindView(R.id.tv_client_log)
    TextView tv_client_log;
    @BindView(R.id.et_input)
    EditText et_input;
    View mView;

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_socket, null);
        }
        return mView;
    }
    QuickTcpServer qdTcpServer;
    QuickTcpClient qdTcpClient;
    int port = 6032;
    String clientID;
    public void initView(View rootView) {
        ButterKnife.bind(this, mView);
        getActionBarTool().setTitle("socket");

        clientID = QDAndroidDeviceUtil.getUniqueID(getContext());
        tv_sn.setText(clientID);
        String ip = NetworkHelper.getLocalIpAddress(mContext);
        tv_local_ip.setText(ip);
        qdTcpClient = new QuickTcpClient(ip, port);
        qdTcpClient.setReceiveListener(this);
        btn_start_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qdTcpServer==null) {
                    qdTcpServer = new QuickTcpServer(ip,port);//
                    qdTcpServer.setOnReceiveMessageListener(new QuickTcpServer.OnReceiveListener() {
                        @Override
                        public void onClientConnect(Socket socket) {
                            QdThreadHelper.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_server_log.append("客户端连接"+socket+"\n");
                                }
                            });
                        }

                        @Override
                        public void onReceiveMessage(Socket socket, byte[] bytes) {
                            QdThreadHelper.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_server_log.append(new String(bytes)+"\n");
                                }
                            });
                        }
                    });
                }
                qdTcpServer.start();
                //port = qdTcpServer.getPort();
                //qdTcpClient.setPort(port);
                //QdToast.showToast(getContext(),"port:"+port);// new QDTcpServer(port);
            }
        });
        btn_send_connect.setOnClickListener(v -> {
            qdTcpClient.connect();
        });

        btn_send_tcp.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(et_input.getText())) {
                String str =et_input.getText().toString();
                qdTcpClient.sendBytes(str.getBytes());
            }
        });
        //QDTcpClient.setStateListener();
    }

    private void initTCPMessage() {
        QdToast.showToast(getContext(),"连接成功");
        /*QDTcpClient.getInstance().request(JSON.toJSONString(connectInfo), new RequestListener() {
            @Override
            public void onReceived(QDMessage qdMessage, byte[] bytes) {

            }

            @Override
            public void onError(String err) {

            }
        });*/
    }

    @Override
    public void onConnect() {
        initTCPMessage();
    }

    @Override
    public void onReceived(byte[] bytes) {
        tv_client_log.append(new String(bytes)+"\n");
    }

    @Override
    public void onLostConnect(int failCode, String msg) {

    }

   /* public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

    }*/
}