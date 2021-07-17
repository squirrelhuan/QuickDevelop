package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
 * 2018/8/25
 */

@ActivityPager(name = "Keyboard3", preViewClass = TextView.class, resType = ResType.Custome)
public class Keyboard3Fragment extends BaseFragment {

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

    @BindView(R.id.compressLayout)
    CompressLayout compressLayout;
    @BindView(R.id.iv_emtion)
    ImageView iv_emtion;


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

        /*KeyboardChangeListener softKeyboardStateHelper = new KeyboardChangeListener(getActivity());
        softKeyboardStateHelper.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    //键盘的弹出
                    compressLayout.setVisibility(View.GONE);
                    //QdToast.show("键盘的弹出");
                    PopToastUtil.ShowToast(getActivity(),"键盘的弹出");
                } else {
                    //键盘的收起
                    //et_seartch.setCursorVisible(false);
                    compressLayout.setVisibility(View.VISIBLE);
                    //QdToast.show("键盘的收起");
                    PopToastUtil.ShowToast(getActivity(),"键盘的收起");
                }
            }
        });*/

        /*findViewById(R.id.rl_content).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // compressLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //QdToast.show(getContext(),"height="+getMeasuredHeight());
                //QDLogger.e("getViewTreeObserver height="+getMeasuredHeight());
                PopToastUtil.ShowToast((Activity) getContext(),"height="+findViewById(R.id.rl_content).getMeasuredHeight());
            }
        });*/

        /*compressLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               // compressLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                QdToast.show(getContext(),"height="+compressLayout.getMeasuredHeight());
            }
        });*/
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



    /**
     * 监听软键盘是否弹出
     * Created by DELL on 2018/7/19.
     */
    public static class KeyboardChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private static final String TAG = "ListenerHandler";
        private View mContentView;
        private int mOriginHeight;
        private int mPreHeight;
        private KeyBoardListener mKeyBoardListen;

        public interface KeyBoardListener {
            /**
             * call back
             *
             * @param isShow         true is show else hidden
             * @param keyboardHeight keyboard height
             */
            void onKeyboardChange(boolean isShow, int keyboardHeight);
        }

        public void setKeyBoardListener(KeyBoardListener keyBoardListen) {
            this.mKeyBoardListen = keyBoardListen;
        }

        public KeyboardChangeListener(Activity contextObj) {
            if (contextObj == null) {
                return;
            }
            mContentView = findContentView(contextObj);
            if (mContentView != null) {
                addContentTreeObserver();
            }
        }

        private View findContentView(Activity contextObj) {
            return contextObj.findViewById(android.R.id.content);
        }

        private void addContentTreeObserver() {
            mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        @Override
        public void onGlobalLayout() {
            int currHeight = mContentView.getHeight();
            if (currHeight == 0) {
                return;
            }
            boolean hasChange = false;
            if (mPreHeight == 0) {
                mPreHeight = currHeight;
                mOriginHeight = currHeight;
            } else {
                if (mPreHeight != currHeight) {
                    hasChange = true;
                    mPreHeight = currHeight;
                } else {
                    hasChange = false;
                }
            }
            if (hasChange) {
                boolean isShow;
                int keyboardHeight = 0;
                if (mOriginHeight == currHeight) {
                    //hidden
                    isShow = false;
                } else {
                    //show
                    keyboardHeight = mOriginHeight - currHeight;
                    isShow = true;
                }

                if (mKeyBoardListen != null) {
                    mKeyBoardListen.onKeyboardChange(isShow, keyboardHeight);
                }
            }
        }

    }



}