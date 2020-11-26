package cn.demomaster.huan.quickdevelop.net;

import cn.demomaster.huan.quickdeveloplibrary.http.URLConstant;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Squirrel桓 on 2019/1/1.
 */
public interface RetrofitInterface {

    //get请求
    @GET(URLConstant.URL_BASE)
    Observable<Object> getSession();

    //get带参数请求
    @GET(URLConstant.URL_BASE)
    Observable<Object> getWithParam(@Query("context") String context);

    //get请求
    @GET(URLConstant.URL_UPDATE_APP)
    Observable<Object> getVersion();

    @FormUrlEncoded
    @POST(URLConstant.URL_BASE)
    Observable<Object> getPostRequest();

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST(URLConstant.URL_BASE)
    Observable<Object> getUserInfo(@Body RequestBody body);
}
