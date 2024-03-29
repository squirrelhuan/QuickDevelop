package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.service.AccessibilityHelper;
import cn.demomaster.huan.quickdeveloplibrary.service.QDAccessibilityService;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "无障碍服务", preViewClass = TextView.class, resType = ResType.Custome)
public class AccessibilityServiceFragment extends QuickFragment {

    @BindView(R.id.btn_01)
    Button btn_01;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_accessibilityservice, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        btn_01.setOnClickListener(v -> {
            //QDAccessibilityService.addPackage("cn.demomaster.huan.quickdevelop");
            if (!AccessibilityHelper.isEnable(getContext(), QDAccessibilityService.class)) {
                //跳转系统自带界面 辅助功能界面
                QDAccessibilityService.startSettintActivity(mContext);
            }else {
                QdToast.show(mContext, "无障碍已开启");
            }
        });
    }
}