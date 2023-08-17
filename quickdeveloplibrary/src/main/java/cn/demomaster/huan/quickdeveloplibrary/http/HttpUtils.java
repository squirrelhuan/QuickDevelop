package cn.demomaster.huan.quickdeveloplibrary.http;


import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        this.loggingInterceptor = loggingInterceptor;
        init();
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
        init();
    }

    private void init() {
        clientBuilder = new OkHttpClient.Builder().
                connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if(loggingInterceptor!=null) {
            clientBuilder.addInterceptor(loggingInterceptor);
        }
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







    public static class HttpTask extends AsyncTask {
        public String url;
        Map<String, String> params;
        HpptCallback callback;

        HttpTask(HpptCallback callback) {
            this.callback = callback;
            //this.webResourceFilter = webResourceFilter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object[] objects) {
            //String searchContent = objects[0].toString();
            url = (String) objects[0];
            params = (Map<String, String>) objects[1];
            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;
            try {
                // 创建URL对象
                URL getUrl = new URL(url);
                // 打开连接
                connection = (HttpURLConnection) getUrl.openConnection();
                // 设置请求方法为GET
                connection.setRequestMethod("GET");
                // 设置是否向HttpURLConnection输出，因为这个是输入流，所以必须设为true,默认情况下是false;
                connection.setDoOutput(true);
                // 设置是否从HttpURLConnection读入，默认情况下是true;
                connection.setDoInput(true);
                // 设置是否使用缓存，默认情况是false;
                connection.setUseCaches(false);
                // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向。默认情况下是 true。
                connection.setInstanceFollowRedirects(true);
                // 连接
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> headers = connection.getHeaderFields();
                // 如果有返回值，则打印出来
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                } else {
                    System.out.println("请求失败，错误码：" + connection.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                if(this.callback!=null) {
                    this.callback.onFailure(e);
                    this.callback=null;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result.toString();
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            QDLogger.println("onProgressUpdate");
            // 可以在此方法内更新操作进度 需要调用publishProgress 方法才会执行
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(this.callback!=null) {
                this.callback.onResponse((String) o);
                this.callback=null;
            }
            url = null;
            params = null;
        }
    }

    public interface HpptCallback {
        void onFailure(Exception e);
        void onResponse(String response);
    }
    public static void sendGetRequest(String url, Map<String, String> params,HpptCallback callback) {
        HttpTask httpTask = new HttpTask(callback);
        httpTask.execute(url,params);
    }


}
