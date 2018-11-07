package cn.demomaster.huan.quickdeveloplibrary.util;

import android.app.Activity;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class StringVerifyUtil {

    /**
     * 电话号码格式校验
     * @param telephone
     * @return
     */
    public static boolean validateTelePhone(String telephone){
        if (TextUtils.isEmpty(telephone) || !isTelePhoneValid(telephone)) {
            return false;
        }
        return true;
    }

    /**
     * 手机号码格式校验
     * @param telephone
     * @return
     */
    public static boolean validateMobilePhone(String telephone){
        if (TextUtils.isEmpty(telephone) || !isTelePhoneValid(telephone)) {
            return false;
        }
        return true;
    }

    /**
     * 密码格式校验
     * @param password
     * @return
     */
    public static boolean validatePassword(String password){
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            return false;
        }
        return true;
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


}
