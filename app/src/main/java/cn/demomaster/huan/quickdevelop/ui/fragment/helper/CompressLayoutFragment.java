package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.keybored.keybored02.QDKeyboard;
import cn.demomaster.huan.quickdeveloplibrary.widget.CompressLayout;


/**
 * Squirrel桓
 * 2021/2/19
 */

@ActivityPager(name = "压缩布局", preViewClass = TextView.class, resType = ResType.Custome)
public class CompressLayoutFragment extends BaseFragment {


    @BindView(R.id.btn_left)
    Button btn_left;
    @BindView(R.id.btn_top)
    Button btn_top;
    @BindView(R.id.btn_right)
    Button btn_right;
    @BindView(R.id.btn_bottom)
    Button btn_bottom;
    @BindView(R.id.btn_open)
    Button btn_open;
    @BindView(R.id.btn_close)
    Button btn_close;

    @BindView(R.id.compressLayout)
    CompressLayout compressLayout;
    @BindView(R.id.iv_emtion)
    ImageView iv_emtion;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_compress, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        // mContext.getCurrentFocus()
        compressLayout.setDuration(1000);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compressLayout.setGravity(Gravity.LEFT);
            }
        });
        btn_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compressLayout.setGravity(Gravity.TOP);
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compressLayout.setGravity(Gravity.RIGHT);
            }
        });
        btn_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compressLayout.setGravity(Gravity.BOTTOM);
            }
        });
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compressLayout.showPanel();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compressLayout.dissmissPanel();
            }
        });
        iv_emtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(compressLayout.isExpanded()){
                    compressLayout.dissmissPanel();
                }else {
                    compressLayout.showPanel();
                }
            }
        });
    }

    // 如果是activity 当点击返回键时, 如果软键盘正在显示, 则隐藏软键盘并是此次返回无效
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (qdKeyboard.isShow()) {
                qdKeyboard.hideKeyboard();
                return false;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}