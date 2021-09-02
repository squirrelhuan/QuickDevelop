package cn.demomaster.huan.quickdevelop.ui.activity.sample.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import cn.demomaster.huan.quickdevelop.ui.fragment.component.AppletsFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.actionbar.ACTIONBAR_TYPE;

@ActivityPager(name = "小程序",preViewClass = TextView.class,resType = ResType.Custome)
public class AppletsFragmentActivity extends QDActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = new FrameLayout(this);
        view.setId(View.generateViewId());
        setContentView(view);
        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        getFragmentHelper().build(this,AppletsFragment.class.getName())
                .setContainerViewId(view.getId()).putExtras(new Bundle())
                .putExtra("password", 666666)
                .putExtra("name", "小三").navigation();
    }
/*
    public int getContentViewId(){
       return R.id.qd_fragment_content_view;
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.println("keyCode="+keyCode);
        return super.onKeyDown(keyCode, event);
    }
}
