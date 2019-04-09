package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.CPopupWindow;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundDrawable;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;

/**
 * @author squirrel桓
 * @date 2018/11/15.
 * description：
 */
public class OptionsMenu {

    public static String TAG = "CGQ";
    private Context context;
    private List<Menu> menus;
    private OnMenuItemClicked onMenuItemClicked;
    private CustomDialog customDialog;
    private LinearLayoutManager linearLayoutManager;
    private OptionsMenuAdapter adapter;
    private RecyclerView rcv_options;
    private int rcv_options_width = -1;
    private PopupWindow popupWindow;
    private View contentView;
    private View anchor;
    private float[] backgroundRadius = new float[8];
    private int backgroundRadiu = 0;
    private int backgroundColor = Color.WHITE;
    private boolean usePadding = true;
    private int textColor = Color.BLACK;
    private int dividerColor = Color.BLACK;
    private int textSize = 16;
    private int textGravity = Gravity.CENTER_VERTICAL;
    private Builder builder;

    public OptionsMenu(Builder builder) {
        this.context = builder.context;
        this.builder = builder;
        this.alpha = builder.alpha;
        this.onMenuItemClicked = builder.onMenuItemClicked;
        this.backgroundColor = builder.backgroundColor;
        this.backgroundRadiu = builder.backgroundRadiu;
        this.anchor = builder.anchor;
        this.usePadding = builder.usePadding;
        this.margin = builder.margin;
        this.menus = builder.menus;
        this.textColor = builder.textColor;
        this.textGravity = builder.textGravity;
        this.dividerColor = builder.dividerColor;
        this.textSize = builder.textSize;
        init();
    }

    public void setBackgroundRadiu(int backgroundRadiu) {
        this.backgroundRadiu = backgroundRadiu;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setUsePadding(boolean usePadding) {
        this.usePadding = usePadding;
    }

    public OptionsMenu(final Context context) {
        this.context = context;
        init();
    }

    public void init() {
        CPopupWindow.PopBuilder builder = new CPopupWindow.PopBuilder((Activity) context);
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_option_menu, null, false);
        rcv_options = contentView.findViewById(R.id.qd_option_menu_recycler);
        QDRoundDrawable qdRoundDrawable = new QDRoundDrawable();
        for (int i = 0; i < backgroundRadius.length; i++) {
            this.backgroundRadius[i] = backgroundRadiu;
        }
        qdRoundDrawable.setCornerRadii(backgroundRadius);
        qdRoundDrawable.setBackGroundColor(backgroundColor);
        rcv_options.setBackground(qdRoundDrawable);
        if (usePadding) {
            rcv_options.setPadding(rcv_options.getPaddingLeft(), rcv_options.getPaddingTop() + backgroundRadiu, rcv_options.getPaddingRight(), rcv_options.getPaddingBottom() + backgroundRadiu);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setAutoMeasureEnabled(true);
        //设置分割线使用的divider
        rcv_options.addItemDecoration(new QDDividerItemDecoration(context, DividerItemDecoration.VERTICAL, dividerColor));
        rcv_options.setLayoutManager(linearLayoutManager);
        rcv_options.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rcv_options.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (rcv_options_width == -1) {//第一次加载完成才能确定位置
                    rcv_options_width = 0;
                    popupWindow.update(anchor, rcv_options.getWidth(), rcv_options.getHeight());
                }
            }
        });

        popupWindow = builder.setContentView(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true).build();
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });
        reBuild();
        //popupWindow.setAnimationStyle(R.style.pop_toast);
        //popupWindow.showAtLocation(getContentView(context), Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, dp2px(context,60));
    }

    private void reBuild() {
        if (menus != null) {
            adapter = new OptionsMenuAdapter(context, menus);
            rcv_options.setAdapter(adapter);
            adapter.setTextColor(textColor);
            adapter.setTextGravity(textGravity);

        }
        if (adapter != null) {
            adapter.setOnItemClickListener(new OptionsMenuAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Menu menu) {
                    onMenuItemClicked.onItemClick(position, null);
                    popupWindow.dismiss();
                }
            });
        }
    }

    private float alpha = 1;

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    private int margin = 4;

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void show() {
        if (anchor != null) {
            WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
            lp.alpha = alpha;
            ((Activity) context).getWindow().setAttributes(lp);

            /*LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) rcv_options.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            rcv_options.setLayoutParams(layoutParams);*/
            /*final int anchorLoc[] = new int[2];
             // 获取锚点View在屏幕上的左上角坐标位置
            anchor.getLocationOnScreen(anchorLoc);*/
            //popupWindow.showAsDropDown(anchor);
            //右侧的算法
            popupWindow.showAsDropDown(anchor, -rcv_options_width + anchor.getWidth() - margin, margin);
            //popupWindow.showAtLocation(anchor,Gravity.LEFT,anchorLoc);
        }
    }
    /*
     *//*
     * 循环找到ListView最大宽度
     *//*
    private int getMaxWidth(ListView listView) {
        int maxWidth = 0;
        if (listView.getAdapter() == null) {
            return maxWidth;
        }

        int count = listView.getAdapter().getCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            view = listView.getAdapter().getView(i, null, listView);
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            int height = view.getMeasuredHeight();
            int width = view.getMeasuredWidth();
            if (maxWidth < width) {
                maxWidth = width;
            }
            System.out.println("measure width=" + width + " height=" + height);
        }
        return maxWidth;
    }*/

    public List getMenus() {
        return menus;
    }

    public void setMenus(List menus) {
        this.menus = menus;
        reBuild();
    }

    public OnMenuItemClicked getOnMenuItemClicked() {
        return onMenuItemClicked;
    }

    public void setOnMenuItemClicked(OnMenuItemClicked onMenuItemClicked) {
        this.onMenuItemClicked = onMenuItemClicked;
        reBuild();
    }

    public void setAnchor(View anchor) {
        this.anchor = anchor;
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public void show(View v) {
        setAnchor(v);
        show();
    }

    public interface OnMenuItemClicked {
        void onItemClick(int position, View view);
    }

    public static class Menu {
        private String title;
        private int iconId;
        private int position;
        private int iconWidth;
        private int iconPadding;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIconId() {
            return iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getIconWidth() {
            return iconWidth;
        }

        public void setIconWidth(int iconWidth) {
            this.iconWidth = iconWidth;
        }

        public int getIconPadding() {
            return iconPadding;
        }

        public void setIconPadding(int iconPadding) {
            this.iconPadding = iconPadding;
        }
    }


    public class MyPopupWindow extends PopupWindow {
        public MyPopupWindow(Context context, WindowManager.LayoutParams lp) {
            super(context);
            this.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
        }

        @Override
        public void setOnDismissListener(OnDismissListener onDismissListener) {
            super.setOnDismissListener(onDismissListener);
        }

        @Override
        public void update(int width, int height) {
            super.update(width, height);
        }
    }

    public static class Builder {

        private Context context;
        private List<Menu> menus;
        private OnMenuItemClicked onMenuItemClicked;
        private float alpha = 1;
        private int margin = 4;
        private int backgroundRadiu = 0;
        private int backgroundColor = Color.WHITE;
        private int textColor = Color.BLACK;
        private int textSize = 16;
        private int dividerColor = Color.BLACK;
        private int textGravity = Gravity.CENTER_VERTICAL;
        private boolean usePadding = true;
        private View anchor;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, List<Menu> menus) {
            this.context = context;
            this.menus = menus;
        }

        public Builder setMenus(List<Menu> menus) {
            this.menus = menus;
            return this;
        }

        public Builder setOnMenuItemClicked(OnMenuItemClicked onMenuItemClicked) {
            this.onMenuItemClicked = onMenuItemClicked;
            return this;
        }

        public Builder setAlpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder setMargin(int margin) {
            this.margin = margin;
            return this;
        }

        public Builder setBackgroundRadiu(int backgroundRadiu) {
            this.backgroundRadiu = backgroundRadiu;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setUsePadding(boolean usePadding) {
            this.usePadding = usePadding;
            return this;
        }

        public Builder setAnchor(View anchor) {
            this.anchor = anchor;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Builder setTextGravity(int textGravity) {
            this.textGravity = textGravity;
            return this;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public OptionsMenu creat() {
            return new OptionsMenu(this);
        }

    }

}
