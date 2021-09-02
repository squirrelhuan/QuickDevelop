package cn.demomaster.huan.quickdeveloplibrary.view.keybored;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.StringUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.popup.QDPopup;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.util.QDViewUtil.getActivityFromView;

public class QuickKeyboard {
    private static final String TAG = QuickKeyboard.class.getName();
    private final Activity mContext;               //上下文
    private QDKeyboardView keyboardView;  //键盘的View
    private Keyboard keyboard;        //数字键盘
    private static boolean isCapes = false;
    private int keyboardType = 1;
    private Drawable delDrawable;
    private Drawable lowDrawable;
    private Drawable upDrawable;
    private final int keyboardContainerResId;

    private EditText mEditText;
    public List<EditText> editTextList;

    public QuickKeyboard(Activity mContext) {
        this.mContext = mContext;
        this.keyboardContainerResId = R.layout.layout_keyboard_containor;
        onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSystemKeyBoard((EditText) v);
                //QDLogger.d(v.getId() + (hasFocus ? "得到焦点" : "失去焦点"));
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    setCurrentFocus((EditText) v);
                    showKeyboard();
                } else {//如果新的焦点不使用自定义键盘则隐藏
                    waitHideKeyboard();
                }
            }
        };
        onTouchListener = (v, event) -> {
            hideSystemKeyBoard((EditText) v);
            if (event.getAction() == MotionEvent.ACTION_UP && v.hasFocus() && v.isFocused()) {
                showKeyboard();
            }
            return false;
        };

        initKeyboard();
    }

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    private void initKeyboard() {
        //解决onresume系统输入法重新弹出，遗留的问题就是不能根据焦点自动弹出输入框了
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //自定义键盘的容器View
        View keyContainer = LayoutInflater.from(mContext).inflate(keyboardContainerResId, null, false);
        int keyboardResId = keyContainer.findViewById(R.id.safeKeyboardLetter).getId();
        keyboard = new Keyboard(mContext, R.xml.keyboard_num);
        keyboardView = keyContainer.findViewById(keyboardResId);
        keyboardView.setDelDrawable(delDrawable);
        keyboardView.setLowDrawable(lowDrawable);
        keyboardView.setUpDrawable(upDrawable);
        keyboardView.setKeyboard(keyboard);//给键盘View设置键盘
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(listener);
        ImageView iv_keyboardDone = keyContainer.findViewById(R.id.iv_keyboardDone);
        Drawable hideDrawable = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_keyboard_hide_black_24dp, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hideDrawable.setTint(mContext.getResources().getColor(R.color.transparent_light_cc));
        }
        iv_keyboardDone.setImageDrawable(hideDrawable);
        iv_keyboardDone.setOnClickListener(v -> hideKeyboard());

        setDelDrawable(mContext.getResources().getDrawable(R.drawable.icon_del));
        setLowDrawable(mContext.getResources().getDrawable(R.drawable.icon_capital_default));
        setUpDrawable(mContext.getResources().getDrawable(R.drawable.icon_capital_selected));

        creatPopWindow(keyContainer);
    }

    /**
     * 判断焦点是否被其他editview获取了
     * @return
     */
    private boolean ortherViewGetFocusState() {
        for (EditText editText: editTextList) {
            if(editText.isFocused()){
                return true;
            }
        }
        return false;
    }

    public void removeEditText(EditText editText) {
        if (editText == null) {
            return;
        }
        if (editTextList == null) {
            return;
        }
        if (editTextList.contains(editText)) {
            editText.setOnTouchListener(null);
            editText.setOnFocusChangeListener(null);
            editText.clearFocus();
            editTextList.remove(editText);
        } else {
            QDLogger.d("contains=false");
        }
    }

    public void addEditText(EditText editText) {
        if (editText == null) {
            return;
        }
        if (editTextList == null) {
            editTextList = new ArrayList<>();
        }
        if (!editTextList.contains(editText)) {
            editTextList.add(editText);
        }
        setCurrentFocus(editText);
    }

    /**
     * 设置默认获取焦点的editText
     *
     * @param editText
     */
    private void setCurrentFocus(EditText editText) {
        mEditText = editText;
        setFocusListener();
    }

    View.OnFocusChangeListener onFocusChangeListener;
    View.OnTouchListener onTouchListener;
    private void setFocusListener() {
        for (int i = 0; i < editTextList.size(); i++) {
            editTextList.get(i).setOnTouchListener(onTouchListener);
            editTextList.get(i).setOnFocusChangeListener(onFocusChangeListener);
        }
    }


    /**
     * 延迟隐藏
     *
     * @return
     */
    private void waitHideKeyboard() {
        View v = getActivityFromView(mEditText).getCurrentFocus();
        //v=((Activity) getActivityFromView( mEditText)).getWindow().getDecorView().findFocus();
        if (v == null || !editTextList.contains(v)) {
            hideKeyboard();
        }
    }

    /**
     * 根据输入类型锁定对应的键盘
     */
    private void switchKeyboardByInputType() {
        //字母 case 1: keyboardView.setKeyboard(keyboardLetter);
        //符号 case 2: keyboardView.setKeyboard(keyboardSymbol);
        //数字 case 3: keyboardView.setKeyboard(keyboardNumber);

       /* Keyboard keyboardNumber;        //数字键盘
        Keyboard keyboardSymbol;        //符号键盘
        keyboardNumber = new Keyboard(mContext, R.xml.keyboard_num);            //实例化数字键盘
         keyboardSymbol = new Keyboard(mContext, R.xml.keyboard_symbol);         //实例化符号键盘
        */
        QDLogger.println("mEditText.getInputType()="+mEditText.getInputType());
        switchKeyboard(mEditText.getInputType());
    }

    /**
     * 切换到指定 键盘
     *
     * @param keyboardType
     */
    public void switchKeyboard(int keyboardType) {
        keyboardView.setKeyboard(getKeyboardView(keyboardType));
    }

    public Keyboard getKeyboardView(int keyboardType) {
        switch (keyboardType) {
            case InputType.TYPE_CLASS_NUMBER://数字
            case InputType.TYPE_CLASS_PHONE:
            case InputType.TYPE_NUMBER_VARIATION_PASSWORD:
            case 18://inputType="numberPassword"
            case InputType.TYPE_NUMBER_VARIATION_NORMAL:
            case InputType.TYPE_NUMBER_FLAG_DECIMAL:
                return new Keyboard(mContext, R.xml.keyboard_num_only);
            default:
                return new Keyboard(mContext, R.xml.keyboard_letter);//实例化字母键盘
        }
    }

    public QDPopup mPopWindow;

    public QDPopup creatPopWindow(View keyContainer) {
        if (mPopWindow == null) {
            mPopWindow = new QDPopup(keyContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopWindow.setFocusable(false);
            mPopWindow.setTouchable(true);
            mPopWindow.setAnimationStyle(R.style.keybored_anim);
        }
        return mPopWindow;
    }

    // 设置键盘点击监听
    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            if (onKeyClickListener != null) {
                onKeyClickListener.onPress(primaryCode);
            }
            if (keyboardType == 3) {
                keyboardView.setPreviewEnabled(false);
            } else {
                keyboardView.setPreviewEnabled(true);
                keyboardView.setPreviewEnabled(!(primaryCode == -1 || primaryCode == -5 || primaryCode == 32 || primaryCode == -2
                        || primaryCode == 100860 || primaryCode == -35));
            }
            //关闭所有按压预览
            keyboardView.setPreviewEnabled(false);
        }

        @Override
        public void onRelease(int primaryCode) {
            if (onKeyClickListener != null) {
                onKeyClickListener.onRelease(primaryCode);
            }
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (onKeyClickListener != null) {
                onKeyClickListener.onKey(primaryCode, keyCodes);
            }
            //QDLogger.d("输入键盘值primaryCode:"+primaryCode);
            try {
                Editable editable = mEditText.getText();
                int start = mEditText.getSelectionStart();
                int end = mEditText.getSelectionEnd();
                if (primaryCode == Keyboard.KEYCODE_CANCEL) {
                    // 隐藏键盘
                    hideKeyboard();
                } else if (primaryCode == Keyboard.KEYCODE_DELETE || primaryCode == -35) {
                    // 回退键,删除字符
                    if (editable != null && editable.length() > 0) {
                        if (start == end) { //光标开始和结束位置相同, 即没有选中内容
                            editable.delete(start - 1, start);
                        } else { //光标开始和结束位置不同, 即选中EditText中的内容
                            editable.delete(start, end);
                        }
                    }
                } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                    // 大小写切换
                    changeKeyboardLetterCase();
                    // 重新setKeyboard, 进而系统重新加载, 键盘内容才会变化(切换大小写)
                    switchKeyboard(keyboardType);
                } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                    // 数字与字母键盘互换
                    if (keyboardType == 3) { //当前为数字键盘
                        switchKeyboard(1);//切换为字母键
                    } else {//当前不是数字键盘
                        switchKeyboard(3);//切换为数字键
                    }
                } else if (primaryCode == 100860) {
                    // 字母与符号切换
                    if (keyboardType == 2) { //当前是符号键盘
                        switchKeyboard(1);//切换到字母键
                    } else {//当前不是符号键盘, 那么切换到符号键盘
                        switchKeyboard(2);//切换到符号键
                    }
                } else {
                    // 输入键盘值
                    // editable.insert(start, Character.toString((char) primaryCode));
                    //QDLogger.d("输入键盘值:"+Character.toString((char) primaryCode));
                    editable.replace(start, end, Character.toString((char) primaryCode));
                }
            } catch (Exception e) {
                QDLogger.e(e);
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeUp() {
        }
    };

    private void changeKeyboardLetterCase() {
        List<Keyboard.Key> keyList = keyboard.getKeys();
        for (Keyboard.Key key : keyList) {
            if (key.label != null) {
                if (isCapes) {
                    if (StringUtil.isUpCaseLetter(key.label.toString())) {
                        key.label = key.label.toString().toLowerCase();
                        key.codes[0] += 32;
                    }
                } else {
                    if (StringUtil.isLowCaseLetter(key.label.toString())) {
                        key.label = key.label.toString().toUpperCase();
                        key.codes[0] -= 32;
                    }
                }
            }
        }
        isCapes = !isCapes;
        keyboardView.setCap(isCapes);
    }

    public void hideKeyboard() {
        mPopWindow.dismiss();
    }

    private void showKeyboard() {
        //qdTipPopup.showAsDropDown(mEditText,0,0,Gravity.BOTTOM);
        switchKeyboardByInputType();
        if (mPopWindow.isShowing()) {
            return;
        }
        IBinder iBinder = mEditText.getWindowToken();
        if (iBinder != null && !getActivityFromView(mEditText).isFinishing()) {
            if (!getActivityFromView(mEditText).isFinishing()) {
                mPopWindow.showAtLocation(getActivityFromView(mEditText).getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM, 0,
                        0);
            }
        }
    }

    public boolean isShow() {
        return (mPopWindow != null && mPopWindow.isShowing());
    }

    //隐藏系统键盘关键代码
    private void hideSystemKeyBoard(EditText edit) {
        this.mEditText = edit;
        InputMethodManager imm = (InputMethodManager) this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;
        //boolean isOpen = imm.isActive();
        //QDLogger.i("isOpen=" + isOpen);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            edit.setInputType(0);
        } else {
            try {
                Method setShowSoftInputOnFocus = EditText.class.getMethod(methodName, Boolean.TYPE);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edit, Boolean.FALSE);
            } catch (NoSuchMethodException e) {
                edit.setInputType(0);
                QDLogger.e(e);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                QDLogger.e(e);
            }
        }
    }

    public void setDelDrawable(Drawable delDrawable) {
        this.delDrawable = delDrawable;
        keyboardView.setDelDrawable(delDrawable);
    }

    public void setLowDrawable(Drawable lowDrawable) {
        this.lowDrawable = lowDrawable;
        keyboardView.setLowDrawable(lowDrawable);
    }

    public void setUpDrawable(Drawable upDrawable) {
        this.upDrawable = upDrawable;
        keyboardView.setUpDrawable(upDrawable);
    }

    private OnKeyClickListener onKeyClickListener;

    public void setOnKeyClickListener(OnKeyClickListener onKeyClickListener) {
        this.onKeyClickListener = onKeyClickListener;
    }

    View.OnTouchListener onTouchKeyboardListener;

    public static abstract class OnKeyClickListener implements KeyboardView.OnKeyboardActionListener {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                waitHideKeyboard();
                break;
            default:
                break;
        }
    }
}
