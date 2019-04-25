package cn.demomaster.huan.quickdeveloplibrary.view.keybored;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class QDKeyboard {

    private static final String TAG = "SafeKeyboard";

    private Context mContext;               //上下文

    private LinearLayout layout;
    private View keyContainer;              //自定义键盘的容器View
    private QDKeyboardView keyboardView;  //键盘的View
    private View ll_keyboard_header;
    private Keyboard keyboardNumber;        //数字键盘
    private Keyboard keyboardNumber_Only;        //数字键盘
    private Keyboard keyboardLetter;        //字母键盘
    private Keyboard keyboardLetter_Only;        //字母键盘
    private Keyboard keyboardSymbol;        //符号键盘
    private Keyboard keyboardSymbol_Only;        //符号键盘
    private static boolean isCapes = false;
    private boolean isOpening = false;
    private boolean isClosing = false;
    private int keyboardType = 1;
    private static final long HIDE_TIME = 300;
    private static final long SHOW_TIME = 300;
    private Drawable delDrawable;
    private Drawable lowDrawable;
    private Drawable upDrawable;
    private int keyboardContainerResId;
    private int keyboardResId;

    // private TranslateAnimation showAnimation;
    //private TranslateAnimation hideAnimation;
    private EditText mEditText;
    public List<EditText> editTextList;

    public QDKeyboard(Context mContext, LinearLayout layout, int id, int keyId) {
        this.mContext = mContext;
        this.layout = layout;
        this.keyboardContainerResId = id;
        this.keyboardResId = keyId;
        //addEditText(mEditText);

        initKeyboard();
    }

    public void removeEditText(EditText editText) {
        if (editText == null) {
            return;
        }
        if (editTextList == null) {
            return;
        }
        if (editTextList.contains(editText)) {
            QDLogger.d("contains=true");
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
        editText.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSystemKeyBoard((EditText) v);
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    setCurrentFocus((EditText) v);
                    if ((isClosed || isClosing)) {
                        showKeyboard();
                    }
                }
            }
        });
    }

    /**
     * 设置默认获取焦点的editText
     *
     * @param editText
     */
    private void setCurrentFocus(EditText editText) {
        mEditText = editText;
        for (int i = 0; i < editTextList.size(); i++) {
            if (editTextList.get(i) == editText) {
                editTextList.get(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        hideSystemKeyBoard((EditText) v);
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if ((isClosed || isClosing)) {
                                showKeyboard();
                            }
                        }
                        return false;
                    }
                });
                editTextList.get(i).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        hideSystemKeyBoard((EditText) v);
                        if (hasFocus) {
                            if ((isClosed || isClosing)) {
                                showKeyboard();
                            }
                        }
                    }
                });
            } else {
                editTextList.get(i).setOnTouchListener(null);
                editTextList.get(i).setOnFocusChangeListener(new View.
                        OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        hideSystemKeyBoard((EditText) v);
                        if (hasFocus) {
                            // 此处为得到焦点时的处理内容
                            setCurrentFocus((EditText) v);
                            if ((isClosed || isClosing)) {
                                showKeyboard();
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean isOpened = false;//
    private boolean isClosed = true;//
    private int duration = 300;
    ValueAnimator animator;
    float keyContainerHeight = -1;
    public void startAnimation() {
        switchKeyboardByInputType();
        if (keyContainerHeight == -1) {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            keyboardView.measure(w, h);
            ll_keyboard_header.measure(w, h);
            keyContainer.setVisibility(View.VISIBLE);
            //keyContainerHeight = keyContainer.getMinimumHeight();
            keyContainerHeight = ll_keyboard_header.getHeight();

            //ViewGroup.LayoutParams layoutParams = keyContainer.getLayoutParams();
            //layoutParams.height = (int) 0;
            //keyContainer.setLayoutParams(layoutParams);
        }
        final float h1 = isOpening?keyContainer.getHeight():0;
        final float h2 = isOpening?keyContainerHeight:keyContainer.getHeight();
        animator = ValueAnimator.ofFloat(h1,h2 );
        long d = (long) (duration*(isOpening?(keyContainerHeight-keyContainer.getHeight()):keyContainer.getHeight())/keyContainerHeight);
        animator.setDuration(d);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = keyContainer.getLayoutParams();
                layoutParams.height = (int) value;
                keyContainer.setLayoutParams(layoutParams);
                QDLogger.i("value=" + value);

                keyContainer.setVisibility(View.VISIBLE);
                if (isOpening && value >= h2) {
                    QDLogger.i("已开启");
                    isOpening = false;
                    isOpened = true;
                    isClosed = false;
                    isClosing = false;
                } else if (isClosing && value <= h1) {
                    QDLogger.i("已隐藏");
                    keyContainer.setVisibility(View.GONE);
                    isClosing = false;
                    isClosed = true;
                    isOpened = false;
                    isOpening = false;
                }
            }
        });
        //animator.setRepeatMode(ValueAnimator.REVERSE);
        //animator.setRepeatCount(ValueAnimator.INFINITE);//accelerate_decelerate_interpolator
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    private void switchKeyboardByInputType() {
            //字母 case 1: keyboardView.setKeyboard(keyboardLetter);
                //符号 case 2: keyboardView.setKeyboard(keyboardSymbol);
                //数字 case 3: keyboardView.setKeyboard(keyboardNumber);
        switch (mEditText.getInputType()){
            case InputType.TYPE_CLASS_NUMBER://数字
                keyboardView.setKeyboard(keyboardNumber_Only);
                break;
            case InputType.TYPE_CLASS_PHONE://数字
                keyboardView.setKeyboard(keyboardNumber_Only);
                break;
            case InputType.TYPE_NUMBER_VARIATION_PASSWORD://数字
                keyboardView.setKeyboard(keyboardNumber_Only);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initKeyboard() {
        keyContainer = LayoutInflater.from(mContext).inflate(keyboardContainerResId, layout, true);
        keyboardNumber = new Keyboard(mContext, R.xml.keyboard_num);            //实例化数字键盘
        keyboardNumber_Only = new Keyboard(mContext, R.xml.keyboard_num_only);            //实例化数字键盘
        keyboardLetter = new Keyboard(mContext, R.xml.keyboard_letter);         //实例化字母键盘
        keyboardLetter_Only = new Keyboard(mContext, R.xml.keyboard_letter);         //实例化字母键盘
        keyboardSymbol = new Keyboard(mContext, R.xml.keyboard_symbol);         //实例化符号键盘
        keyboardSymbol_Only = new Keyboard(mContext, R.xml.keyboard_symbol);         //实例化符号键盘
        ll_keyboard_header = keyContainer.findViewById(R.id.ll_keyboard_header);
        // 由于符号键盘与字母键盘共用一个KeyBoardView, 所以不需要再为符号键盘单独实例化一个KeyBoardView
        keyboardView = keyContainer.findViewById(keyboardResId);
        keyboardView.setDelDrawable(delDrawable);
        keyboardView.setLowDrawable(lowDrawable);
        keyboardView.setUpDrawable(upDrawable);
        keyboardView.setKeyboard(keyboardLetter);                         //给键盘View设置键盘
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(listener);
        keyContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 自动调整箭头的位置
                keyContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (keyContainerHeight == -1) {
                    int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    keyContainer.measure(w, h);
                    keyContainerHeight = keyContainer.getHeight();

                    ViewGroup.LayoutParams layoutParams = keyContainer.getLayoutParams();
                    layoutParams.height =0;
                    keyContainer.setLayoutParams(layoutParams);
                }
            }
        });

        //keyContainer.setVisibility(View.GONE);

        FrameLayout done = keyContainer.findViewById(R.id.keyboardDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow()) {
                    hideKeyboard();
                }
            }
        });

        keyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }
    // 设置键盘点击监听
    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            if (keyboardType == 3) {
                keyboardView.setPreviewEnabled(false);
            } else {
                keyboardView.setPreviewEnabled(true);
                if (primaryCode == -1 || primaryCode == -5 || primaryCode == 32 || primaryCode == -2
                        || primaryCode == 100860 || primaryCode == -35) {
                    keyboardView.setPreviewEnabled(false);
                } else {
                    keyboardView.setPreviewEnabled(true);
                }
            }
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
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
                    keyboardType = 1;
                    switchKeyboard();
                } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                    // 数字与字母键盘互换
                    if (keyboardType == 3) { //当前为数字键盘
                        keyboardType = 1;
                    } else {        //当前不是数字键盘
                        keyboardType = 3;
                    }
                    switchKeyboard();
                } else if (primaryCode == 100860) {
                    // 字母与符号切换
                    if (keyboardType == 2) { //当前是符号键盘
                        keyboardType = 1;
                    } else {        //当前不是符号键盘, 那么切换到符号键盘
                        keyboardType = 2;
                    }
                    switchKeyboard();
                } else {
                    // 输入键盘值
                    // editable.insert(start, Character.toString((char) primaryCode));
                    editable.replace(start, end, Character.toString((char) primaryCode));
                }
            } catch (Exception e) {
                e.printStackTrace();
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


    private void switchKeyboard() {
        switch (keyboardType) {
            case 1:
                keyboardView.setKeyboard(keyboardLetter);
                break;
            case 2:
                keyboardView.setKeyboard(keyboardSymbol);
                break;
            case 3:
                keyboardView.setKeyboard(keyboardNumber);
                break;
            default:
                Log.e(TAG, "ERROR keyboard type");
                break;
        }
    }

    private void changeKeyboardLetterCase() {
        List<Keyboard.Key> keyList = keyboardLetter.getKeys();
        if (isCapes) {
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isUpCaseLetter(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] += 32;
                }
            }
        } else {
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isLowCaseLetter(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] -= 32;
                }
            }
        }
        isCapes = !isCapes;
        keyboardView.setCap(isCapes);
    }

    public void hideKeyboard() {
        if (isOpened || isOpening) {
            isClosing = true;
            isOpening = false;
            animator.reverse();
        }
    }

    private void showKeyboard() {
        keyboardView.setKeyboard(keyboardLetter);
        if (isClosed || isClosing) {
            isOpening = true;
            isClosing = false;
            keyContainer.setVisibility(View.VISIBLE);
            startAnimation();
        }
    }

    private boolean isLowCaseLetter(String str) {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        return letters.contains(str);
    }

    private boolean isUpCaseLetter(String str) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return letters.contains(str);
    }

    public boolean isShow() {
        return isOpening||isOpened;
    }

    //隐藏系统键盘关键代码
    private void hideSystemKeyBoard(EditText edit) {
        this.mEditText = edit;
        InputMethodManager imm = (InputMethodManager) this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }

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
                e.printStackTrace();
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                e.printStackTrace();
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

    public void dispatchTouchEvent(Activity activity, MotionEvent me) {
        QDLogger.i("me.getAction()="+me.getAction());
        /*if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = activity.getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me) && !isKeyboard(v)) { //判断用户点击的是否是输入框以外的区域
                QDLogger.i("isShouldHideKeyboard="+me.getAction());
                if (editTextList.contains(v)) {
                    QDLogger.i("contains="+v.getClass().getName());
                    if (isShow()) {
                        hideKeyboard();
                    }
                }
            }
        }*/
    }

    private boolean isKeyboard(View v) {
        return validateParent(v);
    }

    private boolean validateParent(View v) {
        if (v != null) {
            if ((v.getId()) == layout.getId()) {
                return true;
            } else {
                if (v.getParent() != null) {
                    if (v.getParent() instanceof View) {
                        return validateParent((View) v.getParent());
                    }
                }
            }
        }
        return false;
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }
}
