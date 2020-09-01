package cn.demomaster.huan.quickdeveloplibrary.camera.idcard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.view.CameraCropView;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.view.CustomCameraPreview;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper.PHOTOHELPER_RESULT_CODE;
import static cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper.PHOTOHELPER_RESULT_PATH;


/**
 * Created by Squirrel桓
 * 读取身份证界面
 */
public class CameraIDCardActivity extends QDActivity implements View.OnClickListener {

    /**
     * 身份证正面
     */
    public final static int TYPE_ID_CARD_FRONT = 1;
    /**
     * 身份证反面
     */
    public final static int TYPE_ID_CARD_BACK = 2;

    private CustomCameraPreview customCameraPreview;
    private CameraCropView camera_crop_view;
    private View optionView;

    private int type;
    private int result;

    private OptionsMenu optionsMenu;
    private OptionsMenu.Builder optionsMenubuilder;
    //获取自定义菜单
    public OptionsMenu.Builder getOptionsMenuBuilder() {
        if (optionsMenubuilder == null) {
            optionsMenubuilder = new OptionsMenu.Builder(mContext);
        }
        return optionsMenubuilder;
    }


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", 0);
        result = getIntent().getIntExtra(PHOTOHELPER_RESULT_CODE,0);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_camera_idcard);
        getActionBarLayout().setStateBarColorAuto(true);
        getActionBarLayout().setTitle("身份证拍照");
        initOptionsMenu();
        getActionBarLayout().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsMenu.show();
            }
        });
        getActionBarLayout().getRightView().setImageResource(R.drawable.ic_more_vert_black_24dp);

        customCameraPreview = (CustomCameraPreview) findViewById(R.id.camera_surface);
        camera_crop_view = findViewById(R.id.camera_crop_view);
        optionView = findViewById(R.id.camera_option);

        //获取屏幕最小边，设置为cameraPreview较窄的一边
        float screenMinSize = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        //根据screenMinSize，计算出cameraPreview的较宽的一边，长宽比为标准的16:9
        //float maxSize = screenMinSize / 9.0f * 16.0f;

        /*cropView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cropView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LinearLayout.LayoutParams cropParams  = (LinearLayout.LayoutParams) cropView.getLayoutParams();
                float width =cropView.getWidth();
                float height = (int) (width * 9/16 );
                cropParams.height = (int)height;
                cropView.setLayoutParams(cropParams);
            }
        });*/

        switch (type) {
            case TYPE_ID_CARD_FRONT:
                camera_crop_view.setTip("请将身份证（正面）放在方框内");
                break;
            case TYPE_ID_CARD_BACK:
                camera_crop_view.setTip("请将身份证（反面）放在方框内");
                break;
        }

        customCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCameraPreview.focus();
            }
        });
        findViewById(R.id.camera_take).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    private void initOptionsMenu() {
        List<OptionsMenu.Menu> menus = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            OptionsMenu.Menu menu = new OptionsMenu.Menu();
            menu.setTitle("从相册中选取");
            menu.setPosition(i);
            menus.add(menu);
        }
        if (optionsMenubuilder == null) {
            optionsMenubuilder = getOptionsMenuBuilder().setMenus(menus)
                    .setAlpha(.6f)
                    .setUsePadding(true)
                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                    .setBackgroundRadius(20)
                    .setTextColor(Color.WHITE)
                    .setTextSize(16)
                    .setPadding(0)
                    .setWithArrow(true)
                    .setArrowHeight(30)
                    .setArrowWidth(30)
                    .setGravity(GuiderView.Gravity.BOTTOM)
                    .setDividerColor(getResources().getColor(R.color.transparent))
                    .setAnchor(getActionBarLayout().getRightView());
        }

        if (optionsMenu == null) {
            optionsMenu = new OptionsMenu(optionsMenubuilder);
        }
        optionsMenu.setMenus(menus);
        optionsMenu.setAnchor(getActionBarLayout().getRightView());
        optionsMenu.setOnMenuItemClicked(new OptionsMenu.OnMenuItemClicked() {
            @Override
            public void onItemClick(int position, View view) {
                optionsMenu.dismiss();
                //选取图片并截取
                /*photoHelper.selectPhotoFromGalleryAndCrop(new PhotoHelper.OnTakePhotoResult(){
                    @Override
                    public void onSuccess(Intent data, String path) {
                        setImageToView(data);
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });*/
                //只选取图片不截取
                getPhotoHelper().selectPhotoFromGallery(new PhotoHelper.OnTakePhotoResult() {
                    @Override
                    public void onSuccess(Intent data, String path) {
                        QDLogger.e(path);
                        String relPath = QDFileUtil.getRealPathFromURI(mContext,data.getData());
                        QDLogger.e(relPath);
                        //setImageToView(data);
                        //拍照完成，返回对应图片路径
                        Intent intent = new Intent();
                        intent.putExtra(PHOTOHELPER_RESULT_PATH, relPath);
                        setResult(result, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    private void takePhoto() {
        optionView.setVisibility(View.GONE);
        customCameraPreview.setEnabled(false);
        customCameraPreview.takePhoto(new Camera.PictureCallback() {
            public void onPictureTaken(final byte[] data, final Camera camera) {
                //子线程处理图片，防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = null;
                        if (data != null) {
                            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            camera.stopPreview();
                        }
                        if (bitmap != null) {
                            //计算裁剪位置
                            float left = camera_crop_view.getPercentage_Left();
                            float top = camera_crop_view.getPercentage_top();
                            float width = camera_crop_view.getPercentage_width();
                            float height = camera_crop_view.getPercentage_height();

                            //裁剪及保存到文件
                            Bitmap resBitmap = Bitmap.createBitmap(bitmap,
                                    (int) (left * bitmap.getWidth()),
                                    (int) (top * bitmap.getHeight()),
                                    (int) (width * bitmap.getWidth()),
                                    (int) (height * bitmap.getHeight()));

                            String path = QDFileUtil.saveBitmap(resBitmap);

                            if (!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                            if (!resBitmap.isRecycled()) {
                                resBitmap.recycle();
                            }
                            //拍照完成，返回对应图片路径
                            Intent intent = new Intent();
                            intent.putExtra(PHOTOHELPER_RESULT_PATH, path);
                            setResult(result, intent);
                            finish();
                        }
                        return;
                    }
                }).start();
            }
        });
    }

    private static final int GALLERY_Photo_REQUEST_CODE = 300;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CROP_SMALL_PICTURE = 5;
    private Uri fileUri;

    //获取返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QDLogger.d(TAG, "onActivityResult: requestCode: " + requestCode + ", resultCode: " + requestCode + ", data: " + data + ",resultCode:" + resultCode);
        switch (requestCode) {
            //如果是拍照
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                Log.d(TAG, "CAPTURE_IMAGE");
                if (RESULT_OK == resultCode) {
                    Log.d(TAG, "RESULT_OK");
                    // Check if the result includes a thumbnail Bitmap
                    if (data != null) {
                        // 没有指定特定存储路径的时候
                        Log.d(TAG, "data is NOT null, file on default position.");
                        // 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);）
                        // Image captured and saved to fileUri specified in the Intent
                        if (data.hasExtra("data")) {
//                            startPhotoZoom(data.getData());
                            setImageToView(data);
                            //Bitmap thumbnail = data.getParcelableExtra("data");
                        }
                    } else {
                        //startPhotoZoom(fileUri);
                    }
                }
                break;
            //从相册选取
            case GALLERY_Photo_REQUEST_CODE:
                Log.d(TAG, "GALLERY");
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        //startPhotoZoom(data.getData());
                    }
                }
                break;
            case CROP_SMALL_PICTURE:
                if (data != null) {
                   // setImageToView(data);
                }
            default:
                break;
        }
    }

    protected void setImageToView(final Intent data) {
        //showProgress("正在处理图片...", false);
        Bundle extras = data.getExtras();
        if (extras != null) {
            final Bitmap bitmap = extras.getParcelable("data");
            //photo = ImageUtils.toRoundBitmap(photo, fileUri); // ���ʱ���ͼƬ�Ѿ��������Բ�ε���
            //photo = ImageUtils.savePhoto(photo, Constants.APP_PATH_PICTURE, "")
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (bitmap != null) {
                        String path = QDFileUtil.saveBitmap(bitmap);
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                        //拍照完成，返回对应图片路径
                        Intent intent = new Intent();
                        intent.putExtra(PHOTOHELPER_RESULT_PATH, path);
                        setResult(result, intent);
                        finish();
                    }
                    return;
                }
            }).start();
        }
    }
/*
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        fileUri = uri;
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
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}