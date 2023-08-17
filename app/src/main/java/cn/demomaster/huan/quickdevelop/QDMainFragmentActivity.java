package cn.demomaster.huan.quickdevelop;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import cn.demomaster.huan.quickdevelop.ui.fragment.main.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.network.NetworkHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.qdrouter_library.quickview.QuickViewManager;
import cn.demomaster.qdrouter_library.quickview.QuickView;
import cn.demomaster.quickpermission_library.PermissionHelper;

import cn.demomaster.huan.quickdeveloplibrary.util.QuickDevKit;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil.getAppName;

public class QDMainFragmentActivity extends QDActivity {
    
    @Override
    public boolean isUseActionBarLayout() {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);
        if (savedInstanceState == null) {
            QuickFragment fragment = new MainFragment();
            startFragment(fragment,R.id.container1,null);
        }

        //悬浮工具
        QuickDevKit quickDevKit = new QuickDevKit();
        quickDevKit.init(mContext);
        /*ViewInfo viewInfo = new ViewInfo();
        viewInfo.setResId(R.layout.layout_floating_menu);
        viewInfo.setTag("TGA");
        viewInfo.setX(0);
        viewInfo.setY(DisplayUtil.getScreenHeight(mContext)/2);
        int width = DisplayUtil.dip2px(mContext,50);
        FrameLayout.LayoutParams marginLayoutParams = new FrameLayout.LayoutParams(width,width);
        marginLayoutParams.topMargin = DisplayUtil.getScreenHeight(mContext)/2;
        marginLayoutParams.leftMargin = 0;
        viewInfo.setLayoutParams(marginLayoutParams);
        QuickViewManager.getInstance().setViewInfo(mContext,viewInfo);*/

        //EventBus.getDefault().register(this);
        //changeAppLanguage(mContext);

        //设置系统时间需要系统权限
       /* long time = 1243567568;
        //同步服务器时间
        SystemClock.setCurrentTimeMillis(time);

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
       QDLogger.println("当前时间：" + simpleDateFormat.format(date));*/

        /*QDRuntimeHelper.getInstance().exeCommand("pwd");
        QDRuntimeHelper.getInstance().exeCommand("cd /");
        QDRuntimeHelper.getInstance().exeCommand("ls", new QDRuntimeHelper.CallBack() {
            @Override
            public void onReceive(String data) {
                QDLogger.d("cdata="+data);
            }
        });*/
        /*ADBHelper adbHelper = ADBHelper.getInstance();
        adbHelper.execute("pwd");
        adbHelper.execute("ls");
        adbHelper.execute("cd /proc");
        adbHelper.execute("ls", new ADBHelper.OnReceiveListener() {
            @Override
            public void onReceive(ProcessResult result) {
                if(result.getCode()==0){
                    //QDLogger.d("result="+result.getResult());
                }else {
                    QDLogger.e("result="+result.getCode()+","+result.getError());
                }
            }
        });*/

        /*ClipboardUtil.setClip(mContext, Build.BRAND.toLowerCase());
        CharSequence str = ClipboardUtil.getClip(mContext);
        if(!TextUtils.isEmpty(str)) {
            QdToast.show(mContext, str.toString());
        }
*/
        //QdToast.show(mContext, Build.BRAND.toLowerCase());
        //DebugFloatingService.showConsole(mContext);
        PermissionHelper.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},null);
        QDLogger.i("AppName:"+getAppName(this));
        String ip = null;
        if(NetworkHelper.isNetworkConnected(mContext)){
            ip = NetworkHelper.getLocalIpAddress(mContext);
            QdToast.showToast(mContext,"ip:"+ip);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);//屏蔽转入转出动画
    }

    //当指定了android:configChanges="orientation"后,方向改变时onConfigurationChanged被调用,并且activity不再销毁重建
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                Log.i(TAG,"竖屏");
                break;
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                Log.i(TAG,"横屏");
            default:
                break;
        }
    }
}
