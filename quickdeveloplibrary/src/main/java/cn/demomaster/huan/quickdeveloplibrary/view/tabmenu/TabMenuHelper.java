package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnSingleItemClickListener;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;

public class TabMenuHelper {
    private int[] location;
    private PopupWindow popupWindow;
    private View contentView;
    private RecyclerView recy_tab_content;
    private TabMenuAdapter adapter;
    private int columnCount = 1;//默认内容列表显示几列
    private List<TabMenuModel> tabSelectModels;
    int tabIndex =0;

    AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void showTabPanel(Context context, final View tabButton, List<TabMenuModel> tabSelectModels) {
        this.tabSelectModels =tabSelectModels;
        showTabPanel(context,tabButton,R.layout.quick_layout_mul_menu);
    }
    public void showTabPanel(Context context,final View tabButton, int contentViewId) {
        View contentView = LayoutInflater.from(context).inflate(contentViewId, null, false);
        showTabPanel(context,contentView,tabButton);
    }
    //初始化单个tab内容页
    public void showTabPanel(Context context,View contentView,final View tabButton) {
        if(contentView==null){
            return;
        }
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //
        if(tabButton==null){
            return;
        }
        location = new int[2];
        tabButton.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        tabButton.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        //
        if (popupWindow == null) {
            popupWindow = new PopupWindow();
            //QDLogger.println("view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);
            popupWindow.setContentView(contentView);

            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            //popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(QMUIDisplayHelper.getScreenHeight(context) - location[1]-tabButton.getMeasuredHeight());
            //popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setTouchable(true);
            //contentView.setPadding(0, tabGroup.getHeight(), 0, 0);
            recy_tab_content = contentView.findViewById(R.id.recy_tab_content);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
        }
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent_dark_55));

        if (columnCount >= 1) {//默认布局样式
            int[] locationTab = new int[2];
            tabButton.getLocationInWindow(locationTab); //获取在当前窗口内的绝对坐标
            tabButton.getLocationOnScreen(locationTab);//获取在整个屏幕内的绝对坐标
            recy_tab_content.setVisibility(VISIBLE);
            //LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) recy_tab_content.getLayoutParams());
            if (columnCount == 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setAutoMeasureEnabled(true);
                recy_tab_content.setLayoutManager(linearLayoutManager);
                //view加载完成时回调
                /*recy_tab_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recy_tab_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        recy_tab_content.setX(locationTab[0] + tabButton.getWidth() / 2f - recy_tab_content.getWidth() / 2f);//QMUIDisplayHelper.getScreenWidth(context)
                    }
                });*/
            } else if (columnCount > 1) {
                recy_tab_content.setLayoutManager(new GridLayoutManager(context, columnCount));
                //view加载完成时回调
                recy_tab_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recy_tab_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        recy_tab_content.setX((DisplayUtil.getScreenWidth(context) - recy_tab_content.getWidth()) / 2f);//
                    }
                });
            }
            adapter = new TabMenuAdapter(context, tabSelectModels, tabIndex);
           // adapter.setColors(tabSelectModels.get(tabIndex).getColorSelect_content(), tabSelectModels.get(tabIndex).getColorNormal_content());
            recy_tab_content.setAdapter(adapter);
            //adapter.setColors(color_selected,color_normal);
            //recy_tab_content.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.setOnItemClicked(position);
                    tabSelectModels = adapter.getTabMenuModels();
                    if(onItemClickListener!=null) {
                        onItemClickListener.onItemClick(parent, view, position, id);
                    }
                }
            });
        } else if (columnCount == -1) {//自定义contentView
            recy_tab_content.setVisibility(GONE);
            //View view = LayoutInflater.from(context).inflate(tabSelectModels.get(tabIndex).getContentResId(), rl_tab_menu_custom_panel, true);
           /* if (tabSelectModels.get(tabIndex).getOnCreatTabContentView() != null) {
                tabSelectModels.get(tabIndex).getOnCreatTabContentView().onCreat(view);
            }*/
        }
        popupWindow.showAsDropDown(tabButton);
    }

    public void dismiss() {
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    public static class TButton extends TabRadioGroup.TabRadioButton {
        private TextView tv_tab_name;
        private View view;
        public TButton(Context context) {
            super(context);
        }

        public TButton(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void setState(Boolean state) {
            if (state) {
                tv_tab_name.setTextColor(Color.YELLOW);
            } else {
                tv_tab_name.setTextColor(Color.BLUE);
            }
        }

        @Override
        public void setTabName(String tabName) {
            tv_tab_name.setText(tabName);
        }

        @Override
        public void initView(Context context) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            /*view = inflater.inflate(R.layout.item_tab_menu_layout, null);
            tv_tab_name = view.findViewById(R.id.tv_tab_name);
            view.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            this.addView(view);*/
        }
    }
}
