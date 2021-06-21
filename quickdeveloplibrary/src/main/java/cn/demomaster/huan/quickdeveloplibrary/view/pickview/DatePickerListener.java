package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

public interface DatePickerListener {

    public void onSelectChanged();

    public static interface OnDateSelectListener {
        void onDateSelect(int year, int month, int day, int hour, int minute, int sencond);
    }
/*
    public static interface OnTimePickListener {
        void onTimePickCompleted(int var1, int var2, String var3, String var4);
    }*/
}
