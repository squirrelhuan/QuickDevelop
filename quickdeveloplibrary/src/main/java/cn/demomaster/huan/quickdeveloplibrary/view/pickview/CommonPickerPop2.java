package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;


/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class CommonPickerPop2 extends PickerPopWindow implements View.OnClickListener {
    private Button cancelBtn;
    private Button confirmBtn;
    private String def_value = null;
    private LoopView picker_common;
    private View pickerContainerV;
    private int currentPositon = 0;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;
    private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
    private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
    private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
    ArrayList dataList;
    private OnPickListener mListener;

    public CommonPickerPop2(Builder builder) {
        super(builder);
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        Context mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.topBottomTextColor = builder.topBottomTextColor;
        this.centerTextColor = builder.centerTextColor;
        this.centerLineColor = builder.centerLineColor;
        this.btnTextsize = builder.btnTextSize;
        int viewTextSize = builder.viewTextSize;
        this.dataList = builder.date;
        this.def_value = builder.def_value;
        this.initView();
    }

    private void initView() {
        //this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_common_picker, (ViewGroup) null);
        this.cancelBtn = this.contentView.findViewById(R.id.btn_cancel);
        this.cancelBtn.setTextColor(this.colorCancel);
        this.cancelBtn.setTextSize((float) this.btnTextsize);
        this.confirmBtn = this.contentView.findViewById(R.id.btn_confirm);
        this.confirmBtn.setTextColor(this.colorConfirm);
        this.confirmBtn.setTextSize((float) this.btnTextsize);

        this.picker_common = this.contentView.findViewById(R.id.picker_common);
        this.picker_common.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.picker_common.setLoopListener(item -> CommonPickerPop2.this.currentPositon = item);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);

        this.initPickerViews();
        this.cancelBtn.setOnClickListener(this);
        this.confirmBtn.setOnClickListener(this);
        this.contentView.setOnClickListener(this);
        if (!TextUtils.isEmpty(this.textConfirm)) {
            this.confirmBtn.setText(this.textConfirm);
        }

        if (!TextUtils.isEmpty(this.textCancel)) {
            this.cancelBtn.setText(this.textCancel);
        }

        this.setTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.FadeInPopWin);
        this.setContentView(this.contentView);
        this.setWidth(-1);
        this.setHeight(-1);
    }

    private void initPickerViews() {
        if (TextUtils.isEmpty(this.def_value)) {
            this.currentPositon = 0;
        } else {
            this.currentPositon = getDefIndex(this.def_value, dataList);
        }
        this.picker_common.setDataList(this.dataList);
        this.picker_common.setInitPosition(this.currentPositon);
    }

    private int getDefIndex(String def_provience, List<String> provinceList) {
        for (int i = 0; i < provinceList.size(); i++) {
            if (provinceList.get(i).equals(def_provience)) {
                return i;
            }
        }
        return 0;
    }

    public void onClick(View v) {
        if (v != this.contentView && v != this.cancelBtn) {
            if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    //this.mListener.onPickCompleted(dataList, dataList.get(this.currentPositon), this.currentPositon);
                    this.mListener.onSelect(this,new Object[]{dataList, dataList.get(this.currentPositon), this.currentPositon},new int[]{});
                }
                this.dismissWithView(pickerContainerV);
            }
        } else {
            this.dismissWithView(pickerContainerV);
        }
    }

    public static class Builder extends PickerPopWindow.Builder{
        private OnPickListener listener;
        private String textCancel;
        private String textConfirm;
        private String def_value = null;
        private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
        private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
        private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
        private int colorCancel;
        private int colorConfirm;
        private int btnTextSize = 16;
        private int viewTextSize = 25;
        private ArrayList date;

        public Builder(Context context, OnPickListener listener) {
            super(context,LayoutInflater.from(context).inflate(R.layout.layout_common_picker, null),listener);
            this.listener = listener;
            textCancel = context.getResources().getString(R.string.Cancel);
            textConfirm = context.getResources().getString(R.string.Confirm);
            colorCancel = context.getResources().getColor(R.color.colorAccent);//Color.parseColor("#999999");
            colorConfirm = context.getResources().getColor(R.color.colorAccent);//Color.parseColor("#303F9F");
            contentView = LayoutInflater.from(context).inflate(R.layout.layout_common_picker,null);
        }

        public Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        public Builder colorContentText(int topBottomTextColor, int centerTextColor, int centerLineColor) {
            this.topBottomTextColor = topBottomTextColor;
            this.centerTextColor = centerTextColor;
            this.centerLineColor = centerLineColor;
            return this;
        }

        public Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public Builder setDate(ArrayList date) {
            this.date = date;
            return this;
        }

        public CommonPickerPop2 build() {
            return new CommonPickerPop2(this);
        }

        public Builder setDefaultValue(String def_provience) {
            this.def_value = def_provience;
            return this;
        }
    }
}
