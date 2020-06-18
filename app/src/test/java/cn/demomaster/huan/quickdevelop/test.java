package cn.demomaster.huan.quickdevelop;

import android.util.Log;

import org.junit.Test;

import java.util.Arrays;

public class test {

    @Test
    public void xor(){
        String code = checkcode_0007("1312f70f900168d900007df57b4884");
        System.out.println(code);
    }
    public String checkcode_0007(String para){
        String[] dateArr = new String[15];
        try {
            dateArr[0] = para.substring(0, 2);
            dateArr[1] = para.substring(2, 4);
            dateArr[2] = para.substring(4, 6);
            dateArr[3] = para.substring(6, 8);
            dateArr[4] = para.substring(8, 10);
            dateArr[5] = para.substring(10, 12);
            dateArr[6] = para.substring(12, 14);
            dateArr[7] = para.substring(14, 16);
            dateArr[8] = para.substring(16, 18);
            dateArr[9] = para.substring(18, 20);
            dateArr[10] = para.substring(20, 22);
            dateArr[11] = para.substring(22, 24);
            dateArr[12] = para.substring(24, 26);
            dateArr[13] = para.substring(26, 28);
            dateArr[14] = para.substring(28, 30);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String code = "";
        for (int i = 0; i < dateArr.length-1; i++) {
            if(i == 0){
                code = xor(dateArr[i], dateArr[i+1]);
            }else{
                code = xor(code, dateArr[i]);
            }
        }
        return code;
    }
    private static String xor(String strHex_X,String strHex_Y){
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
        Log.e("code",result);
        return Integer.toHexString(Integer.parseInt(result, 2));
    }

    @Test
    public void time(){
        System.out.println(System.currentTimeMillis());
    }


    /*public static void main(String[] args){

    }*/
    public String convertStringToHex(String str){
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }
        return hex.toString();
    }

    public String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    //504F533838383834  POS88884
    public static void main(String[] args) {

        test strToHex = new test();
        System.out.println("\n-----ASCII码转换为16进制 -----");
        String str = "1024";
        System.out.println("字符串: " + str);
        String hex = strToHex.convertStringToHex(str);
        System.out.println("转换为16进制 : " + hex);

        System.out.println("\n***** 16进制转换为ASCII *****");
        System.out.println("Hex : " + hex);
        System.out.println("ASCII : " + strToHex.convertHexToString(hex));

        /*****************************************************************/
        System.out.println("\n-----ASCII码转换为16进制 -----");
        int ins = 1024;
        System.out.println("int: " + ins);
        hex = strToHex.intToHex(ins);
        System.out.println("转换为16进制 : " + hex);

        System.out.println("\n***** 16进制转换为ASCII *****");
        System.out.println("Hex : " + hex);
        System.out.println("ASCII : " + strToHex.convertHexToString(hex));

        String str1 = "1024";
        int i=Integer.valueOf(str1);
        System.out.println(intToHex(i));

        System.out.println(intToHex(1900));
        setData(str1);
        //byte[] b = intToHex(i);
        //b[0] = 'c';
    }

    private static void setData(String str) {
        int i=Integer.valueOf(str);
        String a = intToHex(i);
        System.out.println(a);
        String arr[] = {};
        if(a.length()>2){
            arr[0] = a.substring(1,a.length()-1);
            arr[1] = a.substring(0,1);
        }else if (a.length()>0){
            arr[1] = a.substring(0,a.length()-1);
        }
        System.out.println(Arrays.toString(arr));
    }

    private static String intToHex(int n) {
        //StringBuffer s = new StringBuffer();
        StringBuilder sb = new StringBuilder(8);
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            sb = sb.append(b[n%16]);
            n = n/16;
        }
        a = sb.reverse().toString();
        return a;
    }

    // unsigned char
    static public short add(short a, short b) {
        return (short)((java_short_to_unsigned_char(a) + java_short_to_unsigned_char(b)) % 256);
    }

    static public short java_short_to_unsigned_char(short a) {
        return (short)(((byte)a + 256) % 256);
    }
}