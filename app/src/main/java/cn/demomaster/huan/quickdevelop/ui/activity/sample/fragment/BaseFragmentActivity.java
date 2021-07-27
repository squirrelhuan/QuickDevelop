package cn.demomaster.huan.quickdevelop.ui.activity.sample.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.ui.fragment.component.AppletsFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.RouterFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.actionbar.ACTIONBAR_TYPE;

@ActivityPager(name = "BaseFragment",preViewClass = TextView.class,resType = ResType.Custome)
public class BaseFragmentActivity extends QDActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*View view = new FrameLayout(this);
        view.setId(getContentViewId());*/
        setContentView(null);
        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        getFragmentHelper().build(this, RouterFragment.class.getName())
                .setContainerViewId(getContentViewId()).putExtras(new Bundle())
                .putExtra("password", 666666)
                .putExtra("name", "小三").navigation();

    }
/*
    public int getContentViewId(){
       return R.id.qd_fragment_content_view;
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        //FragmentActivityHelper.getInstance().unBindActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.i("keyCode="+keyCode);
        return super.onKeyDown(keyCode, event);
    }
}
