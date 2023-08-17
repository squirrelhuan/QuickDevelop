package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import static cn.demomaster.huan.quickdeveloplibrary.http.HttpUtils.FROM_DATA;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.net.RetrofitInterface;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.http.HttpUtils;
import cn.demomaster.huan.quickdeveloplibrary.model.Version;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.qdzxinglibrary.CodeCreator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "二维码", preViewClass = TextView.class, resType = ResType.Resource,iconRes = R.mipmap.ic_crash)
public class BarCodeFragment extends QuickFragment {

    @BindView(R.id.btn_scan)
    Button btn_scan;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.btn_generate)
    Button btn_generate;
    @BindView(R.id.iv_code)
    ImageView iv_code;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_barcode, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();
                if(!TextUtils.isEmpty(content)){
                    Bitmap bitmap = CodeCreator.createQRCode(content, 500, 500, null);
                    iv_code.setImageBitmap(bitmap);
                }
            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoHelper().scanQrcode(new PhotoHelper.OnTakePhotoResult() {
                    @Override
                    public void onSuccess(Intent data, String path) {
                        QdToast.show(mContext, path + "");
                        //showScanDialog(path);
                    }

                    @Override
                    public void onFailure(String error) {
                        QdToast.show(mContext, "error:" + error);
                    }
                });
            }
        });
    }

    public PhotoHelper getPhotoHelper() {
        if (getContext() instanceof QDActivity) {
            return ((QDActivity) getContext()).getPhotoHelper();
        }
        return null;
    }
}