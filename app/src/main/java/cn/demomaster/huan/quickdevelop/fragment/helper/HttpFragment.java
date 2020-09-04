package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.socket.MessageReceiveListener;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdlogger_library.QDLogger;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "https", preViewClass = TextView.class, resType = ResType.Custome)
public class HttpFragment extends QDFragment {

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
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_https, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    private static final MediaType FROM_DATA = MediaType.parse("multipart/form-data");

    @Override
    public void initView(View rootView, ActionBar actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("socket");
        btn_send_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "https://web.baekteori.com/mange-web/login/loginUser";
                        url = "https://web.baekteori.com/mange-web/log/upload";
                        OkHttpClient okHttpClient = new OkHttpClient();

                        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(Environment.getExternalStorageDirectory(), "download/" + "0812-0813ota.txt"));
                        MultipartBody body = new MultipartBody.Builder()
                                .setType(FROM_DATA)
                                .addFormDataPart("file", "abc", fileBody)
                                .build();
                        Request request = new Request.Builder()
                                .post(body)
                                .url(url)
                                .build();
                        try {
                            QDLogger.d(okHttpClient.newCall(request).execute().body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       /* Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("tag", "onFailure: ");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("tag", "onResponse: " + response.body().string());
                            }
                        });*/
                    }
                }).start();
            }});
        btn_send_tcp.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
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

       /* public void initActionBarLayout (ActionBarLayout2 actionBarLayoutOld){
            int i = (int) (Math.random() * 10 % 4);
            actionBarLayoutOld.setTitle("audio play");
            actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

        }*/
    }