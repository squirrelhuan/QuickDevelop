package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.popup.QDTipPopup;
import cn.demomaster.qdrouter_library.base.OnReleaseListener;

/**
 * @author squirrel桓
 * @date 2018/11/15.
 * description：
 */
public class OptionsMenu implements OnReleaseListener {

    private final WeakReference<Context> contextWeakReference;
    private List<Menu> menus;
    private OnMenuItemClicked onMenuItemClicked;
    private OptionsMenuAdapter adapter;
    private RecyclerView rcv_options;
    private int rcv_options_width = -1;
    private View contentView;
    private View anchor;
    private float[] backgroundRadius = new float[8];
    private float backgroundRadiu = 0;
    private int backgroundColor = Color.WHITE;
    private boolean usePadding = true;
    private int textColor = Color.BLACK;
    private int dividerColor = Color.BLACK;
    private int textSize = 16;
    private boolean withArrow = true;
    public int padding;
    private int arrowWidth;
    private int arrowHeight;
    private int animationStyleID = R.style.qd_option_menu_pop_animation;
    private int textGravity = Gravity.CENTER_VERTICAL;
    private GuiderView.Gravity gravity = GuiderView.Gravity.TOP;

    public OptionsMenu(Builder builder) {
        this.contextWeakReference = builder.contextWeakReference;
        this.alpha = builder.alpha;
        this.gravity = builder.gravity;
        this.onMenuItemClicked = builder.onMenuItemClicked;
        this.backgroundColor = builder.backgroundColor;
        this.backgroundRadius = builder.backgroundRadius;
        this.backgroundRadiu = builder.backgroundRadiu;
        this.anchor = builder.anchor;
        this.usePadding = builder.usePadding;
        this.margin = builder.margin;
        this.menus = builder.menus;
        this.textColor = builder.textColor;
        this.textGravity = builder.textGravity;
        this.dividerColor = builder.dividerColor;
        this.textSize = builder.textSize;
        this.padding = builder.padding;
        this.arrowHeight = builder.arrowHeight;
        this.arrowWidth = builder.arrowWidth;
        this.withArrow = builder.withArrow;
        this.animationStyleID = builder.animationStyleID;
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

   /* public OptionsMenu(final Context context) {
        this.context = context;
        init();
    }*/

    public void init() {
        contentView = LayoutInflater.from(contextWeakReference.get()).inflate(R.layout.layout_dialog_option_menu, null, false);
        rcv_options = contentView.findViewById(R.id.qd_option_menu_recycler);
       /* QDRoundDrawable qdRoundDrawable = new QDRoundDrawable();
        for (int i = 0; i < backgroundRadius.length; i++) {
            this.backgroundRadius[i] = backgroundRadiu;
        }
        qdRoundDrawable.setCornerRadii(backgroundRadius);
        qdRoundDrawable.setBackGroundColor(backgroundColor);
        rcv_options.setBackground(qdRoundDrawable);*/
        rcv_options.setBackgroundColor(Color.TRANSPARENT);
        if (usePadding) {
            int l = (rcv_options == null) ? 0 : rcv_options.getPaddingLeft();
            int t = (rcv_options == null) ? 0 : rcv_options.getPaddingTop() + (int) backgroundRadiu;
            int r = (rcv_options == null) ? 0 : rcv_options.getPaddingRight();
            int b = (rcv_options == null) ? 0 : rcv_options.getPaddingBottom() + (int) backgroundRadiu;
            rcv_options.setPadding(l, t, r, b);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(contextWeakReference.get());
        linearLayoutManager.setAutoMeasureEnabled(true);
        //设置分割线使用的divider
        rcv_options.addItemDecoration(new QDDividerItemDecoration(contextWeakReference.get(), DividerItemDecoration.VERTICAL, dividerColor));
        rcv_options.setLayoutManager(linearLayoutManager);

        qdTipPopup = new QDTipPopup.Builder(contextWeakReference.get())
                .setBackgroundRadius(backgroundRadius)
                .setBackgroundColor(backgroundColor)
                .setPadding(padding)
                .setWithArrow(withArrow)
                .setArrowHeight(arrowHeight)
                .setArrowWidth(arrowWidth)
                .setTouchable(true)
                .setAnimationStyleID(animationStyleID)
                .create();
        qdTipPopup.setContentView(contentView);
        qdTipPopup.setOnDismissListener(onDismissListener);
        reBuild();
        //popupWindow.setAnimationStyle(R.style.pop_toast);
        //popupWindow.showAtLocation(getContentView(context), Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, dp2px(context,60));
    }

    PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams lp = ((Activity) contextWeakReference.get()).getWindow().getAttributes();
            lp.alpha = 1f;
            ((Activity) contextWeakReference.get()).getWindow().setAttributes(lp);
        }
    };

    private void reBuild() {
        if (menus != null) {
            adapter = new OptionsMenuAdapter(contextWeakReference.get(), menus);
            rcv_options.setAdapter(adapter);
            adapter.setTextColor(textColor);
            adapter.setTextGravity(textGravity);
        }
        if (adapter != null) {
            adapter.setOnItemClickListener(onItemClickListener);
        }
    }

    OptionsMenuAdapter.OnItemClickListener onItemClickListener = new OptionsMenuAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Menu menu) {
            if(onMenuItemClicked!=null)
            onMenuItemClicked.onItemClick(position, null);
            //popupWindow.dismiss();
        }
    };

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

    private QDTipPopup qdTipPopup;

    public void show() {
        if (anchor != null) {
            WindowManager.LayoutParams lp = ((Activity) contextWeakReference.get()).getWindow().getAttributes();
            lp.alpha = alpha;
            ((Activity) contextWeakReference.get()).getWindow().setAttributes(lp);
            /*LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) rcv_options.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            rcv_options.setLayoutParams(layoutParams);*/
            /*final int anchorLoc[] = new int[2];
             // 获取锚点View在屏幕上的左上角坐标位置
            anchor.getLocationOnScreen(anchorLoc);*/
            //popupWindow.showAsDropDown(anchor);
            //右侧的算法
            //这是旧的处理方式 popupWindow.showAsDropDown(anchor, -rcv_options_width + anchor.getWidth() - margin, margin);
            //popupWindow.showAtLocation(anchor,Gravity.LEFT,anchorLoc);
            qdTipPopup.showTip(anchor, gravity);
        }
    }

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
        //popupWindow.dismiss();
        if (qdTipPopup != null) {
            qdTipPopup.dismiss();
        }
    }

    public void show(View v) {
        setAnchor(v);
        show();
    }

    @Override
    public void onRelease(Object self) {
        dismiss();
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

    public static class Builder {
        private WeakReference<Context> contextWeakReference;
        private List<Menu> menus;
        private OnMenuItemClicked onMenuItemClicked;
        private float alpha = 1;
        private int margin = 4;
        private float[] backgroundRadius = new float[8];
        private float backgroundRadiu;
        private int backgroundColor = Color.WHITE;
        private int textColor = Color.BLACK;
        private int textSize = 16;
        private int dividerColor = Color.BLACK;
        private int textGravity = Gravity.CENTER_VERTICAL;
        private boolean usePadding = false;
        private boolean withArrow = true;
        public int padding;
        private int arrowWidth;
        private int arrowHeight;
        public int animationStyleID = R.style.qd_option_menu_pop_animation;
        private View anchor;
        private GuiderView.Gravity gravity = GuiderView.Gravity.TOP;

        public Builder setGravity(GuiderView.Gravity gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder(Context context) {
            this.contextWeakReference = new WeakReference<>(context);
            initBuilder();
        }

        public Builder(Context context, List<Menu> menus) {
            this.contextWeakReference = new WeakReference<>(context);
            this.menus = menus;
            initBuilder();
        }

        private void initBuilder() {
            arrowHeight = DisplayUtil.dip2px(contextWeakReference.get(), 8);
            arrowWidth = DisplayUtil.dip2px(contextWeakReference.get(), 8);
            padding = DisplayUtil.dip2px(contextWeakReference.get(), 6);
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

        public Builder setBackgroundRadius(float backgroundRadiu) {
            this.backgroundRadiu = backgroundRadiu;
            for (int i = 0; i < backgroundRadius.length; i++) {
                this.backgroundRadius[i] = backgroundRadiu;
            }
            return this;
        }

        public Builder setBackgroundRadius(float[] backgroundRadius) {
            this.backgroundRadius = backgroundRadius;
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

        public Builder setPadding(int padding) {
            this.padding = padding;
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

        public Builder setWithArrow(boolean withArrow) {
            this.withArrow = withArrow;
            return this;
        }

        public Builder setArrowWidth(int arrowWidth) {
            this.arrowWidth = arrowWidth;
            return this;
        }

        public Builder setArrowHeight(int arrowHeight) {
            this.arrowHeight = arrowHeight;
            return this;
        }

        public Builder setAnimationStyleID(int animationStyleID) {
            this.animationStyleID = animationStyleID;
            return this;
        }

        public OptionsMenu creat() {
            return new OptionsMenu(this);
        }

    }

}
