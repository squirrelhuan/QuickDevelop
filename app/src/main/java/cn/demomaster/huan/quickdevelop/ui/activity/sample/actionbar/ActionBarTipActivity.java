package cn.demomaster.huan.quickdevelop.ui.activity.sample.actionbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.EmoticonView;
import cn.demomaster.qdrouter_library.actionbar.ACTIONBARTIP_TYPE;
import cn.demomaster.qdrouter_library.actionbar.ActionBarState;
import cn.demomaster.qdrouter_library.actionbar.ActionBarTipLayout;
import cn.demomaster.qdrouter_library.actionbar.LoadStateType;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

@ActivityPager(name = "ActionBarTip", preViewClass = TextView.class, resType = ResType.Custome)
public class ActionBarTipActivity extends QuickFragment implements View.OnClickListener {

    @BindView(R.id.ev_emtion)
    EmoticonView emoticonView;
    @BindView(R.id.btn_emui_halo)
    Button btn_emui_halo;
    @BindView(R.id.btn_emui_sad)
    Button btn_emui_sad;
    @BindView(R.id.btn_emui_sluggish)
    Button btn_emui_sluggish;
    @BindView(R.id.btn_emui_smile)
    Button btn_emui_smile;
    ActionBarTipLayout actionBarTipLayout;


    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_action_bar_tip, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this,mContext);
        getActionBarTool().getActionBarLayout().getActionBarView().setBackgroundColor(Color.RED);

        btn_emui_halo.setOnClickListener(v -> emoticonView.setStateType(LoadStateType.LOADING));
        btn_emui_sad.setOnClickListener(v -> emoticonView.setStateType(LoadStateType.ERROR));
        btn_emui_sluggish.setOnClickListener(v -> emoticonView.setStateType(LoadStateType.WARNING));
        btn_emui_smile.setOnClickListener(v -> emoticonView.setStateType(LoadStateType.COMPLETE));

        Button btn_complete = findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(this);
        Button btn_loading = findViewById(R.id.btn_loading);
        btn_loading.setOnClickListener(this);
        Button btn_warning = findViewById(R.id.btn_warning);
        btn_warning.setOnClickListener(this);
        Button btn_error = findViewById(R.id.btn_error);
        btn_error.setOnClickListener(this);
        CheckBox cb_001 = findViewById(R.id.cb_001);
        cb_001.setOnClickListener(this);
        cb_001.setOnCheckedChangeListener((buttonView, isChecked) -> getActionBarTool().setActionBarTipType(isChecked ? ACTIONBARTIP_TYPE.NORMAL : ACTIONBARTIP_TYPE.STACK));

        getActionBarTool().setHeaderBackgroundColor(Color.TRANSPARENT);
        actionBarTipLayout =new ActionBarTipLayout(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBarTipLayout.setZ(10.f);
        }
        actionBarTipLayout.setActionBarHeight(getActionBarTool().getActionBarLayout().getActionBarPositionY());
        actionBarTipLayout.post(new Runnable() {
            @Override
            public void run() {
                actionBarTipLayout.setActionBarHeight(getActionBarTool().getActionBarLayout().getActionBarPositionY());
            }
        });
        actionBarTipLayout.setLoadingStateListener(new ActionBarState.OnLoadingStateListener() {
            @Override
            public void onLoading(final ActionBarState.Loading loading) {
                //TODO 处理状态
                loading.setText("处理状态");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //loading.hide();
                loading.success("加载success");
            }
        });
        actionBarTipLayout.setContentView(R.layout.qd_activity_actionbar_tip);
        getActionBarTool().getActionBarLayout().addView(actionBarTipLayout);

        /*btn_complete.setOnClickListener(view -> getActionBarTool().getActionBarTip().showComplete("完成"));
        btn_loading.setOnClickListener(view -> getActionBarTool().getActionBarTip().showLoading("加载"));
        btn_warning.setOnClickListener(view -> getActionBarTool().getActionBarTip().showWarning("警告"));
        btn_error.setOnClickListener(view -> getActionBarTool().getActionBarTip().showError("错误"));*/

        SeekBar sb_background = findViewById(R.id.sb_background);
        sb_background.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 0xff000000 + 0xffffff * progress / 100;
                //int w = DisplayUtil.dip2px(mContext,max* progress/100);
                //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w,w);
                //getActionBarTool().getActionBarTip().setBackgroundColor(max);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_background.setProgress(50);
    }


    @Override
    public void onClick(View v) {
        if(actionBarTipLayout !=null){
            LoadStateType loadStateType = LoadStateType.LOADING;
            switch (v.getId()){
                case R.id.btn_loading:
                    actionBarTipLayout.loading();
                    break;
                case R.id.btn_complete:
                    actionBarTipLayout.completeAndHide();
                    break;
                case R.id.btn_error:
                    actionBarTipLayout.showError("123456");
                    break;
                case R.id.btn_warning:
                    actionBarTipLayout.showWarning("警告警告");
                    break;
            }
        }
    }

}
