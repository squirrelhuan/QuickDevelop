package cn.demomaster.huan.quickdevelop.ui.activity.sample.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.ui.fragment.component.RouterFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.RouterPaper;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.actionbar.ACTIONBAR_TYPE;
import cn.demomaster.qdrouter_library.paper.PaperManager;

@ActivityPager(name = "BasePaper",preViewClass = TextView.class,resType = ResType.Custome)
public class BasePaperActivity extends QDActivity {
    PaperManager myPayerManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* View view = new FrameLayout(this);
        view.setId(getContentViewId());*/
        setContentView(null);
        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        QDLogger.i("BasePaperActivity onCreate");

        myPayerManager = new PaperManager(this,android.R.id.content);
        myPayerManager.addElement(new RouterPaper());
        RouterFragment f1 = new RouterFragment();
        startFragment(f1,getContentViewId(),null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //FragmentActivityHelper.getInstance().unBindActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.i("BasePaperActivity onKeyDown"+keyCode);
        if(myPayerManager.onKeyDown(this,keyCode,event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
