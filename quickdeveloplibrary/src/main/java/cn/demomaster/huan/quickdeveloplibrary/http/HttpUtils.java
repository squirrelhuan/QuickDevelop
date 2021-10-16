package cn.demomaster.huan.quickdeveloplibrary.http;


import android.text.TextUtils;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.demomaster.qdlogger_library.QDLogger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http工具类
 */
public class HttpUtils {
    public static String TAG = HttpUtils.class.getSimpleName();
    private static final HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
    //新建log拦截器
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
        if (!TextUtils.isEmpty(message)) {
            QDLogger.i(TAG, message);
        }
    }).setLevel(level);
    
    public void setLoggingInterceptor(HttpLoggingInterceptor loggingInterceptor) {
        HttpUtils.loggingInterceptor = loggingInterceptor;
    }
    
    /* static {
        if (BuildConfig.DEBUG) {
            //日志拦截器
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        QDLogger.i(TAG, message);
                    }
                }
            });
            loggingInterceptor.setLevel(level);
        }
    }*/
    
    private static final int DEFAULT_TIMEOUT = 30; //连接 超时的时间，单位：秒
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).addInterceptor(loggingInterceptor).
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
            writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();

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

    private static String baseUrl = "";
    private static Class clazz;
    public static void setBaseUrl(String baseUrl) {
        HttpUtils.baseUrl = baseUrl;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        if (clazz != null) {
            retrofitInterface = retrofit.create(clazz);
        }
    }

    public synchronized <T> T getRetrofit(Class<T> targetClazz) {
        clazz = targetClazz;
        //初始化retrofit的配置
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        retrofitInterface = retrofit.create(targetClazz);
        return (T) retrofitInterface;
    }

    public synchronized <T> T getRetrofit(Class<T> targetClazz, String baseUrl) {
        HttpUtils.baseUrl = baseUrl;
        clazz = targetClazz;
        //初始化retrofit的配置
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        retrofitInterface = retrofit.create(targetClazz);
        return (T) retrofitInterface;
    }

    /**
     * 使用了String 转换器，将以文本的方式输出
     *
     * @param targetClazz
     * @param baseUrl
     * @param <T>
     * @return
     */
    public synchronized <T> T createRetrofit(Class<T> targetClazz, String baseUrl) {
        HttpUtils.baseUrl = baseUrl;
        clazz = targetClazz;
        //初始化retrofit的配置
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(new ToStringConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(targetClazz);
    }

    private static HttpUtils instance;

    public static HttpUtils getInstance() {
        if (instance == null) {
            instance = new HttpUtils();
        }
        return instance;
    }

    public static final MediaType FROM_DATA = MediaType.parse("multipart/form-data");

    public void uploadFile(String name, String url, String filePath, Callback callback) {
        File file = new File(filePath);
        if (file.exists()) {
            uploadFile(name, file.getName(), url, filePath, callback);
        }
    }

    /**
     * 文件上传
     *
     * @param name
     * @param fileName
     * @param url
     * @param filePath
     * @param callback
     */
    public void uploadFile(String name, String fileName, String url, String filePath, Callback callback) {
        File file = new File(filePath);
        if (file.exists()) {
            new Thread(() -> {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),
                        new File(filePath));
                MultipartBody body = new MultipartBody.Builder()
                        .setType(FROM_DATA)
                        .addFormDataPart("key1", "1")
                        .addFormDataPart("key2", "2")
                        .addFormDataPart(name, fileName, fileBody)
                        .build();
                Request request = new Request.Builder()
                        .post(body)
                        .url(url)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(callback);
            }).start();
        }
    }




    public static  SSLSocketFactory createSSLSocketFactory(){
        SSLSocketFactory ssfFactory=null;
        try{
            SSLContext sc=SSLContext.getInstance("TLS");
            //SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null,new TrustManager[]{trustManager},new SecureRandom());
            ssfFactory=sc.getSocketFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ssfFactory;
    }
    
   public static final X509TrustManager trustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
}
