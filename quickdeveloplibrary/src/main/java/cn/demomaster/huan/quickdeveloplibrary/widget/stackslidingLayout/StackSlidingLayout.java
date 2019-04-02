package cn.demomaster.huan.quickdeveloplibrary.widget.stackslidingLayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

@CoordinatorLayout.DefaultBehavior(StackSlidingBehavior.class)
public class StackSlidingLayout extends FrameLayout {
    public StackSlidingLayout(@NonNull Context context) {
        super(context);
        init();

    }

    public StackSlidingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StackSlidingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.stack_sliding_item,this);
        RecyclerView list = (RecyclerView)findViewById(R.id.list);
        TextView header = findViewById(R.id.header);
        int colors[] ={Color.RED,Color.GREEN,Color.YELLOW};
        header.setBackgroundColor(colors[(int) (Math.random()*10%3)]);
        header.setText(Math.random()*10+"");
        QDLogger.d("StackSlidingLayout",header.getText());
        ComponentAdapter adapter = new ComponentAdapter(getContext());
        List<String> items = new ArrayList();
        int c = (int) (Math.random()*10)*3;
        for(int i=0;i<c;i++){
            items.add("A"+i);
        }
        adapter.updateList(items);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        setBackgroundColor(Color.TRANSPARENT);
    }

    private int mHeaderViewHeight;
    private int mRecycleViewHeight;

    private int mViewHeight;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        QDLogger.d("StackSlidingLayout","onSizeChanged w="+w+",h="+h+",oldw="+oldw+ ",oldh="+oldh);
        if(w!=oldw||h!=oldh){
            mHeaderViewHeight = findViewById(R.id.header).getMeasuredHeight();
            mRecycleViewHeight = findViewById(R.id.list).getMeasuredHeight();
            mViewHeight = mHeaderViewHeight+mRecycleViewHeight;
        }
        QDLogger.d("StackSlidingLayout","mHeaderViewHeight="+mHeaderViewHeight);
       // super.onSizeChanged(w, h, oldw, oldh);
    }

    public int getHeaderViewHeight() {
        return mHeaderViewHeight;
    }

    public int getViewHeight() {
        return mViewHeight;
    }
}
