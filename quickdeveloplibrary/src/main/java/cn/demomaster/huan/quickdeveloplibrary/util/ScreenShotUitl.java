package cn.demomaster.huan.quickdeveloplibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.CPopupWindow;

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

        CPopupWindow.PopBuilder builder = new CPopupWindow.PopBuilder(context);
        ((ImageView) contentView.findViewById(R.id.iv_content)).setImageBitmap(shotActivityNoBar(context));
        final PopupWindow popupWindow = builder.setContentView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true).build();
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
        ImageView iv_code = contentView.findViewById(R.id.iv_code);
        Bitmap bitmap_app = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        String codeStr = "http://weixin.qq.com/r/E0M1LcDE6Z2WrYRO9xYB";
        try {
            //////////iv_code.setImageBitmap(BarcodeUtil.createCode(codeStr,bitmap_app));
        }catch (Exception e){
            e.printStackTrace();
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

        // Log.i("CGQ","edn");
    }

    public static Bitmap shotActivityNoBar(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();//这句话可加可不加，因为getDrawingCache()执行的主体就是buildDrawingCache()
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() - view.getPaddingBottom());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
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
            path = file.getPath() + "/" + "欢迎分享" + ".jpg";
            int permission = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
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
            e.printStackTrace();
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
            e.printStackTrace();
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

}
