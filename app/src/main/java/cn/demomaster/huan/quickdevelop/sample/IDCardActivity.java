package cn.demomaster.huan.quickdevelop.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.CameraIDCardActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;

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
     * 拍摄证件照片
     * @param type 拍摄证件类型
     */
    private void takePhoto(final int type) {
        photoHelper.takePhotoForIDCard(new PhotoHelper.OnTakePhotoResult() {
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
    public void frontIdCard(View view) {
        takePhoto(CameraIDCardActivity.TYPE_ID_CARD_FRONT);
    }

    /**
     * 身份证反面
     */
    public void backIdCard(View view) {
        takePhoto(CameraIDCardActivity.TYPE_ID_CARD_BACK);
    }
}
