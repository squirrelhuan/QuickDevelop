package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.StringUtil;

/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class TimePickerPopWin extends PickerPopWindow implements View.OnClickListener {
    private Button cancelBtn;
    private Button confirmBtn;
    public TextView tv_sign_year;
    public TextView tv_sign_month;
    public TextView tv_sign_day;
    private LoopView hourLoopView;
    private LoopView minuteLoopView;
    private LoopView meridianLoopView;
    private View pickerContainerV;
    private int hourPos = 0;
    private int minutePos = 0;
    private int meridianPos = 0;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;
    private int colorSignText;

    private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
    private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
    private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
    List<String> hourList = new ArrayList();
    List<String> minList = new ArrayList();
    List<String> meridianList = new ArrayList();

    public TimePickerPopWin(TimePickerPopWin.Builder builder) {
        super(builder);
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.topBottomTextColor = builder.topBottomTextColor;
        this.centerTextColor = builder.centerTextColor;
        this.centerLineColor = builder.centerLineColor;
        this.btnTextsize = builder.btnTextSize;
        this.initView();
    }

    private void initView() {
        //this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_time_picker, null);
        this.cancelBtn = this.contentView.findViewById(R.id.btn_cancel);
        this.cancelBtn.setTextColor(this.colorCancel);
        this.cancelBtn.setTextSize((float) this.btnTextsize);
        this.confirmBtn = this.contentView.findViewById(R.id.btn_confirm);
        this.confirmBtn.setTextColor(this.colorConfirm);
        this.confirmBtn.setTextSize((float) this.btnTextsize);
        /*this.tv_sign_year = this.contentView.findViewById(R.id.tv_sign_year);
        this.tv_sign_year.setTextColor(this.colorSignText);

        this.tv_sign_month = this.contentView.findViewById(R.id.tv_sign_month);
        this.tv_sign_month.setTextColor(this.colorSignText);

        this.tv_sign_day = this.contentView.findViewById(R.id.tv_sign_day);
        this.tv_sign_day.setTextColor(this.colorSignText);*/
        this.hourLoopView = this.contentView.findViewById(R.id.picker_hour);
        this.minuteLoopView = this.contentView.findViewById(R.id.picker_minute);
        this.meridianLoopView = this.contentView.findViewById(R.id.picker_meridian);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.hourLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.hourLoopView.setLoopListener(item -> TimePickerPopWin.this.hourPos = item);
        this.minuteLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.minuteLoopView.setLoopListener(item -> TimePickerPopWin.this.minutePos = item);
        this.meridianLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.meridianLoopView.setLoopListener(item -> TimePickerPopWin.this.meridianPos = item);
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
    }

    private void initPickerViews() {
        this.hourPos = Calendar.getInstance().get(10) - 1;
        this.minutePos = Calendar.getInstance().get(12);
        this.meridianPos = Calendar.getInstance().get(9);

        int j;
        for (j = 1; j <= 12; ++j) {
            this.hourList.add(StringUtil.formatNumberToStr(j, 2));
        }

        for (j = 0; j < 60; ++j) {
            this.minList.add(StringUtil.formatNumberToStr(j, 2));
        }

        this.meridianList.add("AM");
        this.meridianList.add("PM");
        this.hourLoopView.setDataList(this.hourList);
        this.hourLoopView.setInitPosition(this.hourPos);
        this.minuteLoopView.setDataList(this.minList);
        this.minuteLoopView.setInitPosition(this.minutePos);
        this.meridianLoopView.setDataList(this.meridianList);
        this.meridianLoopView.setInitPosition(this.meridianPos);
    }

    public void onClick(View v) {
        if (v != this.contentView && v != this.cancelBtn) {
            if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    // this.mListener.onDateSelect(0, 0, 0, this.hourPos + 1 + (this.meridianPos == 0 ? 0 : 12), this.minutePos, 0);
                    this.mListener.onSelect(this, new Object[]{0, 0, 0, this.hourPos + 1 + (this.meridianPos == 0 ? 0 : 12), this.minutePos, 0}, new int[]{});
                }
                this.dismissWithView(pickerContainerV);
            }
        } else {
            this.dismissWithView(pickerContainerV);
        }
    }

    public static class Builder extends PickerPopWindow.Builder {
        private String textCancel;
        private String textConfirm;
        private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
        private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
        private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
        private int colorCancel;
        private int colorConfirm;
        private int btnTextSize = 16;

        public Builder(Context context, OnPickListener listener) {
            super(context,LayoutInflater.from(context).inflate(R.layout.layout_time_picker, null), listener);
            textCancel = context.getResources().getString(R.string.Cancel);
            textConfirm = context.getResources().getString(R.string.Confirm);
            colorCancel = context.getResources().getColor(R.color.colorAccent);//Color.parseColor("#999999");
            colorConfirm = context.getResources().getColor(R.color.colorAccent);//Color.parseColor("#303F9F");
            contentView = LayoutInflater.from(context).inflate(R.layout.layout_time_picker, null);
        }

        public TimePickerPopWin.Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public TimePickerPopWin.Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public TimePickerPopWin.Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public TimePickerPopWin.Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        public TimePickerPopWin.Builder colorContentText(int topBottomTextColor, int centerTextColor, int centerLineColor) {
            this.topBottomTextColor = topBottomTextColor;
            this.centerTextColor = centerTextColor;
            this.centerLineColor = centerLineColor;
            return this;
        }

        public TimePickerPopWin.Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public TimePickerPopWin build() {
            return new TimePickerPopWin(this);
        }
    }
}
