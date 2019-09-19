package cn.demomaster.huan.quickdevelop;

import org.junit.Test;

import java.util.Arrays;

public class test {
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