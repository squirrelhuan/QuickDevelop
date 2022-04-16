package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderHelper;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderModel;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "操作引导", preViewClass = TextView.class, resType = ResType.Custome)
public class GuiderFragment extends QuickFragment {

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_guider, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        Button btn_01 = rootView.findViewById(R.id.btn_01);
        btn_01.setText("点击按钮");
        btn_01.setOnClickListener(view -> {
            showGuider(view,tips[0], GuiderModel.TouchType.TargetView,GuiderModel.SHAPE.rectangle);//点击按钮关闭引导！
        });
        Button btn_02 = rootView.findViewById(R.id.btn_02);
        btn_02.setText("点击提示框");
        btn_02.setOnClickListener(view -> {
            showGuider(view,tips[1],GuiderModel.TouchType.TipView,GuiderModel.SHAPE.rectangle);//点击提示框关闭引导！
        });
        Button btn_03 = rootView.findViewById(R.id.btn_03);
        btn_03.setText("点击任意位置");
        btn_03.setOnClickListener(view -> {
            showGuider(view,tips[2], GuiderModel.TouchType.Other,GuiderModel.SHAPE.rectangle);//点击任意位置关闭提示！

        });
        Button btn_04 = rootView.findViewById(R.id.btn_04);
        btn_04.setText("椭圆提示框");
        btn_04.setOnClickListener(view -> {
            showGuider(view, tips[3],GuiderModel.TouchType.Other,GuiderModel.SHAPE.oval);//椭圆提示框！

        });
        Button btn_05 = rootView.findViewById(R.id.btn_05);
        btn_05.setText("超长文本");
        btn_05.setOnClickListener(view -> {
            showGuider(view, tips[4],GuiderModel.TouchType.Other,GuiderModel.SHAPE.roundedrectangle);//超长文本！
        });
        Button btn_06 = rootView.findViewById(R.id.btn_06);
        btn_06.setOnClickListener(view -> {
            showGuider(view, btn_06.getText().toString(),GuiderModel.TouchType.Other,GuiderModel.SHAPE.rectangle);//点击按钮关闭引导！
        });
        Button btn_07 = rootView.findViewById(R.id.btn_07);
        btn_07.setOnClickListener(view -> {
            showGuider(view, btn_07.getText().toString(),GuiderModel.TouchType.Other,GuiderModel.SHAPE.rectangle);//点击按钮关闭引导！
        });
        Button btn_08 = rootView.findViewById(R.id.btn_08);
        btn_08.setOnClickListener(view -> {
            showGuider(view, btn_08.getText().toString(),GuiderModel.TouchType.Other,GuiderModel.SHAPE.rectangle);//点击按钮关闭引导！
        });
        Button btn_09 = rootView.findViewById(R.id.btn_09);
        btn_09.setOnClickListener(view -> {
            showGuider(view, btn_09.getText().toString(),GuiderModel.TouchType.Other,GuiderModel.SHAPE.rectangle);//点击按钮关闭引导！
        });
        Button btn_10 = rootView.findViewById(R.id.btn_10);
        btn_10.setOnClickListener(view -> {
            showGuider(view, btn_10.getText().toString(),GuiderModel.TouchType.Other,GuiderModel.SHAPE.rectangle);//点击按钮关闭引导！
        });
        Button btn_11 = rootView.findViewById(R.id.btn_11);
        btn_11.setOnClickListener(view -> {
            showGuider(view, btn_11.getText().toString(),GuiderModel.TouchType.Other,GuiderModel.SHAPE.rectangle);//点击按钮关闭引导！
        });
        Button btn_12 = rootView.findViewById(R.id.btn_12);
        btn_12.setOnClickListener(view -> {
            showGuider(view, btn_12.getText().toString(), GuiderModel.TouchType.TargetView,GuiderModel.SHAPE.rectangle);//点击按钮关闭引导！
        });
    }

    String[] tips = {"点击按钮关闭引导！", "点击提示框关闭引导", "这里是文本说明区域，点击任意位置关闭提示", "椭圆提示框", "这是一段很长的文本内容，入乐的叫歌，不入乐的叫诗（或词）。入乐的歌在感情抒发、形象塑造上和诗没有任何区别，但在结构上、节奏上要受音乐的制约。。。"};

    private void showGuider(View view, String tip, GuiderModel.TouchType touchType, GuiderModel.SHAPE shape) {
        GuiderModel guiderModel = new GuiderModel();
        guiderModel.setLineType(GuiderModel.LINETYPE.straight);
        guiderModel.setTargetView(new WeakReference<>(view));
        guiderModel.setMessage(tip);
        guiderModel.setComplateType(GuiderModel.GuidActionType.CLICK);
        //guiderModel.setLineColor(Color.BLACK);
        guiderModel.setLineWidth(2);
        //guiderModel.setTextColor(Color.GREEN);
        guiderModel.setTextSize(36);
        guiderModel.setImgResourceId(R.mipmap.ic_launcher);
        //guiderModel.setTextBackgroundColor(Color.YELLOW);
        GuiderHelper.getInstance().setGuiderModel(guiderModel);
        GuiderHelper.getInstance().add(guiderModel);
        guiderModel.setTouchType(touchType);
        guiderModel.setShape(shape);
        GuiderHelper.getInstance().setTextBackgroundColor(Color.GREEN);
        GuiderHelper.getInstance().setBackgroundColor(0x99000000);
        GuiderHelper.getInstance().setLineColor(Color.WHITE);
        GuiderHelper.getInstance().setTextColor(Color.RED);
        //GuiderHelper.getInstance().startGuider(mContext,view,"DBGUIDER");
        GuiderHelper.getInstance().startGuider(getActivity(), view, "");
        /*ViewGroup mParentView = (FrameLayout) getActivity().getWindow().getDecorView();
        GuiderView guiderSurfaceView = new GuiderView(getContext(), guiderModel,false,null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mParentView.addView(guiderSurfaceView, layoutParams);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GuiderHelper.getInstance().destory();
    }

}