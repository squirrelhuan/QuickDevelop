package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.CameraIDCardActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.SimplePictureActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;


/**
 * 图片采集类
 *
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */

public class PhotoHelper {


    private Context context;

    public PhotoHelper(Context context) {
        this.context = context;
    }

    public void scanQrcode(OnTakePhotoResult onTakePhotoResult) {
        takePhoto2(onTakePhotoResult, RESULT_CODE_SCAN_QRCODE);
    }

    public void takePhoto(OnTakePhotoResult onTakePhotoResult) {
        takePhoto2(onTakePhotoResult, RESULT_CODE_TAKE_PHOTO);
    }

    //图片截取
    public void photoCrop(Uri uri, OnTakePhotoResult onTakePhotoResult) {
        takePhoto2(onTakePhotoResult, RESULT_CODE_PHOTO_CROP, uri);
    }

    // 调用图库获取图片
    public void selectPhotoFromGallery(OnTakePhotoResult onTakePhotoResult) {
        takePhoto2(onTakePhotoResult, RESULT_CODE_SELECT_PHOTO_FROM_GALLERY);
    }

    // 调用自定义图库获取图片
    public void selectPhotoFromMyGallery(OnSelectPictureResult onSelectPictureResult) {
        takePhoto2(onSelectPictureResult, RESULT_CODE_SIMPLE_PICTURE);
    }

    //调用相册并截取
    public void selectPhotoFromGalleryAndCrop(final OnTakePhotoResult onTakePhotoResult) {
        selectPhotoFromGallery(new PhotoHelper.OnTakePhotoResult() {
            @Override
            public void onSuccess(Intent data, String path) {
                photoCrop(data.getData(), onTakePhotoResult);
            }

            @Override
            public void onFailure(String error) {
                onTakePhotoResult.onFailure(error);
            }
        });
    }

    public void takePhotoForIDCard(OnTakePhotoResult onTakePhotoResult) {
        takePhoto2(onTakePhotoResult, RESULT_CODE_TAKE_PHOTO_FOR_IDCARD);
    }

    private void takePhoto2(final Object onTakePhotoResult, final int resultCodeTakePhoto) {
        takePhoto2(onTakePhotoResult, resultCodeTakePhoto, null);
    }

    private void takePhoto2(final Object onTakePhotoResult, final int resultCodeTakePhoto, final Uri uri) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        PermissionManager.chekPermission(context, permissions, new PermissionManager.OnCheckPermissionListener() {
            @Override
            public void onPassed() {
                photoResultMap.put(resultCodeTakePhoto, onTakePhotoResult);
                switch (resultCodeTakePhoto) {
                    case RESULT_CODE_TAKE_PHOTO:
                        takePhoto(resultCodeTakePhoto);
                        break;
                    case RESULT_CODE_PHOTO_CROP:
                        cropPhoto(uri, resultCodeTakePhoto);
                        break;
                    case RESULT_CODE_TAKE_PHOTO_FOR_IDCARD:
                        takePhotoForIDCard(resultCodeTakePhoto);
                        break;
                    case RESULT_CODE_SELECT_PHOTO_FROM_GALLERY:
                        selectPhotoFromGallery(resultCodeTakePhoto);
                        break;
                    case RESULT_CODE_SCAN_QRCODE:
                        scanQrcode(resultCodeTakePhoto);
                        break;
                    case RESULT_CODE_SIMPLE_PICTURE:
                        startSimplePictureActivity(resultCodeTakePhoto,onTakePhotoResult);
                        break;


                }
            }

            @Override
            public void onNoPassed() {

            }
        });
    }


    private Map<Integer, Object> photoResultMap = new HashMap<>();

    private void takePhoto(int resultCodeTakePhoto) {
        Uri fileUri;
        Intent intent = null;
        // 判断存储卡是否可以用，可用进行存储
//        if (StorageUtils.hasSdcard()) {
        //设定拍照存放到自己指定的目录,可以先建好
//            File file = new File(savePath);
        String savePath = Environment.getExternalStorageDirectory().getPath() ;
        File file_Uri = new File(savePath + "/photo.jpg");
        if (!file_Uri.exists()) {
            file_Uri.mkdirs();
        }
        File pictureFile = new File(savePath, "photo.jpg");
        if (!file_Uri.exists()) {
            try {
                file_Uri.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
            fileUri = ((Activity) context).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);//步骤二：Android 7.0及以上获取文件 Uri
            //fileUri = FileProvider.getUriForFile(context, "cn.demomaster.huan.quickdeveloplibrary.fileprovider", pictureFile);

        } else {
            fileUri = Uri.fromFile(pictureFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        ((Activity) context).startActivityForResult(intent, PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO);
    }

    private void cropPhoto(Uri uri, int resultCodeTakePhoto) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        // ���òü�
        intent.putExtra("crop", "true");
        // aspectX aspectY �ǿ�ߵı���
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 10);
        // outputX outputY �ǲü�ͼƬ���
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        ((Activity) context).startActivityForResult(intent, PHOTOHELPER_REQUEST_CODE_PHOTO_CROP);
        //startActivityForResult(CameraIDCardActivity.class,resultCodeTakePhoto);
    }

    private void takePhotoForIDCard(int resultCodeTakePhoto) {
        startActivityForResult(CameraIDCardActivity.class, resultCodeTakePhoto);
    }

    private void selectPhotoFromGallery(int resultCodeTakePhoto) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(intent, PHOTOHELPER_REQUEST_CODE_GALLERY);
    }

    private void scanQrcode(int resultCodeTakePhoto) {
        Intent intent = new Intent(context, CaptureActivity.class);
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */

        //ZxingConfig config = new ZxingConfig();
        //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
        //config.setPlayBeep(true);//是否播放提示音
        //config.setShake(true);//是否震动
        //config.setShowAlbum(true);//是否显示相册
        //config.setShowFlashLight(true);//是否显示闪光灯
        //intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        ((Activity) context).startActivityForResult(intent, PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE);
    }

    private void startSimplePictureActivity(int resultCodeTakePhoto, Object onTakePhotoResult) {
        OnSelectPictureResult onSelectPictureResult = (OnSelectPictureResult) onTakePhotoResult;
        Intent intent = new Intent(context, SimplePictureActivity.class);
        intent.putExtra(PHOTOHELPER_RESULT_CODE, resultCodeTakePhoto);
        intent.putExtra("MaxCount", onSelectPictureResult.getImageCount());
        ((Activity) context).startActivityForResult(intent, PHOTOHELPER_REQUEST_CODE_SIMPLE_PICTURE);
    }


    private void startActivityForResult(Class<CameraIDCardActivity> cameraIDCardActivityClass, int resultCodeTakePhoto) {
        Intent intent = new Intent(context, cameraIDCardActivityClass);
        intent.putExtra(PHOTOHELPER_RESULT_CODE, resultCodeTakePhoto);
        ((Activity) context).startActivityForResult(intent, PHOTOHELPER_REQUEST_CODE_CAMERA);
    }

    public final static int RESULT_CODE_TAKE_PHOTO = 2001;
    public final static int RESULT_CODE_CUSTOM_CAMERA = 2001;
    public final static int RESULT_CODE_PHOTO_CROP = 2002;
    public final static int RESULT_CODE_TAKE_PHOTO_FOR_IDCARD = 2003;
    public final static int RESULT_CODE_SELECT_PHOTO_FROM_GALLERY = 2004;
    public final static int RESULT_CODE_SELECT_PHOTO_FROM_GALLERY_AND_CROP = 2005;
    public final static int RESULT_CODE_SIMPLE_PICTURE = 2020;
    public final static int RESULT_CODE_SCAN_QRCODE = 2006;


    public final static int PHOTOHELPER_REQUEST_CODE_CAMERA = 1000;
    public final static int PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO = 1001;
    public final static int PHOTOHELPER_REQUEST_CODE_PHOTO_CROP = 1002;
    public final static int PHOTOHELPER_REQUEST_CODE_GALLERY = 1003;
    public final static int PHOTOHELPER_REQUEST_CODE_GALLERY_AND_CROP = 1004;
    public final static int PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE = 1005;

    public final static int PHOTOHELPER_REQUEST_CODE_SIMPLE_PICTURE = 1020;

    public final static String PHOTOHELPER_RESULT_CODE = "PHOTOHELPER_RESULT_CODE";
    public final static String PHOTOHELPER_RESULT_PATH = "PHOTOHELPER_RESULT_PATH";
    public final static String PHOTOHELPER_RESULT_PATHES = "PHOTOHELPER_RESULT_PATHES";

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = null;
        OnTakePhotoResult onTakePhotoResult = null;
        OnSelectPictureResult onSelectPictureResult = null;
        if (data != null) {

            switch (resultCode) {
                case RESULT_CODE_TAKE_PHOTO:
                    onTakePhotoResult = (OnTakePhotoResult) photoResultMap.get(resultCode);
                    break;
                case RESULT_CODE_PHOTO_CROP:
                    onTakePhotoResult = (OnTakePhotoResult) photoResultMap.get(resultCode);
                    break;
                case RESULT_CODE_TAKE_PHOTO_FOR_IDCARD:
                    onTakePhotoResult = (OnTakePhotoResult) photoResultMap.get(resultCode);
                    break;
                case RESULT_CODE_SELECT_PHOTO_FROM_GALLERY:
                    onTakePhotoResult = (OnTakePhotoResult) photoResultMap.get(resultCode);
                    break;
                case RESULT_CODE_SIMPLE_PICTURE:
                    onSelectPictureResult = (OnSelectPictureResult) photoResultMap.get(resultCode);
                    break;
                default:
                    onTakePhotoResult = (OnTakePhotoResult) photoResultMap.get(resultCode);
                    break;
            }

            if (onTakePhotoResult != null) {
                if (data.getExtras() != null && data.getExtras().containsKey(PHOTOHELPER_RESULT_PATH)) {
                    path = data.getStringExtra(PHOTOHELPER_RESULT_PATH);
                } else if (data.getExtras() != null && data.getExtras().containsKey(Constant.CODED_CONTENT)) {
                    path = data.getStringExtra(Constant.CODED_CONTENT);
                } else if (data.getData() != null) {
                    path = data.getData().toString();
                }
                onTakePhotoResult.onSuccess(data, path);
            }
            if (onSelectPictureResult != null) {
                Bundle bundle = data.getExtras();
                ArrayList<Image> images = (ArrayList<Image>) bundle.getSerializable(PHOTOHELPER_RESULT_PATHES);
                onSelectPictureResult.onSuccess(data, images);
            }
        }

    }

    public static interface OnTakePhotoResult {
        void onSuccess(Intent data, String path);
        void onFailure(String error);
    }

    public static interface OnSelectPictureResult {
        void onSuccess(Intent data, ArrayList<Image> images);
        void onFailure(String error);
        int getImageCount();
    }

}
