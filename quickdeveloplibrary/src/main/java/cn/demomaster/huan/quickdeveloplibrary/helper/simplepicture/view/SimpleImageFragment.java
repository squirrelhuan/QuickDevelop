package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import static cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil.isHttpUrl;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.UrlType;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class SimpleImageFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_layout_preview, null);
        Bundle bundle = getArguments();
        PhotoView pv_image = contentView.findViewById(R.id.pv_image);
        if (bundle != null && bundle.containsKey("image")) {
            String imagePath = bundle.getString("image");
            if (!TextUtils.isEmpty(imagePath)) {
                if (isHttpUrl(imagePath)) {
                    Glide.with(getContext()).load(imagePath).into(pv_image);
                } else {
                    Glide.with(getContext()).load(new File(imagePath)).into(pv_image);
                }
            }
        }
        return contentView;
    }
}