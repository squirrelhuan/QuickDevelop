package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdlogger_library.QDLogger;


/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class QuickPicker extends QuickPickerPopWindow implements View.OnClickListener {

    //private LoopView picker_common;
    private View pickerContainerV;
    public QuickPicker(Builder builder) {
        super(builder);
        this.mListener = builder.listener;
        this.initView();
    }

    private void initView() {
        //this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_common_picker, (ViewGroup) null);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.contentView.setOnClickListener(this);
        if(dataAdapter!=null){
            dataAdapter.init(QuickPicker.this, (ViewGroup) contentView);
        }
    }

    public interface DataAdapterInterface{
        //控件数量
        int getCount();
        View getView(Context context, ViewGroup parent,int position);
        List<String> getData(Context context,int position);
    }

    DataAdapter dataAdapter;
    public void setDataAdapter(DataAdapter dataAdapter) {
        this.dataAdapter = dataAdapter;
        initView();
    }

    public static abstract class DataAdapter implements DataAdapterInterface{
        public Context context;
        public View.OnClickListener onClickListener;
        public QuickPickerPopWindow.OnPickListener onPickListener;
        public DataAdapter(Context context){
            this.context = context;
        }

        public int[] selectArr = new int[]{};
        //返回第n个控件的当前选项
        public int getSelectIndex(int position){
            if(selectArr.length>position){
                return selectArr[position];
            }
            return 0;
        }
        //获取当前 所选内容
        public Object[] getSelectObjs(int[] selectArr) {
            Object[] objectList = new Object[selectArr.length];
            for(int i=0;i<selectArr.length;i++){
                Object obj = getData(context,i).get(selectArr[i]);
                objectList[i]=(obj);
            }
            return objectList;
        }
        public void bindView(QuickPicker quickPicker,Context context,ViewGroup parent,int position){
            View view = getView(context,parent,position);
            List<String> data = getData(context,position);
            /*int selectIndex = getSelectIndex(position);
            selectArr[position] = selectIndex;*/
            if(view instanceof LoopView){
                ((LoopView)view).setDataList(data);
                ((LoopView)view).setLoopListener(new LoopScrollListener() {
                    @Override
                    public void onItemSelect(int var1) {
                        selectArr[position] = var1;
                        QDLogger.e("selectArr["+position+"]="+var1);
                        Object[] objectList = getSelectObjs(selectArr);
                        onSelectChanged(quickPicker,parent,position);
                        if(onPickListener!=null) {
                            onPickListener.onSelect(quickPicker, objectList, selectArr);
                        }
                    }
                });
            }
        }

        public void onSelectChanged(QuickPicker quickPicker, ViewGroup parent, int position){
            int count = getCount();
            QDLogger.println("onSelectChanged"+Arrays.toString(selectArr));
            for(int i=position+1;i<count;i++){
                bindView(quickPicker,context,parent,i);
            }
        }

        public void setOnPickListener(OnPickListener onPickListener) {
            this.onPickListener = onPickListener;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public void init(QuickPicker quickPicker, ViewGroup viewGroup){
            int count = getCount();
            selectArr = new int[count];
            for(int i=0;i<count;i++){
                bindView(quickPicker,context,viewGroup,i);
            }
            initView(quickPicker,viewGroup);
        }

        protected abstract void initView(QuickPicker quickPicker, ViewGroup viewGroup);
    }

    @Override
    public void showWithView(Activity activity) {
        if (null != activity) {
            TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
            this.showAtLocation(activity.getWindow().getDecorView(), 80, 0, 0);
            trans.setDuration(200L);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());
            pickerContainerV.startAnimation(trans);
        }
    }

    public void onClick(View v) {
        if (v != this.contentView) {
           /* if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    //this.mListener.onPickCompleted(dataList, dataList.get(this.currentPositon), this.currentPositon);
                    //this.mListener.onSelect(this,new Object[]{dataList, dataList.get(this.currentPositon), this.currentPositon},new int[]{});
                }
                this.dismissWithView(pickerContainerV);
            }*/
        } else {
            this.dismissWithView(pickerContainerV);
        }
    }

    public static class Builder extends QuickPickerPopWindow.Builder{
        public Builder(Context context) {
            super(context,LayoutInflater.from(context).inflate(R.layout.quick_layout_common_picker, null));
        }

        public QuickPicker build() {
            return new QuickPicker(this);
        }
    }
}
