package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.CPopupWindow;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;

/**
 * @author squirrel桓
 * @date 2018/11/23.
 * description：
 */
public class TabMenuLayout extends LinearLayout {
    public TabMenuLayout(Context context) {
        super(context);
        this.context = context;
    }

    public TabMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TabMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private int center_x, center_y, mwidth, width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        center_x = width / 2;
    }

    private View tabButtonView;//点击的按钮
    private View tabDividerView;//分割符

    public void setTabDividerView(View tabDividerView) {
        this.tabDividerView = tabDividerView;
    }

    public void setData(List<TabMenuModel> tabSelectModels, TabMenuInterface tabMenuInterface) {
        this.tabSelectModels = tabSelectModels;
        this.tabMenuInterface = tabMenuInterface;
        init();
    }

    //private ShowType ;
    public enum TabMenuShowType {

    }

    private Context context;
    private TabMenuInterface tabMenuInterface;//构建组建不可缺少
    private List<TabMenuModel> tabSelectModels;
    private int tabCount;//可选项个数
    private TabRadioGroup tabRadioGroup;//放置tab按钮的group
    /*private List<TabListViewItem> tabListViewItems;//tab下的list数据源*/

    //初始化
    public void init() {
        if (tabMenuInterface == null && tabSelectModels == null) {
            return;
        }
        if (tabRadioGroup == null) {
            tabRadioGroup = new TabRadioGroup(context);
            tabRadioGroup.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tabRadioGroup.setOrientation(HORIZONTAL);
            //tabListViewItems = new ArrayList<>();
        } else {
            tabRadioGroup.removeAllViews();
            //tabListViewItems.clear();
            this.removeAllViews();
        }
        tabCount = tabSelectModels.size();
        //遍历生成tab按钮
        for (int i = 0; i < tabCount; i++) {
            TabRadioGroup.TabRadioButton textView = tabSelectModels.get(i).getTabButtonView();
            if (textView == null) {
                textView = new TabButton(context);
            }
            textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            textView.setGravity(Gravity.CENTER);
            textView.setTabName(tabSelectModels.get(i).getTabName());
            textView.setState(false);
            tabRadioGroup.addTabButton(textView);
            tabRadioGroup.addTabDividerView(tabDividerView);
        }

        tabRadioGroup.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabRadioGroup.setOnCheckedChangeListener(new TabRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, int i) {
                showTabMenuView(tabRadioGroup, view, i);
            }
        });
        addView(tabRadioGroup);
    }

    private void showTabMenuView(View tabGroup, View tabButton, int tabIndex) {
        //获取第tabIndex个标签下的字符数组来生成list
        String[] items = tabSelectModels.get(tabIndex).getTabItems();
       /* List<TabListViewItem> menus = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            TabListViewItem menu = new TabListViewItem();
            menu.setItemName(items[i]);
            menu.setPosition(i);
            menus.add(menu);
        }
        tabListViewItems.clear();
        tabListViewItems.addAll(menus);*/
        initSingTabContent(tabGroup, tabIndex);
        popupWindow.showAsDropDown(tabGroup);
        // popupWindow.showAsDropDown(v,0,0,Gravity.TOP);

    }

    private PopupWindow popupWindow;
    private ListView lv_options;
    private TabMenuAdapter adapter;
    private RelativeLayout rel_root;
    private View contentView;
    private int tabToBottom = 100;


    public void setTabToBottom(int tabToBottom) {
        this.tabToBottom = tabToBottom;
    }

    //初始化单个tab内容页
    private void initSingTabContent(View view, final int tabIndex) {
        if (popupWindow == null) {
            CPopupWindow.PopBuilder builder = new CPopupWindow.PopBuilder((Activity) context);
            contentView = LayoutInflater.from(context).inflate(R.layout.layout_mul_menu, null, false);
            int[] location = new int[2];
            view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
            view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
            System.out.println("view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);
            popupWindow = builder.setContentView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, (int) (QMUIDisplayHelper.getScreenHeight(context) - location[1] - view.getHeight()), true).build();

            LinearLayout ll_tab_panel = contentView.findViewById(R.id.cgq_ll_tab_menu_item_panel);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_tab_panel.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, DisplayUtil.dp2px(context, tabToBottom));
            rel_root = contentView.findViewById(R.id.rel_root);
            rel_root.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
            lv_options = contentView.findViewById(R.id.lv_menus);

            popupWindow.setTouchable(true);
            popupWindow.setFocusable(false);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //弹窗消失tab按钮需要恢复之前的状态
                    tabRadioGroup.resume();
                }
            });
        }
        adapter = new TabMenuAdapter(context, tabSelectModels, tabIndex);
        lv_options.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) lv_options.getLayoutParams());
        //默认位置在当前tab下平分viewgroup宽度
        layoutParams.width = width / tabCount;
        lv_options.setLayoutParams(layoutParams);
        lv_options.setX(view.getX());//QMUIDisplayHelper.getScreenWidth(context)
        lv_options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabMenuInterface.onSelected(tabIndex, position);
                //adapter.setOnClickItem(position);
                adapter.setOnItemClicked(position);
                tabSelectModels = adapter.getTabMenuModels();
                //popupWindow.dismiss();
            }
        });


    }

    public void setTabMenu(TabMenuInterface menuTab) {
        this.tabMenuInterface = menuTab;
    }

    public interface TabMenuInterface {/*
        //根据tabIndex获取tab显示内容的list列表
        List<String> getItemText(int tabIndex);*/

        //点击选项时候触发
        String onSelected(int tabIndex, int position);/*
        //获取tab的名字
        String[] getTabNames();
        //获取默认状态集合（多选则为list,单选则为int）
        List<TabSelectModel> getDefaultState();*/
    }

    public static class TabButton extends TabRadioGroup.TabRadioButton {
        private Context context;
        private TextView nameTextView;

        public TabButton(Context context) {
            super(context);
            initView(context);
        }

        public TabButton(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            initView(context);
        }

        public TabButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView(context);
        }

        @Override
        public void setState(Boolean state) {
            if (nameTextView != null) {
                nameTextView.setTextColor(state ? Color.RED : Color.BLACK);
            }
        }

        @Override
        public void setTabName(String tabName) {
            if (nameTextView != null) {
                nameTextView.setText(tabName);
            }
        }

        @Override
        public void initView(Context context) {
            this.context = context;
            nameTextView = new TextView(context);
            nameTextView.setTextSize(18);
            addView(nameTextView);
        }
    }

}
