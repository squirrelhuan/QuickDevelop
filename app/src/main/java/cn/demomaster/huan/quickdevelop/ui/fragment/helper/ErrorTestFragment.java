package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnDoubleClickListener;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnMultClickListener;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "异常捕获", preViewClass = TextView.class, resType = ResType.Custome)
public class ErrorTestFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @BindView(R.id.btn_error_01)
    QDButton btn_error_01;
    @BindView(R.id.btn_error_02)
    QDButton btn_error_02;
    @BindView(R.id.btn_error_03)
    QDButton btn_error_03;

    @BindView(R.id.btn_double_click)
    QDButton btn_double_click;
    @BindView(R.id.btn_three_click)
    QDButton btn_three_click;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_errortest, null);
        return  mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("异常捕获");
        btn_error_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 0;
                int b = 1;
                int c = b / a;
            }
        });
        btn_error_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paint paint =null;
                paint.setColor(Color.RED);
            }
        });
        btn_error_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Object linearLayout = new LinearLayout(getContext());
                        RelativeLayout relativeLayout = (RelativeLayout) linearLayout;
                    }
                }).start();
            }
        });

        btn_double_click.setOnClickListener(new OnDoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                Toast.makeText(mContext, "double click", Toast.LENGTH_SHORT).show();
            }
        });
        btn_three_click.setOnClickListener(new OnMultClickListener(3, 300) {
            @Override
            public void onClickEvent(View v) {
                Toast.makeText(mContext, "three click", Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);

    }*/
}