package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author squirrel桓
 * @date 2019/1/10.
 * description：
 */
public interface ActionBarLayoutInterface {
    void onBack(AppCompatActivity activity );

    void onKeyDown(int eventCode, KeyEvent down);
}
