package cn.demomaster.huan.quickdeveloplibrary.view.keybored;

import android.content.Context;

public class QDInputKeyboard extends android.inputmethodservice.Keyboard {
    public QDInputKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public QDInputKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public QDInputKeyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public QDInputKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }
}
