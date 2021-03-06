package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.Base64;

import androidx.core.graphics.drawable.DrawableCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @Description: bitmap、drawable转换工具
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


    /**
     * Matrix 缩放宽高
     *
     * @param srcBitmap    源图
     * @param targetwidth  目标宽度
     * @param targetheight 目标高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap srcBitmap, double targetwidth, double targetheight) {
        // 获取这个图片的宽和高
        float width = srcBitmap.getWidth();
        float height = srcBitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) targetwidth) / width;
        float scaleHeight = ((float) targetheight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 采样率压缩
     * 采样率压缩其原理其实也是缩放bitamp的尺寸，通过调节其inSampleSize参数，比如调节为2，宽高会为原来的1/2，内存变回原来的1/4.
     */
    private void compressSampling(float scal, Bitmap bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        //mSrcBitmap = BitmapFactory.decodeResource(bitmap,  options);
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    public static String imageToBase64(String path) {
        File file = new File(path);
        String imgBase64 = null;
        byte[] content = new byte[(int) file.length()];
        try {
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(content);
            inputStream.close();
            imgBase64 = Base64.encodeToString(content, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            QDLogger.e(e);
        } catch (IOException e) {
            QDLogger.e(e);
        } finally {
            return imgBase64;
        }
    }

    /**
     * 生成纯色bitmap
     *
     * @param width
     * @param height
     * @param color
     * @return
     */
    public static Bitmap generateColorBitmap(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(color);//填充颜色
        return bitmap;
    }


    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static String savePhoto(Bitmap photoBitmap, String path, String photoName) {
        String localPath = null;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName + ".jpg");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) { // ת�����
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                localPath = null;
                QDLogger.e(e);
            } catch (IOException e) {
                photoFile.delete();
                localPath = null;
                QDLogger.e(e);
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }
                } catch (IOException e) {
                    QDLogger.e(e);
                }
            }
        }
        return localPath;
    }

    /**
     * 图片明度调整
     *
     * @param bitmap
     * @param value
     * @return
     */
    public static Bitmap setBitmapLight(Bitmap bitmap, int value) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

       /* int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);*/
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = bitmap.getPixel(i, j);//x、y为bitmap所对应的位置
                //QDLogger.i("colorStr1 = " + color);
                if (color != 0) {
                    int r = Color.red(color);
                    int g = Color.green(color);
                    int b = Color.blue(color);
                    //int a = Color.alpha(color);
                    if (value >= 0) {
                        r = r + (255 - r) * value / 255;
                        g = g + (255 - g) * value / 255;
                        b = b + (255 - b) * value / 255;
                    } else {
                        r = r + r * value / 255;
                        g = g + g * value / 255;
                        b = b + b * value / 255;
                    }
                    String r1 = Integer.toHexString(r);
                    String g1 = Integer.toHexString(g);
                    String b1 = Integer.toHexString(b);
                    String colorStr = "#" + r1 + g1 + b1;    //十六进制的颜色字符串。
                    //QDLogger.i("colorStr2 = " + colorStr);
                    int color1 = (r << 16) | (g << 8) | b | color;
                    bitmap.setPixel(i, j, Color.parseColor(colorStr));
                }
            }
        }
        return (bitmap);
    }

    public static Bitmap getBitmapByDrawableId(Context context, int vectorDrawableId) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            return getBitmapByDrawable(vectorDrawable);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
    }

    public static Bitmap getBitmapByDrawable(Drawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapByDrawableId(Context context, int vectorDrawableId, int apha) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static Bitmap drawable2Bitmap(Context context, int resId) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inScaled = false;         //设置这个属性防止因为不同的dpi文件夹导致缩放
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, opt).copy(Bitmap.Config.ARGB_8888, true);
            if (bitmap != null) {
                bitmap.setDensity(context.getResources().getDisplayMetrics().densityDpi);
            }
            //bitmap.recycle();
            return bitmap;
        } catch (Exception e) {
            QDLogger.e(e);
            return null;
        }
    }
}
