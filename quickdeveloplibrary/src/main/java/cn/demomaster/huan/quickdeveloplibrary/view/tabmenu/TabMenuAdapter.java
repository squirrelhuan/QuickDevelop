package cn.demomaster.huan.quickdeveloplibrary.view.tabmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdlogger_library.QDLogger;


/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class TabMenuAdapter extends RecyclerView.Adapter<TabMenuAdapter.ViewHolder> {

    private List<TabListViewItem> tabListViewItems = new ArrayList();
    private List<TabMenuModel> tabMenuModels;
    private int tabIndex;
    private int selectCount = 1;
    private Context context;
    private LayoutInflater inflater;
    private boolean isSingle;//单个tab中的状态集合（多选则为list,单选则为int）
    private int color_selected = Color.RED;
    private int color_normal = Color.BLACK;

    public void setColors(int color_selected, int color_normal) {
        this.color_selected = color_selected;
        this.color_normal = color_normal;
    }

    public TabMenuAdapter(Context context, List<TabMenuModel> tabMenuModels, int tabIndex) {
        this.context = context;
        this.tabIndex = tabIndex;
        this.tabMenuModels = tabMenuModels;
        TabMenuModel tabMenuModel = tabMenuModels.get(tabIndex);
        //判断单选还是多选
        selectCount = tabMenuModel.getSelectCount();
        isSingle = (selectCount == 1);

        //遍历循环初始化viewData
        tabListViewItems.clear();
        for (int i = 0; i < tabMenuModel.getTabItems().length; i++) {
            TabListViewItem item = new TabListViewItem();
            item.setPosition(i);
            item.setItemName(tabMenuModel.getTabItems()[i]);
            if (selectCount < tabMenuModel.getSelectDeftData().size()) {
                QDLogger.e("默认选中个数不能超过最大个数");
                return;
            }
            if (tabMenuModel.getSelectDeftData() != null) {
                for (int j = 0; j < tabMenuModel.getSelectDeftData().size(); j++) {
                    if (tabMenuModel.getSelectDeftData().get(j) != null && item.getPosition() == tabMenuModel.getSelectDeftData().get(j)) {
                        item.setSelected(true);
                    }
                }
            }
            tabListViewItems.add(item);
        }
        this.inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (tabListViewItems.get(position).isSelected()) {
            holder.tv_title.setTextColor(color_selected);
        } else {
            holder.tv_title.setTextColor(color_normal);
        }
        holder.tv_title.setText(((TabListViewItem) this.tabListViewItems.get(position)).getItemName());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int p = (int) v.getTag();
                    onItemClickListener.onItemClick(v, p);
                }
                QDLogger.e("这里是点击每一行item的响应事件", "" + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tabListViewItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);

        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public List<TabMenuModel> getTabMenuModels() {
        return tabMenuModels;
    }

    public void setOnItemClicked(int position) {
        boolean b = !tabListViewItems.get(position).isSelected();
        TabMenuModel tabMenuModel = tabMenuModels.get(tabIndex);
        List<Integer> current = tabMenuModel.getSelectDeftData();
        if (current.contains(position)) {//如果存在则remove
            current.remove((Object) position);
        } else {//如果不存在
            if (current.size() + 1 > tabMenuModel.getSelectCount()) {//判断个数是否超出，没超出则追加，超出则remove第一个
                tabListViewItems.get(current.get(0)).setSelected(false);
                current.remove(0);
                current.add(position);
            } else {
                current.add(position);
            }
        }
        tabMenuModel.setSelectDeftData(current);
        tabMenuModels.set(tabIndex, tabMenuModel);
        tabListViewItems.get(position).setSelected(b);
        notifyDataSetChanged();
    }
}