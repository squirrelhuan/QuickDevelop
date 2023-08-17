package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.view.picktime.LanguageAdapter;
import cn.demomaster.huan.quickdevelop.view.picktime.LanguagePickerPop;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.AreaPickerPopBuilder;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.CommonPickerPop;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.DatePickerPopBuilder;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.DateTimePickerPopBuilder;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView2;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.PickerPopWindow;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.TimePickerPopWin;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.TimerPickerPopBuilder;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

@ActivityPager(name = "Cascader级联选择器", preViewClass = TextView.class, resType = ResType.Resource,iconRes = R.mipmap.ic_mul_selector)
public class CascaderSelectorFragment extends QuickFragment {
//    private ArrayList<ProvinceModel> mProvinceList = null; // 省份列表
//    private String mProvince = null; // 省份
//    private String mCity = null; // 城市
    @BindView(R.id.loop_view)
    public LoopView loopView;
    private LoopView2 loopView2;


    public ArrayList<String> getList(int c) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            list.add("DAY :" + i);
        }
        return list;
    }

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pick, null);
        return view;
    }

    @Override
    public String getTitle() {
        return "Cascader 级联选择器";
    }

    @Override
    public void initView(View rootView) {
        getActionBarTool().getRightView().setVisibility(View.GONE);
        ButterKnife.bind(this,mContext);
        findViewById(R.id.date).setOnClickListener(v -> {
           /* new DatePickerListener.OnDateSelectListener() {
                @Override
                public void onDateSelect(int year, int month, int day, int hour, int minute, int sencond) {
                    Toast.makeText(mContext, year+"-"+month+"-"+day, Toast.LENGTH_SHORT).show();
                }
            })*/
            CommonPickerPop pickerPopWin = new DatePickerPopBuilder(mContext, new PickerPopWindow.OnPickListener() {
                @Override
                public void onSelect(PickerPopWindow pickerPopWindow, Object[] args, int[] positions) {
                    Toast.makeText(mContext, Arrays.toString(args), Toast.LENGTH_SHORT).show();
                }
            }).textConfirm("确定") //text of confirm button
                    .textCancel("取消") //text of cancel button
                    .colorContentText(Color.GRAY, Color.RED, Color.GRAY)
                    .build();
            pickerPopWin.showWithView(mContext);
        });

        findViewById(R.id.timepick).setOnClickListener(v -> {
            TimePickerPopWin timePickerPopWin = new TimePickerPopWin.Builder(mContext, new PickerPopWindow.OnPickListener() {
                @Override
                public void onSelect(PickerPopWindow pickerPopWindow, Object[] args, int[] positions) {
                    Toast.makeText(mContext, Arrays.toString(args), Toast.LENGTH_SHORT).show();
                }
            }).textConfirm("CONFIRM")
                    .textCancel("CANCEL")
                    .btnTextSize(16)
                    .colorCancel(Color.parseColor("#11DDAF"))
                    .colorConfirm(Color.parseColor("#11DDAF"))
                    .colorContentText(Color.GRAY, Color.RED, Color.GRAY)
                    .build();
            // timePickerPopWin.showWithView(mContext);
            new TimerPickerPopBuilder(mContext, new PickerPopWindow.OnPickListener() {
                @Override
                public void onSelect(PickerPopWindow pickerPopWindow, Object[] args, int[] positions) {

                }
            }).build().showWithView(mContext);
        });

        findViewById(R.id.province).setOnClickListener(v -> {
            CommonPickerPop pickerPopWin = new AreaPickerPopBuilder(mContext, new PickerPopWindow.OnPickListener() {
                @Override
                public void onSelect(PickerPopWindow pickerPopWindow, Object[] args, int[] positions) {
                    Toast.makeText(mContext, Arrays.toString(args), Toast.LENGTH_SHORT).show();
                }
            }).textConfirm("确定") //text of confirm button
                    .textCancel("取消") //text of cancel button
                    .btnTextSize(16) // button text size
                    .viewTextSize(35) // pick view text size
                    .colorCancel(Color.parseColor("#11DDAF")) //color of cancel button
                    .colorConfirm(Color.parseColor("#11DDAF"))//color of confirm button
                    .colorContentText(Color.GRAY, Color.RED, Color.GRAY)
                    .build();
            pickerPopWin.showWithView(mContext);
        });

        findViewById(R.id.timepick2).setOnClickListener(v -> {
            CommonPickerPop datePickerPopWindow = new DateTimePickerPopBuilder(mContext, new PickerPopWindow.OnPickListener() {
                @Override
                public void onSelect(PickerPopWindow pickerPopWindow, Object[] args, int[] positions) {

                }
            }).viewTextSize(25)
                    .build();
            datePickerPopWindow.showWithView(mContext);
        });

        findViewById(R.id.common_pick).setOnClickListener(v -> {
            CommonPickerPop timePickerPopWin = new CommonPickerPop.Builder(mContext, new PickerPopWindow.OnPickListener() {
                @Override
                public void onSelect(PickerPopWindow pickerPopWindow, Object[] args, int[] positions) {

                }
            })
                    .viewTextSize(25)
                    .colorContentText(Color.GRAY, Color.RED, Color.GRAY)
                    .build();
            timePickerPopWin.showWithView(mContext);
        });
        findViewById(R.id.btn_launge_selector).setOnClickListener(v -> {
            List<LanguageAdapter.LanguageModel> data = new ArrayList<>();
            for(int i=0;i<10;i++){
                LanguageAdapter.LanguageModel model = new LanguageAdapter.LanguageModel();
                model.setName("china"+i);
                data.add(model);
            }
            LanguagePickerPop timePickerPopWin = new LanguagePickerPop.Builder(mContext, new PickerPopWindow.OnPickListener() {
                @Override
                public void onSelect(PickerPopWindow pickerPopWindow, Object[] args, int[] positions) {

                }
            })
                    .viewTextSize(25)
                    .setDate(data)
                    .colorCancel(Color.parseColor("#11DDAF"))
                    .colorConfirm(Color.parseColor("#11DDAF"))
                    .colorContentText(Color.GRAY, Color.RED, Color.GRAY)
                    .build();
            timePickerPopWin.showWithView(mContext);
        });


        // loopView = (LoopView) findViewById(R.id.loop_view);
        loopView.setInitPosition(2);
        loopView.setCanLoop(false);
        loopView.setLoopListener(item -> {

        });
        loopView.setTextSize(25);//must be called before setDateList
        loopView.setDataList(getList(50));

        loopView2 = findViewById(R.id.loop_view2);
        loopView2.setLoopListener(item -> QdToast.showToast(mContext, "item=" + item));
        loopView2.setTextSize(25);//must be called before setDateList
        loopView2.setDataList(getList(20));
        loopView2.setCurrentIndex(10);
//        ((new ProvinceInfoParserTask(this, mHandler))).execute();// 解析本地地址信息文件
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
