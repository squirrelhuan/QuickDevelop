package cn.demomaster.huan.quickdevelop;

//import com.umeng.commonsdk.UMConfigure;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.NotifycationHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;
//import com.umeng.socialize.PlatformConfig;


public class App extends ApplicationParent {

    @Override
    public void onCreate() {
        super.onCreate();

        NotifycationHelper.getInstance().init(this);
        //SoundHelper.init(this);
        SoundHelper.init(this, true, R.raw.class);//自动加载raw下的音频文件


        //初始化友盟分享
        initUmengShare("5c79138f61f564e0380012fa");

    }
}
