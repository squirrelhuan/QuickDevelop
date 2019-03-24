package cn.demomaster.huan.quickdeveloplibrary.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

public class QDBaseActivity extends QDBaseFragmentActivity {

    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
        mContext.overridePendingTransition(R.anim.translate_from_right_to_left_enter,  R.anim.anim_null);
    }
}
