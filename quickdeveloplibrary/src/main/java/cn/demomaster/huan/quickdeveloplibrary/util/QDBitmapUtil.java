package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
/*
import core.base.XBaseApplication;*/

/**
 * @Description: bitmap、drawable转换工具
 * @Functions:
 * @Author:
 * @Date: 2016-02-17
 */
public class QDBitmapUtil {

   /* public static Drawable bitmap2Drawable(int resId){
        Drawable d = XBaseApplication.getApplication().getResources().getDrawable(resId);
        return d;
    }*/

    /**
     * bitmap to drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }


    /**
     * drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {//转换成Bitmap
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {//.9图片转换成Bitmap
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ?
                            Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap drawable2Bitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;         //设置这个属性防止因为不同的dpi文件夹导致缩放
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, opt).copy(Bitmap.Config.ARGB_8888, true);
        bitmap.setDensity(context.getResources().getDisplayMetrics().densityDpi);
        return bitmap;
    }

    public static   Bitmap zoomImage(Bitmap bgimage, double newWidth,
                             double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

}
