package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.DatePickerListener;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.DatePickerPopWin;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopScrollListener;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView2;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.TimePickerPopWin;

@ActivityPager(name = "多级选择器",preViewClass = TextView.class,resType = ResType.Custome)
public class PickActivity extends BaseActivity {


    //    private ArrayList<ProvinceModel> mProvinceList = null; // 省份列表
//    private String mProvince = null; // 省份
//    private String mCity = null; // 城市
    @BindView(R.id.loop_view)
    public LoopView loopView;
    private LoopView2 loopView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        ButterKnife.bind(this);

        findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(mContext, new DatePickerListener.OnDateSelectListener() {
                    @Override
                    public void onDateSelect(int year, int month, int day, int hour, int minute, int sencond) {
                        Toast.makeText(mContext, year+"-"+month+"-"+day, Toast.LENGTH_SHORT).show();
                    }
                }).textConfirm("确定") //text of confirm button
                        .textCancel("取消") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(35) // pick view text size
                        .colorCancel(Color.parseColor("#11DDAF")) //color of cancel button
                        .colorConfirm(Color.parseColor("#11DDAF"))//color of confirm button
                        .colorSignText(Color.RED)
                        .colorContentText(Color.GRAY,Color.RED,Color.GRAY)
                        .setSignText(getResources().getString(R.string.year),getResources().getString(R.string.month),getResources().getString(R.string.day))
                        .minYear(1900) //min year in loop
                        .maxYear(2030) // max year in loop
                        .dateChose("1980-01-01") // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(mContext);
            }
        });

        findViewById(R.id.timepick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerPopWin timePickerPopWin=new TimePickerPopWin.Builder(mContext, new DatePickerListener.OnDateSelectListener() {
                    @Override
                    public void onDateSelect(int year, int month, int day, int hour, int minute, int sencond) {

                        Toast.makeText(mContext, hour+":"+minute+":"+sencond, Toast.LENGTH_SHORT).show();
                    }
                }).textConfirm("CONFIRM")
                        .textCancel("CANCEL")
                        .btnTextSize(16)
                        .viewTextSize(25)
                        .colorCancel(Color.parseColor("#11DDAF"))
                        .colorConfirm(Color.parseColor("#11DDAF"))
                        .colorSignText(Color.RED)
                        .colorContentText(Color.GRAY,Color.RED,Color.GRAY)
                        .setSignText(getResources().getString(R.string.year),getResources().getString(R.string.month),getResources().getString(R.string.day))
                        .build();
                timePickerPopWin.showPopWin(mContext);

            }
        });


        findViewById(R.id.province).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Working on...", Toast.LENGTH_SHORT).show();
//                if(null != mProvinceList) {
//                    ProvincePickPopWin pickPopWin = new ProvincePickPopWin(mContext,
//                            mProvince, mCity, mProvinceList, mContext);
//                    pickPopWin.showPopWin(mContext);
//                }
            }
        });

       // loopView = (LoopView) findViewById(R.id.loop_view);
        loopView.setInitPosition(2);
        loopView.setCanLoop(false);
        loopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {

            }
        });
        loopView.setTextSize(25);//must be called before setDateList
        loopView.setDataList(getList(50));

        loopView2 = (LoopView2) findViewById(R.id.loop_view2);
        loopView2.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                PopToastUtil.showToast(mContext,"item="+item);
            }
        });
        loopView2.setTextSize(25);//must be called before setDateList
        loopView2.setDataList(getList(20));
        loopView2.setCurrentIndex(10);
//        ((new ProvinceInfoParserTask(this, mHandler))).execute();// 解析本地地址信息文件
    }

    public ArrayList<String> getList(int c){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            list.add("DAY TEST:" + i);
        }
        return list;
    }


//    private Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case ProvinceInfoParserTask.MSG_PARSE_RESULT_CALLBACK: // 解析地址完成
//                    mProvinceList = (ArrayList<ProvinceModel>) msg.obj;
//                    break;
//            }
//            return false;
//        }
//    });

//    @Override
//    public void onAddressPickCompleted(String province, String provinceId, String city, String cityId) {
////        Toast.makeText(this,province+"-"+provinceId+"-"+city+"-"+cityId,Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,ProvinceInfoUtils.matchAddress(this,provinceId,cityId,mProvinceList),Toast.LENGTH_SHORT).show();
//        ProvinceInfoUtils.matchAddress(this,provinceId,cityId,mProvinceList);
//    }
}
