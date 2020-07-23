package cn.demomaster.huan.quickdeveloplibrary.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ACTIONBAR_TYPE;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdzxinglibrary.CodeCreator;
import cn.demomaster.qdzxinglibrary.ScanHelper;
import cn.demomaster.qdzxinglibrary.ScanSurfaceView;


public class MyCaptureActivity extends QDActivity {

    public static final String CODED_CONTENT = "codedContent";
    //遮盖层视图
    ScanMakerView scanMakerView;
    //背景预览图层
    ScanSurfaceView surfaceView;

    ImageView iv_light;
    ImageView iv_gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_capture);

        //请求屏幕常亮
        getActionBarLayout().setActionBarType(ACTIONBAR_TYPE.ACTION_STACK);
        getActionBarLayout().setActionBarThemeColors(Color.WHITE,Color.WHITE);
        //getActionBarLayout().setHeaderBackgroundColor(Color.TRANSPARENT);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        scanMakerView = findViewById(R.id.viewfinder_view);
        scanMakerView.setMarkerWidth(DisplayUtil.dip2px(mContext,200));
        scanMakerView.setMarkerHeight(DisplayUtil.dip2px(mContext,200));
        surfaceView= findViewById(R.id.preview_view);
        surfaceView.setOnScanResultListener(new ScanHelper.OnScanResultListener() {
            @Override
            public void handleDecode(Result obj, Bitmap barcode, float scaleFactor) {
               // Toast.makeText(mContext,obj.toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(CODED_CONTENT, obj.toString());
                intent.putExtras(bundle);
                intent.putExtra(CODED_CONTENT, obj.toString());
                //setResult(PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE, intent);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void foundPossiblePoint(ResultPoint resultPoint) {
                scanMakerView.foundPossibleResultPoint(resultPoint);
            }
        });

        iv_light = findViewById(R.id.iv_light);
        iv_gallery = findViewById(R.id.iv_gallery);
        iv_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ScanHelper.getInstance().isFlashOpened(MyCaptureActivity.this)){
                    ScanHelper.getInstance().closeFlash(MyCaptureActivity.this);
                }else {
                    ScanHelper.getInstance().openFlash(MyCaptureActivity.this);
                }
            }
        });
        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoHelper().selectPhotoFromGallery(new PhotoHelper.OnTakePhotoResult() {
                    @Override
                    public void onSuccess(Intent data, String path) {
                        setImageToView(path,data);
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
            }
        });
        if(ScanHelper.getInstance().isSupportCameraLedFlash(mContext)){
            iv_light.setVisibility(View.VISIBLE);
        }else {
            iv_light.setVisibility(View.GONE);
        }
    }

    protected void setImageToView(String path, final Intent data) {
        //showProgress("正在处理图片...", false);
        Bundle extras = data.getExtras();
        Bitmap bitmap;
        Result result = null;
        if (extras != null) {
             bitmap = extras.getParcelable("data");
            if (bitmap == null) {
                Uri uri = data.getData();
                String filePath = QDFileUtil.getFilePathByUri(this, uri);
                bitmap = QDFileUtil.getBitmapFromPath(filePath);
            }
            if (bitmap == null) {
                QdToast.show(mContext, "机柜失败 fail");
                return;
            }
            result = CodeCreator.readQRcode(this,bitmap);
        } else {
            if (!TextUtils.isEmpty(path)) {
                String filePath = QDFileUtil.getFilePathByUri(this, Uri.parse(path));
                bitmap = QDFileUtil.getBitmapFromPath(filePath);
                result = CodeCreator.readQRcode(this,bitmap);
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        String cordStr =  "error null";
        if(result!=null){
            cordStr =  result.toString();
        }
        bundle.putString(CODED_CONTENT, cordStr);
        intent.putExtras(bundle);
        intent.putExtra(CODED_CONTENT, cordStr);
        //setResult(PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE, intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        surfaceView.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.start();
    }
}
