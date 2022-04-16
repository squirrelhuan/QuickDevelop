package cn.demomaster.huan.quickdevelop.util;


import org.apache.commons_android.codec.DecoderException;
import org.apache.commons_android.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil2 {
    static String ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    /**
     * 字符编码(用哪个都可以，要注意new String()默认使用UTF-8编码 getBytes()默认使用ISO8859-1编码)
     */
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public static final String bm = "utf-8";

    /**
     * 字节数组转化为大写16进制字符串
     *
     * @param b
     * @return
     */
    private static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            String s = Integer.toHexString(value & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转字节数组
     *
     * @param s
     * @return
     */
    private static byte[] str2ByteArray(String s) {
        int byteArrayLength = s.length() / 2;
        byte[] b = new byte[byteArrayLength];
        for (int i = 0; i < byteArrayLength; i++) {
            byte b0 = (byte) Integer.valueOf(s.substring(i * 2, i * 2 + 2), 16)
                    .intValue();
            b[i] = b0;
        }

        return b;
    }


    /**
     * AES 加密
     *
     * @param content 明文
     * @param keyStr  生成秘钥的关键字
     * @return
     */

    public static String aesEncrypt(String keyStr, String content) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes(CHARSET_UTF8), ECB_PKCS5_PADDING);
            Cipher cipher = Cipher.getInstance(ECB_PKCS5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedData = cipher.doFinal(content.getBytes(bm));

            return Hex.encodeHexString(encryptedData);//Base64.encode(encryptedData);
//			return byte2HexStr(encryptedData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 解密
     *
     * @param keyStr  生成秘钥的关键字
     * @param content 密文
     * @return
     */
    public static String aesDecrypt(String keyStr, String content) {
        try {
            byte[] byteMi = Hex.decodeHex(content);//Base64.decode(content);
//			byte[] byteMi=	str2ByteArray(content);
            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes(CHARSET_UTF8), "AES");
            Cipher cipher = Cipher.getInstance(ECB_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData, CHARSET_UTF8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return null;
    }

}