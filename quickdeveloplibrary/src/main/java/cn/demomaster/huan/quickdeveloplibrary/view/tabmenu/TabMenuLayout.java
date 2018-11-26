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
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;

/**
 * @author squirrel桓
 * @date 2018/11/23.
 * description：
 */
public class TabMenuLayout extends LinearLayout {
    public TabMenuLayout(Context context) {
        super(context);
    }

    public TabMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private View tabButtonView;//点击的按钮
    private View tabDividerView;//分割符

    //private ShowType ;
    public enum TabMenuShowType {

    }

    TabRadioGroup linearLayout;
    //初始化
    public void init(Context context) {
        this.context = context;
         linearLayout = new TabRadioGroup(context);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(HORIZONTAL);
        tabCount = tabMenuInterface.getTabCount();

        for (int i = 0; i < tabMenuInterface.getTabCount(); i++) {
            TabButton textView = new TabButton(context);
            textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            textView.setGravity(Gravity.CENTER);
            textView.setTabName(tabMenuInterface.getTabName(i));/*
            textView.setTag(i);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTabMenuView(v, (int) v.getTag());
                }
            });*/
            linearLayout.addTabButton(textView);
        }
        linearLayout.setOnCheckedChangeListener(new TabRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, int i) {
                showTabMenuView(view, i);
            }
        });
        addView(linearLayout);
    }

    private void showTabMenuView(View v, int tabIndex) {
        List<String> arr = tabMenuInterface.getItemText(tabIndex);
        List<TabListViewItem> menus = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            TabListViewItem menu = new TabListViewItem();
            menu.setItemName(arr.get(i));
            menu.setPosition(i);
            menus.add(menu);
        }
        mulMenus.clear();
        mulMenus.addAll(menus);
        initMulMenu(v, tabIndex);
        popupWindow.showAsDropDown(v);
        // popupWindow.showAsDropDown(v,0,0,Gravity.TOP);

    }

    PopupWindow popupWindow;
    private ListView lv_options;
    private TabMenuAdapter adapter;
    private List<TabListViewItem> mulMenus = new ArrayList<>();
    private RelativeLayout rel_root;
    private View contentView;
    private List<Object> selected_list=new ArrayList();
    private Object selected_arr;
    private int tabCount;

    private void initMulMenu(View view, final int tabIndex) {
        if (popupWindow == null) {
            selected_list.addAll(tabMenuInterface.getDefaultState());
            CPopupWindow.PopBuilder builder = new CPopupWindow.PopBuilder((Activity) context);
            contentView = LayoutInflater.from(context).inflate(R.layout.layout_mul_menu, null, false);
            int[] location = new int[2];
            view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
            view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
            System.out.println("view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);
            popupWindow = builder.setContentView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, (int) (QMUIDisplayHelper.getScreenHeight(context) - location[1] - view.getHeight()), true).build();

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
                    linearLayout.resume();
                }
            });
        }
        selected_arr =  selected_list.get(tabIndex);
        adapter = new TabMenuAdapter(context, mulMenus,selected_arr);
        lv_options.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams)lv_options.getLayoutParams());
        layoutParams.width=QMUIDisplayHelper.getScreenWidth(context)/tabCount;
        lv_options.setLayoutParams(layoutParams);
        lv_options.setX(view.getX());//QMUIDisplayHelper.getScreenWidth(context)
        lv_options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabMenuInterface.onSelected(tabIndex, position);
                //adapter.setOnClickItem(position);
                adapter.setOnItemClicked(position);
                selected_list.set(tabIndex,adapter.getSelectedList());
                //popupWindow.dismiss();
            }
        });


    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    private Context context;
    private TabMenuInterface tabMenuInterface;

    public void setTabMenu(TabMenuInterface menuTab) {
        this.tabMenuInterface = menuTab;
    }

    public interface TabMenuInterface {
        List<String> getItemText(int tabIndex);

        String onSelected(int tabIndex, int position);

        String getTabName(int tabIndex);

        int getTabCount();

        List<Object> getDefaultState();
    }

    public class TabButton extends TabRadioGroup.TabRadioButton{
        private Context context;
        private TextView nameTextView;
        public TabButton(Context context) {
            super(context);
            init(context);
        }

        public TabButton(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public TabButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        void init(Context context){
            this.context = context;
            nameTextView = new TextView(context);
            nameTextView.setTextSize(18);
            addView(nameTextView);
        }
        @Override
        public void setState(Boolean state) {
            if(nameTextView!=null){
                nameTextView.setTextColor(state?Color.RED:Color.BLACK);
            }
        }

        @Override
        public void setTabName(String tabName) {
            if(nameTextView!=null){
                nameTextView.setText(tabName);
            }
        }
    }

}
