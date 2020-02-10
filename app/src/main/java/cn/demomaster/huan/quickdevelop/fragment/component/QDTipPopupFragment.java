package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.popup.QDTipPopup;

import static com.umeng.socialize.utils.ContextUtil.getContext;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "QDTipPopupFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class QDTipPopupFragment extends QDBaseFragment implements View.OnClickListener {
    //Components
    ViewGroup mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_popup_tip, null);
        }
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        //Button btn_01 = rootView.findViewById(R.id.btn_01);
        rootView.findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setWithArrow(false).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"普通无箭头提示").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.TOP);
            }
        });
        rootView.findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"顶部提示").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.TOP);
            }
        });
        rootView.findViewById(R.id.btn_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"底部提示").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.BOTTOM);
            }
        });
        rootView.findViewById(R.id.btn_11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"左边提示").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.LEFT);
            }
        });
        rootView.findViewById(R.id.btn_12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"右边提示").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.RIGHT);
            }
        });
        rootView.findViewById(R.id.btn_13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"顶部多行提示*************************************************************************提示。").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.TOP);
            }
        });
        rootView.findViewById(R.id.btn_21).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"右侧多行提示---------------------------------------------------------------------------------------提示。").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.RIGHT);
            }
        });
        rootView.findViewById(R.id.btn_22).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setBackgroundColor(Color.BLUE).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"普通多行提示--------&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&-------------------------------------------------------------------------------提示。").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.TOP);
            }
        });
        rootView.findViewById(R.id.btn_23).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setBackgroundColor(Color.BLUE).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"左侧多行提示---------------------------------------------------------------------------------------提示。").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.LEFT);
            }
        });


        rootView.findViewById(R.id.btn_31).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setBackgroundColor(Color.YELLOW).setTextColor(Color.BLACK).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"底部多行提示---------------------------------------------------------------------------------------提示。").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.BOTTOM);
            }
        });

        rootView.findViewById(R.id.btn_32).setOnClickListener(this);

        rootView.findViewById(R.id.btn_33).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setBackgroundColor(Color.YELLOW).setTextColor(Color.BLACK).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"底部多行提示---------------------------------------------------------------------------------------提示。").create();
                qdTipPopup.showTip(v, GuiderView.Gravity.BOTTOM);
            }
        });

       /* btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"111",Toast.LENGTH_LONG).show();
                QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage("普通提示窗").create();
                //qdTipPopup.showAsDropDown(v,0,0);
                qdTipPopup.showAsDropDown(v);
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(getContext(),"111",Toast.LENGTH_LONG).show();
        QDTipPopup qdTipPopup = new QDTipPopup.Builder(getContext()).setBackgroundRadius(10).setMessage(v.getContentDescription()!=null?v.getContentDescription().toString():"提示").create();
        //qdTipPopup.showAsDropDown(v,0,0);
        qdTipPopup.showTip(v, GuiderView.Gravity.TOP);
    }
}