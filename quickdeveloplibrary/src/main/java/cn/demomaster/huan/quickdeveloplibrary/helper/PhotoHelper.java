package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.CameraIDCardActivity;


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

    public void takephoto3(OnTakePhotoResult onTakePhotoResult) {
        takePhoto2(onTakePhotoResult, RESULT_CODE_TAKE_PHOTO);

    }

    public void takePhotoForIDCard(OnTakePhotoResult onTakePhotoResult) {
        takePhoto2(onTakePhotoResult, RESULT_CODE_TAKE_PHOTO_FOR_IDCARD);
    }

    private void takePhoto2(final OnTakePhotoResult onTakePhotoResult, final int resultCodeTakePhoto) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        PermissionManager.chekPermission(context, permissions, new PermissionManager.OnCheckPermissionListener() {
            @Override
            public void onPassed() {
                photoResultMap.put(resultCodeTakePhoto, onTakePhotoResult);
                switch (resultCodeTakePhoto) {
                    case RESULT_CODE_TAKE_PHOTO:
                        takePhoto(resultCodeTakePhoto);
                        break;
                    case RESULT_CODE_TAKE_PHOTO_FOR_IDCARD:
                        takePhotoForIDCard(resultCodeTakePhoto);
                        break;
                }
            }

            @Override
            public void onNoPassed() {

            }
        });
    }

    private Map<Integer, OnTakePhotoResult> photoResultMap = new HashMap<>();

    private void takePhoto(int resultCodeTakePhoto) {
        startActivityForResult(CameraIDCardActivity.class,resultCodeTakePhoto);
    }


    private void takePhotoForIDCard(int resultCodeTakePhoto) {
        startActivityForResult(CameraIDCardActivity.class,resultCodeTakePhoto);
    }

    private void startActivityForResult(Class<CameraIDCardActivity> cameraIDCardActivityClass, int resultCodeTakePhoto) {
        Intent intent = new Intent(context, cameraIDCardActivityClass);
        intent.putExtra(PHOTOHELPER_RESULT_CODE, resultCodeTakePhoto);
        ((Activity) context).startActivityForResult(intent, PHOTOHELPER_REQUEST_CODE);
    }

    public final static int RESULT_CODE_TAKE_PHOTO = 2001;
    public final static int RESULT_CODE_TAKE_PHOTO_FOR_IDCARD = 2002;

    public final static int PHOTOHELPER_REQUEST_CODE = 1001;
    public final static String PHOTOHELPER_RESULT_CODE = "PHOTOHELPER_RESULT_CODE";
    public final static String PHOTOHELPER_RESULT_PATH = "PHOTOHELPER_RESULT_PATH";
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = null;
        OnTakePhotoResult onTakePhotoResult = null;
        if (data != null) {
            path = data.getStringExtra(PHOTOHELPER_RESULT_PATH);
            switch (resultCode) {
                case RESULT_CODE_TAKE_PHOTO_FOR_IDCARD:
                    onTakePhotoResult = photoResultMap.get(resultCode);
                    break;
                default:
                    onTakePhotoResult = photoResultMap.get(resultCode);
                    break;
            }
            if (onTakePhotoResult!=null&&!TextUtils.isEmpty(path)) {
                onTakePhotoResult.onSuccess(path);
            }else {
                onTakePhotoResult.onFailure("");
            }
        }

    }


    public static interface OnTakePhotoResult {
        void onSuccess(String path);
        void onFailure(String error);
    }

}
