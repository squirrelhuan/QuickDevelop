package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "WheelImage", preViewClass = TextView.class, resType = ResType.Custome)
public class WheelImageFragment extends QDBaseFragment {
    //Components
    ViewGroup mView;
    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //QDLogger.d("拦截Activity:"+getClass().getName() + "返回事件");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_wheelimage, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";

        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {

    }

}