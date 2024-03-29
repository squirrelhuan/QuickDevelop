package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnSingleItemClickListener;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.DividerGravity;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.adapter.SheetAdapter;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * 多选
 * @author squirrel桓
 * @date 2019/1/7.
 * description：
 */
public class QDMulSheetDialog extends QDDialog2 {
    private List<String> data;
    private int columnCount = 1;
    private ShowType showType = ShowType.List;
    private OnDialogActionListener onDialogActionListener;
    private int gravity;
    private int boxColor;

    public QDMulSheetDialog(Context context, Builder builder) {
        super(context);
        data = builder.data;
        columnCount = builder.columnCount;
        showType = builder.showType;
        onDialogActionListener = builder.onDialogActionListener;
        gravity = builder.gravity;
        boxColor = builder.boxColor;
        backgroundRadius = builder.backgroundRadius;
        animationStyleID = builder.animationStyleID;
        init();
    }

    public QDMulSheetDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected QDMulSheetDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public boolean isCanSliding() {
        return true;
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        initData();
    }

    @Override
    public boolean isHasPadding() {
        return false;
    }

    private void initData() {
        Window win = getWindow();
        if (animationStyleID != -1) {
            win.setWindowAnimations(animationStyleID);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = gravity;

        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        switch (showType) {
            case List:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setAutoMeasureEnabled(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
            case Grid:
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
                break;
        }

        SheetAdapter<RecyclerView.ViewHolder> adapter = new SheetAdapter<RecyclerView.ViewHolder>(getContext(), data);
        adapter.setOnItemClickListener(new OnSingleItemClickListener() {
            @Override
            public void onItemClickEvent(AdapterView<?> parent, View view, int position, long id) {
                onDialogActionListener.onItemClick(QDMulSheetDialog.this, position, data);
            }
        });

        recyclerView.setAdapter(adapter);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        //relativeLayout.setBackgroundColor(boxColor);
        QDividerDrawable drawable_bg = new QDividerDrawable(DividerGravity.NONE);
        float c = DisplayUtil.dip2px(getContext(), 20);
        drawable_bg.setCornerRadii(new float[]{c, c, c, c, 0, 0, 0, 0});
        drawable_bg.setBackGroundColor(boxColor);
        relativeLayout.setBackground(drawable_bg);
        relativeLayout.addView(recyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(relativeLayout, layoutParams);

        //setPanelMaginTop(DisplayUtil.getStatusBarHeight(getContext()) + DisplayUtil.getActionBarHeight(getContext()));
    }

    public enum ShowType {
        List, Grid
    }

    public static class Builder {
        private List<String> data;
        private Context context;
        private int columnCount = 1;
        private ShowType showType = ShowType.List;
        private int heightLayoutType = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int widthLayoutType = ViewGroup.LayoutParams.WRAP_CONTENT;
        private OnDialogActionListener onDialogActionListener;
        private int gravity = Gravity.BOTTOM;
        private int boxColor = Color.WHITE;
        private float[] backgroundRadius = new float[8];
        private int animationStyleID = R.style.keybored_anim;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setData(List<String> data) {
            this.data = data;
            return this;
        }

        public Builder setData(String[] data) {
            this.data = Arrays.asList(data);
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setShowType(ShowType showType) {
            this.showType = showType;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setBoxColor(int backgroundColor) {
            this.boxColor = backgroundColor;
            return this;
        }

        public Builder setBackgroundRadius(float backgroundRadiu) {
            for (int i = 0; i < backgroundRadius.length; i++) {
                this.backgroundRadius[i] = backgroundRadiu;
            }
            return this;
        }

        public Builder setBackgroundRadius(float[] backgroundRadius) {
            this.backgroundRadius = backgroundRadius;
            return this;
        }

        public Builder setColumnCount(int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        public Builder setHeightLayoutType(int heightLayoutType) {
            this.heightLayoutType = heightLayoutType;
            return this;
        }

        public Builder setWidthLayoutType(int widthLayoutType) {
            this.widthLayoutType = widthLayoutType;
            return this;
        }

        public Builder setOnDialogActionListener(OnDialogActionListener onDialogActionListener) {
            this.onDialogActionListener = onDialogActionListener;
            return this;
        }

        public QDMulSheetDialog create() {
            return new QDMulSheetDialog(context, this);
        }
    }

    /**
     * 菜单弹窗构建器
     */
    public static class MenuBuilder extends Builder {
        public MenuBuilder(Context context) {
            super(context);
        }
    }

    public interface OnDialogActionListener {
        void onItemClick(QDMulSheetDialog dialog, int position, List<String> data);
    }
}
