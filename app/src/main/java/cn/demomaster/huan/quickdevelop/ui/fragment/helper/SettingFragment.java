package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "设置", preViewClass = TextView.class, resType = ResType.Custome)
public class SettingFragment extends BaseFragment {

    //Components
    @BindView(R.id.btn_send_tcp)
    QDButton btn_send_tcp;
    @BindView(R.id.btn_send_connect)
    QDButton btn_send_connect;
    View mView;
    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_setting, null);
        }
        ButterKnife.bind(this, mView);
        return mView;
    }

    //同步模式改变系统发送的广播
    private static final String SYNC_CONN_STATUS_CHANGED = "com.android.sync.SYNC_CONN_STATUS_CHANGED";
    private TestChange mTestChange;
    private IntentFilter mIntentFilter;
    public void initView(View rootView) {
        getActionBarTool().setTitle("setting");

        mTestChange = new TestChange();
        mIntentFilter = new IntentFilter();
        //添加广播接收器过滤的广播
        mIntentFilter.addAction("com.android.sync.SYNC_CONN_STATUS_CHANGED");
//注册广播接收器
        getContext().registerReceiver(mTestChange, mIntentFilter);
        btn_send_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSyncStatus(!getSyncStatus(mContext));
            }
        });
        btn_send_tcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAutoTime(true);
            }
        });
        //QDTcpClient.setStateListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除广播接收器
        getContext().unregisterReceiver(mTestChange);
    }

    private boolean getAutoState(String name) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.Global.getInt(getContext().getContentResolver(), name) > 0;
            }
            return false;
        } catch (Settings.SettingNotFoundException snfe) {
            return false;
        }
    }

    public void setAutoTime(boolean autoEnabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                Settings.Global.putInt(getContext().getContentResolver(), Settings.Global.AUTO_TIME,
                        autoEnabled ? 1 : 0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean getSyncStatus(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getBackgroundDataSetting() && ContentResolver.getMasterSyncAutomatically();
    }

    private void setSyncStatus(boolean enbled) {
        /*getMasterSyncAutomatically和setMasterSyncAutomatically为抽象类ContentResolver的静态函数，
         * 所以可以直接通过类来调用
         */
        ContentResolver.setMasterSyncAutomatically(enbled);
    }

    private class TestChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (SYNC_CONN_STATUS_CHANGED.equals(action)) {
                Toast.makeText(mContext, "同步模式设置有改变", Toast.LENGTH_SHORT).show();
            }
        }
    }
}