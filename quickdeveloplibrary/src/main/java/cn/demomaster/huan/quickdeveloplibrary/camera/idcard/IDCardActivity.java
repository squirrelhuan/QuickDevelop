package cn.demomaster.huan.quickdeveloplibrary.camera.idcard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;


public class IDCardActivity extends BaseActivityParent {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        imageView = (ImageView) findViewById(R.id.main_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CameraIDCardActivity.REQUEST_CODE) {
            //获取文件路径，显示图片
            if (data != null) {
                String path = data.getStringExtra("result");
                if (!TextUtils.isEmpty(path)) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                }

                String imgBase64 = imageToBase64(path);
                Log.i("CGQ",imgBase64);
            }
        }
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
     *
     * @param type 拍摄证件类型
     */
    private void takePhoto(int type) {
        if (ActivityCompat.checkSelfPermission(IDCardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IDCardActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0x12);
            return;
        }
        CameraIDCardActivity.navToCamera(this, type);
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
