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
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.StringUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.popup.QDPopup;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.util.QDViewUtil.getActivityFromView;

/**
 * Created by Administrator on 2018/3/7 0007.
 */
public class QDKeyboard_back {
    private static final String TAG = "QDKeyboard";
    private Context mContext;               //上下文
    //private LinearLayout layout;
    private View keyContainer;              //自定义键盘的容器View
    private QDKeyboardView keyboardView;  //键盘的View
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
    private Drawable delDrawable;
    private Drawable lowDrawable;
    private Drawable upDrawable;
    private int keyboardContainerResId;
    private int keyboardResId;

    private EditText mEditText;
    public List<EditText> editTextList;

    public QDKeyboard_back(Activity mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_keyboard_containor, null);
        this.mContext = mContext;
        this.keyboardContainerResId = R.layout.layout_keyboard_containor;
        this.keyboardResId = view.findViewById(R.id.safeKeyboardLetter).getId();
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
            //QDLogger.d("contains=true");
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

    private boolean isTouchedEditText;

    /**
     * 设置默认获取焦点的editText
     *
     * @param editText
     */
    private void setCurrentFocus(EditText editText) {
        mEditText = editText;
        for (int i = 0; i < editTextList.size(); i++) {
            if (editTextList.get(i) == editText) {
                editTextList.get(i).setOnTouchListener((v, event) -> {
                    isTouchedEditText = true;
                    hideSystemKeyBoard((EditText) v);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if ((isClosed || isClosing)) {
                            showKeyboard();
                        }
                    }
                    return false;
                });
                editTextList.get(i).setOnFocusChangeListener((v, hasFocus) -> {
                    hideSystemKeyBoard((EditText) v);
                    //QDLogger.d(v.getId() + (hasFocus ? "得到焦点" : "失去焦点"));
                    if (hasFocus) {
                        if ((isClosed || isClosing)) {
                            showKeyboard();
                        }
                    } else {//如果新的焦点不使用自定义键盘则隐藏
                        waitHideKeyboard();
                    }
                });
            } else {
                editTextList.get(i).setOnTouchListener(null);
                editTextList.get(i).setOnFocusChangeListener((v, hasFocus) -> {
                    hideSystemKeyBoard((EditText) v);
                    //QDLogger.d(v.getId() + (hasFocus ? "得到焦点" : "失去焦点"));
                    if (hasFocus) {
                        // 此处为得到焦点时的处理内容
                        setCurrentFocus((EditText) v);
                        if ((isClosed || isClosing)) {
                            showKeyboard();
                        }
                    } else {//如果新的焦点不使用自定义键盘则隐藏
                        waitHideKeyboard();
                    }
                });
            }
        }
    }

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            View v = getActivityFromView(mEditText).getCurrentFocus();
            //v=((Activity) mEditText.getContext()).getWindow().getDecorView().findFocus();
            //QDLogger.d(v ==null?"null":v.toString()+ v.getId());
            if (v != null) {
                if (!editTextList.contains(v)) {
                    hideKeyboard();
                }
            }
        }
    };

    /**
     * 延迟隐藏
     *
     * @return
     */
    private void waitHideKeyboard() {
        mHandler.removeCallbacks(hideRunnable);
        mHandler.postAtTime(hideRunnable, 50);
    }

    private Handler mHandler = new Handler();
    private boolean isOpened = false;
    private boolean isClosed = true;
    private int duration = 300;
    ValueAnimator animator;
    float keyContainerHeight = -1;

    public void startAnimation() {
        switchKeyboardByInputType();
        final float h1 = isOpening ? keyContainer.getHeight() : 0;
        final float h2 = isOpening ? keyContainerHeight : keyContainer.getHeight();
        animator = ValueAnimator.ofFloat(h1, h2);
        long d = (long) (duration * (isOpening ? (keyContainerHeight - keyContainer.getHeight()) : keyContainer.getHeight()) / keyContainerHeight);
        animator.setDuration(d);
        animator.addUpdateListener(animation -> {
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
        });
        //animator.setRepeatMode(ValueAnimator.REVERSE);
        //animator.setRepeatCount(ValueAnimator.INFINITE);//accelerate_decelerate_interpolator
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    /**
     * 根据输入类型锁定对应的键盘
     */
    private void switchKeyboardByInputType() {
        //字母 case 1: keyboardView.setKeyboard(keyboardLetter);
        //符号 case 2: keyboardView.setKeyboard(keyboardSymbol);
        //数字 case 3: keyboardView.setKeyboard(keyboardNumber);
        switch (mEditText.getInputType()) {
            case InputType.TYPE_CLASS_NUMBER://数字
            case InputType.TYPE_CLASS_PHONE:
            case InputType.TYPE_NUMBER_VARIATION_PASSWORD:
            case InputType.TYPE_NUMBER_VARIATION_NORMAL:
            case InputType.TYPE_NUMBER_FLAG_DECIMAL:
                keyboardView.setKeyboard(keyboardNumber_Only);
                break;
            default:
                keyboardView.setKeyboard(keyboardLetter);
                break;
        }
    }

    public QDPopup qdTipPopup;

    @SuppressLint("ClickableViewAccessibility")
    private void initKeyboard() {
        //解决onresume系统输入法重新弹出，遗留的问题就是不能根据焦点自动弹出输入框了
        ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        keyContainer = LayoutInflater.from(mContext).inflate(keyboardContainerResId, null, false);
        keyboardNumber = new Keyboard(mContext, R.xml.keyboard_num);            //实例化数字键盘
        keyboardNumber_Only = new Keyboard(mContext, R.xml.keyboard_num_only);            //实例化数字键盘
        keyboardLetter = new Keyboard(mContext, R.xml.keyboard_letter);         //实例化字母键盘
        keyboardLetter_Only = new Keyboard(mContext, R.xml.keyboard_letter);         //实例化字母键盘
        keyboardSymbol = new Keyboard(mContext, R.xml.keyboard_symbol);         //实例化符号键盘
        keyboardSymbol_Only = new Keyboard(mContext, R.xml.keyboard_symbol);         //实例化符号键盘
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
                keyContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (keyContainerHeight == -1) {
                    //获取键盘的高度
                    int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    keyContainer.measure(w, h);
                    keyContainerHeight = keyContainer.getHeight();
                    //第一次展示时候高度才确定，要重新关闭再展示一下
                    hideKeyboard();
                    showKeyboard();
                }
            }
        });

        ImageView iv_keyboardDone = keyContainer.findViewById(R.id.iv_keyboardDone);
        Drawable hideDrawable = mContext.getResources().getDrawable(R.drawable.ic_keyboard_hide_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hideDrawable.setTint(mContext.getResources().getColor(R.color.transparent_light_cc));
        }
        iv_keyboardDone.setImageDrawable(hideDrawable);
        iv_keyboardDone.setOnClickListener(v -> hideKeyboard());

        qdTipPopup = new QDPopup(keyContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        qdTipPopup.setFocusable(false);
        qdTipPopup.setTouchable(true);
        qdTipPopup.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp = getActivityFromView(mEditText).getWindow().getAttributes();
            lp.alpha = 1f;
            getActivityFromView(mEditText).getWindow().setAttributes(lp);
            contentView = getActivityFromView(mEditText).getWindow().getDecorView().findViewById(android.R.id.content);
            mDecorView = (View) contentView.getParent();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mDecorView.getLayoutParams();
            layoutParams.bottomMargin = 0;
            layoutParams.topMargin = 0;
            mDecorView.setLayoutParams(layoutParams);
        });

        setDelDrawable(mContext.getResources().getDrawable(R.drawable.icon_del));
        setLowDrawable(mContext.getResources().getDrawable(R.drawable.icon_capital_default));
        setUpDrawable(mContext.getResources().getDrawable(R.drawable.icon_capital_selected));
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
                if (key.label != null && StringUtil.isUpCaseLetter(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] += 32;
                }
            }
        } else {
            for (Keyboard.Key key : keyList) {
                if (key.label != null && StringUtil.isLowCaseLetter(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] -= 32;
                }
            }
        }
        isCapes = !isCapes;
        keyboardView.setCap(isCapes);
    }

    public void hideKeyboard() {
        qdTipPopup.dismiss();
        /*if (isOpened || isOpening) {
            isClosing = true;
            isOpening = false;
            animator.reverse();
        }*/
    }

    View contentView;
    View mDecorView;

    private void showKeyboard() {
        //qdTipPopup.showAsDropDown(mEditText,0,0,Gravity.BOTTOM);
        switchKeyboardByInputType();
        if (qdTipPopup.isShowing()) {
            return;
        }
        contentView = getActivityFromView(mEditText).getWindow().getDecorView().findViewById(android.R.id.content);
        mDecorView = (View) contentView.getParent();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mDecorView.getLayoutParams();
        layoutParams.bottomMargin = getEditorBottomY();
        layoutParams.topMargin = -getEditorBottomY();
        mDecorView.setLayoutParams(layoutParams);
        IBinder iBinder = mEditText.getWindowToken();
        if (iBinder != null && !getActivityFromView(mEditText).isFinishing()) {
            if (!getActivityFromView(mEditText).isFinishing()) {
                qdTipPopup.showAtLocation(getActivityFromView(mEditText).getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM, 0,
                        0);
            }
        }
        /*keyContainer.setFocusable(true);
        keyContainer.setFocusableInTouchMode(true);
        keyContainer.requestFocus();*/
        /*keyboardView.setKeyboard(keyboardLetter);
        if (isClosed || isClosing) {
            isOpening = true;
            isClosing = false;
            keyContainer.setVisibility(View.VISIBLE);
            startAnimation();
        }*/
    }


    /**
     * 获取需要向上调整的布局高度
     *
     * @return
     */
    private int getEditorBottomY() {
        int[] location = new int[2];
        mEditText.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int d = DisplayUtil.getScreenHeight(getActivityFromView(mEditText)) - y - mEditText.getHeight();
        return d > keyContainerHeight ? 0 : (int) (keyContainerHeight - d);
    }

    public boolean isShow() {
        return isOpening || isOpened;
    }

    //隐藏系统键盘关键代码
    private void hideSystemKeyBoard(EditText edit) {
        this.mEditText = edit;
        InputMethodManager imm = (InputMethodManager) this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;
        boolean isOpen = imm.isActive();
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

    private QDKeyboard.OnKeyClickListener onKeyClickListener;

    public void setOnKeyClickListener(QDKeyboard.OnKeyClickListener onKeyClickListener) {
        this.onKeyClickListener = onKeyClickListener;
    }

    View.OnTouchListener onTouchKeyboardListener;

    public void dispatchTouchEvent(MotionEvent me) {
        //QDLogger.i("me.getAction()=" + me.getAction());
       /* if(keyContainer==null||keyContainer.getHeight()==0){
            return;
        }
        int[] location = new int[2];
        keyContainer.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(me.getY()>y){
            onTouchListener.onTouch(keyContainer,null);
        }*/
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


 /*   private boolean isKeyboard(View v) {
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
    }*/

}
