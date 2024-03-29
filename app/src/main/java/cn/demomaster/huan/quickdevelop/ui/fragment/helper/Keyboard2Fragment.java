package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.keybored.keybored01.QDKeyboard;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Keyboard2", preViewClass = TextView.class, resType = ResType.Custome)
public class Keyboard2Fragment extends QuickFragment {

    @BindView(R.id.et_system_none)
    EditText et_system_none;
    @BindView(R.id.et_system_number)
    EditText et_system_number;
    @BindView(R.id.et_system_numberPassword)
    EditText et_system_numberPassword;

    @BindView(R.id.et_custom_none)
    EditText et_custom_none;
    @BindView(R.id.et_custom_number)
    EditText et_custom_number;
    @BindView(R.id.et_custom_numberPassword)
    EditText et_custom_numberPassword;

    @BindView(R.id.et_custom_phone)
    EditText et_custom_phone;
    @BindView(R.id.et_custom_textEmailAddress)
    EditText et_custom_textEmailAddress;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_keyboard_01, null);
        return mView;
    }

    private QDKeyboard qdKeyboard;

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("键盘");

        qdKeyboard = new QDKeyboard(mContext);
        qdKeyboard.addEditText(et_custom_none);
        qdKeyboard.addEditText(et_custom_number);
        qdKeyboard.addEditText(et_custom_numberPassword);
        qdKeyboard.addEditText(et_custom_phone);
        qdKeyboard.addEditText(et_custom_textEmailAddress);
        // mContext.getCurrentFocus()
       /* qdKeyboard.addEditText(et_system_none);
        qdKeyboard.addEditText(et_system_number);
        qdKeyboard.addEditText(et_system_numberPassword);*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // qdKeyboard.dispatchTouchEvent();
        return super.onKeyDown(keyCode, event);
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
        qdKeyboard.removeEditText(et_custom_none);
        qdKeyboard.removeEditText(et_custom_number);
        qdKeyboard.removeEditText(et_custom_numberPassword);/*
        qdKeyboard.removeEditText(et_system_none);
        qdKeyboard.removeEditText(et_system_number);
        qdKeyboard.removeEditText(et_system_numberPassword);*/
        if (qdKeyboard.isShow()) {
            qdKeyboard.hideKeyboard();
        }
        super.onDestroy();
    }
}