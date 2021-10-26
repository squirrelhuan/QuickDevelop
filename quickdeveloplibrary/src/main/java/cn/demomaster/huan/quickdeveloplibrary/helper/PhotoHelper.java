package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.CameraIDCardActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.SimplePictureActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.ui.MyCaptureActivity;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.OnReleaseListener;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static android.app.Activity.RESULT_OK;
import static cn.demomaster.huan.quickdeveloplibrary.ui.MyCaptureActivity.CODED_CONTENT;

/**
 * 图片采集类
 *
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */

public class PhotoHelper implements OnReleaseListener {
    String authority;//FileProvider
    @Override
    public void onRelease(Object self) {
        if(self instanceof Activity) {
            contextWeakReference.clear();
            if (currentBuilder != null) {
                currentBuilder.setOnTakePhotoResult(null);
                currentBuilder.setOnSelectPictureResult(null);
                currentBuilder.setOutUri(null);
                currentBuilder.setSrcUri(null);
            }
        }
    }

    public enum RequestType {
        takePhoto(0),//拍照
        scanQrcode(1),//扫描二维码
        photoCrop(2),//图片裁剪
        takePhotoForIDCard(3),
        selectFromGallery(4),
        selectFromMyGallery(5);//

        private int value = 0;

        RequestType(int value) {//必须是private的，否则编译错误
            this.value = value;
        }

        public static RequestType getEnum(int value) {
            RequestType resultEnum = null;
            RequestType[] enumArray = RequestType.values();
            for (RequestType requestType : enumArray) {
                if (requestType.value() == value) {
                    resultEnum = requestType;
                    break;
                }
            }
            return resultEnum;
        }

        public int value() {
            return this.value;
        }
    }

    private final WeakReference<Activity> contextWeakReference;
    public PhotoHelper(Activity context,String authority) {
        this.contextWeakReference = new WeakReference<>(context);
        this.authority = authority;
    }

    public void scanQrcode(OnTakePhotoResult onTakePhotoResult) {
        Builder builder = new Builder(contextWeakReference.get());
        builder.setRequestType(RequestType.scanQrcode);
        builder.onTakePhotoResult = onTakePhotoResult;
        takePhoto2(builder);
    }

    /**
     * 调用相机拍照
     *
     * @param onTakePhotoResult
     * @param outUri            拍照保存的路径
     */
    public void takePhoto(Uri outUri, OnTakePhotoResult onTakePhotoResult) {
        Builder builder = new Builder(contextWeakReference.get());
        builder.setOutUri(outUri);
        builder.setRequestType(RequestType.takePhoto);
        builder.onTakePhotoResult = onTakePhotoResult;
        takePhoto2(builder);
    }

    /**
     * 调用相机拍照并且裁剪
     *
     * @param onTakePhotoResult
     */
    public void takePhotoCrop(Uri outUri, OnTakePhotoResult onTakePhotoResult) {
        Builder builder = new Builder(contextWeakReference.get());
        builder.setOutUri(outUri);
        builder.setRequestType(RequestType.takePhoto);
        builder.onTakePhotoResult = new OnTakePhotoResult() {
            @Override
            public void onSuccess(Intent data, String path) {
                builder.setSrcUri(outUri);
                builder.setOnTakePhotoResult(onTakePhotoResult);
                photoCrop(builder);
            }

            @Override
            public void onFailure(String error) {

            }
        };
        takePhoto2(builder);
    }

    /**
     * 图片截取
     */
    public void photoCrop(Builder builder) {
        if (builder == null) {
            return;
        }
        builder.setRequestType(RequestType.photoCrop);
        takePhoto2(builder);
    }

    /**
     * 调用图库获取图片
     *
     * @param onTakePhotoResult
     */
    public void selectPhotoFromGallery(OnTakePhotoResult onTakePhotoResult) {
        Builder builder = new Builder(contextWeakReference.get());
        builder.setOnTakePhotoResult(onTakePhotoResult);
        builder.setRequestType(RequestType.selectFromGallery);
        takePhoto2(builder);
    }

    /**
     * 调用自定义图库获取图片
     *
     * @param onSelectPictureResult
     */
    public void selectPhotoFromMyGallery(OnSelectPictureResult onSelectPictureResult) {
        Builder builder = new Builder(contextWeakReference.get());
        builder.setRequestType(RequestType.selectFromMyGallery);
        builder.setOnSelectPictureResult(onSelectPictureResult);
        takePhoto2(builder);
    }

    /**
     * 调用相册并截取
     *
     * @param onTakePhotoResult
     */
    public void selectPhotoFromGalleryAndCrop(Uri outUri, final OnTakePhotoResult onTakePhotoResult) {
        Builder builder = new Builder(contextWeakReference.get());
        builder.setOutUri(outUri);
        builder.setRequestType(RequestType.selectFromGallery);
        builder.onTakePhotoResult = new OnTakePhotoResult() {
            @Override
            public void onSuccess(Intent data, String path) {
                QDLogger.e("selectPhotoFromGalleryAndCrop 2 =" + data + "," + path);
                if (data != null) {
                    builder.setSrcUri(data.getData());
                } else {
                    builder.setSrcUri(Uri.parse(path));
                }
                builder.setOutUri(outUri);
                builder.setOnTakePhotoResult(onTakePhotoResult);
                photoCrop(builder);
            }

            @Override
            public void onFailure(String error) {

            }
        };
        takePhoto2(builder);
    }

    /**
     * 拍摄身份证
     *
     * @param onTakePhotoResult
     */
    public void takePhotoForIDCard(OnTakePhotoResult onTakePhotoResult) {
        Builder builder = new Builder(contextWeakReference.get());
        builder.setOnTakePhotoResult(onTakePhotoResult);
        builder.setRequestType(RequestType.takePhotoForIDCard);
        takePhoto2(builder);
    }

   /* private void takePhoto2(final Object onTakePhotoResult, final int resultCodeTakePhoto) {
        takePhoto2(onTakePhotoResult, resultCodeTakePhoto, null);
    }*/

    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    Builder currentBuilder;
    private int mRequestCode = 1111;

    /**
     * 设置请求码，防止请求码冲突的情况
     *
     * @param mRequestCode
     */
    public void setRequestCode(int mRequestCode) {
        this.mRequestCode = mRequestCode;
    }

    private void takePhoto2(Builder builder) {
        if (builder.requestCode == -1) {
            builder.requestCode = mRequestCode;
        }
        // final Object onTakePhotoResult,
        PermissionHelper.requestPermission(contextWeakReference.get(), permissions, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                final RequestType requestType = builder.requestType;
                currentBuilder = builder;
                Uri uri1 = builder.outUri;
                switch (requestType) {
                    case takePhoto: //PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO://拍照
                        uri1 = takePhoto(builder);
                        break;
                    case photoCrop:// PHOTOHELPER_REQUEST_CODE_PHOTO_CROP://图片截取
                        cropPhotoImp(builder);
                        break;
                    case takePhotoForIDCard://PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO_FOR_IDCARD://拍身份证
                        takePhotoForIDCardImp(builder);
                        break;
                    case selectFromGallery:// PHOTOHELPER_REQUEST_CODE_GALLERY://从相册选取照片
                        selectPhotoFromGalleryImp(builder);
                        break;
                    case scanQrcode:// PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE://扫描二维码
                        scanQrcodeImp(builder);
                        break;
                    case selectFromMyGallery://PHOTOHELPER_REQUEST_CODE_GALLERY2://从自定义图库选择
                        startSimplePictureActivityImp(builder);
                        break;
                }

                if (builder.onTakePhotoResult instanceof OnTakePhotoResult) {
                    builder.onTakePhotoResult.setTag(uri1);
                    currentBuilder = builder;
                }
            }

            @Override
            public void onRefused() {
                if (builder.onTakePhotoResult instanceof OnTakePhotoResult) {
                    builder.onTakePhotoResult.onFailure("权限不足");
                }
            }
        });
    }

    private Uri takePhoto(Builder builder) {
        Uri fileUri = builder.outUri;
        if (fileUri == null) {
            // 判断存储卡是否可以用，可用进行存储
            // if (StorageUtils.hasSdcard()) {
            String savePath = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + contextWeakReference.get().getPackageName() + "/photo";
            File file = new File(savePath, System.currentTimeMillis() + ".jpg");
            if (!file.exists()) {
                QDFileUtil.createFile(file);
            }
            fileUri = QDFileUtil.getUrifromFile(contextWeakReference.get(),authority, file);
        } else {
            File file = new File(fileUri.getPath());
            if (!file.exists()) {
                QDFileUtil.createFile(file);
            }
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);//如果此处指定，返回值的data为null
        contextWeakReference.get().startActivityForResult(intent, builder.requestCode);
        return fileUri;
    }

    /**
     * 图片裁剪功能
     */
    private static void cropPhotoImp(Builder builder) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(uri.getPath()));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(builder.srcUri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", builder.aspectX);//输出是X方向的比例
        intent.putExtra("aspectY", builder.aspectY);
        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
        intent.putExtra("outputX", builder.outputX);//输出X方向的像素
        intent.putExtra("outputY", builder.outputY);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        if (builder.outUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, builder.outUri);
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
            } else {
                String path = builder.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
                //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
                //storage/emulated/0/Pictures
                File mOnputFile = new File(path, System.currentTimeMillis() + ".png");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file://" + mOnputFile.getAbsolutePath()));
            }
        }
        intent.putExtra("return-data", false);//如果此处指定，返回值的data为null
        builder.context.startActivityForResult(intent, builder.requestCode);
    }

    private void takePhotoForIDCardImp(Builder builder) {
        startActivityForResult(CameraIDCardActivity.class, builder.requestCode);
    }

    private void selectPhotoFromGalleryImp(Builder builder) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        //intent.setType("image/*");
        contextWeakReference.get().startActivityForResult(intent, builder.requestCode);
    }

    private void scanQrcodeImp(Builder builder) {
        Intent intent = new Intent(contextWeakReference.get(), MyCaptureActivity.class);
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
        contextWeakReference.get().startActivityForResult(intent, builder.requestCode);
    }

    /**
     * 打开图片选择器页面
     */
    private void startSimplePictureActivityImp(Builder builder) {
        OnSelectPictureResult onSelectPictureResult = builder.onSelectPictureResult;
        Intent intent = new Intent(contextWeakReference.get(), SimplePictureActivity.class);
        //intent.putExtra(PHOTOHELPER_RESULT_CODE, resultCodeTakePhoto);
        intent.putExtra("MaxCount", onSelectPictureResult.getImageCount());
        contextWeakReference.get().startActivityForResult(intent, builder.requestCode);
    }

    private void startActivityForResult(Class<CameraIDCardActivity> cameraIDCardActivityClass, int requestCode) {
        Intent intent = new Intent(contextWeakReference.get(), cameraIDCardActivityClass);
        //intent.putExtra(PHOTOHELPER_RESULT_CODE, resultCodeTakePhoto);
        contextWeakReference.get().startActivityForResult(intent, requestCode);
    }

 /*   public final static int RESULT_CODE_TAKE_PHOTO = 2001;
    public final static int RESULT_CODE_CUSTOM_CAMERA = 2001;
    public final static int RESULT_CODE_PHOTO_CROP = 2002;
    public final static int RESULT_CODE_TAKE_PHOTO_FOR_IDCARD = 2003;
    public final static int RESULT_CODE_SELECT_PHOTO_FROM_GALLERY = 2004;
    public final static int RESULT_CODE_SELECT_PHOTO_FROM_GALLERY_AND_CROP = 2005;
    public final static int RESULT_CODE_SIMPLE_PICTURE = 2020;
    public final static int RESULT_CODE_SCAN_QRCODE = 2006;*/

/*    public final static int PHOTOHELPER_REQUEST_CODE_CAMERA = 1000;//拍照
    public final static int PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO = 1001;//拍照
    public final static int PHOTOHELPER_REQUEST_CODE_PHOTO_CROP = 1002;//图片裁剪
    public final static int PHOTOHELPER_REQUEST_CODE_GALLERY = 1003;//从图库选择
    public final static int PHOTOHELPER_REQUEST_CODE_GALLERY2 = 1004;//从自定义图库选择
    public final static int PHOTOHELPER_REQUEST_CODE_GALLERY_AND_CROP = 1005;
    public final static int PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE = 1006;//扫描二维码
    public final static int PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO_FOR_IDCARD = 1007;//拍身份证*/


    //public final static String PHOTOHELPER_RESULT_CODE = "PHOTOHELPER_RESULT_CODE";
    public final static String PHOTOHELPER_RESULT_PATH = "PHOTOHELPER_RESULT_PATH";
    public final static String PHOTOHELPER_RESULT_PATHES = "PHOTOHELPER_RESULT_PATHES";
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultImp(requestCode, resultCode, data);
        /*switch (requestCode) {
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE://扫描
                onActivityResultImp(requestCode, PhotoHelper.RESULT_CODE_SCAN_QRCODE, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_CAMERA://自定义拍照
                onActivityResultImp(requestCode, resultCode, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO://拍照
                onActivityResultImp(requestCode, PhotoHelper.RESULT_CODE_TAKE_PHOTO, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_GALLERY://相册选取
                onActivityResultImp(requestCode, PhotoHelper.RESULT_CODE_SELECT_PHOTO_FROM_GALLERY, data);//偷梁换柱
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_PHOTO_CROP://图片截取
                onActivityResultImp(requestCode, PhotoHelper.RESULT_CODE_PHOTO_CROP, data);//偷梁换柱
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_GALLERY_AND_CROP://相册并截取
                onActivityResultImp(requestCode, PhotoHelper.RESULT_CODE_SELECT_PHOTO_FROM_GALLERY_AND_CROP, data);//偷梁换柱
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_SIMPLE_PICTURE://自定义的图片选择器
                onActivityResultImp(requestCode, PhotoHelper.RESULT_CODE_SIMPLE_PICTURE, data);//偷梁换柱
                break;
        }*/
    }

    public void onActivityResultImp(int requestCode, int resultCode, Intent data) {
        if (currentBuilder == null || currentBuilder.requestCode != requestCode) {
            return;
        }
        Builder builder = currentBuilder;
        currentBuilder = null;
        String path = null;
        OnTakePhotoResult onTakePhotoResult = null;
        OnSelectPictureResult onSelectPictureResult = null;
        switch (builder.requestType) {
            case takePhoto: //PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO:
            case photoCrop: //PHOTOHELPER_REQUEST_CODE_PHOTO_CROP:
            case takePhotoForIDCard://PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO_FOR_IDCARD:
            case selectFromGallery://PHOTOHELPER_REQUEST_CODE_GALLERY:
            default:
                onTakePhotoResult = builder.onTakePhotoResult;
                break;
            case selectFromMyGallery://PHOTOHELPER_REQUEST_CODE_GALLERY2:
                onSelectPictureResult = builder.onSelectPictureResult;
                break;
        }

        if (onTakePhotoResult != null) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.getExtras() != null && data.getExtras().containsKey(PHOTOHELPER_RESULT_PATH)) {
                        path = data.getStringExtra(PHOTOHELPER_RESULT_PATH);
                    } else if (data.getExtras() != null && data.getExtras().containsKey(CODED_CONTENT)) {
                        path = data.getStringExtra(CODED_CONTENT);
                    } else if (data.getData() != null) {
                        path = data.getData().getPath();
                    }
                    onTakePhotoResult.onSuccess(data, path);
                } else {
                    onTakePhotoResult.onSuccess(data, QDFileUtil.getFilePathByUri(contextWeakReference.get(), (Uri) onTakePhotoResult.tag));
                }
            } else {
                onTakePhotoResult.onFailure("resultCode=" + resultCode + ",data=null");
            }
        }
        if (resultCode == RESULT_OK && onSelectPictureResult != null) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                ArrayList<Image> images = (ArrayList<Image>) bundle.getSerializable(PHOTOHELPER_RESULT_PATHES);
                onSelectPictureResult.onSuccess(data, images);
            } else {
                onSelectPictureResult.onFailure("resultCode=" + resultCode + ",data=null");
            }
        }
    }

    public interface OnTakePhotoResultInterface {
        void onSuccess(Intent data, String path);

        void onFailure(String error);
    }

    public static abstract class OnTakePhotoResult implements OnTakePhotoResultInterface {
        private Object tag;

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }
    }

    public interface OnSelectPictureResult {
        void onSuccess(Intent data, ArrayList<Image> images);

        void onFailure(String error);

        int getImageCount();
    }


    public static class Builder {
        RequestType requestType;//请求类型
        float aspectX = 1;
        float aspectY = 1;
        int outputX = 800;
        int outputY = 800;
        Uri srcUri = null;//源文件存放uri
        Uri outUri = null;//输出文件存放位置
        int requestCode = -1;
        Activity context;
        OnTakePhotoResult onTakePhotoResult;
        OnSelectPictureResult onSelectPictureResult;

        public Builder(Activity context) {
            this.context = context;
        }

        Builder(Activity context, RequestType requestType) {
            this.requestType = requestType;
        }


        public Builder setRequestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        public Builder setAspectX(float aspectX) {
            this.aspectX = aspectX;
            return this;
        }

        public Builder setAspectY(float aspectY) {
            this.aspectY = aspectY;
            return this;
        }

        public Builder setOutputX(int outputX) {
            this.outputX = outputX;
            return this;
        }

        public Builder setOutputY(int outputY) {
            this.outputY = outputY;
            return this;
        }

        public Builder setSrcUri(Uri srcUri) {
            this.srcUri = srcUri;
            return this;
        }

        public Builder setOutUri(Uri outUri) {
            this.outUri = outUri;
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder setContext(Activity context) {
            this.context = context;
            return this;
        }

        public Builder setOnTakePhotoResult(OnTakePhotoResult onTakePhotoResult) {
            this.onTakePhotoResult = onTakePhotoResult;
            return this;
        }

        public Builder setOnSelectPictureResult(OnSelectPictureResult onSelectPictureResult) {
            this.onSelectPictureResult = onSelectPictureResult;
            return this;
        }
    }

}
