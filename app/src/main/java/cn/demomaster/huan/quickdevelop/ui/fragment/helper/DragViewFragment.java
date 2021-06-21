package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "DragView", preViewClass = TextView.class, resType = ResType.Custome)
public class DragViewFragment extends BaseFragment {

    @BindView(R.id.btn_error_01)
    QDButton btn_error_01;
    @BindView(R.id.btn_error_buggly)
    QDButton btn_error_buggly;

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_dragview, null);
        // ButterKnife.bind(this,mView);
        return (ViewGroup) mView;
    }

    private TextView mFirstOne;

    public void initView(View rootView) {
        // actionBarLayoutOld.setTitle("异常捕获");
        mFirstOne = rootView.findViewById(R.id.tv_first_one);
        mFirstOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

}