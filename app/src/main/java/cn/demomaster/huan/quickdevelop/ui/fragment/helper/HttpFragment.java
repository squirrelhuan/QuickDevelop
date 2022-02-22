package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.net.RetrofitInterface;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.http.HttpUtils;
import cn.demomaster.huan.quickdeveloplibrary.model.Version;
import cn.demomaster.huan.quickdeveloplibrary.socket.RequestListener;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDMessage;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdlogger_library.QDLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static cn.demomaster.huan.quickdeveloplibrary.http.HttpUtils.FROM_DATA;

/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "网络请求", preViewClass = TextView.class, resType = ResType.Custome)
public class HttpFragment extends BaseFragment {

    @BindView(R.id.btn_send_tcp)
    QDButton btn_send_tcp;
    @BindView(R.id.btn_send_connect)
    QDButton btn_send_connect;
    @BindView(R.id.btn_retrofit)
    QDButton btn_retrofit;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_https, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        btn_send_connect.setOnClickListener(v -> new Thread(new Runnable() {
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
        }).start());
        btn_send_tcp.setOnClickListener(v -> QDTcpClient.getInstance().send("你好",null));
        //QDTcpClient.setStateListener();
        btn_retrofit.setOnClickListener(v -> test(""));
    }

    public void test(String filePath){
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), filePath);
        // MultipartBody.Part和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", new File(filePath).getName(), requestFile);
        //Retrofit
        RetrofitInterface retrofitInterface = HttpUtils.getInstance().getRetrofit(RetrofitInterface.class, "http://www.demomaster.cn/");
        String userId = "001";
        retrofitInterface.uploadHeaderImg(userId,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object response) {
                        Gson gson = new Gson();
                        QDLogger.i("onNext: " + gson.toJson(response));
                        try {
                            //JSONObject jsonObject = JSON.parseObject(response.getData().toString());
                            //List doctors1 = JSON.parseArray(response.getData().toString(), DoctorModelApi.class);
                            //String token = jsonObject.get("token").toString();
                            JsonObject jsonObject = new JsonParser().parse(gson.toJson(response)).getAsJsonObject();
                            jsonObject = jsonObject.get("data").getAsJsonObject();
                            Version version = gson.fromJson(gson.toJson(jsonObject), Version.class);
                            int ver_code = version.getVersionCode();
                            String ver_name = version.getVersionName();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onStart() {
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        QDLogger.e(throwable);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}