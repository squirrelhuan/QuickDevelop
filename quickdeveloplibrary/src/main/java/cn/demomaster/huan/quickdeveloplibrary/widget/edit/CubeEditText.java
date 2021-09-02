package cn.demomaster.huan.quickdeveloplibrary.widget.edit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.demomaster.qdlogger_library.QDLogger;

public class CubeEditText extends androidx.appcompat.widget.AppCompatEditText {
    public CubeEditText(Context context) {
        super(context);
        init(context);
    }

    public CubeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CubeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        /*super.creat
        if (mEditor == null) {
            mEditor = new Editor(this);
        }*/
        getEditor();
        addTextChangedListener(null);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (watcher != null)
                    watcher.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (watcher != null)
                    watcher.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (watcher != null)
                    watcher.afterTextChanged(s);
                // postInvalidate();
            }
        };
        super.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Layout mLayout = getLayout();
        //mLayout.draw(canvas, highlight, mHighlightPaint, cursorOffsetVertical);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        Rect sTempRect = (Rect) getPrivateValueByName(mLayout.getClass(), "sTempRect");
        if (sTempRect != null) {
            RectF rectF = new RectF(sTempRect.left, sTempRect.top, sTempRect.right, sTempRect.bottom);
            canvas.drawRoundRect(rectF, 5, 5, mPaint);
        }
        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();
        final int compoundPaddingBottom = getCompoundPaddingBottom();
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, 5, 5, mPaint);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            RectF rectF2 = null;
            float letterSpacing = getLetterSpacing();
            mPaint.setLetterSpacing(letterSpacing);
            float cw = (letterSpacing + 1) * getTextSize();
            int count = (int) (mLayout.getWidth() / getTextSize());
            if (cw != 0) {
                count = (int) Math.min(Math.floor(mLayout.getWidth() / cw), 30);
            }
            int[] colors = new int[]{Color.RED, Color.LTGRAY, Color.YELLOW, Color.GREEN, Color.BLUE, Color.BLACK, Color.CYAN};
            for (int i = 0; i < count; i++) {
                mPaint.setColor(colors[i % 7]);
                mPaint.setStrokeWidth(5);
                float textWidth = (i + 1) * getTextSize();
                float charWidth = getTextSize();
                float left = 0;
                if (!TextUtils.isEmpty(getText()) && getText().length() > i) {
                    textWidth = getPaint().measureText(getText().toString(), 0, i + 1);
                    charWidth = getPaint().measureText(getText().toString(), i, i + 1);
                    left = compoundPaddingLeft + textWidth - charWidth;
                } else {
                    left = (float) (compoundPaddingLeft + textWidth - charWidth + letterSpacing * (i + 0.5) * getTextSize());
                }
                // QDLogger.i("textWidth="+textWidth+",charWidth="+charWidth+",left="+left);
                rectF2 = new RectF(left, compoundPaddingTop, left + charWidth, mLayout.getHeight() + compoundPaddingTop);
                canvas.drawRoundRect(rectF2, 5, 5, mPaint);
            }
        }
    }

    private Layout getLayout1() {
        Layout mLayout = (Layout) getPrivateValueByName(this, "mLayout");
        return mLayout;
    }

    private Object getEditor() {
        TextView t = this;//改成你要操作的子类
        Class className = t.getClass();
        try {
            for (; className != Object.class; className = className.getSuperclass()) {//获取本身和父级对象
                Field[] fields = className.getDeclaredFields();//获取所有私有字段
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equals("")) {
                        return (field.get(t) == null ? null : field.get(t));
                    }
                }
            }
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return null;
    }

    /**
     * @param targetClazz 你要操作的子类
     * @param paramName
     * @return
     */
    private Object getPrivateValueByName(Object targetClazz, String paramName) {
        Class className = targetClazz.getClass();
        try {
            for (; className != Object.class; className = className.getSuperclass()) {//获取本身和父级对象
                Field[] fields = className.getDeclaredFields();//获取所有私有字段
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equals(paramName)) {
                        return (field.get(targetClazz) == null ? null : field.get(targetClazz));
                    }
                }
            }
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return null;
    }

}
