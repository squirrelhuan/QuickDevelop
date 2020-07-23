package cn.demomaster.huan.quickdevelop;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @org.junit.Test
    public void xor(){
        //String code = checkcode_0007("000b412e30392e30312e303600");
        String code2 = checkcode_0007("");
        //System.out.println(code);
       QDLogger.println(code2);
    }
    public String checkcode_0007(String para){
        String[] dateArr = new String[para.length()/2];
        for(int i=0;i<dateArr.length;i++){
            dateArr[i] = para.substring(i*2, (i+1)*2);
        }
       QDLogger.println(Arrays.asList(dateArr));
        String code = "00";
        for (int i = 0; i < dateArr.length-1; i++) {
            if(i == 0){
                code = xor(dateArr[i],code);
            }else{
                code = xor(code, dateArr[i]);
            }
        }
        return code;
    }

    private static String xor(String strHex_X,String strHex_Y){
       QDLogger.println("strHex_X="+strHex_X+",strHex_Y="+strHex_Y);
        //将x、y转成二进制形式
        String anotherBinary=Integer.toBinaryString(Integer.valueOf(strHex_X,16));
        String thisBinary=Integer.toBinaryString(Integer.valueOf(strHex_Y,16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if(anotherBinary.length() != 8){
            for (int i = anotherBinary.length(); i <8; i++) {
                anotherBinary = "0"+anotherBinary;
            }
        }
        if(thisBinary.length() != 8){
            for (int i = thisBinary.length(); i <8; i++) {
                thisBinary = "0"+thisBinary;
            }
        }
        //异或运算
        for(int i=0;i<anotherBinary.length();i++){
            //如果相同位置数相同，则补0，否则补1
            if(thisBinary.charAt(i)==anotherBinary.charAt(i))
                result+="0";
            else{
                result+="1";
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
    public void fmate(){
        String code ="3";
        String r = String.format("%0" + 2 + "d", Integer.parseInt(code));

       QDLogger.println(r);
    }


    @Test
    public void testClass(){
        String className = this.getClass().getName();
       QDLogger.println(className);
        Class clazz = null;
        try {
            clazz = Class.forName("cn.demomaster.huan.quickdevelop.ExampleUnitTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
           QDLogger.println("----------");
        }

       QDLogger.println(clazz.getClass().getName());
       QDLogger.println(this.getClass().getPackage().getName());
       QDLogger.println(Test.class.getResource(""));
       QDLogger.println(Test.class.getResource("/"));
    }

    @Test
    public void time(){
       QDLogger.println(System.currentTimeMillis());
    }
}