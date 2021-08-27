package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "截图", preViewClass = TextView.class, resType = ResType.Custome)
public class ScreenShotFragment extends BaseFragment {

    //Components
    @BindView(R.id.btn_shot)
    QDButton btn_shot;
    @BindView(R.id.iv_prev)
    ImageView iv_prev;

    @BindView(R.id.sb_scale)
    SeekBar sb_scale;
    @BindView(R.id.tv_scale)
    TextView tv_scale;

    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_size2)
    TextView tv_size2;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_screen_shot, null);
        return mView;
    }

    Bitmap bitmap;
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().getActionBarLayout().getActionBarView().setBackgroundColor(Color.RED);
        btn_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = ScreenShotUitl.shotActivity((Activity) v.getContext(),false);
                QDFileUtil.saveBitmap(bitmap);
                iv_prev.setImageBitmap(bitmap);
                tv_size.setText("原图大小:" + QDBitmapUtil.getBitmapSize(bitmap) / 1024 / 8 + "kb");
            }
        });
        sb_scale.setMax(200);
        sb_scale.setProgress(100);
        sb_scale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Bitmap bitmap1 = QDBitmapUtil.zoomImage(bitmap, bitmap.getWidth() * (progress) / 100f, bitmap.getHeight() * (progress) / 100f);
                iv_prev.setImageBitmap(bitmap1);
                tv_scale.setText("尺寸压缩(" + (progress) / 100f + ")");
                tv_size2.setText("压缩后大小:" + QDBitmapUtil.getBitmapSize(bitmap1) / 1024 / 8 + "kb");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}