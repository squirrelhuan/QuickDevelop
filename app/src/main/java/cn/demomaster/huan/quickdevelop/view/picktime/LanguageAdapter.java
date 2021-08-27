package cn.demomaster.huan.quickdevelop.view.picktime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.demomaster.huan.quickdevelop.R;

/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder {
        public CheckBox cb_checkBox;

        public VH(View v) {
            super(v);
            cb_checkBox = v.findViewById(R.id.cb_checkBox);
        }
    }

    private Context context;
    private List<LanguageModel> mDatas;
    private OnItemClicked onItemClicked;

    public LanguageAdapter(Context context, List<LanguageModel> data, OnItemClicked onItemClicked) {
        this.context = context;
        this.mDatas = data;
        this.onItemClicked = onItemClicked;
    }

    //③ 在Adapter中实现3个方法
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            VH holder1 = (VH) holder;
            holder1.cb_checkBox.setText(mDatas.get(position).getName());
            if(mDatas.get(position).getName().equals("中文")){
                holder1.cb_checkBox.setChecked(true);
                holder1.cb_checkBox.setClickable(false);
            }else {
                holder1.cb_checkBox.setClickable(true);
                if (mDatas.get(position).isSelected()) {
                    holder1.cb_checkBox.setChecked(true);
                } else {
                    holder1.cb_checkBox.setChecked(false);
                }
            }
        /*    holder1.itemView.setTag(position);
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //item 点击事件
                    LanguageModel languageModel = mDatas.get((int) v.getTag());
                    languageModel.setSelected(!languageModel.isSelected);
                    onItemClicked.onItemClicked(v, (int) v.getTag(), languageModel);
                }
            });*/
        holder1.cb_checkBox.setTag(position);
        holder1.cb_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //item 点击事件
                LanguageModel languageModel = mDatas.get((int) buttonView.getTag());
                languageModel.setSelected(isChecked);
                onItemClicked.onItemClicked(buttonView, (int) buttonView.getTag(), languageModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDatas.size() ) {
            return -1;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //LayoutInflater.from指定写法
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
            return new VH(v);
    }

    public static interface OnItemClicked {
        void onItemClicked(View view, int position, LanguageModel languageModel);
    }

    public static class LanguageModel{
        private String code;
        private String name;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
