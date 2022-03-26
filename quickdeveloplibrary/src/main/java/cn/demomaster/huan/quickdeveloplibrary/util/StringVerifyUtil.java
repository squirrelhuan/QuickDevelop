package cn.demomaster.huan.quickdeveloplibrary.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class StringVerifyUtil {

    /**
     * 电话号码格式校验
     *
     * @param telephone
     * @return
     */
    public static boolean validateTelePhone(String telephone) {
        return !(TextUtils.isEmpty(telephone) || !isTelePhoneValid(telephone));
    }

    /**
     * 手机号码格式校验
     *
     * @param telephone
     * @return
     */
    public static boolean validateMobilePhone(String telephone) {
        return !(TextUtils.isEmpty(telephone) || !isTelePhoneValid(telephone));
    }

    /**
     * 身份证格式校验
     *
     * @param idNumber
     * @return
     */
    public static boolean validateIdCard(String idNumber) {
        return !(TextUtils.isEmpty(idNumber) || !isIdCardValid(idNumber)) ;
    }

    /**
     * 密码格式校验
     *
     * @param password
     * @return
     */
    public static boolean validatePassword(String password) {
        return !(TextUtils.isEmpty(password) || !isPasswordValid(password));
    }

    //手机格式验证
    public static boolean validatelength(String telephone, int min, int max) {
        if (TextUtils.isEmpty(telephone)) {
            return false;
        }
        return (telephone.length() >=min && telephone.length() <= max);
    }

    //手机格式验证
    public static boolean isTelePhoneValid(String telephone) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(16[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(telephone);
        return m.matches();
    }

    //密码长度判断大于4
    public static boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }


    //身份证长度判断等于18
    public static boolean isIdCardValid(String password) {
        return password.length() == 18;
    }

    //手机格式验证
    public static boolean isEmailValid(String email) {
        // 邮箱验证规则
        String regEx = "[a-zA-Z_]{0,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(email);
        return m.matches();
    }
}
