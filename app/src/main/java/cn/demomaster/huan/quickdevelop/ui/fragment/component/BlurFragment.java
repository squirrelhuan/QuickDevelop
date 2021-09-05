package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.GroundGlassUtil;
import cn.demomaster.quicksticker_annotations.BindView;
import cn.demomaster.quicksticker_annotations.QuickStickerBinder;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "模糊", preViewClass = TextView.class, resType = ResType.Resource)
public class BlurFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.iv_marker)
    ImageView iv_marker;

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_blur, null);
        return mView;
    }

    SeekBar seekBarone, seekbartwo, seekbarthree, seekbarfour;
    ImageView imageView;
    float one = 1, two = 1, three = 1;
    private Bitmap baseBitmap, copyBitmap;
    GroundGlassUtil glassUtil = null;
    View rootView;
    public void initView(View view) {
        rootView = view;
        QuickStickerBinder.getInstance().bind(this, rootView);
        glassUtil = new GroundGlassUtil(getContext());
        glassUtil.setTargetView(iv_marker);
        glassUtil.setBackgroundView(rootView.findViewById(R.id.scrollView));

        imageView = rootView.findViewById(R.id.img);
        seekBarone = rootView.findViewById(R.id.one);
        seekbartwo = rootView.findViewById(R.id.two);
        seekbarthree = rootView.findViewById(R.id.three);
        seekbarfour = rootView.findViewById(R.id.four);
        seekBarone.setOnSeekBarChangeListener(this);
        seekbartwo.setOnSeekBarChangeListener(this);
        seekbarthree.setOnSeekBarChangeListener(this);
        seekbarfour.setOnSeekBarChangeListener(this);
        baseBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.meizi);
        //既然是复制一张与原图一模一样的图片那么这张复制图片的画纸的宽度和高度以及分辨率都要与原图一样,copyBitmap就为一张与原图相同尺寸分辨率的空白画纸
        copyBitmap = Bitmap.createBitmap(baseBitmap.getWidth(), baseBitmap.getHeight(), baseBitmap.getConfig());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(glassUtil!=null) {
            glassUtil.setTargetView(iv_marker);
            glassUtil.setBackgroundView(rootView.findViewById(R.id.scrollView));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(glassUtil!=null) {
            glassUtil.onRelease(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuickStickerBinder.getInstance().unBind(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.one:
                one = progress * 1.0f / seekBar.getMax() * 360;
                break;
            case R.id.two:
                two = progress * 1.0f / (seekBar.getMax() - 50);
                break;
            case R.id.three:
                three = progress * 1.0f / (seekBar.getMax() - 50);
                break;
            case R.id.four:
                glassUtil.setRadius(progress);
                break;
        }
        Paint paint = new Paint();
        Canvas canvas = new Canvas(copyBitmap);
        ColorMatrix colorMatrixS = new ColorMatrix();
        colorMatrixS.setRotate(0, one);
        colorMatrixS.setRotate(1, one);
        colorMatrixS.setRotate(2, one);
        ColorMatrix colorMatrixL = new ColorMatrix();
        colorMatrixL.setScale(two, two, two, 1);
        ColorMatrix colorMatrixB = new ColorMatrix();
        colorMatrixB.setSaturation(three);
        ColorMatrix colorMatriximg = new ColorMatrix();
        //通过postConcat()方法可以将以上效果叠加到一起
        colorMatriximg.postConcat(colorMatrixB);
        colorMatriximg.postConcat(colorMatrixL);
        colorMatriximg.postConcat(colorMatrixS);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatriximg);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);
        imageView.setImageBitmap(copyBitmap);
        glassUtil.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(glassUtil!=null){
            glassUtil.onRelease(null);
        }
    }
}