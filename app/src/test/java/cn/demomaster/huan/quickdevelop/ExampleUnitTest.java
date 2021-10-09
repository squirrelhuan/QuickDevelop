package cn.demomaster.huan.quickdevelop;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static class A {
        int v;

        public void setV(int v) {
            this.v = v;
        }
    }

    public static class B extends A {
        int v;

        public void setV1(int v) {
            this.v = v;
        }
    }
    public static class ChargerModel  {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @org.junit.Test
    public void testList() {
        //创建局部变量
        List<ChargerModel> chargerModels = new ArrayList<>();
        for(int i=0;i<10;i++){
            ChargerModel model = new ChargerModel();
            model.setName("n"+i);
            chargerModels.add(model);
        }
        System.out.println("------------   111  -------------");
        printList(chargerModels);
        testList2(chargerModels);
        System.out.println("------------   333  -------------");
        chargerModels.clear();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("------------   end  -------------");
        printList(chargerModels);
    }

    private void testList2(List<ChargerModel> chargerModels) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("------------   222  -------------");
                printList(chargerModels);

                for(int i=100;i<110;i++){
                    ChargerModel model = new ChargerModel();
                    model.setName("n"+i);
                    chargerModels.add(model);
                }
            }
        }).start();
    }

    private void printList(List<ChargerModel> chargerModels) {
        for(ChargerModel model:chargerModels){
            System.out.println(""+model.getName());
        }
    }

    @org.junit.Test
    public void xor() {
        //String code = checkcode_0007("000b412e30392e30312e303600");
        String code2 = checkcode_0007("");
        //System.out.println(code);
        System.out.println(code2);
        A b = new B();
        System.out.println("v="+b.v);
        b.setV(2);
        System.out.println("v="+b.v);
        ((B)b).setV1(3);
        System.out.println("v="+b.v);
        System.out.println("v="+((B)b).v);
    }

    public String checkcode_0007(String para) {
        String[] dateArr = new String[para.length() / 2];
        for (int i = 0; i < dateArr.length; i++) {
            dateArr[i] = para.substring(i * 2, (i + 1) * 2);
        }
        System.out.println(Arrays.asList(dateArr));
        String code = "00";
        for (int i = 0; i < dateArr.length - 1; i++) {
            if (i == 0) {
                code = xor(dateArr[i], code);
            } else {
                code = xor(code, dateArr[i]);
            }
        }
        return code;
    }

    private static String xor(String strHex_X, String strHex_Y) {
        System.out.println("strHex_X=" + strHex_X + ",strHex_Y=" + strHex_Y);
        //将x、y转成二进制形式
        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {
                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }
        //异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            //如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i))
                result += "0";
            else {
                result += "1";
            }
        }
        //System.out.println("code="+result);
        return Integer.toHexString(Integer.parseInt(result, 2));
    }


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void fmate() {
        String code = "3";
        String r = String.format("%0" + 2 + "d", Integer.parseInt(code));

        System.out.println(r);
    }


    @Test
    public void testClass() {
        String className = this.getClass().getName();
        System.out.println(className);
        Class clazz = null;
        try {
            clazz = Class.forName("cn.demomaster.huan.quickdevelop.ExampleUnitTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("----------");
        }

        System.out.println(clazz.getClass().getName());
        System.out.println(this.getClass().getPackage().getName());
        System.out.println(Test.class.getResource(""));
        System.out.println(Test.class.getResource("/"));
    }

    @Test
    public void time() {
        System.out.println(System.currentTimeMillis());
    }
}