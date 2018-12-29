package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.CPopupWindow;
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
        //String[] items = tabSelectModels.get(tabIndex).getTabItems();
       /* List<TabListViewItem> menus = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            TabListViewItem menu = new TabListViewItem();
            menu.setItemName(items[i]);
            menu.setPosition(i);
            menus.add(menu);
        }
        tabListViewItems.clear();
        tabListViewItems.addAll(menus);*/
        columnCount = tabSelectModels.get(tabIndex).getColumnCount();//默认内容列表显示几列
        initSingTabContent(tabGroup, tabButton, tabIndex);
        popupWindow.showAsDropDown(tabGroup);
        // popupWindow.showAsDropDown(v,0,0,Gravity.TOP);

    }

    private PopupWindow popupWindow;
    private RecyclerView recy_tab_content;
    private TabMenuAdapter adapter;
    private RelativeLayout rel_root, rl_tab_menu_custom_panel;
    private LinearLayout ll_tab_content_panel;
    private View contentView;
    private int tabToBottom = 100;


    public void setTabToBottom(int tabToBottom) {
        this.tabToBottom = tabToBottom;
    }

    private int[] location;
    private int columnCount = 1;//默认内容列表显示几列


    //初始化单个tab内容页
    private void initSingTabContent(final View tabGroup, final View tabButton, final int tabIndex) {
        if (popupWindow == null) {
            CPopupWindow.PopBuilder builder = new CPopupWindow.PopBuilder((Activity) context);
            contentView = LayoutInflater.from(context).inflate(R.layout.layout_mul_menu, null, false);
            location = new int[2];
            tabGroup.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
            tabGroup.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
            System.out.println("view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);
            popupWindow = builder.setContentView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, (int) (QMUIDisplayHelper.getScreenHeight(context) - location[1]), true).build();

            contentView.setPadding(0, tabGroup.getHeight(), 0, 0);
            ll_tab_content_panel = contentView.findViewById(R.id.cgq_ll_tab_menu_item_panel);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_tab_content_panel.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, DisplayUtil.dp2px(context, tabToBottom));
            rl_tab_menu_custom_panel = contentView.findViewById(R.id.rl_tab_menu_custom_panel);
            recy_tab_content = contentView.findViewById(R.id.recy_tab_content);
            rel_root = contentView.findViewById(R.id.rel_root);
            rel_root.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getX() > location[0] && motionEvent.getX() < (location[0] + tabGroup.getWidth()) && motionEvent.getY() < tabGroup.getHeight()) {
                        int wc = tabGroup.getWidth() / tabCount;
                        int x = (int) motionEvent.getX() - location[0];
                        int index = (int) (x / wc);
                        tabRadioGroup.setCurrentTab(index);
                    } else {
                        popupWindow.dismiss();
                    }
                    return false;
                }
            });

            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //弹窗消失tab按钮需要恢复之前的状态
                    tabRadioGroup.resume();
                }
            });
        }
        if (columnCount >= 1) {//默认布局样式
            rl_tab_menu_custom_panel.setVisibility(GONE);
            recy_tab_content.setVisibility(VISIBLE);
           // LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) recy_tab_content.getLayoutParams());
            if (columnCount == 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setAutoMeasureEnabled(true);
                recy_tab_content.setLayoutManager(linearLayoutManager);
                //view加载完成时回调
                recy_tab_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recy_tab_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        recy_tab_content.setX(location[0] + ((tabIndex * 2) + 1) * tabButton.getWidth() / 2 - recy_tab_content.getWidth() / 2);//QMUIDisplayHelper.getScreenWidth(context)
                    }
                });
            } else if (columnCount > 1) {
                recy_tab_content.setLayoutManager(new GridLayoutManager(context, columnCount));
                //view加载完成时回调
                recy_tab_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recy_tab_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        recy_tab_content.setX((QMUIDisplayHelper.getScreenWidth(context)-recy_tab_content.getWidth()) / 2);//
                    }
                });
            }
            adapter = new TabMenuAdapter(context, tabSelectModels, tabIndex);
            adapter.setColors(tabSelectModels.get(tabIndex).getColorSelect_content(),tabSelectModels.get(tabIndex).getColorNormal_content());
            recy_tab_content.setAdapter(adapter);
            //adapter.setColors(color_selected,color_normal);
            //recy_tab_content.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            adapter.setOnItemClickListener(new TabMenuAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    tabMenuInterface.onSelected(tabIndex, position);
                    //adapter.setOnClickItem(position);
                    adapter.setOnItemClicked(position);
                    tabSelectModels = adapter.getTabMenuModels();
                    //popupWindow.dismiss();
                }
            });
        } else if (columnCount == -1) {//自定义contentView
            recy_tab_content.setVisibility(GONE);
            rl_tab_menu_custom_panel.removeAllViews();
            rl_tab_menu_custom_panel.setVisibility(VISIBLE);
            View view = LayoutInflater.from(context).inflate(tabSelectModels.get(tabIndex).getContentResId(), rl_tab_menu_custom_panel, true);
            if (tabSelectModels.get(tabIndex).getOnCreatTabContentView() != null) {
                tabSelectModels.get(tabIndex).getOnCreatTabContentView().onCreat(view);
            }
        }

    }

   /* private int color_selected=Color.RED;
    private int color_normal=Color.BLACK;

    //设置选中的颜色
    public void setColors(int color_selected,int color_normal) {
        this.color_selected = color_selected;
        this.color_normal = color_normal;
    }*/

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
