package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.popup.QDPopup;

/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "PopUp", preViewClass = TextView.class, resType = ResType.Custome)
public class PopUpFragment extends BaseFragment {

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_popup, null);
        return mView;
    }

    public void initView(View rootView) {
        QDButton btn_01 = rootView.findViewById(R.id.btn_01);
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
            }
        });
    }

    private QDPopup pop = null;

    private void showPop() {
        pop = new QDPopup(getContext());
        View view = getLayoutInflater().inflate(R.layout.item_popup_common,
                null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setBackgroundDrawable(new ColorDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        /*startAnimation(AnimationUtils.loadAnimation(getThemeActivity(),
                R.anim.bottom_up));*/
    }

}