package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class PreviewFragment extends Fragment {

    View mView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_preview, null);
        }
        Bundle bundle = getArguments();
        PhotoView pv_image = mView.findViewById(R.id.pv_image);
        Image image = null;
        if (bundle!=null&& bundle.containsKey("image")){
            image = (Image) bundle.getSerializable("image");
            if (image != null && image.getPath() != null) {
                Glide.with(getContext()).load(new File(image.getPath())).into(pv_image);
            }
        }

        return mView;
    }
}