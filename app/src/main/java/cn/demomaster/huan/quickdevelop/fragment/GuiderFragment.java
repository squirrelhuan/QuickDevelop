package cn.demomaster.huan.quickdevelop.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderHelper;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderModel;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class GuiderFragment extends BaseFragment {
    //Components
    ViewGroup mView;


    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_guider, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";
        Button btn_01 = mView.findViewById(R.id.btn_01);
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,0);//点击按钮关闭引导！
            }
        });
        Button btn_02 = mView.findViewById(R.id.btn_02);
        btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击提示框关闭引导！

            }
        });
        Button btn_03 = mView.findViewById(R.id.btn_03);
        btn_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,2);//点击任意位置关闭提示！

            }
        });
        Button btn_04 = mView.findViewById(R.id.btn_04);
        btn_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,3);//椭圆提示框！

            }
        });
        Button btn_05 = mView.findViewById(R.id.btn_05);
        btn_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击按钮关闭引导！

            }
        });
        Button btn_06 = mView.findViewById(R.id.btn_06);
        btn_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击按钮关闭引导！

            }
        });
        Button btn_07 = mView.findViewById(R.id.btn_07);
        btn_07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击按钮关闭引导！

            }
        });
        Button btn_08 = mView.findViewById(R.id.btn_08);
        btn_08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击按钮关闭引导！

            }
        });
        Button btn_09 = mView.findViewById(R.id.btn_09);
        btn_09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击按钮关闭引导！

            }
        });
        Button btn_10 = mView.findViewById(R.id.btn_10);
        btn_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击按钮关闭引导！

            }
        });
        Button btn_11 = mView.findViewById(R.id.btn_11);
        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击按钮关闭引导！

            }
        });
        Button btn_12 = mView.findViewById(R.id.btn_12);
        btn_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuider(view,1);//点击按钮关闭引导！

            }
        });
        return mView;
    }

    private String[] titles = {"1", "2", "3", "4"};
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};

    public void initActionBarLayout(ActionBarLayout actionBarLayout) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayout.setTitle(titles[i]+"---------ASDFGGHHJ");
        actionBarLayout.setHeaderBackgroundColor(colors[i]);
    }

    String tips[] ={"点击按钮关闭引导！","点击提示框关闭引导","这里是文本说明区域，点击任意位置关闭提示","椭圆提示框"};

    private void showGuider(View view,int i) {

        GuiderModel guiderModel = new GuiderModel();
        guiderModel.setLineType(GuiderModel.LINETYPE.straight);
        guiderModel.setTargetView(new WeakReference<View>(view));
        guiderModel.setMessage(tips[i]);
        guiderModel.setComplateType(GuiderModel.GuidActionType.CLICK);
        GuiderHelper.getInstance().setGuiderModel(guiderModel);


        switch (i){
            case 0:
                guiderModel.setTouchType(GuiderModel.TouchType.TargetView);
                break;
            case 1:
                guiderModel.setTouchType(GuiderModel.TouchType.TipView);
                break;
            case 2:
                guiderModel.setTouchType(GuiderModel.TouchType.Other);
                break;
            case 3:
                guiderModel.setShape(GuiderModel.SHAPE.oval);
                break;
        }

        GuiderHelper.getInstance().startGuider(mContext,view,"DBGUIDER");
        //GuiderHelper.getInstance().startGuider(getActivity(),view,"");
        /*ViewGroup mParentView = (FrameLayout) getActivity().getWindow().getDecorView();
        GuiderView guiderSurfaceView = new GuiderView(getContext(), guiderModel,false,null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mParentView.addView(guiderSurfaceView, layoutParams);*/
    }
}