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

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdlogger_library.QDLogger;


/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class CommonPickerPop extends PickerPopWindow implements View.OnClickListener {
    private Button cancelBtn;
    private Button confirmBtn;
    //private LoopView picker_common;
    private View pickerContainerV;
    //private int currentPositon = 0;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;
    private String viewFormat;
    private String[] viewFormatTag;
    private PickData pickData;
    
    public CommonPickerPop(Builder builder) {
        super(builder);
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        //array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
        int topBottomTextColor = builder.topBottomTextColor;
        //array.getColor(styleable.LoopView_centerTextColor, -13553359);
        int centerTextColor = builder.centerTextColor;
        //array.getColor(styleable.LoopView_lineColor, -3815995);
        int centerLineColor = builder.centerLineColor;
        this.btnTextsize = builder.btnTextSize;
        int viewTextSize = builder.viewTextSize;
        String def_value = builder.def_value;
        this.viewFormat = builder.viewFormat;
        this.viewFormatTag = builder.viewFormatTag;
        this.pickData = builder.pickData;
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

        /*this.picker_common = this.contentView.findViewById(R.id.picker_common);
        this.picker_common.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.picker_common.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                CommonPickerPop.this.currentPositon = item;
            }
        });*/
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.cancelBtn.setOnClickListener(this);
        this.confirmBtn.setOnClickListener(this);
        this.contentView.setOnClickListener(this);
        this.confirmBtn.setText(this.textConfirm);
        this.cancelBtn.setText(this.textCancel);

        ViewGroup ll_view_container = contentView.findViewById(R.id.ll_view_container);
        QDLogger.d("viewFormat="+viewFormat);
        if(!TextUtils.isEmpty(viewFormat)) {
            ll_view_container.removeAllViews();
            String[] strs = viewFormat.split("-");
            int i=0;
            for(String str:strs) {
                if(str.equals("v")) {
                    LoopView loopView = new LoopView(context);
                    loopView.setTag(viewFormatTag[i]);
                    List<PickData.PickDataItem> pickDataItemList = pickData.getDataByTag(viewFormatTag[i]);
                    if(pickDataItemList!=null) {
                        List<String> data = new ArrayList<>();
                        for (PickData.PickDataItem pickDataItem : pickDataItemList) {
                            data.add(pickDataItem.getTitle());
                        }
                        loopView.setDataList(data);
                    }
                    loopView.setTextSize(DisplayUtil.dip2px(context,16));
                    loopView.setCanLoop(false);
                    ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    ll_view_container.addView(loopView, layoutParams);
                }else if(str.equals("t")) {
                    TextView textView = new TextView(context);
                    textView.setText(viewFormatTag[i]);
                    //textView.setTag(viewFormatTag[i]);
                    textView.setTextColor(Color.BLACK);
                    ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ll_view_container.addView(textView,layoutParams);
                }
                i++;
            }
        }
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
        if (v != this.contentView && v != this.cancelBtn) {
            if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    //this.mListener.onPickCompleted(dataList, dataList.get(this.currentPositon), this.currentPositon);
                    //this.mListener.onSelect(this,new Object[]{dataList, dataList.get(this.currentPositon), this.currentPositon},new int[]{});
                }
                this.dismissWithView(pickerContainerV);
            }
        } else {
            this.dismissWithView(pickerContainerV);
        }
    }

    public static class Builder extends PickerPopWindow.Builder{
        public PickData pickData;
        private OnPickListener listener;
        private String viewFormat = "v-t-v-t-v-t";
        private String[] viewFormatTag = new String[]{"YY","text1","MM","text2","DD","text3"};//"yyyy年MM月dd日 HH:mm:ss"
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

        public Builder(Context context, OnPickListener listener) {
            super(context,LayoutInflater.from(context).inflate(R.layout.layout_common_picker, null),listener);
            this.listener = listener;
            textCancel = context.getResources().getString(cn.demomaster.huan.quickdeveloplibrary.R.string.Cancel);
            textConfirm = context.getResources().getString(cn.demomaster.huan.quickdeveloplibrary.R.string.Confirm);
            colorCancel = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#999999");
            colorConfirm = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#303F9F");
            int colorText = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#f60");

            this.pickData = new PickData() {
                @Override
                public List<PickDataItem> getDataByTag(String tag, int[] selectIndexs) {
                    if(tag=="YY"){
                        List<PickDataItem> pickDataItemList = new ArrayList<>();
                        for(int i=0;i<10;i++){
                            PickData.PickDataItem pickDataItem = new PickDataItem(""+i);
                            pickDataItemList.add(pickDataItem);
                        }
                        return pickDataItemList;
                    }
                    return null;
                }

                @Override
                public String[] getTags() {
                    return new String[]{"YY","MM","DD"};
                }

                @Override
                public int[] getDefautIndex() {
                    return new int[]{0,0,0};
                }
            };/*
            String str = QDFileUtil.getFromAssets(mContext, "area_shanghai.conf");
            areaModelList = new Gson().fromJson(str, new TypeToken<List<PickModel>>() {
            }.getType());*/
        }

        /**
         * 将视图和数据绑定
         * @param viewFormat
         * @param viewFormatTag
         * @return
         */
        public Builder setViewFormat(String viewFormat,String[] viewFormatTag) {
            this.viewFormat = viewFormat;
            this.viewFormatTag = viewFormatTag;
            return this;
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

        public CommonPickerPop build() {
            return new CommonPickerPop(this);
        }

        public Builder setDefaultValue(String def_provience) {
            this.def_value = def_provience;
            return this;
        }
    }
}
