package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.util.StringUtil;

/**
 * 区域选择器
 */
public class AreaPickerPopBuilder extends CommonPickerPop.Builder {
    public AreaPickerPopBuilder(Context context, PickerPopWindow.OnPickListener listener) {
        super(context, listener);
        //private String[] viewFormatTag = new String[]{"YY","text1","MM","text2","DD","text3"};//"yyyy年MM月dd日 HH:mm:ss"
        setViewFormat("v-t-v-t-v-t", new String[]{"YY", "省", "MM", "市", "DD", "区"});
        
        this.pickData = new PickData() {
            @Override
            public List<PickDataItem> getDataByTag(String tag, int[] selectIndexs) {
                if (tag == "YY") {
                    List<PickDataItem> pickDataItemList = new ArrayList<>();
                    int yearCount = 2050 - 1980;
                    int j;
                    for (j = 0; j < yearCount; ++j) {
                        PickDataItem pickDataItem = new PickDataItem(StringUtil.formatNumberToStr(1980 + j, 2));
                        pickDataItemList.add(pickDataItem);
                    }
                    return pickDataItemList;
                } else if (tag == "MM") {
                    List<PickDataItem> pickDataItemList = new ArrayList<>();
                    for (int i = 0; i < 12; i++) {
                        PickDataItem pickDataItem = new PickDataItem(StringUtil.formatNumberToStr(i + 1, 2));
                        pickDataItemList.add(pickDataItem);
                    }
                    return pickDataItemList;
                } else if (tag == "DD") {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(1, selectIndexs[0] + 1980);
                    calendar.set(2, selectIndexs[1]);
                    int dayMaxInMonth = calendar.getActualMaximum(5);
                    List<PickDataItem> pickDataItemList = new ArrayList<>();
                    for (int i = 0; i < dayMaxInMonth; i++) {
                        PickDataItem pickDataItem = new PickDataItem(StringUtil.formatNumberToStr(i + 1, 2));
                        pickDataItemList.add(pickDataItem);
                    }
                    return pickDataItemList;
                }
                return null;
            }

            @Override
            public String[] getTags() {
                return new String[]{"YY", "MM", "DD"};
            }

            @Override
            public int[] getDefautIndex() {
                return new int[]{0, 0, 0};
            }
        };
    }
}
