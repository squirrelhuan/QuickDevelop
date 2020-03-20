package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.DividerGravity;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDTextView;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * @author squirrel桓
 * @date 2019/1/7.
 * description：
 */
public class QDSheetDialog extends Dialog {
    private List<String> data;
    private Context context;
    private int columnCount = 1;
    private ShowType showType = ShowType.List;
    private int heightLayoutType = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int widthLayoutType = ViewGroup.LayoutParams.WRAP_CONTENT;
    private OnDialogActionListener onDialogActionListener;
    private int gravity = Gravity.CENTER;
    private int backgroundColor = Color.WHITE;
    private float[] backgroundRadius = new float[8];
    public QDSheetDialog(Context context, Builder builder) {
        super(context);
        data = builder.data;
        this.context = builder.context;
        columnCount = builder.columnCount;
        showType = builder.showType;
        heightLayoutType = builder.heightLayoutType;
        widthLayoutType = builder.widthLayoutType;
        onDialogActionListener = builder.onDialogActionListener;
        gravity = builder.gravity;
        backgroundColor = builder.backgroundColor;
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

        if (widthLayoutType == ViewGroup.LayoutParams.MATCH_PARENT) {
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
        }
        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = heightLayoutType;
        params.width = widthLayoutType;
        params.gravity =  Gravity.CENTER;

       /* int screenWidth = QMUIDisplayHelper.getScreenWidth(getContext());
        int screenHeight = QMUIDisplayHelper.getScreenHeight(getContext());
        params.width = screenWidth < screenHeight ? screenWidth : screenHeight;*/

       // int gravity = gravity;
        //getWindow().setWindowAnimations(R.style.FadeInPopWin);  //添加动画
        getWindow().setWindowAnimations(-1);  //添加动画
       /* if (MenuBuilder.class.isAssignableFrom(builder.getClass())) {//菜单类型弹窗

        } else {

        }*/

        getWindow().setAttributes(params);
        getWindow().setGravity(gravity);
        setCanceledOnTouchOutside(true);


        initData();

    }

    private LinearLayout contentLinearView;

    private void initData() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        int p = DisplayUtil.dip2px(getContext(), 15);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentLinearView = new LinearLayout(context);
        contentLinearView.setOrientation(LinearLayout.VERTICAL);

        //新建一个Drawable对象
        QDividerDrawable drawable_bg = new QDividerDrawable(DividerGravity.NONE);
        drawable_bg.setCornerRadii(backgroundRadius);
        drawable_bg.setBackGroundColor(backgroundColor);
        contentLinearView.setBackground(drawable_bg);

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

        SheetAdapter adapter = new SheetAdapter(getContext(), data);
        adapter.setOnItemClickListener(new TabMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onDialogActionListener.onItemClick(QDSheetDialog.this,position,data);
            }
        });

        recyclerView.setAdapter(adapter);
        contentLinearView.addView(recyclerView);
        ViewGroup layout = new RelativeLayout(getContext());
        layout.addView(contentLinearView, layoutParams);
        setContentView(layout, layoutParams);
    }

    public static enum ShowType {
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
        private int gravity = Gravity.CENTER;
        private int backgroundColor = Color.WHITE;
        private float[] backgroundRadius = new float[8];

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

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
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

    public static interface OnDialogActionListener {
          void onItemClick(QDSheetDialog dialog, int position, List<String> data);
    }


    public static class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.VHItem> {

        private List data;
        private Context context;
        private TabMenuAdapter.OnItemClickListener onItemClickListener;

        public TabMenuAdapter.OnItemClickListener getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(TabMenuAdapter.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public SheetAdapter(Context context, List data) {
            this.context = context;
            this.data = data;
        }

        @NonNull
        @Override
        public VHItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = new FrameLayout(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new VHItem(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final VHItem vhItem, int i) {
            vhItem.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int position = vhItem.getAdapterPosition();
                   if(onItemClickListener!=null){
                       onItemClickListener.onItemClick(v,position);
                   }
                }
            });
            vhItem.onbind(i);
        }

        @Override
        public int getItemCount() {
            return this.data.size();
        }

        public class VHItem extends RecyclerView.ViewHolder {
            QDTextView textView;
            public VHItem(@NonNull View itemView) {
                super(itemView);
                textView = new QDTextView(itemView.getContext());
                textView.setText("");
                int p = DisplayUtil.dip2px(itemView.getContext(), 15);
                textView.setPadding(p, p, p, p);
                textView.setTextSize(18);
                textView.setTextColor(Color.BLACK);
                if (Build.VERSION.SDK_INT >= 19) {
                    textView.setBackgroundResource(cn.demomaster.huan.quickdeveloplibrary.R.drawable.ripple_bg);
                }
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ((ViewGroup) itemView).addView(textView, layoutParams);
            }

            public void onbind(int position){
                textView.setText(data.get(position)+"");
            }
        }

    }


}
