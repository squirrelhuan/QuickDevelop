package cn.demomaster.huan.quickdevelop;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.demomaster.huan.quickdevelop.net.RetrofitInterface;
import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.NotifycationHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.http.HttpUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class App extends ApplicationParent {

    @Override
    public void onCreate() {
        super.onCreate();

        NotifycationHelper.getInstance().init(this);

    }
}
