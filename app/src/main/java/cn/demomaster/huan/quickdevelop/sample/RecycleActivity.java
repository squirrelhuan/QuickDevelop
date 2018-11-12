package cn.demomaster.huan.quickdevelop.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.RecycleViewAdapter;

public class RecycleActivity extends AppCompatActivity {

    private RecyclerView recy_drag;
    private LinearLayoutManager linearLayoutManager;
    private List<String> lists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        recy_drag=findViewById(R.id.recy_drag);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        mItemTouchHelper.attachToRecyclerView(recy_drag);

        //模拟一些数据加载
        lists = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            lists.add(i + "item");
        }
        //这里使用线性布局像listview那样展示列表,第二个参数可以改为 HORIZONTAL实现水平展示
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//使用网格布局展示
        recy_drag.setLayoutManager(new GridLayoutManager(this, 5));
        //recy_drag.setLayoutManager(linearLayoutManager);
        final RecycleViewAdapter adapter = new RecycleViewAdapter(this, lists);
        //设置分割线使用的divider
        recy_drag.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(this, android.support.v7.widget.DividerItemDecoration.VERTICAL));
        recy_drag.setAdapter(adapter);

    }
}
