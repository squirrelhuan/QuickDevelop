package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.CPopupWindow;
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
    private ListView lv_options;
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
        lv_options = contentView.findViewById(R.id.lv_options);
        popupWindow = builder.setContentView(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true).build();
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                /*WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity)context).getWindow().setAttributes(lp);*/
            }
        });
        //popupWindow.setAnimationStyle(R.style.pop_toast);
        //popupWindow.showAtLocation(getContentView(context), Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, dp2px(context,60));

    }

    private void reBuild() {
        if (menus != null) {
            adapter = new OptionsMenuAdapter(context, menus);
            lv_options.setAdapter(adapter);
        }
        lv_options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMenuItemClicked.onItemClick(position, view);
                popupWindow.dismiss();
            }
        });

    }

    public void show() {
        if(anchor!=null){
           /* WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
            lp.alpha = 0.4f;
            ((Activity)context).getWindow().setAttributes(lp);*/
           //右侧的算法
            popupWindow.showAsDropDown(anchor,-context.getResources().getDimensionPixelOffset(R.dimen.quickdev_option_menu_width)+anchor.getWidth(),0);

        }

        //customDialog.show();
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


    public class MyPopupWindow extends PopupWindow{
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


    }
}
