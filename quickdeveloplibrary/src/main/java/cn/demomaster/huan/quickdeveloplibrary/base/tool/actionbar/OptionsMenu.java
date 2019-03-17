package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.CPopupWindow;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
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

    public OptionsMenu(final Context context) {
        this.context = context;
        /*CustomDialog.Builder builder = new CustomDialog.Builder(context, R.layout.layout_dialog_option_menu);
        customDialog = builder.setCanTouch(true).create();
        lv_options = customDialog.getContentView().findViewById(R.id.lv_options);
        Window window = customDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.NO_GRAVITY);
        lp.x = 10;
        lp.y = 10;
        lp.width = 200;
        lp.height = 200;
        lp.alpha = 0.6f;
        window.setAttributes(lp);*/

        CPopupWindow.PopBuilder builder = new CPopupWindow.PopBuilder((Activity) context);
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_option_menu, null, false);
        rcv_options = contentView.findViewById(R.id.rcv_options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setAutoMeasureEnabled(true);
        //设置分割线使用的divider
        rcv_options.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rcv_options.setLayoutManager(linearLayoutManager);
        rcv_options.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rcv_options.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                /*if (rcv_options_width == -1) {
                    rcv_options_width = rcv_options.getWidth();
                    LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) rcv_options.getLayoutParams();
                    layoutParams.setMargins(rcv_options_width,0,0,0);
                    rcv_options.setLayoutParams(layoutParams);
                }*/
                if (rcv_options_width == -1) {//第一次加载完成才能确定位置
                    rcv_options_width =0;
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
        //popupWindow.setAnimationStyle(R.style.pop_toast);
        //popupWindow.showAtLocation(getContentView(context), Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, dp2px(context,60));

    }

    private void reBuild() {
        if (menus != null) {
            adapter = new OptionsMenuAdapter(context, menus);
            rcv_options.setAdapter(adapter);
        }
        adapter.setOnItemClickListener(new OptionsMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Menu menu) {
                onMenuItemClicked.onItemClick(position, null);
                popupWindow.dismiss();
            }
        });

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
            popupWindow.showAsDropDown(anchor, -rcv_options_width + anchor.getWidth() - DisplayUtil.dip2px(context, margin), DisplayUtil.dip2px(context, margin));
            //popupWindow.showAtLocation(anchor,Gravity.LEFT,anchorLoc);
        }
    }

    /*
     * 循环找到ListView最大宽度
     */
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

        /*if (context.getResources().getDisplayMetrics().widthPixels < maxWidth) {
            return context.getResources().getDisplayMetrics().widthPixels - 50;
        }*/

    }



    /*OptionsMenu(List<Menu> menus) {
        this.menus = menus;
    }

    OptionsMenu(List<Menu> menus, OnMenuItemClicked onMenuItemClicked) {
        this.menus = menus;
        this.onMenuItemClicked = onMenuItemClicked;
        Log.d(TAG, "menu子菜单个数："+menus.size());
        Log.d(TAG, "menu的第一个子菜单标题："+menus.get(0).getTitle());
        Log.d(TAG, "menu的第一个子菜单id："+menus.get(0).getPosition());
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

    public interface OnMenuItemClicked {
        void onItemClick(int position, View view);
    }

    public static class Menu {
        private String title;
        private int iconId;
        private int position;

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
}
