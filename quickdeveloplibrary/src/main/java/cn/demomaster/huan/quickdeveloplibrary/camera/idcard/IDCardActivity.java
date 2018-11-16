package cn.demomaster.huan.quickdeveloplibrary.camera.idcard;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.constant.FilePath;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper.PHOTOHELPER_RESULT_PATH;


public class IDCardActivity extends BaseActivityParent {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        imageView = (ImageView) findViewById(R.id.main_image);
    }

    private String imageToBase64(String path) {
        File file = new File(path);
        String imgBase64 =null ;
        byte[] content = new byte[(int) file.length()];
        try {
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(content);
            inputStream.close();
            imgBase64 = Base64.encodeToString( content,Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return imgBase64;
        }
    }

    /**
     * 身份证正面
     */
    public void frontIdCard(View view) {
        photoHelper.takePhotoForIDCard(new PhotoHelper.OnTakePhotoResult() {
            @Override
            public void onSuccess(String path) {
                if (!TextUtils.isEmpty(path)) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                }
                String imgBase64 = imageToBase64(path);
                Log.i("CGQ",imgBase64);
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
    /**
     * 身份证反面
     */
    public void backIdCard(View view) {
        SelectPhotoPopupWindow();
    }

    //选择图片dialog
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    //选择照片的popupWindow
    private void SelectPhotoPopupWindow() {
        pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows_as_dialog, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button btn_camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button btn_file = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.scaling_big));
            }
        });
        //拍照
        btn_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                takePicture();
                camera();
            }
        });
        //文件选取
        btn_file.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                getPictureByGallery(mContext);
            }
        });
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scaling_big));
    }

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;

    /**
     * 从拍照获取图片
     */
    public void camera() {
        Intent intent = null;
        // 判断存储卡是否可以用，可用进行存储
//        if (StorageUtils.hasSdcard()) {
        //设定拍照存放到自己指定的目录,可以先建好
//            File file = new File(savePath);
        String savePath = Environment.getExternalStorageDirectory().getPath() + "/photo.jpg";
        File file_Uri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
        if (!file_Uri.exists()) {
            file_Uri.mkdirs();
        }
        File pictureFile = new File(savePath, "photo.jpg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
            fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = Uri.fromFile(pictureFile);
        }
        if (intent != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    fileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
//        }
    }


    private static final int GALLERY_Photo_REQUEST_CODE = 300;
    /**
     * 调用图库获取图片
     *
     * @param context 必须是Activity
     */
    public void getPictureByGallery(Activity context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent, GALLERY_Photo_REQUEST_CODE);
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(FilePath.APP_PATH_CACHE_DIR);

        try {
            mediaStorageDir = new File(FilePath.APP_PATH_PICTURE);
            Log.d(TAG, "Successfully created mediaStorageDir: " + mediaStorageDir);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error in Creating mediaStorageDir: " + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                File file = new File(mediaStorageDir.getPath() + "/.nomedia");// 用来屏蔽相册读取
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                //return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else if (type == MEDIA_TYPE_AUDIO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "AUD_" + timeStamp + ".mp3");
        } else {
            return null;
        }

        return mediaFile;
    }




}
