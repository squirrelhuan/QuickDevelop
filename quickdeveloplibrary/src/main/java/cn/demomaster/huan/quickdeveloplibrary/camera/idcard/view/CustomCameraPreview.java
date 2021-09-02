package cn.demomaster.huan.quickdeveloplibrary.camera.idcard.view;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.CamParaUtil;
import cn.demomaster.qdlogger_library.QDLogger;


/**
 * Created by gxj on 2018/2/17 01:46.
 * 相机预览封装
 */
public class CustomCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CustomCameraPreview.class.getName();

    private Camera mCamera;

    public CustomCameraPreview(Context context) {
        super(context);
        init();
    }

    public CustomCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setKeepScreenOn(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = CamParaUtil.openCamera();
        if (mCamera != null) {
            startPreview(holder);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mCamera != null) {
            mCamera.stopPreview();
            startPreview(holder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //回收释放资源
        release();
    }

    /**
     * 预览相机
     */
    private void startPreview(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters = mCamera.getParameters();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //竖屏拍照时，需要设置旋转90度，否者看到的相机预览方向和界面方向不相同
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else {
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            Camera.Size bestSize = CamParaUtil.getBestSize(parameters.getSupportedPreviewSizes());
            if (bestSize != null) {
                parameters.setPreviewSize(bestSize.width, bestSize.height);
                parameters.setPictureSize(bestSize.width, bestSize.height);
            } else {
                parameters.setPreviewSize(1920, 1080);
                parameters.setPictureSize(1920, 1080);
            }
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            focus();
        } catch (Exception e) {
            QDLogger.e(e);
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mCamera.setDisplayOrientation(90);
                    parameters.setRotation(90);
                } else {
                    mCamera.setDisplayOrientation(0);
                    parameters.setRotation(0);
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                focus();
            } catch (Exception e1) {
                QDLogger.e(e1);
                mCamera = null;
            }
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 对焦，在CameraActivity中触摸对焦
     */
    public void focus() {
        if (mCamera != null) {
            mCamera.autoFocus(null);
        }
    }

    /**
     * 拍摄照片
     *
     * @param pictureCallback 在pictureCallback处理拍照回调
     */
    public void takePhoto(Camera.PictureCallback pictureCallback) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, pictureCallback);
        }
    }
}