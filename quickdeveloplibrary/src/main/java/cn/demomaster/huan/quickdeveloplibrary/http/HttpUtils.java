package cn.demomaster.huan.quickdeveloplibrary.http;


import android.text.TextUtils;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.demomaster.qdlogger_library.QDLogger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.TlsVersion;
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
    private static OkHttpClient client;

    HttpUtils(){
        clientBuilder = new OkHttpClient.Builder().
                connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor).
                readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //client = clientBuilder.build();
        client = getUnsafeOkHttpClientBuilder().build();
    }

    static OkHttpClient.Builder clientBuilder;
    public static OkHttpClient.Builder getUnsafeOkHttpClientBuilder() {
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        // Https 异常：javax.net.ssl.SSLHandshakeException: Handshake failed
        // Caused by: javax.net.ssl.SSLProtocolException: SSL handshake aborted: ssl=0x7a59e45208: Failure in SSL library, usually a protocol error
        // 解决在Android5.0版本以下https无法访问（亲测5.0以上版本也报同样的错误，猜测应该通过服务器配置协议兼容可以解决，目前是Android端自己做了兼容）
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(CipherSuite.TLS_AES_128_GCM_SHA256,
                        CipherSuite.TLS_AES_256_GCM_SHA384,
                        CipherSuite.TLS_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256)
                .allEnabledCipherSuites()
                .build();

        // 兼容http接口
        ConnectionSpec spec1 = new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build();
        clientBuilder.connectionSpecs(Arrays.asList(spec, spec1));
        //clientBuilder.sslSocketFactory(createSSLSocketFactory(), trustManager)
        //设置https证书
        clientBuilder.sslSocketFactory(createSSLSocketFactory(trustManager), trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
        return clientBuilder;
    }

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

    public static  SSLSocketFactory createSSLSocketFactory(X509TrustManager trustManager){
        SSLSocketFactory ssfFactory=null;
        try{
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //SSLContext sc = SSLContext.getInstance("SSL");
            sslContext.init(null,new TrustManager[]{trustManager},new SecureRandom());
            //sslContext.init(null, new X509TrustManager[]{trustManager}, new SecureRandom());
            ssfFactory=sslContext.getSocketFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ssfFactory;
    }
}
