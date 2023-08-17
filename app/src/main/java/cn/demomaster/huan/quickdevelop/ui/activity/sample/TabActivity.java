package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.ui.main.SectionsPagerAdapter;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuLayout;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;

@ActivityPager(name = "Tab页面", preViewClass = TextView.class, resType = ResType.Custome)
public class TabActivity extends QuickActivity {

    //private ActivityMainTabBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = ActivityMainTabBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main_tab);
        getActionBarTool().setTitle(getTitle());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);//binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs =findViewById(R.id.tabs);// binding.tabs;
        tabs.setupWithViewPager(viewPager);
        ScrollableTabView tabs2 =findViewById(R.id.tabs2);// binding.tabs;
        tabs2.setupWithViewPager(viewPager);
//        FloatingActionButton fab = binding.fab;
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}