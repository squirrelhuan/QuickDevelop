package cn.demomaster.huan.quickdeveloplibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
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

    /**
     * 按比例缩放图片
     *
     * @param bitmap 原图
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float scaleX, float scaleY) {
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(scaleX, scaleY);
        Bitmap newBM = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBM;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            return null;
        }
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float scaleX = ((float) newWidth) / width;
        float scaleY = ((float) newHeight) / height;
        return scaleBitmap(bitmap, scaleX, scaleY);
    }

    /**
     * 居中裁剪正方形
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public static Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = Math.min(h, w);// 裁切后所取的正方形区域边长
        return cropBitmap(bitmap, (w - cropWidth) / 2, (h - cropWidth) / 2, cropWidth, cropWidth);
    }

    public static Bitmap cropBitmap(Bitmap bitmap, int centerX, int centerY, int tartgetWidth, int tartgetHeight) {
        return Bitmap.createBitmap(bitmap, centerX, centerY, tartgetWidth, tartgetHeight, null, false);
    }

    public static Bitmap cropBitmap(Bitmap bitmap, int width, int height) {
        float aspectRatio = width / (height * 1f);//宽高比
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float aspectRatio2 = w / (h * 1f);//宽高比
        int x = 0;
        int y = 0;
        int new_width = w;
        int new_height = h;
        if (aspectRatio > aspectRatio2) {//宽度不足裁剪高度
            new_height = (int) (w / (aspectRatio * 1f));
            y = (h - new_height) / 2;
        } else if (aspectRatio < aspectRatio2) {//高度不足裁剪宽度
            new_width = (int) (h * (aspectRatio * 1f));
            x = (w - new_width) / 2;
        }

        return Bitmap.createBitmap(bitmap, x, y, new_width, new_height, null, false);
    }

    public static void setBackground(View view, Bitmap bitmap) {
        if (bitmap == null) {
            view.setBackgroundResource(0);
            return;
        }

        int vwidth = view.getWidth();
        int vheight = view.getHeight();
        int bwidth = bitmap.getWidth();
        int bheight = bitmap.getHeight();

        float scalex = (float) vwidth / bwidth;
        float scaley = (float) vheight / bheight;
        float scale = Math.max(scalex, scaley) * 1.0f;

        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap background = Bitmap.createBitmap(vwidth, vheight, config);

        Canvas canvas = new Canvas(background);
        Matrix matrix = new Matrix();
        matrix.setTranslate(-bwidth / 2f, -bheight / 2f);
        matrix.postScale(scale, scale);
        matrix.postTranslate(vwidth / 2f, vheight / 2f);

        canvas.drawBitmap(bitmap, matrix, null);
        view.setBackground(new BitmapDrawable(view.getResources(), background));//setBackgroundDrawable
    }

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
    public static Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
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
            return getBitmapFormDrawable(drawable);
        } else {
            return getBitmapFormDrawable(drawable);
        }
    }

    public static Bitmap getBitmapFormDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE
                        ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //设置绘画的边界，此处表示完整绘制
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 通过Uir获取bitmap
     *
     * @param uri
     * @param mContext
     * @return
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap zoomImageWithWidth(Bitmap srcBitmap, int width) {
        float scale = width / (srcBitmap.getWidth() * 1.f);
        return zoomImage(srcBitmap, scale);
    }

    /**
     * 等比例缩放
     *
     * @param srcBitmap
     * @param scale
     * @return
     */
    public static Bitmap zoomImage(Bitmap srcBitmap, double scale) {
        return zoomImage(srcBitmap, srcBitmap.getWidth() * scale, srcBitmap.getHeight() * scale);
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
        if (srcBitmap == null) {
            return null;
        }
        // 获取这个图片的宽和高
        float width = srcBitmap.getWidth();
        float height = srcBitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) targetwidth) / (width == 0 ? 1 : width);
        float scaleHeight = ((float) targetheight) / (height == 0 ? 1 : height);
        //QDLogger.println("targetwidth="+targetwidth+",targetheight="+targetheight+",width="+width+",height="+height);
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 合并图层
     *
     * @param background
     * @param foreground
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        //int fgWidth = foreground.getWidth();
        //int fgHeight = foreground.getHeight();
        //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);//在 0，0坐标开始画入bg
        //draw fg into
        cv.drawBitmap(foreground, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
        //save all clip
        //cv.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
        //cv.restore();//存储
        return newbmp;
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
        if(bitmap==null){
            return 0;
        }
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
        }
        return imgBase64;
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
                    try {
                        int color3 = Color.parseColor(colorStr);
                        bitmap.setPixel(i, j, color3);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return (bitmap);
    }

    public static Bitmap getBitmapByDrawableId(Context context, int vectorDrawableId) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), vectorDrawableId, null);
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
            Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), vectorDrawableId, null);
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

    public static Bitmap getBitmapFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        return getBitmapFromPath(file.getAbsolutePath());
    }

    public static Bitmap getBitmapFromPath(String path) {
        if (!TextUtils.isEmpty(path)) {
            try {
                FileInputStream fis = new FileInputStream(path);
                return BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
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


    /**
     * 图片裁剪功能
     *
     * @param context
     * @param srcUri      源路径
     * @param targetUri   输出路径
     * @param requestCode
     */
    public static void startPhotoZoom(Activity context, Uri srcUri, Uri targetUri,
                                      int requestCode) {
        int dp = 800;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(uri.getPath()));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(srcUri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
        intent.putExtra("outputX", dp);//输出X方向的像素
        intent.putExtra("outputY", dp);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        if (targetUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
        } else {
            if (Build.VERSION.SDK_INT < 30) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
            } else {
                String path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
                //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
                //storage/emulated/0/Pictures
                File mOnputFile = new File(path, System.currentTimeMillis() + ".png");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file://" + mOnputFile.getAbsolutePath()));
            }
        }
        intent.putExtra("return-data", false);//如果此处指定，返回值的data为null
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * getRoundBiemap
     *
     * @param bitmap ����Bitmap����
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap, float roundPx) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        int min = Math.min(width, height);

        left = 0;
        right = 0;
        top = 0;
        bottom = min;
        width = min;

        dst_left = 0;
        dst_top = 0;
        dst_right = min;
        dst_bottom = min;

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// ���û����޾��
        canvas.drawARGB(0, 0, 0, 0); // �������Canvas
        paint.setColor(color);

        if (roundPx != -1) {
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
        } else {
            canvas.drawCircle(roundPx, roundPx, roundPx, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// ��������ͼƬ�ཻʱ��ģʽ,�ο�http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // ��Mode.SRC_INģʽ�ϲ�bitmap���Ѿ�draw�˵�Circle
        return output;
    }

}
