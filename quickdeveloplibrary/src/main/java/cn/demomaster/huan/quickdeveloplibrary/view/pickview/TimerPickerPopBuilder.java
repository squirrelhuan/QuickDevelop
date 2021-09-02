package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.util.StringUtil;

public class TimerPickerPopBuilder extends CommonPickerPop.Builder {
    public TimerPickerPopBuilder(Context context, PickerPopWindow.OnPickListener listener) {
        super(context, listener);
        //private String[] viewFormatTag = new String[]{"YY","text1","MM","text2","DD","text3"};//"yyyy年MM月dd日 HH:mm:ss"
        setViewFormat("v-v", new String[]{"HH", "mm"});

        this.pickData = new PickData() {
            @Override
            public List<PickDataItem> getDataByTag(String tag, int[] selectIndexs) {
                if (tag == "HH") {
                    List<PickDataItem> pickDataItemList = new ArrayList<>();
                    int j;
                    for (j = 1; j < 12; ++j) {
                        PickDataItem pickDataItem = new PickDataItem(StringUtil.formatNumberToStr( j, 2));
                        pickDataItemList.add(pickDataItem);
                    }
                    return pickDataItemList;
                } else if (tag == "mm") {
                    List<PickDataItem> pickDataItemList = new ArrayList<>();
                    for (int i = 0; i < 60; i++) {
                        PickDataItem pickDataItem = new PickDataItem(StringUtil.formatNumberToStr(i , 2));
                        pickDataItemList.add(pickDataItem);
                    }
                    return pickDataItemList;
                }
                return null;
            }

            @Override
            public String[] getTags() {
                return new String[]{"HH", "mm"};
            }

            @Override
            public int[] getDefautIndex() {
                return new int[]{0, 0};
            }
        };
    }
}
