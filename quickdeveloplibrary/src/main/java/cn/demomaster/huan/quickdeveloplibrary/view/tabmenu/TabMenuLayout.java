package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
    public enum TabMenuShowType{

    }

    //初始化
   public void init(Context context){
       this.context = context;
        LinearLayout linearLayout = new LinearLayout(context);
       linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(HORIZONTAL);
        for(int i=0;i<tabMenuInterface.getTabCount();i++){
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1));
            textView.setGravity(Gravity.CENTER);
            textView.setText(tabMenuInterface.getTabName(i));
            textView.setTag(i);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTabMenuView(v,(int)v.getTag());
                }
            });
            linearLayout.addView(textView);
        }
        addView(linearLayout);
    }

    private void showTabMenuView(View v,int tabIndex) {
        List<String> arr = tabMenuInterface.getItemText(tabIndex);
        List<OptionsMenu.Menu> menus = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            OptionsMenu.Menu menu = new OptionsMenu.Menu();
            menu.setTitle(arr.get(i));
            menu.setPosition(i);
            menus.add(menu);
        }
        mulMenus.clear();
        mulMenus.addAll(menus);
        initMulMenu(v,tabIndex);
        popupWindow.showAsDropDown(v);
       // popupWindow.showAsDropDown(v,0,0,Gravity.TOP);

    }
    PopupWindow popupWindow;
    private ListView lv_options;
    private TabMenuAdapter adapter;
    private List<OptionsMenu.Menu> mulMenus = new ArrayList<>();
    private void initMulMenu(View view, final int tabIndex) {
        CPopupWindow.PopBuilder builder = new CPopupWindow.PopBuilder((Activity) context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_mul_menu, null, false);
        lv_options = contentView.findViewById(R.id.lv_menus);
        adapter = new TabMenuAdapter(context, mulMenus);
        lv_options.setAdapter(adapter);
        lv_options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabMenuInterface.onSelected(position,tabIndex);
            }
        });
        //lv_options = contentView.findViewById(cn.demomaster.huan.quickdeveloplibrary.R.id.lv_options);
        int[] location = new  int[2] ;
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        System.out.println("view--->x坐标:"+location [0]+"view--->y坐标:"+location [1]);
        popupWindow = builder.setContentView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, (int)(QMUIDisplayHelper.getScreenHeight(context)-location [1]-view.getHeight()), true).build();
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

    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    private Context context;
    private TabMenuInterface tabMenuInterface ;

    public void setTabMenu(TabMenuInterface menuTab) {
        this.tabMenuInterface = menuTab;
    }

   public interface TabMenuInterface{
        List<String> getItemText(int tabIndex);
        String onSelected(int tabIndex,int position);
        String getTabName(int tabIndex);
        int getTabCount();
    }

}
