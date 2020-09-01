package cn.demomaster.huan.quickdeveloplibrary;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static cn.demomaster.huan.quickdeveloplibrary.util.StringUtil.hexStr2Str;

/**
 * @author squirrel桓
 * @date 2018/11/23.
 * description：
 */
public class Test {


    public static String numToHex8(int b) {
        return String.format("%02x", b);//2表示需要两个16进制数
    }

    @org.junit.Test
    public void TextHex(){
        String a = numToHex8(10);
        System.out.println("a="+a.toUpperCase());
    }

    private List<Character> list;
    private List<String> aaa;

    @org.junit.Test
    public void test() {
        testa(Test.class, "menuTabs");
    }

    public void testa(Class clazza, String fieldName) {
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
            QDLogger.e(e);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            QDLogger.e(e);
        } catch (ClassNotFoundException e) {
            QDLogger.e(e);
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
    public void test1() {
        Integer[] a = {1, 2, 3};
        List<Integer> al = Arrays.asList(a);
        /*al = new ArrayList<>();
        al.add(1);
        al.add(2);
        al.add(3);*/
        al.remove((Integer) 1);
        for (Integer i : al) {
            System.out.println("===============" + i);
        }
    }


    /**
     * 获取今天往后一周的集合
     */
    @org.junit.Test
    public void get7week() {
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
        for (String str : weeksList) {
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
        for (int i = 0; i < count - 1; i++) {
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
     *
     * @return
     */
    public static String StringData() {


        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth)))) {
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth))));
        }
        return mYear + "-" + (mMonth.length() == 1 ? "0" + mMonth : mMonth) + "-" + (mDay.length() == 1 ? "0" + mDay : mDay);
    }

    /**
     * 得到当年当月的最大日期
     **/
    public static int MaxDayFromDay_OF_MONTH(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0                 
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        return day;
    }


    /**
     * 根据当前日期获得是星期几
     *  
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
            QDLogger.e(e);
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


    @org.junit.Test
    public void testTimer() {

        String str = "[\n" +
                "        {\n" +
                "            \"Id\": \"6\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545051762\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"7\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545051771\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"8\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545051777\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"9\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545051829\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"10\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545051857\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"11\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545051862\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"12\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545051898\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"13\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545051906\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"14\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545052277\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"15\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545052316\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"16\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545052322\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"17\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545052326\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"18\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545052331\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"19\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545052338\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"20\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545058147\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"21\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545058267\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"22\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545058287\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"23\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545058342\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"24\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545058356\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"25\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545058428\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"26\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545058518\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"27\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545058526\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"28\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545058619\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"29\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545058752\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"30\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545058926\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"31\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545058957\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"32\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545058967\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"33\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545058986\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"34\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059171\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"35\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059181\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"36\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059215\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"37\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059229\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"38\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059274\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"39\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059285\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"40\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059305\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"41\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059317\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"42\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059342\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"43\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059354\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"44\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059361\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"45\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059399\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"46\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059406\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"47\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059417\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"48\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059425\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"49\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059590\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"50\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059595\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"51\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059634\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"52\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059652\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"53\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545059686\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"54\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545059709\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"55\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545059819\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"56\",\n" +
                "            \"IsOnline\": \"1\",\n" +
                "            \"Insert_Time\": \"1545059832\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"57\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545059852\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"58\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545060303\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"59\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545060344\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"60\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545060362\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"61\",\n" +
                "            \"IsOnline\": \"2\",\n" +
                "            \"Insert_Time\": \"1545060562\"\n" +
                "        }\n" +
                "    ]";
        List<Timer> timers = new ArrayList();//建立一个存贮时间段的对象

        String lastState = "-1";//上次的状态（1/2）
        int position = -1;//上次状态的位置


        List<Result> results = new ArrayList<>();
        results = JSON.parseArray(str, Result.class);

        for (int i = 0; i < results.size(); i++) {

            System.out.println("" + i);
            if (position == -1) {//初始化
                position = 0;
            }
            if (lastState.equals("-1")) {//初始化
                lastState = results.get(i).getIsOnline();
            }

            Result resultc = results.get(i);
            if (!lastState.equals(resultc.getIsOnline())) {//判断当前状态是否和上次相等

                //获取上个状态开始的时间
                int t1 = Integer.valueOf(results.get(position).getInsert_Time());
                //获取当前第i条的时间
                int t2 = Integer.valueOf(results.get(i).getInsert_Time());

                //添加一条新时间段到新的数组
                Timer timer = new Timer();
                timer.setTime(t2 - t1);
                timer.setState(Integer.valueOf(lastState));
                timers.add(timer);

                lastState = results.get(i).getIsOnline();//更改当前的状态
                position = i;
            }
        }
        for (int i = 0; i < timers.size(); i++) {
            System.out.println("第" + i + "次" + (timers.get(i).getState() == 1 ? "在线" : "离线") + ",时间:" + timers.get(i).getTime());
        }
    }


    //时间端 对象
    public static class Timer {
        int id;
        int time;
        int state;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    public static class Result {

        /**
         * Id : 6
         * IsOnline : 1
         * Insert_Time : 1545051762
         */

        private String Id;
        private String IsOnline;
        private String Insert_Time;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getIsOnline() {
            return IsOnline;
        }

        public void setIsOnline(String IsOnline) {
            this.IsOnline = IsOnline;
        }

        public String getInsert_Time() {
            return Insert_Time;
        }

        public void setInsert_Time(String Insert_Time) {
            this.Insert_Time = Insert_Time;
        }
    }

    @org.junit.Test
    public void logger() {
        Class clazz =getClass();
        System.out.println("clazz:"+clazz.getName());
        Zprint.log(clazz, "ok");
    }


    @org.junit.Test
    public void fomatTime(){
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.KOREA);// HH:mm:ss
        System.out.println( simpleDateFormat.format(1574931387065l));
    }
    @org.junit.Test
    public void testHexStr2Str(){
       System.out.println(hexStr2Str("5355444B30383139313030393032323600"));
    }

}
