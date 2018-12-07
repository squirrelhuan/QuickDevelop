package cn.demomaster.huan.quickdevelop.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuLayout;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuModel;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabRadioGroup;

public class TabMenuActivity extends BaseActivityParent {

    TabMenuLayout tabMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_menu);


        /*********************      数据初始化                   ************************/
        String[] aaa=new String[30];
        for (int i = 0; i < aaa.length; i++) {
            aaa[i]="aaa" + i;
        }
        String[] bbb=new String[10];
        for (int i = 0; i < bbb.length; i++) {
            bbb[i]="bbb"+i;
        }
        String[] ccc=new String[120];
        for (int i = 0; i < ccc.length; i++) {
           ccc[i]="ccc" + i;
        }
        List<Integer> selectData_a = new ArrayList<>();selectData_a.add(1);
        List<Integer>  selectData_b =  new ArrayList<>();selectData_b.add(2);selectData_b.add(3);
        List<Integer>  selectData_c =  new ArrayList<>();selectData_c.add(4);selectData_c.add(7);
        final List<TabMenuModel> tabSelectModels = new ArrayList<>();//用来存放初始化选项的状态
        TabMenuModel tabMenuModel =new TabMenuModel("first",aaa, selectData_a);
        tabMenuModel.setTabButtonView(new TButton(this));
        tabSelectModels.add(tabMenuModel);
        TabMenuModel tabMenuModel1 =new TabMenuModel("secend",bbb,2, selectData_b);
        tabMenuModel1.setTabButtonView(new TButton(this));
        tabSelectModels.add(tabMenuModel1);
        TabMenuModel tabMenuModel2 =new TabMenuModel("c",ccc,3, selectData_c);
        tabMenuModel2.setTabButtonView(new TButton(this));
        tabMenuModel2.setColumnCount(3);
        tabSelectModels.add(tabMenuModel2);

        //添加自定义布局
        TabMenuModel tabMenuModel3 =new TabMenuModel("自定义", R.layout.layout_date_picker, new TabMenuModel.OnCreatTabContentView() {
            @Override
            public void onCreat(View root) {

            }
        });
        tabMenuModel3.setTabButtonView(new TButton(this));
        tabSelectModels.add(tabMenuModel3);

        /*********************      组建初始化                   ************************/
        tabMenuLayout = findViewById(R.id.tab_menu_layout);
        tabMenuLayout.setData(tabSelectModels,new TabMenuLayout.TabMenuInterface() {
            @Override
            public String onSelected(int tabIndex, int position) {
                PopToastUtil.ShowToast((Activity) mContext, "" + tabIndex + ":" + position);
                return null;
            }

        });
    }

    public class TButton extends TabRadioGroup.TabRadioButton{
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
            if(state){
                tv_tab_name.setTextColor(Color.YELLOW);
            }else {
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
            view = inflater.inflate(R.layout.item_tab_menu_layout,null);
            tv_tab_name =(TextView)view.findViewById(R.id.tv_tab_name);
            view.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            this.addView(view);
        }
    }


}
