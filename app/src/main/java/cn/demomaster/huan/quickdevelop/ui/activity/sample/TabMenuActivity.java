package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView2;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuLayout;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuModel;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabRadioGroup;

@ActivityPager(name = "Tab菜单", preViewClass = TextView.class, resType = ResType.Custome)
public class TabMenuActivity extends BaseActivity {

    TabMenuLayout tabMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_menu);

        //数据初始化
        String[] aaa = new String[30];
        for (int i = 0; i < aaa.length; i++) {
            aaa[i] = "aaa" + (i < 10 ? "---" : "****#####a");
        }
        String[] bbb = new String[20];
        for (int i = 0; i < bbb.length; i++) {
            bbb[i] = "bbb" + (i < 10 ? "---" : "****#####bbbbbbbbbb");
        }
        String[] ccc = new String[120];
        for (int i = 0; i < ccc.length; i++) {
            ccc[i] = "ccc" + (i < 10 ? "---" : "****#####c");
        }
        List<Integer> selectData_a = new ArrayList<>();
        selectData_a.add(1);
        List<Integer> selectData_b = new ArrayList<>();
        selectData_b.add(2);
        selectData_b.add(3);
        List<Integer> selectData_c = new ArrayList<>();
        selectData_c.add(4);
        selectData_c.add(7);
        final List<TabMenuModel> tabSelectModels = new ArrayList<>();//用来存放初始化选项的状态
        TabMenuModel tabMenuModel = new TabMenuModel("单选", aaa, selectData_a);
        tabMenuModel.setTabButtonView(new TButton(this));
        tabSelectModels.add(tabMenuModel);
        TabMenuModel tabMenuModel1 = new TabMenuModel("多选", bbb, 2, selectData_b);
        tabMenuModel1.setTabButtonView(new TButton(this));
        tabSelectModels.add(tabMenuModel1);
        TabMenuModel tabMenuModel2 = new TabMenuModel("多列", ccc, 3, selectData_c);
        tabMenuModel2.setTabButtonView(new TButton(this));
        tabMenuModel2.setColumnCount(3);
        tabMenuModel2.setColorContent(Color.GREEN, Color.BLUE);
        tabSelectModels.add(tabMenuModel2);

        //添加自定义布局
        TabMenuModel tabMenuModel3 = new TabMenuModel("自定义", R.layout.layout_date_picker2, root -> {
            LoopView2 loopView2;
            loopView2 = root.findViewById(R.id.loop_view2);
            loopView2.setLoopListener(item -> QdToast.showToast(mContext, "item=" + item));
            loopView2.setTextSize(16);//must be called before setDateList
            loopView2.setDataList(getList(20));
            loopView2.setCurrentIndex(10);
        });
        tabMenuModel3.setTabButtonView(new TButton(this));
        tabSelectModels.add(tabMenuModel3);

        //组建初始化
        tabMenuLayout = findViewById(R.id.tab_menu_layout);
        tabMenuLayout.setTabDividerResId(R.layout.activity_layout_driver);
        tabMenuLayout.setData(tabSelectModels, (tabButton, tabIndex, position) -> {
            QdToast.showToast(mContext, "" + tabIndex + ":" + position);
            tabMenuLayout.getPopupWindow().dismiss();
            switch (tabIndex) {
                case 0:
                    tabButton.setTabName(aaa[position]);
                    break;
                case 1:
                    tabButton.setTabName(bbb[position]);
                    break;
                case 2:
                    tabButton.setTabName(ccc[position]);
                    break;
            }
            return null;
        });
    }

    public ArrayList<String> getList(int c) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            list.add("DAY TEST:" + i);
        }
        return list;
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
            view = inflater.inflate(R.layout.item_tab_menu_layout, null);
            tv_tab_name = view.findViewById(R.id.tv_tab_name);
            view.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            this.addView(view);
        }
    }



}
