package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;

import static cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS;

public class PreviewActivity extends BaseActivityParent {


    private PhotoView photo_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        actionBarLayout.getRightView().setVisibility(View.GONE);
        actionBarLayout.setActionBarModel(ACTION_STACK_NO_STATUS);
        init();
    }

    private void init() {
        if (mBundle != null && mBundle.containsKey("image")) {
            Image image = (Image) mBundle.getSerializable("image");
            photo_view = findViewById(R.id.photo_view);
            if (image != null && image.getPath() != null) {
                Glide.with(mContext).load(new File(image.getPath())).into(photo_view);
            }
            //File file = new File(image.getPath());
            /*FileInputStream fs = null;
            try {
                fs = new FileInputStream(image.getPath());
                Bitmap bitmap = BitmapFactory.decodeStream(fs);
                Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                photo_view.setImageURI(imageUri);
                actionBarLayout.setStateBarColorAuto(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        }
    }


}
