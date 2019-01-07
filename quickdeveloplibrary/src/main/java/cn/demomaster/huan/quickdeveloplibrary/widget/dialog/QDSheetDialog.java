package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
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
    private Builder builder;
    public QDSheetDialog( Context context, Builder builder) {
        super(context);
        this.builder = builder;
        init();
    }

    public QDSheetDialog( Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected QDSheetDialog( Context context, boolean cancelable,  DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        //noinspection ConstantConditions
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        int screenWidth = QMUIDisplayHelper.getScreenWidth(getContext());
        int screenHeight = QMUIDisplayHelper.getScreenHeight(getContext());
        params.width = screenWidth < screenHeight ? screenWidth : screenHeight;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM );
        setCanceledOnTouchOutside(true);

        //getWindow().setWindowAnimations(R.style.FadeInPopWin);  //添加动画
        getWindow().setWindowAnimations(-1);  //添加动画
        initData();

    }

    private LinearLayout contentView;
    private void initData() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        int p = DisplayUtil.dip2px(getContext(), 15);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView = new LinearLayout(builder.context);
        contentView.setOrientation(LinearLayout.VERTICAL);

        //新建一个Drawable对象
        QDividerDrawable drawable_bg=new QDividerDrawable(QDividerDrawable.Gravity.NONE);
        drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(builder.backgroundColor);
        contentView.setBackground(drawable_bg);

        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        switch (builder.showType){
            case List:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setAutoMeasureEnabled(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
            case Grid:
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), builder.columnCount));
                break;
        }

        SheetAdapter adapter = new SheetAdapter(getContext(),builder.data);

        recyclerView.setAdapter(adapter);
        contentView.addView(recyclerView);
        ViewGroup layout = new RelativeLayout(getContext());
        layout.addView(contentView, layoutParams);
        setContentView(layout, layoutParams);
    }

    public static enum ShowType {
        List,Grid
    }


    public static class Builder{
        private List data;
        private Context context;
        private int columnCount =1;
        private ShowType showType =ShowType.List;
        private int gravity = Gravity.BOTTOM;
        private int backgroundColor = Color.WHITE;
        private float[] backgroundRadius=new float[8];
        public Builder(Context context){
            this.context =context;
        }

        public Builder setData(List data) {
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
            for(int i=0;i<backgroundRadius.length;i++) {
                this.backgroundRadius[i] = backgroundRadiu;
            }
            return this;
        }
        public Builder setBackgroundRadius(float[] backgroundRadius) {
            this.backgroundRadius = backgroundRadius;
            return this;
        }

        public void setColumnCount(int columnCount) {
            this.columnCount = columnCount;
        }

        public QDSheetDialog create(){
            return new QDSheetDialog(context,this);
        }

    }



public class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.VHItem>{
    private List data;
    private Context context;
    public SheetAdapter(Context context,List data) {
        this.context =context;
        this.data = data;
    }

    @NonNull
    @Override
    public VHItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = new FrameLayout(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        return new VHItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHItem vhItem, int i) {

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class VHItem extends RecyclerView.ViewHolder{

        public VHItem(@NonNull View itemView) {
            super(itemView);
            QDTextView textView = new QDTextView(itemView.getContext());
            textView.setText("aaa");
            int p = DisplayUtil.dip2px(itemView.getContext(),15);
            textView.setPadding(p,p,p,p);
            textView.setTextSize(18);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundResource(R.drawable.ripple_bg);
            ViewGroup.LayoutParams layoutParams= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ((ViewGroup)itemView).addView(textView,layoutParams);
        }
    }

}








}
