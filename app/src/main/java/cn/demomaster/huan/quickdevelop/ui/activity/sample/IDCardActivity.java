package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.CameraIDCardActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.base.Gravity;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil.imageToBase64;

@ActivityPager(name = "身份证读取", preViewClass = TextView.class, resType = ResType.Resource)
public class IDCardActivity extends BaseActivity {

    @BindView(R.id.main_image)
    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        ButterKnife.bind(this);
    }

    /**
     * 拍摄证件照片
     *
     * @param type 拍摄证件类型
     */
    private void takePhoto(final int type) {
        getPhotoHelper().takePhotoForIDCard(new PhotoHelper.OnTakePhotoResult() {
            @Override
            public void onSuccess(Intent data, String path) {
                if (!TextUtils.isEmpty(path)) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                }
                String imgBase64 = imageToBase64(path);
                Log.i("CGQ", imgBase64);
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
            public void onSuccess(Intent data, String path) {
                if (!TextUtils.isEmpty(path)) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                }
                String imgBase64 = imageToBase64(path);
                Log.i("CGQ", imgBase64);
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
                .addAction("拍照", (dialog, view, tag) -> {
                    dialog.dismiss();
                    getPhotoHelper().takePhoto(null,new PhotoHelper.OnTakePhotoResult() {
                        @Override
                        public void onSuccess(Intent data, String path) {
                            Log.i(TAG, "map_path=" + path);
                            Gson gson = new Gson();
                            String map_str2 = gson.toJson(data);
                            Log.i(TAG, "map_str2=" + map_str2);
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

                }).addAction("从相册中选取", (dialog, view, tag) -> {
                    dialog.dismiss();
                    getPhotoHelper().selectPhotoFromGallery(new PhotoHelper.OnTakePhotoResult() {
                        @Override
                        public void onSuccess(Intent data, String path) {
                            QDLogger.i("path=" + path);
                        }

                        @Override
                        public void onFailure(String error) {
                            QDLogger.i("error=" + error);
                        }
                    });
                }).setGravity_foot(Gravity.CENTER).create();
        qdDialog.show();
    }
}
