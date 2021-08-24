package cn.demomaster.huan.quickdevelop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;

public class ColorMixView extends androidx.appcompat.widget.AppCompatImageView {
    public ColorMixView(@NonNull @NotNull Context context) {
        super(context);
    }

    public ColorMixView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorMixView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        paint.setColor(Color.YELLOW);
        Rect rect2 = new Rect(0,0,getMeasuredWidth(),getMeasuredHeight());
        //canvas.drawRect(rect2,paint);
        Bitmap bitmap = QDBitmapUtil.drawable2Bitmap(getContext(),R.drawable.umeng_socialize_fav);
        canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),new Rect(0,0,getMeasuredWidth(),getMeasuredHeight()),paint);


        paint.setColor(getResources().getColor(R.color.transparent_light_77));
        Rect rect = new Rect(0,0,getMeasuredWidth(),getMeasuredHeight());
        canvas.drawRect(rect,paint);
    }
}
