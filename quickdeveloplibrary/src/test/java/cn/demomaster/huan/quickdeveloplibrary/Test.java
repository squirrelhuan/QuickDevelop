package cn.demomaster.huan.quickdeveloplibrary;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuLayout;

/**
 * @author squirrel桓
 * @date 2018/11/23.
 * description：
 */
public class Test {


    private List<Character> list;
    private List<String> aaa;
    @org.junit.Test
    public  void test(){
        testa(Test.class,"menuTabs");
    }
    public  void testa(Class clazza,String fieldName){
        // TODO Auto-generated method stub
        try {
            Field listField = clazza.getDeclaredField(fieldName);
            System.out.println(listField.getGenericType());
            //获取 list 字段的泛型参数
            ParameterizedType listGenericType = (ParameterizedType) listField.getGenericType();
            Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
            System.out.println(listActualTypeArguments[listActualTypeArguments.length - 1]);
            for (int i = 0; i < listActualTypeArguments.length; i++) {
                System.out.println(listActualTypeArguments[i]);
                testa(Class.forName(listActualTypeArguments[i].toString()), "showTextField");
            }
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

   /* List<TabMenuLayout.MenuTab> menuTabs;
    @org.junit.Test
    public void aaa(){
        menuTabs = new ArrayList<>();

        List<TabMenuLayout.TextModel> textModels = new ArrayList<>();
        textModels.add(new TabMenuLayout.TextModel("a",1,"11a"));
        TabMenuLayout.MenuTab menuTab1 = new TabMenuLayout.MenuTab("name", "code", textModels);
        menuTabs.add(menuTab1);

        List<TabMenuLayout.TextModel> textModels2 = new ArrayList<>();
        textModels2.add(new TabMenuLayout.TextModel("b",2,"11b"));
        TabMenuLayout.MenuTab menuTab2 = new TabMenuLayout.MenuTab("name", "code", textModels2);
        menuTabs.add(menuTab2);

        List<TabMenuLayout.TextModel> textModels3 = new ArrayList<>();
        textModels3.add(new TabMenuLayout.TextModel("e",7,"eeb"));
        textModels3.add(new TabMenuLayout.TextModel("f",8,"f8"));
        TabMenuLayout.MenuTab menuTab3 = new TabMenuLayout.MenuTab("name", "code", textModels3);
        menuTabs.add(menuTab3);

        for(TabMenuLayout.MenuTab menuTab:menuTabs){
            List list = menuTab.getRelList();
            Class clazz = menuTab.getClazz();
            clazz = list.get(0).getClass();
            System.out.println("C="+clazz);
            testa(clazz,menuTab.getShowTextField());
            //System.out.println("C="+menuTab.getRelList().getClass());
        }
        //test();

        //addMenus(menuTabs);
    }*/



    @org.junit.Test
    public void test1(){
        Integer[] a ={1,2,3};
        List<Integer> al = Arrays.asList(a);
        /*al = new ArrayList<>();
        al.add(1);
        al.add(2);
        al.add(3);*/
        al.remove((Integer) 1);
        for(Integer i:al){
            System.out.println("==============="+i);
        }
    }


    /**
     * 获取今天往后一周的集合
     */
    @org.junit.Test
    public  void get7week(){
        String week = "";
        List<String> weeksList = new ArrayList<String>();
        List<String> dateList = get7date(21);
        for (String s : dateList) {
            System.out.println(s);
            if (s.equals(StringData())) {
                week = "今天";
            } else {
                week = getWeek(s);
            }
            weeksList.add(week);
        }
        for(String str :weeksList){
            System.out.println(str);
        }
        //return weeksList;
    }



    public static List<String> get7date(int count) {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < count-1; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        return dates;
    }


    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mDay;
    private static String mWay;

    /**
     * 获取当前年月日
     * 
     * @return
     */
    public static String StringData() {


        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if(Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear),(Integer.parseInt(mMonth)))){
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear),(Integer.parseInt(mMonth))));
        }
        return mYear + "-" + (mMonth.length()==1?"0"+mMonth:mMonth) + "-" + (mDay.length()==1?"0"+mDay:mDay);
    }

    /**得到当年当月的最大日期**/
    public static int MaxDayFromDay_OF_MONTH(int year,int month){
        Calendar time=Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR,year);
        time.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0                 
        int day=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        return day;
    }


    /**
     * 根据当前日期获得是星期几
     * 
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {


            c.setTime(format.parse(time));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "周天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "周六";
        }
        return Week;
    }


}
