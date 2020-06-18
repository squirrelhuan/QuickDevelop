package cn.demomaster.huan.quickdevelop.activity.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.CameraIDCardActivity;
import cn.demomaster.huan.quickdeveloplibrary.constant.FilePath;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

@ActivityPager(name = "身份证读取",preViewClass = TextView.class,resType = ResType.Resource)
public class IDCardActivity extends QDActivity {

    @BindView(R.id.main_image)
    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
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
     * 拍摄证件照片
     * @param type 拍摄证件类型
     */
    private void takePhoto(final int type) {
        getPhotoHelper().takePhotoForIDCard(new PhotoHelper.OnTakePhotoResult() {
            @Override
            public void onSuccess(Intent data, String path){
                if (!TextUtils.isEmpty(path)) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                }
                String imgBase64 = imageToBase64(path);
                Log.i("CGQ",imgBase64);

                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    imageView.setImageBitmap(photo);
                    //photo = ImageUtils.toRoundBitmap(photo, fileUri); // ���ʱ���ͼƬ�Ѿ��������Բ�ε���
                    //photo = ImageUtils.savePhoto(photo, Constants.APP_PATH_PICTURE, "")
                    //iv_personal_icon.setImageBitmap(photo);
                    //uploadPic(photo);
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    /**
     * 身份证正面
     */
    public void frontIdCard2(View view) {
        takePhoto(CameraIDCardActivity.TYPE_ID_CARD_FRONT);
    }

    /**
     * 身份证正面
     */
    public void frontIdCard(View view) {
        getPhotoHelper().takePhotoForIDCard(new PhotoHelper.OnTakePhotoResult() {
            @Override
            public void onSuccess(Intent data,String path) {
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
    public void backIdCard2(View view) {
        takePhoto(CameraIDCardActivity.TYPE_ID_CARD_BACK);
    }


    /**
     * 身份证反面
     */
    public void backIdCard(View view) {
        SelectPhotoPopupWindow();
    }

    QDDialog qdDialog;
    //选择照片的popupWindow
    private void SelectPhotoPopupWindow() {
        //@drawable/listview_option_menu
        qdDialog = new QDDialog.Builder(mContext)
                .setMessage("请选择你要上传的图片")
                .setBackgroundRadius(50)
                .addAction("拍照", new QDDialog.OnClickActionListener() {
                    @Override
                    public void onClick(QDDialog dialog) {
                        dialog.dismiss();
                        getPhotoHelper().takePhoto(new PhotoHelper.OnTakePhotoResult() {
                            @Override
                            public void onSuccess(Intent data, String path) {
                                Log.i(TAG,"map_path="+path);
                                String map_str2 = JSON.toJSONString(data);
                                Log.i(TAG,"map_str2="+map_str2);
                                if (!TextUtils.isEmpty(path)) {
                                    imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                                }

                                Bundle extras = data.getExtras();
                                if (extras != null) {
                                    Bitmap photo = extras.getParcelable("data");
                                    imageView.setImageBitmap(photo);
                                    //photo = ImageUtils.toRoundBitmap(photo, fileUri); // ���ʱ���ͼƬ�Ѿ��������Բ�ε���
                                    //photo = ImageUtils.savePhoto(photo, Constants.APP_PATH_PICTURE, "")
                                    //iv_personal_icon.setImageBitmap(photo);
                                    //uploadPic(photo);
                                }
                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        });

                    }
                }).addAction("从相册中选取", new QDDialog.OnClickActionListener() {
            @Override
            public void onClick(QDDialog dialog) {
                dialog.dismiss();
                getPhotoHelper().selectPhotoFromGallery(new PhotoHelper.OnTakePhotoResult() {
                    @Override
                    public void onSuccess(Intent data, String path) {
                        QDLogger.i("path="+path);
                    }

                    @Override
                    public void onFailure(String error) {
                        QDLogger.i("error="+error);
                    }
                });
            }
        }).setGravity_foot(Gravity.CENTER).create();
        qdDialog.show();

      /*  pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);*/

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
