package cn.demomaster.huan.quickdeveloplibrary.http;


import android.util.Log;


import java.util.concurrent.TimeUnit;

import cn.demomaster.huan.quickdeveloplibrary.BuildConfig;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpUtils {

    private static HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
    //新建log拦截器
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.i("CGQ", "OkHttpMessage:" + message);
        }
    }).setLevel(level);

    static {
        if (BuildConfig.DEBUG) {
            //日志拦截器
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.i("CGQ", "OkHttpMessage:" + message);
                }
            });
            loggingInterceptor.setLevel(level);
        }
    }

    private static final int DEFAULT_TIMEOUT = 8; //连接 超时的时间，单位：秒
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).addInterceptor(loggingInterceptor).
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
            writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();

    private static HttpUtils httpUtils;
    private static Retrofit retrofit;
    private static Object retrofitInterface;
    /*private synchronized static Object getRetrofit() {
        //初始化retrofit的配置
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLConstant.URL_BASE)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            retrofitInterface = retrofit.create(RetrofitInterfaceParent.class);
        }
        return retrofitInterface;
    }*/

    public synchronized static <T> T getRetrofit(Class<T> clazz) {
        //初始化retrofit的配置
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLConstant.URL_BASE)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        retrofitInterface = retrofit.create(clazz);
        return (T) retrofitInterface;
    }

    private static HttpUtils instance;
    public static HttpUtils getInstance() {
        if (instance == null) {
            instance = new HttpUtils();
        }
        return instance;
    }
}