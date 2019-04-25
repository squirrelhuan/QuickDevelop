package cn.demomaster.huan.quickdeveloplibrary.widget.stackslidingLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

@CoordinatorLayout.DefaultBehavior(MultiRecycleBehavior.class)
public class MultiRecycleContainer extends FrameLayout {
    private RecyclerView recyclerView;

  /*  public RecyclerView getRecyclerView() {
        return recyclerView;
    }*/

    public MultiRecycleContainer(@NonNull Context context) {
        super(context);
        init(null);
    }

    public MultiRecycleContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiRecycleContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        return super.addViewInLayout(child, index, params);
    }

    private void init(AttributeSet attrs) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onAddView(MultiRecycleContainer.this);
            }
        });
        /*LayoutInflater.from(getContext()).inflate(R.layout.stack_sliding_item,this);
        recyclerView  = findViewById(R.id.list);
        TextView header = findViewById(R.id.header);
        QDLogger.d("StackSlidingLayout",header.getText());
        ComponentAdapter adapter = new ComponentAdapter(getContext());
        List<String> items = new ArrayList();
        int c = (int) (Math.random()*10)*6;
        for(int i=0;i<c;i++){
            items.add("A"+i);
        }
        adapter.updateList(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setBackgroundColor(Color.TRANSPARENT);
        TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.StackSlidingLayout);
        //header.setBackgroundColor(a.getColor(R.styleable.StackSlidingLayout_android_colorBackground,Color.BLACK));
        findViewById(R.id.ll_header).setBackgroundColor(a.getColor(R.styleable.StackSlidingLayout_android_colorBackground,Color.BLACK));
        //header.setText(a.getText(R.styleable.StackSlidingLayout_android_text));
        a.recycle();*/
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        onAddView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        onAddView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        onAddView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        onAddView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        onAddView(child);
    }

    private void onAddView(View child) {
        if (child instanceof ViewGroup &&!(child instanceof RecyclerView)) {
            for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
                View v = ((ViewGroup) child).getChildAt(i);
                onAddView(v);
            }
        } else if (child instanceof RecyclerView) {
            recyclerView = (RecyclerView) child;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        QDLogger.d("onTouchEvent="+event);
        if (recyclerView != null) {
            QDLogger.d("recyclerView != null");
            recyclerView.dispatchTouchEvent(event);
        }else {
            QDLogger.d("recyclerView != null null null null null");
        }
        return true;
        //return super.onTouchEvent(event);
    }

    private int mHeaderViewHeight;
    private int mRecycleViewHeight;
    private int mViewHeight;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        onAddView(this);
        if(recyclerView==null)return;
        QDLogger.d("StackSlidingLayout", "onSizeChanged w=" + w + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh);
        if (w != oldw || h != oldh) {
           // mHeaderViewHeight = findViewById(R.id.header).getMeasuredHeight();
            mRecycleViewHeight = recyclerView.getMeasuredHeight();
            mViewHeight = mHeaderViewHeight + mRecycleViewHeight;
        }
        QDLogger.d("StackSlidingLayout", "mHeaderViewHeight=" + mHeaderViewHeight);
    }

    public int getHeaderViewHeight() {
        return mHeaderViewHeight;
    }

    public int getViewHeight() {
        return mViewHeight;
    }
}
