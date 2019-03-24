package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.demomaster.huan.quickdeveloplibrary.R;

public class ActionBarLayoutFragmentView extends FrameLayout {

    public ActionBarLayoutFragmentView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ActionBarLayoutFragmentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ActionBarLayoutFragmentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private View contentView;

    public void setContentView(View contentView) {
        this.contentView = contentView;
        removeAllViews();
        addView(contentView);
    }


    private LayoutInflater mInflater;

    private void initView() {
        mInflater = LayoutInflater.from(getContext());
        removeAllViews();
        setId(R.id.qd_fragment_content_view);
    }
}
