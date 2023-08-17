package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnSingleItemClickListener;
import cn.demomaster.huan.quickdeveloplibrary.widget.base.Gravity;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.adapter.SheetAdapter;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * @author squirrel桓
 * @date 2019/1/7.
 * description：
 */
public class QDSheetDialog extends QDDialog2 {
    private List<String> data;
    private int columnCount = 1;
    private ShowType showType = ShowType.List;
    private OnDialogActionListener onDialogActionListener;
    private Gravity gravity;
    private int boxColor;

    public QDSheetDialog(Context context, Builder builder) {
        super(context);
        data = builder.data;
        columnCount = builder.columnCount;
        showType = builder.showType;
        onDialogActionListener = builder.onDialogActionListener;
        gravity = builder.gravity;
        boxColor = builder.boxColor;
        backgroundRadius = builder.backgroundRadius;
        init();
    }

    public QDSheetDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected QDSheetDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        initData();
        //setCanSliding(false);
    }

    @Override
    public boolean isHasPadding() {
        return true;
    }

    private void initData() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = gravity.value();
        setBackgroundColor(backgroundColor);

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
                if(onDialogActionListener!=null) {
                    onDialogActionListener.onItemClick(QDSheetDialog.this, position, data);
                }
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setBackgroundColor(boxColor);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.addView(recyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(relativeLayout, layoutParams);
    }

    public void setOnDialogActionListener(OnDialogActionListener onDialogActionListener) {
        this.onDialogActionListener = onDialogActionListener;
    }

    public enum ShowType {
        List, Grid
    }

    public static class Builder extends QDDialog2.Builder{
        private List<String> data;
        private Context context;
        private int columnCount = 1;
        private ShowType showType = ShowType.List;
        private int heightLayoutType = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int widthLayoutType = ViewGroup.LayoutParams.WRAP_CONTENT;
        private OnDialogActionListener onDialogActionListener;
        private cn.demomaster.huan.quickdeveloplibrary.widget.base.Gravity gravity = Gravity.CENTER;
        private int boxColor = Color.WHITE;
        private float[] backgroundRadius = new float[8];

        public Builder(Context context) {
            super(context);
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

        public Builder setGravity(Gravity gravity) {
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

        public QDSheetDialog create() {
            return new QDSheetDialog(context, this);
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
        void onItemClick(QDSheetDialog dialog, int position, List<String> data);
    }

}
