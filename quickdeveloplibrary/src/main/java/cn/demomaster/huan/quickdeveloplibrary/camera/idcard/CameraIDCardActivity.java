package cn.demomaster.huan.quickdeveloplibrary.camera.idcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.view.CameraCropView;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.view.CustomCameraPreview;


/**
 * Created by gxj on 2018/2/18 11:46.
 * 拍照界面
 */
public class CameraIDCardActivity extends BaseActivityParent implements View.OnClickListener {
    /*
     */
    /**
     * 身份证正面
     */
    public final static int TYPE_ID_CARD_FRONT = 1;
    /**
     * 身份证反面
     */
    public final static int TYPE_ID_CARD_BACK = 2;

    public final static int REQUEST_CODE = 0X13;

    private CustomCameraPreview customCameraPreview;
    private CameraCropView camera_crop_view;
    //private View containerView;
    //private ImageView cropView;
    private View optionView;

    private int type;

    /**
     * 跳转到拍照页面
     */
    public static void navToCamera(Context context, int type) {
        Intent intent = new Intent(context, CameraIDCardActivity.class);
        intent.putExtra("type", type);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", 0);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_camera_idcard);
        actionBarLayout.setTitle("身份证拍照");
        initOptionsMenu();
        actionBarLayout.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsMenu.show();
            }
        });

        customCameraPreview = (CustomCameraPreview) findViewById(R.id.camera_surface);
        camera_crop_view = findViewById(R.id.camera_crop_view);
        //containerView = findViewById(R.id.camera_crop_container);
        //cropView = (ImageView) findViewById(R.id.camera_crop);
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
                //cropView.setImageResource(R.mipmap.camera_front);
                break;
            case TYPE_ID_CARD_BACK:
                camera_crop_view.setTip("请将身份证（反面）放在方框内");
                //cropView.setImageResource(R.mipmap.camera_back);
                break;
        }

        customCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCameraPreview.focus();
            }
        });
        //findViewById(R.id.camera_close).setOnClickListener(this);
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
        optionsMenu.setMenus(menus);
        optionsMenu.setAnchor(actionBarLayout.getRightView());
        optionsMenu.setOnMenuItemClicked(new OptionsMenu.OnMenuItemClicked() {
            @Override
            public void onItemClick(int position, View view) {

            }
        });
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.camera_surface:
                customCameraPreview.focus();
                break;
           *//* case R.id.camera_close:
                finish();
                break;*//*
            case R.id.camera_take:

        }*/
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
                            // float left = ((float) containerView.getLeft() - (float) customCameraPreview.getLeft()) / (float) customCameraPreview.getWidth();

                            // float top = 0;//(float) cropView.getTop() / (float) customCameraPreview.getHeight();
                            //float right = (float) containerView.getRight() / (float) customCameraPreview.getWidth();
                            // float bottom = 200;//(float) cropView.getBottom() / (float) customCameraPreview.getHeight();

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

                            FileUtil.saveBitmap(resBitmap);

                            if (!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                            if (!resBitmap.isRecycled()) {
                                resBitmap.recycle();
                            }

                            //拍照完成，返回对应图片路径
                            Intent intent = new Intent();
                            intent.putExtra("result", FileUtil.getImgPath());
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        return;
                    }
                }).start();
            }
        });
    }
}