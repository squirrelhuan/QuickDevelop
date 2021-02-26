package cn.demomaster.huan.quickdevelop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.ui.fragment.main.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickpermission_library.PermissionHelper;

/**
 *
 */
public class QDMainFragmentActivity extends QDActivity {

    @Override
    public boolean isUseActionBarLayout() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);
        if (savedInstanceState == null) {
            QDFragment fragment = new MainFragment();
            startFragment(fragment,R.id.container1);
        }
       /* getActionBarTool().setHeaderBackgroundColor(Color.GRAY);
        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NORMAL);
        getActionBarTool().getLeftView().setVisibility(View.GONE);*/

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
        PermissionHelper.getInstance().requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},null);
        QDLogger.d("getAppName="+getAppName(this));
    }
    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            Log.i(TAG, "getAppName=" + context.getApplicationContext().getPackageName());
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
