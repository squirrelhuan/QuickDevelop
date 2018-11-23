package cn.demomaster.huan.quickdevelop.sample;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuLayout;

public class TabMenuActivity extends BaseActivityParent {

    TabMenuLayout tabMenuLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_menu);

        final String[] tabNames ={"a","b","c"};
        final List<String> aaa = new ArrayList<>();
        for(int i=0;i<10;i++){
        aaa.add("a"+i);
        }
        final List<String> bbb = new ArrayList<>();
        for(int i=0;i<10;i++){
            bbb.add("bb"+i);
        }
        final List<String> ccc = new ArrayList<>();
        for(int i=0;i<10;i++){
            ccc.add("ccc"+i);
        }
        final List<List> arr = new ArrayList();
        arr.add(aaa);
        arr.add(bbb);
        arr.add(ccc);
        tabMenuLayout = findViewById(R.id.tab_menu_layout);
        tabMenuLayout.setTabMenu(new TabMenuLayout.TabMenuInterface() {
            @Override
            public List<String> getItemText(int tabIndex) {
                return (arr.get(tabIndex));
            }

            @Override
            public String onSelected(int tabIndex, int position) {

                PopToastUtil.ShowToast((Activity) mContext,""+tabIndex+":"+tabIndex);
                return null;
            }

            @Override
            public String getTabName(int tabIndex) {
                return tabNames[tabIndex];
            }

            @Override
            public int getTabCount() {
                return tabNames.length;
            }
        });
        tabMenuLayout.init(mContext);
    }
}
