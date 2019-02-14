package cn.demomaster.huan.quickdevelop;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.NotifycationHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;

public class App extends ApplicationParent {

    @Override
    public void onCreate() {
        super.onCreate();

        NotifycationHelper.getInstance().init(this);
        //SoundHelper.init(this);
        SoundHelper.init(this, true, R.raw.class);//自动加载raw下的音频文件

    }
}
