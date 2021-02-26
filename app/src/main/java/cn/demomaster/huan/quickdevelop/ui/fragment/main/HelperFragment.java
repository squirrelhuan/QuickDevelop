package cn.demomaster.huan.quickdevelop.ui.fragment.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.CsqliteActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.IDCardActivity;
import cn.demomaster.huan.quickdevelop.adapter.ComponentAdapter;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.AboutMobileFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.AccessibilityServiceFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.AdbForwardFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.BluetoothFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.DeviceFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.DownloadFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.DragViewFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.ErrorTestFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.ExeCommandFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.FileManagerFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.FloatingFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.HttpFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.Keyboard2Fragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.Keyboard3Fragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.KeyboardFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.LanguageFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.LmSensorFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.NotifycationFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.PermitionFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.PositionFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.ScreenShotFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.SettingFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.SocketFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.TrafficFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.UpdateAppFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.WifiFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.serialport.sample.SerialportMain;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;

/**
 * Components视图
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(iconRes = R.mipmap.quickdevelop_ic_launcher)
public class HelperFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ComponentAdapter componentAdapter;

    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public boolean isUseActionBarLayout() {
        return false;//不带导航栏
    }

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_component, null);
        return view;
    }

    public void initView(View rootView) {
        /*actionBarLayout.setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        //actionBarLayout.getLeftView().setVisibility(View.GONE);
        actionBarLayout.setHeaderBackgroundColor(Color.RED);*/
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        componentAdapter = new ComponentAdapter(getContext());
        List<Class> classList = new ArrayList<>();

        classList.add(IDCardActivity.class);
        classList.add(CsqliteActivity.class);
        classList.add(LmSensorFragment.class);
        classList.add(ScreenShotFragment.class);
        classList.add(ErrorTestFragment.class);
        classList.add(FileManagerFragment.class);
        classList.add(LanguageFragment.class);
        classList.add(KeyboardFragment.class);
        classList.add(Keyboard2Fragment.class);
        classList.add(Keyboard3Fragment.class);

        classList.add(DragViewFragment.class);
        classList.add(SerialportMain.class);
        classList.add(DeviceFragment.class);
        classList.add(DownloadFragment.class);
        classList.add(UpdateAppFragment.class);
        classList.add(PermitionFragment.class);
        classList.add(PositionFragment.class);
        classList.add(ExeCommandFragment.class);
        classList.add(AdbForwardFragment.class);
        classList.add(WifiFragment.class);
        classList.add(BluetoothFragment.class);
        classList.add(FloatingFragment.class);
        classList.add(SocketFragment.class);
        classList.add(TrafficFragment.class);
        classList.add(HttpFragment.class);
        classList.add(SettingFragment.class);
        classList.add(AccessibilityServiceFragment.class);
        classList.add(NotifycationFragment.class);
        classList.add(AboutMobileFragment.class);


        //classList.add(QDTerminalFragment.class);

        componentAdapter.updateList(classList);
        //设置Adapter
        recyclerView.setAdapter(componentAdapter);
        //设置分隔线
        //recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
        //设置行级分割线使用的divider
        //recyclerView.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getContext(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

        int spanCount = 3;
        //使用网格布局展示
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        //设置分隔线
        recyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), spanCount));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}