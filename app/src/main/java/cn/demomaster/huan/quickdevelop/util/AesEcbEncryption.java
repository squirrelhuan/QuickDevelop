package cn.demomaster.huan.quickdevelop.util;

/*import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;*/

import org.apache.commons_android.codec.DecoderException;
import org.apache.commons_android.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES 对称加密工具类
 * @author lsz
 * 2021-04-09 9:25
 */
public class AesEcbEncryption {
    /**
     * 加解密算法/工作模式/填充方式
     */
    public static final String ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    public static final String CBC_PKCS7_PADDING = "AES/ECB/PKCS7Padding";
    /**
     * 加密算法类型
     */
    static final String ALGORITHM_KEY = "AES";
    
    /**
     * 算法长度
     */
    private static final int KEY_SIZE = 128;

    /**
     * 字符编码(用哪个都可以，要注意new String()默认使用UTF-8编码 getBytes()默认使用ISO8859-1编码)
     */
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    public static final String VIPARA = "1269571569321021";
    /**
     * 创建Cipher对象，为加密和解密提供密码功能
     * @param keyStr 密钥
     * @param mode 模式 (加密：Cipher.ENCRYPT_MODE,解密：Cipher.DECRYPT_MODE)
     * @return 返回 密码加密
     * @throws NoSuchAlgorithmException 算法类型异常
     * @throws NoSuchPaddingException   算法填充异常
     * @throws InvalidKeyException      无效的密钥异常
     */
    public static Cipher cipher(String keyStr, int mode) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyStr.getBytes(), ALGORITHM_KEY);
        Cipher cipher =  Cipher.getInstance(ECB_PKCS5_PADDING);
        cipher.init(mode, secretKeySpec);
        return cipher;
    }

    /**
     * 加密
     * @param key 密钥
     * @param content 加密内容
     * @return 加密字符串
     */
    public static String AESEncode(String key, String content) {
        try{
            Cipher cipher = cipher(key, Cipher.ENCRYPT_MODE);
            byte[] encryptBytes =cipher.doFinal(content.getBytes(CHARSET_UTF8));
            //QDLogger.e("encryptBytes="+ Arrays.toString(encryptBytes));
            return Hex.encodeHexString(encryptBytes);
        }catch (NoSuchAlgorithmException nsa){
            nsa.printStackTrace();
            //Log.error("算法类型异常========>",nsa);
            return null;
        }catch (NoSuchPaddingException nsp){
            nsp.printStackTrace();
            //Log.error("算法填充异常========>",nsp);
            return null;
        }catch (InvalidKeyException ik){
            ik.printStackTrace();
            //Log.error("无效的密钥异常========>",ik);
            return null;
        }catch (BadPaddingException bp){
            bp.printStackTrace();
            //Log.error("错误填充异常========>",bp);
            return null;
        }catch (IllegalBlockSizeException ibs){
            ibs.printStackTrace();
            //Log.error("非法的块大小异常========>",ibs);
            return null;
        }
    }

    /**
     * 解密
     * @param key 密钥
     * @param content 加密字符串
     * @return 解密后的数据
     */
    public static String AESDecode(String key, String content) {
        try {
            byte[] decryptFrom = Hex.decodeHex(content);
            Cipher cipher = cipher(key, Cipher.DECRYPT_MODE);
            byte[] decryptBytes = cipher.doFinal(decryptFrom);
            return new String(decryptBytes);
        }catch (NoSuchAlgorithmException nsa){
            nsa.printStackTrace();
            //Log.error("算法类型异常========>",nsa);
            return null;
        }catch (NoSuchPaddingException nsp){
            nsp.printStackTrace();
            //Log.error("算法填充异常========>",nsp);
            return null;
        }catch (InvalidKeyException ik){
            ik.printStackTrace();
            //Log.error("无效的密钥异常========>",ik);
            return null;
        }catch (BadPaddingException bp){
            bp.printStackTrace();
            //Log.error("错误填充异常========>",bp);
            return null;
        }catch (IllegalBlockSizeException ibs){
            ibs.printStackTrace();
            //Log.error("非法的块大小异常========>",ibs);
            return null;
        }catch (DecoderException d){
            d.printStackTrace();
            //Log.error("解码器异常========>",d);
            return null;
        }
    }

    public static  final String AES_SECRET_SERVER="lSzWQIvbJHutWFHD";//服务器加密用
    public static  final String AES_SECRET_APP="CgQbnTVEsdwXWGKwb100wv";//APP加密用
    public static void main(String[] args){
        String content = "{ \"employee\":{ \"name\":\"小明\", \"age\":62, \"city\":\"Seattle\" }}";

        String encryptStr = AESEncode(AES_SECRET_SERVER,content);
        System.out.println("加密结果：" + encryptStr);

        String decryptStr = AESDecode(AES_SECRET_SERVER,"858a4561ebf76b4defb9d995726a633726fb8311393fb710ffc4a6ef3c4c626cbf4fd0c5665239d4a9337f3371efc5de18657e8ae883dec4802288bd939dc1f751c21129a182613b3d2a697ea38db2d230877e7a2e3a1c54b0d16bf77c4ec64cab8cf64c9cba2a77ed511508f9ae0d33a8c9e98aab62257f47dc8d21346e461cdebccd022173e0e3848680929a3bbd1b86c210dbebb33a7ed17372e5c3926b9f2d09172ca8f3ad1c38e744fae699d0b2203d3487118f723895a7823231fb3179c6c4d1a8589e4db8dabfed7767623efeaba3327ccf08280d3b219df70c67ec0785d0ea30f4ebb654c38dc5a30667a4612d0316e901c812358aed61488f5817609ba068b34aca9a3f9dc754d6dc19a8e2cc116ee8fe19a966af9367cb4b582edbe58058e723bce3047edd33a3e8d3f92ef90551d6ee5ff6f158608668a57cc57af68842cd637c25a6720a74f58462b762e5fbf3d690a25f4bd44e0158daa5f88c60d9d1999e8cfc660a1163b08b0135cfcacfe6cdcc807b2560a203960a570c17c92af6daed18afa4a58efd0d3c5a121960292f1968d158af0e3cda019895913f8b4dabc7888eb65bfa8b2ab1b409a510d83d34c5b538d7bf031e37c95421f34db4454665c735fa341de2e76ced6a2b5ca291852c91706f2e51d5b8afebda4c94d3b1788ed112684e3e032a8ceebbecb06cb2d3eeb4ed58ac50faac5a9707cea00e74378fb06ddc95958ad69a7127150ad921e183fe573a623279b07ef04ca35f536114bf6e1b46ba96f3aefe24d98bfc8e2ba2ca78b56ec56b62a416b5e1e604");
        System.out.println("解密结果：" + decryptStr);
    }
}
