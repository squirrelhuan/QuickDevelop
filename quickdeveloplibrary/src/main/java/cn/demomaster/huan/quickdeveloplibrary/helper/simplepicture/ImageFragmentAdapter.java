package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view.SimpleImageFragment;

public class ImageFragmentAdapter extends FragmentStateAdapter {
    
    private ArrayList<Image> images;
    
    public ImageFragmentAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, ArrayList<Image> images) {
        super(fragmentActivity);
        this.images = images;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        SimpleImageFragment fragment = new SimpleImageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("image", images.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
