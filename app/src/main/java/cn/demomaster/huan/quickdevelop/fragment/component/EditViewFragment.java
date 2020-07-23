package cn.demomaster.huan.quickdevelop.fragment.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.util.GroundGlassUtil;
import cn.demomaster.quicksticker_annotations.BindEditView;
import cn.demomaster.quicksticker_annotations.BindView;
import cn.demomaster.quicksticker_annotations.QuickStickerBinder;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "EditView",preViewClass = TextView.class,resType = ResType.Resource)
public class EditViewFragment extends QDFragment {
    //Components
    ViewGroup mView;
    @BindEditView(R.id.et_username)
    EditText et_username;
    @BindEditView(R.id.et_password)
    EditText et_password;
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_editview, null);
        }
        QuickStickerBinder.getInstance().bind(this,mView);
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayout) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuickStickerBinder.getInstance().unBind(this);
    }

}