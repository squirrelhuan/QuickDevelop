package cn.demomaster.huan.quickdeveloplibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2018/10/29.
 */
public class ScreenShotUitl {

    public static View getContentView(Activity context) {
        return context.findViewById(android.R.id.content);
    }
    public static Bitmap shotActivity(Activity activity,boolean withStatusBar) {
        int statusBarHeights = 0;
        if(!withStatusBar){
            // 获取windows中最顶层的view
            View view = activity.getWindow().getDecorView().getRootView();
            // 获取状态栏高度
            Rect rect = new Rect();
            view.getWindowVisibleDisplayFrame(rect);
            statusBarHeights = rect.top;
        }
        return shotActivity(activity, statusBarHeights);
    }

    public static Bitmap shotActivity(Activity activity, int top) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        /*Bitmap bmp = Bitmap.createBitmap(bitmap, 0,
                top, bitmap.getWidth(), bitmap.getHeight());*/
        Bitmap bitmap = view.getDrawingCache();
        Bitmap bmp =null;
        if(bitmap!=null) {
            bmp = Bitmap.createBitmap(bitmap);
        }
        //Bitmap bmp1=bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
        view.setDrawingCacheEnabled(false);
        // 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View v) {
        try {
           /* view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();//这句话可加可不加，因为getDrawingCache()执行的主体就是buildDrawingCache()*/
            if (null == v) {
                return null;
            }
            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache();
            Bitmap bitmap = v.getDrawingCache();
            //Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, w, h);
            v.setDrawingCacheEnabled(false);
            v.destroyDrawingCache();
            return bitmap;
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return null;
    }

    public static Bitmap getCacheBitmapFromView2(View view) {
        Bitmap drawingCache = null;
        try {
            drawingCache = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(drawingCache);
            c.drawColor(Color.WHITE);
            //如果不设置canvas画布为白色，则生成透明
            view.layout(0, 0, view.getWidth(), view.getHeight());
            view.draw(c);
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return drawingCache;
    }

    public static Bitmap getCacheBitmapFromView(View view, int height, int width) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();//这句话可加可不加，因为getDrawingCache()执行的主体就是buildDrawingCache()
        if (width == 0) {
            width = view.getMeasuredWidth();
            if (width == 0) {
                return null;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), height);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 分享截图
     *
     * @param activity
     * @param bitmap
     */
    public static Uri saveImage(Activity activity, Bitmap bitmap) {
        //String time = TimeUtils.getTimeAs_yyyyMMdd(System.currentTimeMillis());
        Uri uri = null;
        String path = android.os.Environment.getExternalStorageDirectory() + "/GENG";
        FileOutputStream outputStream;
        try {
            File file = new File(path);
            if (!file.exists())
                file.mkdirs();
            path = file.getPath() + "/欢迎分享.jpg";
            int permission = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
                return uri;
            }
            outputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            String imageUri = insertImageToSystem(activity, path, "口袋基因", "欢迎来到基因世界");
            uri = Uri.parse(imageUri);
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return uri;
    }

    /**
     * 通知系统资源有更新
     *
     * @param context
     * @param imagePath
     * @param picName
     * @param picDesc
     * @return
     */
    private static String insertImageToSystem(Context context, String imagePath, String picName, String picDesc) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, picName, picDesc);
        } catch (FileNotFoundException e) {
            QDLogger.e(e);
        }
        return url;
    }

    /**
     * 分享图片和文字内容
     *
     * @param dlgTitle 分享对话框标题
     * @param subject  主题
     * @param content  分享内容（文字）
     * @param uri      图片资源URI
     */
    public static void shareImg(Context context, String dlgTitle, String subject, String content,
                                Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (content != null && !"".equals(content)) {
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }

        // 设置弹出框标题
        if (dlgTitle != null && !"".equals(dlgTitle)) { // 自定义标题
            context.startActivity(Intent.createChooser(intent, dlgTitle));
        } else { // 系统默认标题
            context.startActivity(intent);
        }
    }

    public static int pixel(Activity activity, int x, int y) {
        int color = shotActivity(activity,true).getPixel(x, y);
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        return color;
    }
}
