package cn.demomaster.huan.quickdevelop.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import org.jetbrains.annotations.NotNull;

public class MyDrawerLayout extends DrawerLayout {
    public MyDrawerLayout(@NonNull @NotNull Context context) {
        super(context);
    }

    public MyDrawerLayout(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawerLayout(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
