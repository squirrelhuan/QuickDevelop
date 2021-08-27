package cn.demomaster.huan.quickdevelop.view.picktime;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.view.picktime.data.AreaModel;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopScrollListener;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView;

/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class AreaPickerPopWin extends PopupWindow implements View.OnClickListener {
    private Button cancelBtn;
    private Button confirmBtn;
    public TextView tv_sign_year;
    public TextView tv_sign_month;
    public TextView tv_sign_day;
    private String def_provience = null;
    private String def_city = null;
    private LoopView provinceLoopView;
    private LoopView cityLoopView;
    // private LoopView meridianLoopView;
    private View pickerContainerV;
    private View contentView;
    private int cityPos = 0;
    private int provincePos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private String text_sign_year;
    private String text_sign_month;
    private String text_sign_day;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;
    private int colorSignText;

    private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
    private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
    private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
    private int viewTextSize;
    List<String> provinceList = new ArrayList();
    List<String> cityList = new ArrayList();
    private OnAreaPickListener mListener;

    public AreaPickerPopWin(Builder builder) {
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.topBottomTextColor = builder.topBottomTextColor;
        this.centerTextColor = builder.centerTextColor;
        this.centerLineColor = builder.centerLineColor;
        this.text_sign_year = builder.text_sign_year;
        this.text_sign_month = builder.text_sign_month;
        this.text_sign_day = builder.text_sign_day;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;

        this.def_provience = builder.def_provience;
        this.def_city = builder.def_city;
        init();
        this.initView();
    }

    private WindowManager.LayoutParams mWindowParams;

    private void init() {
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.alpha = 1.0f;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        //mWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //窗口类型
        if (Build.VERSION.SDK_INT > 25) {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        mWindowParams.setTitle("ToastHelper");
        mWindowParams.packageName = mContext.getPackageName();
        setClippingEnabled(false);
        //mWindowParams.windowAnimations = animStyleId;// TODO
        //mWindowParams.y = mContext.getResources().getDisplayMetrics().widthPixels / 5;
        //mWindowParams.windowAnimations = R.style.CustomToast;//动画
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_area_picker, (ViewGroup) null);
        this.cancelBtn = (Button) this.contentView.findViewById(R.id.btn_cancel);
        this.cancelBtn.setTextColor(this.colorCancel);
        this.cancelBtn.setTextSize((float) this.btnTextsize);
        this.confirmBtn = (Button) this.contentView.findViewById(R.id.btn_confirm);
        this.confirmBtn.setTextColor(this.colorConfirm);
        this.confirmBtn.setTextSize((float) this.btnTextsize);
        /*this.tv_sign_year = this.contentView.findViewById(R.id.tv_sign_year);
        this.tv_sign_year.setTextColor(this.colorSignText);

        this.tv_sign_month = this.contentView.findViewById(R.id.tv_sign_month);
        this.tv_sign_month.setTextColor(this.colorSignText);

        this.tv_sign_day = this.contentView.findViewById(R.id.tv_sign_day);
        this.tv_sign_day.setTextColor(this.colorSignText);*/

        this.provinceLoopView = (LoopView) this.contentView.findViewById(R.id.picker_province);
        this.provinceLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.provinceLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                AreaPickerPopWin.this.provincePos = item;
            }
        });
        this.cityLoopView = (LoopView) this.contentView.findViewById(R.id.picker_city);
        //this.meridianLoopView = (LoopView) this.contentView.findViewById(R.id.picker_meridian);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.cityLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.cityLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                AreaPickerPopWin.this.cityPos = item;
            }
        });

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
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setAnimationStyle(cn.demomaster.huan.quickdeveloplibrary.R.style.FadeInPopWin);
        this.setContentView(this.contentView);
        this.setWidth(-1);
        this.setHeight(-1);
    }

    List<AreaModel> provinceModels;
    List<AreaModel> cityModels;

    private void initPickerViews() {


        provinceModels = getArea(0);
        cityModels = getArea(1);
        provinceList.clear();
        for (AreaModel areaModel : provinceModels) {
            provinceList.add(areaModel.getAreaName());
        }
        cityList.clear();
        for (AreaModel areaModel : cityModels) {
            cityList.add(areaModel.getAreaName());
        }

        if( TextUtils.isEmpty(this.def_provience)){
            this.provincePos = 0;
        }else {
            this.provincePos = getDefIndex(this.def_provience,provinceList);
        }
        if(TextUtils.isEmpty(this.def_city)){
            this.cityPos = 0;
        }else {
            this.cityPos = getDefIndex(this.def_city,cityList);
        }

        this.provinceLoopView.setDataList(this.provinceList);
        this.provinceLoopView.setInitPosition(this.provincePos);
        this.cityLoopView.setDataList(this.cityList);
        this.cityLoopView.setInitPosition(this.cityPos);
    }

    private int getDefIndex(String def_provience, List<String> provinceList) {
        for(int i=0;i<provinceList.size();i++){
            if(provinceList.get(i).equals(def_provience)){
                return i;
            }
        }
        return 0;
    }

    public void onClick(View v) {
        if (v != this.contentView && v != this.cancelBtn) {
            if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(this.provinceList.get(this.provincePos));
                    sb.append(" ");
                    sb.append(this.cityList.get(this.cityPos));
                    this.mListener.onAreaPickCompleted(provinceModels.get(this.provincePos), cityModels.get(this.cityPos));
                }

                this.dismissPopWin();
            }
        } else {
            this.dismissPopWin();
        }
    }

    public void showPopWin(Activity activity) {
        if (null != activity) {
            TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
            this.showAtLocation(activity.getWindow().getDecorView(), 80, 0, 0);
            trans.setDuration(200L);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());
            this.pickerContainerV.startAnimation(trans);
        }
    }

    public void dismissPopWin() {
        TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
        trans.setDuration(160L);
        trans.setInterpolator(new AccelerateDecelerateInterpolator());//AccelerateInterpolator
        trans.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                AreaPickerPopWin.this.dismiss();
            }
        });
        this.pickerContainerV.startAnimation(trans);
    }


    public interface OnAreaPickListener {
        void onAreaPickCompleted(AreaModel provience, AreaModel city);
    }

    public static class Builder {
        private Context context;
        private OnAreaPickListener listener;
        private String textCancel;
        private String textConfirm;
        private String def_provience = null;
        private String def_city = null;
        private String text_sign_year = "时";
        private String text_sign_month = "分";
        private String text_sign_day = "秒";
        private int colorSignText = Color.BLACK;
        private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
        private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
        private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
        private int colorCancel;
        private int colorConfirm;
        private int colorText;
        private int btnTextSize = 16;
        private int viewTextSize = 25;

        public Builder(Context context, OnAreaPickListener listener) {
            this.context = context;
            this.listener = listener;
            textCancel = context.getResources().getString(cn.demomaster.huan.quickdeveloplibrary.R.string.Cancel);
            textConfirm = context.getResources().getString(cn.demomaster.huan.quickdeveloplibrary.R.string.Confirm);
            colorCancel = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#999999");
            colorConfirm = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#303F9F");
            colorText = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#f60");
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

        public Builder colorSignText(int colorSignText) {
            this.colorSignText = colorSignText;
            return this;
        }

        public Builder colorContentText(int topBottomTextColor, int centerTextColor, int centerLineColor) {
            this.topBottomTextColor = topBottomTextColor;
            this.centerTextColor = centerTextColor;
            this.centerLineColor = centerLineColor;
            return this;
        }

        public Builder setSignText(String year, String month, String day) {
            this.text_sign_year = year;
            this.text_sign_month = month;
            this.text_sign_day = day;
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

        public AreaPickerPopWin build() {
            return new AreaPickerPopWin(this);
        }

        public Builder setDefaultPosition(String def_provience, String def_city) {
            this.def_provience = def_provience;
            this.def_city = def_city;
            return this;
        }
    }


    private List<AreaModel> getArea(int type) {
        if (type == 0) {
            List<AreaModel> areaModelList = new ArrayList<>();
            AreaModel areaModel = new AreaModel();
            areaModel.setAreaName("上海市");
            areaModel.setAreaCode("1234");
            areaModelList.add(areaModel);
            return areaModelList;
        } else {
            List<AreaModel> areaModelList;
            String str = QDFileUtil.getFromAssets(mContext, "area_shanghai.conf");
            areaModelList = new Gson().fromJson(str, new TypeToken<List<AreaModel>>() {
            }.getType());
            return areaModelList;
        }
    }

}
