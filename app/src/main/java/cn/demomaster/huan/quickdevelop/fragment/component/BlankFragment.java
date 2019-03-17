package cn.demomaster.huan.quickdevelop.fragment.component;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import cn.demomaster.huan.quickdevelop.R;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class BlankFragment extends Fragment {
    //Components
    View mView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";

        return mView;
    }
}