package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.stackslidingLayout.ComponentAdapter;

/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "StackSliding", preViewClass = TextView.class, resType = ResType.Custome)
public class StackSlidingLayoutFragment extends BaseFragment {

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_stack_sliding, null);
        return mView;
    }

    private RecyclerView recyclerView_A, recyclerView_B, recyclerView_C, recyclerView_D, recyclerView_E;
    private TextView header_A, header_B, header_C, header_D, header_E;
    private ComponentAdapter adapter_A, adapter_B, adapter_C, adapter_D, adapter_E;
    private List<String> items_A, items_B, items_C, items_D, items_E;

    public void initView(View rootView) {
        // actionBarLayout.setActionBarType(ACTIONBAR_TYPE.ACTION_TRANSPARENT);
        // actionBarLayout.setHeaderBackgroundColor(Color.WHITE);

        //A
        recyclerView_A = rootView.findViewById(R.id.list_A);
        header_A = rootView.findViewById(R.id.header_A);
        adapter_A = new ComponentAdapter(getContext(), Color.BLACK);
        items_A = new ArrayList();
        int c = 20;
        for (int i = 0; i < c; i++) {
            items_A.add("A" + i);
        }
        adapter_A.updateList(items_A);
        recyclerView_A.setAdapter(adapter_A);
        recyclerView_A.setLayoutManager(new LinearLayoutManager(getContext()));

        //B
        recyclerView_B = rootView.findViewById(R.id.list_B);
        header_B = rootView.findViewById(R.id.header_B);
        adapter_B = new ComponentAdapter(getContext(), Color.BLACK);
        items_B = new ArrayList();
        c = 6;
        for (int i = 0; i < c; i++) {
            items_B.add("B" + i);
        }
        adapter_B.updateList(items_B);
        recyclerView_B.setAdapter(adapter_B);
        recyclerView_B.setLayoutManager(new LinearLayoutManager(getContext()));

        //C
        recyclerView_C = rootView.findViewById(R.id.list_C);
        header_C = rootView.findViewById(R.id.header_C);
        adapter_C = new ComponentAdapter(getContext(), Color.BLACK);
        items_C = new ArrayList();
        c = 33;
        for (int i = 0; i < c; i++) {
            items_C.add("C" + i);
        }
        adapter_C.updateList(items_C);
        recyclerView_C.setAdapter(adapter_C);
        recyclerView_C.setLayoutManager(new LinearLayoutManager(getContext()));

        //D
        recyclerView_D = rootView.findViewById(R.id.list_D);
        header_D = rootView.findViewById(R.id.header_D);
        adapter_D = new ComponentAdapter(getContext(), Color.BLACK);
        items_D = new ArrayList();
        c = 3;
        for (int i = 0; i < c; i++) {
            items_D.add("D" + i);
        }
        adapter_D.updateList(items_D);
        recyclerView_D.setAdapter(adapter_D);
        recyclerView_D.setLayoutManager(new LinearLayoutManager(getContext()));

        //E
        recyclerView_E = rootView.findViewById(R.id.list_E);
        header_E = rootView.findViewById(R.id.header_E);
        adapter_E = new ComponentAdapter(getContext(), Color.BLACK);
        items_E = new ArrayList();
        c = 16;
        for (int i = 0; i < c; i++) {
            items_E.add("E" + i);
        }
        adapter_E.updateList(items_E);
        recyclerView_E.setAdapter(adapter_E);
        recyclerView_E.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}