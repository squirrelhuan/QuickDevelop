package cn.demomaster.huan.quickdeveloplibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2018/10/29.
 */
public class ScreenShotUitl {

    public static View getContentView(Activity context){
        return context.findViewById(android.R.id.content);
    }
    public static void shot(final Activity context) {
        View anchor = getContentView(context);
        shot(context,anchor);
    }
    public static void shot(final Activity context, View anchor) {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_screen_shot, null, false);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        PopupWindow window = new PopupWindow(contentView, 100, 100, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        // window.showAsDropDown(anchor, xoff, yoff);
        //window.showAsDropDown(anchor);

        PopupWindow popupWindow = new PopupWindow();
        ((ImageView) contentView.findViewById(R.id.iv_content)).setImageBitmap(shotActivityNoBar(context));
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
        ImageView iv_code = contentView.findViewById(R.id.iv_code);
        Bitmap bitmap_app = BitmapFactory.decodeResource(context.getResources(), R.mipmap.quickdevelop_ic_launcher);
        String codeStr = "http://weixin.qq.com/r/E0M1LcDE6Z2WrYRO9xYB";
        try {
            //////////iv_code.setImageBitmap(BarcodeUtil.createCode(codeStr,bitmap_app));
        }catch (Exception e){
            QDLogger.e(e);
        }

        final View ll_content = contentView.findViewById(R.id.ll_content);
        ll_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Uri uri = saveImage(context,getCacheBitmapFromView(ll_content));
                //shareImg(context, "口袋基因", "口袋基因", "欢迎来到基因世界", uri);
            }
        });

        TextView tv_share = contentView.findViewById(R.id.tv_share);
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = saveImage(context, getCacheBitmapFromView(ll_content));
                shareImg(context, "口袋基因", "口袋基因", "欢迎来到基因世界", uri);
            }
        });

        RelativeLayout rl_root = contentView.findViewById(R.id.rl_root);
        rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    public static Bitmap shotActivityNoBar(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        return shotActivity(activity,statusBarHeights);
    }

    public static Bitmap shotActivity(Activity activity) {
        return shotActivity(activity,0);
    }

    public static Bitmap shotActivity(Activity activity,int top) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                top, widths, heights - top);

        // 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 区域截图
     * @param activity
     * @param x0 左上角的x
     * @param y0 左上角的y
     * @param x1 右下角的x
     * @param y1 右下角的y
     * @return
     */
    public static Bitmap shotActivity(Activity activity,int x0,int y0,int x1,int y1) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), x0, y0, x1-x0, y1-y0);

        // 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 获取一个 View 的缓存视图
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View v) {
        try {
           /* view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();//这句话可加可不加，因为getDrawingCache()执行的主体就是buildDrawingCache()
            bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() - view.getPaddingBottom());
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();*/
            if (null == v) {
                return null;
            }
            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache();
            if (Build.VERSION.SDK_INT >= 11) {
                v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(),
                        View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                        v.getHeight(), View.MeasureSpec.EXACTLY));
                v.layout((int) v.getX(), (int) v.getY(),
                        (int) v.getX() + v.getMeasuredWidth(),
                        (int) v.getY() + v.getMeasuredHeight());
            } else {
                v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            }
            Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

            v.setDrawingCacheEnabled(false);
            v.destroyDrawingCache();
            return b;
        }catch (Exception e){
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
            /** 如果不设置canvas画布为白色，则生成透明 */
            view.layout(0, 0, view.getWidth(), view.getHeight());
            view.draw(c);
        }catch (Exception e){
            QDLogger.e(e);
        }
        return drawingCache;
    }

    public static Bitmap getCacheBitmapFromViewTop(View view,int height){
        return getCacheBitmapFromViewTop( view, height, 0);
    }
    public static Bitmap getCacheBitmapFromViewTop(View view,int height,int width) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();//这句话可加可不加，因为getDrawingCache()执行的主体就是buildDrawingCache()
        if(width==0){
            width = view.getMeasuredWidth();
            if(width==0){
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
    private static void shareImg(Context context, String dlgTitle, String subject, String content,
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

    public static int pixel(Activity activity,int x, int y) {
        int color = shotActivity(activity).getPixel(x, y);
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        return color;
    }
}
