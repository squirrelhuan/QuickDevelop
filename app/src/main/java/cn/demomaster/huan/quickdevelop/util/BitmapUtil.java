package cn.demomaster.huan.quickdevelop.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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
public class BitmapUtil {

   /* public static Drawable bitmap2Drawable(int resId){
        Drawable d = XBaseApplication.getApplication().getResources().getDrawable(resId);
        return d;
    }*/

    /**
     * bitmap to drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap){
        return new BitmapDrawable(bitmap);
    }


    /**
     * drawable to bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable){
        if(drawable instanceof BitmapDrawable){//转换成Bitmap
            return ((BitmapDrawable)drawable).getBitmap() ;
        }else if(drawable instanceof NinePatchDrawable){//.9图片转换成Bitmap
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
        }else{
            return null ;
        }
    }


}
