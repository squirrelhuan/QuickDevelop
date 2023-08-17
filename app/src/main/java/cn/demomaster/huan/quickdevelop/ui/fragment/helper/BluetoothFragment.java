package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QuickToggleButton;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2021/01/14
 */
@ActivityPager(name = "蓝牙", preViewClass = TextView.class, resType = ResType.Resource, iconRes = R.drawable.ic_baseline_bluetooth_24)
public class BluetoothFragment extends QuickFragment {

    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.tooglebutton)
    QuickToggleButton tooglebutton;

    @BindView(R.id.cb_discoverable)
    CheckBox cb_discoverable;

    @BindView(R.id.btn_search)
    Button btn_search;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    View mView;

    cn.demomaster.huan.quickdevelop.adapter.BluetoothAdapter bluetoothAdapter;

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_bluetooth, null);
        }
        return mView;
    }

    BluetoothAdapter mBluetoothAdapter;
    int REQUEST_ENBLE_BT = 11223;

    public void initView(View rootView) {
        ButterKnife.bind(this, mView);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // 此设备不支持蓝牙操作
            tv_info.setText("此设备不支持蓝牙操作");
            return;
        }

        tooglebutton.setChecked(mBluetoothAdapter.isEnabled());
        tooglebutton.setOnToggleChanged((view, on) -> {
            if (!mBluetoothAdapter.isEnabled()) {// 没有开始蓝牙
                // Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // startActivityForResult(enableBtIntent,REQUEST_ENBLE_BT);
                mBluetoothAdapter.enable();
            } else {
                mBluetoothAdapter.disable();
            }
        });


        cb_discoverable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                requestDiscoverable(300);
            }
        });

        registerBoradcastReceiver();
        //QDLogger.i("v0="+v0+",v1="+v1);

        btn_search.setOnClickListener(v -> mBluetoothAdapter.startDiscovery());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        bluetoothAdapter = new cn.demomaster.huan.quickdevelop.adapter.BluetoothAdapter(mContext);
        bluetoothAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connect(bluetoothAdapter.getData().get(position));
            }
        });
        bluetoothAdapter.updateList(mBluetoothAdapter.getBondedDevices());
        recyclerView.setAdapter(bluetoothAdapter);
        //设置分隔线
        //recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
        //设置行级分割线使用的divider
        //recyclerView.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getContext(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

        int spanCount = 1;
        //recyclerView.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        recyclerView.setLayoutManager(layoutManager);
        //设置分隔线
        recyclerView.addItemDecoration(new GridDividerItemDecoration(mContext, spanCount));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void connect(BluetoothDevice bluetoothDevice) {
        AcceptThread acceptThread = new AcceptThread();
        acceptThread.start();
    }

    int REQUEST_DISCOVERABLE = 2054;

    /**
     * 启用被检测
     *
     * @param time 时长
     */
    private void requestDiscoverable(int time) {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time);//最多可以设置3600秒，如果将值设置为0就表示设备永远进入可被发现模式。任何小于0或者大于3600的值都会自动设置为120秒
        ((Activity) getContext()).startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);//REQUEST_DISCOVERABLE为自定义的requestcode值
    }

    BluetoothStateReceiver stateChangeReceiver = new BluetoothStateReceiver();

    private void registerBoradcastReceiver() {
        IntentFilter stateChangeFilter = new IntentFilter(
                BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter connectedFilter = new IntentFilter(
                BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter disConnectedFilter = new IntentFilter(
                BluetoothDevice.ACTION_ACL_DISCONNECTED);
        IntentFilter foundFilter = new IntentFilter(
                BluetoothDevice.ACTION_FOUND);

        IntentFilter scanModeChangedFilter = new IntentFilter(
                BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);

        getContext().registerReceiver(stateChangeReceiver, scanModeChangedFilter);
        getContext().registerReceiver(stateChangeReceiver, stateChangeFilter);
        getContext().registerReceiver(stateChangeReceiver, connectedFilter);
        getContext().registerReceiver(stateChangeReceiver, disConnectedFilter);
        getContext().registerReceiver(new BluetoothDeviceScanListener(), foundFilter);
    }

    public class BluetoothStateReceiver extends BroadcastReceiver {

        public BluetoothStateReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //QDLogger.i("my2", "收到蓝牙状态变化:"+action);
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        QDLogger.i("收到蓝牙 STATE_TURNING_ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        QDLogger.i("收到蓝牙 STATE_ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        QDLogger.i("收到蓝牙 STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        QDLogger.i("收到蓝牙 STATE_OFF");
                        break;
                }
            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);//之前的状态使用EXTRA_PREVIOUS_SCAN_MODE获取
                switch (state) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE://可检测到模式
                        cb_discoverable.setChecked(true);
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE://未处于可检测模式但可以接受连接

                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE://未处于可检测到模式并且无法连接
                        cb_discoverable.setChecked(false);
                        break;
                }
            }

            refreshState();
        }
    }

    /**
     * 蓝牙状态变更时更新ui
     */
    private void refreshState() {
        int state = mBluetoothAdapter.getState();
        switch (state) {
            case BluetoothAdapter.STATE_ON:
                bluetoothAdapter.updateList(mBluetoothAdapter.getBondedDevices());
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
            case BluetoothAdapter.STATE_TURNING_OFF:
            case BluetoothAdapter.STATE_OFF:
                break;
        }
    }

    private class BluetoothDeviceScanListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // 这里可以把我们的将我们的设备添加到一个列表中
            System.out.println("发现新设备：" + device.getName() + " : " + device.getAddress());
            bluetoothAdapter.updateList(mBluetoothAdapter.getBondedDevices());
        }
    }

    /**
     * 设置可被搜索
     */
    public void discoverable() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        // 这个可以用来设置时间
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
        ((Activity) getContext()).startActivityForResult(intent, 2);
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket mServerSocket;

        public AcceptThread() {
            UUID uuid = UUID.randomUUID();
            try {
                mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(getContext().getPackageName(), uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (true) {
                try {
                    BluetoothSocket socket = mServerSocket.accept();
                    if (socket != null) {
                        manageConnectedSocket(socket);
                        mServerSocket.close();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancle() {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        QdToast.show(getContext(), "连接成功");
    }
}