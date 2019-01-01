package cn.demomaster.huan.quickdevelop.net;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.*;

import cn.demomaster.huan.quickdeveloplibrary.http.URLConstant;

/**
 * Created by Squirrel桓 on 2019/1/1.
 */
public interface RetrofitInterface {

    //获取session
    @GET(URLConstant.URL_BASE)
    Observable<Object> getSession();
}
