package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.popup.QDPopup;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "PopUp",preViewClass = StateView.class,resType = ResType.Custome)
public class PopUpFragment extends QDBaseFragment {
    //Components
    ViewGroup mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_popup, null);
        }
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {
        //actionBarLayout.setHeaderBackgroundColor(colors[i]);
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
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        Button btn_camera = (Button) view
                .findViewById(R.id.item_popupwindows_camera);

        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        /*startAnimation(AnimationUtils.loadAnimation(getThemeActivity(),
                R.anim.bottom_up));*/
    }

}