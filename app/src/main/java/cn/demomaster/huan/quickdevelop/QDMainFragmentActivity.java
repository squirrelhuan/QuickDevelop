package cn.demomaster.huan.quickdevelop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.fragment.main.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ACTIONBAR_TYPE;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ADBHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ProcessResult;

/**
 *
 */
public class QDMainFragmentActivity extends QDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);
        if (savedInstanceState == null) {
            QDFragment fragment = new MainFragment();
            startFragment(mContext, fragment);
        }
        getActionBarLayout().setHeaderBackgroundColor(Color.GRAY);
        //actionBarLayoutView.setHeaderBackgroundColor();
        getActionBarLayout().setActionBarType(ACTIONBAR_TYPE.NORMAL);
        getActionBarLayout().getLeftView().setVisibility(View.GONE);
        //EventBus.getDefault().register(this);
        //changeAppLanguage(mContext);
        initHelper();

        //设置系统时间需要系统权限
       /* long time = 1243567568;
        //同步服务器时间
        SystemClock.setCurrentTimeMillis(time);

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        System.out.println("当前时间：" + simpleDateFormat.format(date));*/

        /*QDRuntimeHelper.getInstance().exeCommand("pwd");
        QDRuntimeHelper.getInstance().exeCommand("cd /");
        QDRuntimeHelper.getInstance().exeCommand("ls", new QDRuntimeHelper.CallBack() {
            @Override
            public void onReceive(String data) {
                QDLogger.d("cdata="+data);
            }
        });*/
        ADBHelper adbHelper = ADBHelper.getInstance();
        adbHelper.execute("pwd");
        adbHelper.execute("ls");
        adbHelper.execute("cd /proc");
        adbHelper.execute("ls", new ADBHelper.OnReceiveListener() {
            @Override
            public void onReceive(ProcessResult result) {
                if(result.getCode()==0){
                    QDLogger.d("result="+result.getResult());
                }else {
                    QDLogger.e("result="+result.getCode()+","+result.getError());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
