package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.os.Bundle;
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
public class PreviewFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_layout_preview, null);
        Bundle bundle = getArguments();
        PhotoView pv_image = contentView.findViewById(R.id.pv_image);
        if (bundle != null && bundle.containsKey("image")) {
            Image image = (Image) bundle.getSerializable("image");
            if (image != null && image.getPath() != null) {
                if (image.getUrlType() == UrlType.url) {
                    Glide.with(getContext()).load(image.getPath()).into(pv_image);
                } else if (image.getUrlType() == UrlType.file) {
                    Glide.with(getContext()).load(new File(image.getPath())).into(pv_image);
                }
            }
        }
        return contentView;
    }
}