package cn.demomaster.huan.quickdeveloplibrary.util;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.http.PUT;

/**
 * @author squirrel桓
 * @date 2018/12/7.
 * description：
 */
public class StringUtil {


    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 隐藏部分位数
     * @param str
     * @return
     */
    public static String hidePartString(String str) {
        if(str!=null&& !TextUtils.isEmpty(str)){
            int length = str.length();
            int l2 = length/3;
           return hidePartString(str,l2);
        }
        return "";
    }

    /**
     * 隐藏部分位数
     * @param str
     * @param len
     * @return
     */
    public static String hidePartString(String str,int len) {
        if(str!=null&& !TextUtils.isEmpty(str.toString())){
            String p = str.toString();
            int length = p.length();
            String t = "";
            for(int i=0;i<len;i++){
                t+="*";
            }
            return p.replace(p.substring(len,length-len),t);
        }
        return "";
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param String str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            //sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static int bytes2int(byte[]  bytes){
        int  i;
        int a=bytes[0];
        int b=(bytes[1]&0xFF)<<8;
        int c=(bytes[2]&0xFF)<<16;
        int d=(bytes[3]&0xFF)<<24;
        return a|b|c|d;
    }

    /**
     * 十六进制转换字符串 --- 不去除空字符的结果
     * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str_(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    /**
     * 十六进制转换字符串(去除空字符的结果)
     * @param String hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        int n;
        LinkedHashMap<Integer,Byte> characterLinkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < hexStr.length() / 2; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            byte b = (byte) (n & 0xff);
            if(b!='\0') {
                characterLinkedHashMap.put(i,b);
            }
        }
        byte[] bytes = new byte[characterLinkedHashMap.size()];
        int i=0;
        for(Map.Entry entry: characterLinkedHashMap.entrySet()){
            bytes[i] = (byte) entry.getValue();
            i++;
        }
        String result = new String(bytes);
        return result;
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            //sb.append("");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * bytes字符串转换为Byte值
     *
     * @param String src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
        }
        return ret;
    }

    /**
     * convert HexString to byte[]
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStringToByte(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            if (n == 0) {
                n = 0x00;
            }
            bytes[i] = (byte) (n & 0xff);
        }
        System.out.println(new String(bytes).trim());
        return bytes;
    }

    public static int hexString2Int(String str) {
       byte[] bytes = str.getBytes();
       int r =0;
       for(int i=0;i<bytes.length;i++){
           r= (int) (r+(bytes[i]*(Math.pow(10, i))));
       }
       return r;
    }

    /**
     * String的字符串转换成unicode的String
     *
     * @param String strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public static String strToUnicode(String strText)
            throws Exception {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u" + strHex);
            else // 低位在前面补00
                str.append("\\u00" + strHex);
        }
        return str.toString();
    }

    /**
     * unicode的String转换成String的字符串
     * @param String hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }


    /**
     * 数字转指定位数的string
     * @param num 要转的数字
     * @param length 目标长度
     * @return
     */
    public static String formatNumberToStr(int num,int length) {
        String a = String.valueOf(num);
        int count = length - a.length();
        String r = null;
        for(int i=0;i<count;i++){
            r = (r==null?"":r)+"0";
        }
        return (r==null?"":r)+a;
    }
}
