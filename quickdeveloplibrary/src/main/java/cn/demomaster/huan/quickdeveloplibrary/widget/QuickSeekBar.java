package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.ThemeUtils;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class QuickSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private final QuickSeekBarHelper mAppCompatSeekBarHelper;

    public QuickSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public QuickSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarStyle);
    }

    @SuppressLint("RestrictedApi")
    public QuickSeekBar(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ThemeUtils.checkAppCompatTheme(this, getContext());

        mAppCompatSeekBarHelper = new QuickSeekBarHelper(this);
        mAppCompatSeekBarHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mAppCompatSeekBarHelper.drawTickMarks(canvas);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mAppCompatSeekBarHelper.drawableStateChanged();
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        mAppCompatSeekBarHelper.jumpDrawablesToCurrentState();
    }
}
