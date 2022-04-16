package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "截图", preViewClass = TextView.class, resType = ResType.Custome)
public class ScreenShotFragment extends QuickFragment {

    //Components
    @BindView(R.id.btn_shot)
    QDButton btn_shot;
    @BindView(R.id.btn_share_shot)
    QDButton btn_share_shot;

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
        btn_shot.setOnClickListener(v -> {
            bitmap = ScreenShotUitl.shotActivity((Activity) v.getContext(),false);
            QDFileUtil.saveBitmap(getContext(),bitmap);
            iv_prev.setImageBitmap(bitmap);
            tv_size.setText("原图大小:" + QDBitmapUtil.getBitmapSize(bitmap) / 1024 / 8 + "kb");
        });
        sb_scale.setMax(200);
        sb_scale.setProgress(100);
        sb_scale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(bitmap!=null) {
                    Bitmap bitmap1 = QDBitmapUtil.zoomImage(bitmap, bitmap.getWidth() * (progress) / 100f, bitmap.getHeight() * (progress) / 100f);
                    iv_prev.setImageBitmap(bitmap1);
                    tv_scale.setText("尺寸压缩(" + (progress) / 100f + ")");
                    tv_size2.setText("压缩后大小:" + QDBitmapUtil.getBitmapSize(bitmap1) / 1024 / 8 + "kb");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_share_shot.setOnClickListener(v -> showShot(mContext,  v));

    }

    public void showShot(final Activity context, View anchor){
            // 用于PopupWindow的View
            View contentView = LayoutInflater.from(context).inflate(R.layout.layout_screen_shot, null, false);

            PopupWindow popupWindow = new PopupWindow();
            ((ImageView) contentView.findViewById(R.id.iv_content)).setImageBitmap(ScreenShotUitl.shotActivity(context,false));
            popupWindow.setContentView(contentView);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
            ImageView iv_code = contentView.findViewById(R.id.iv_code);
            Bitmap bitmap_app = BitmapFactory.decodeResource(context.getResources(), cn.demomaster.huan.quickdeveloplibrary.R.mipmap.quickdevelop_ic_launcher);
            String codeStr = "http://weixin.qq.com/r/E0M1LcDE6Z2WrYRO9xYB";
            final View ll_content = contentView.findViewById(cn.demomaster.huan.quickdeveloplibrary.R.id.ll_content);
            ll_content.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                //Uri uri = saveImage(context,getCacheBitmapFromView(ll_content));
                //shareImg(context, "口袋基因", "口袋基因", "欢迎来到基因世界", uri);
            });

            TextView tv_share = contentView.findViewById(R.id.tv_share);
            tv_share.setOnClickListener(view -> {
                Uri uri = ScreenShotUitl.saveImage(context, ScreenShotUitl.getCacheBitmapFromView(ll_content));
                ScreenShotUitl.shareImg(context, "口袋基因", "口袋基因", "欢迎来到基因世界", uri);
            });

            RelativeLayout rl_root = contentView.findViewById(R.id.rl_root);
            rl_root.setOnClickListener(view -> popupWindow.dismiss());
    }
}