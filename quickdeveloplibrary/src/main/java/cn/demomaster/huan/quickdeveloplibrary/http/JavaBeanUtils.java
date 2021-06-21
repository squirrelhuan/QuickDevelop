package cn.demomaster.huan.quickdeveloplibrary.http;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

public class JavaBeanUtils {
    public static <T> T getBean(String json, Class<T> dataClazz) {
        T obj = null;
        try {
            obj = JSON.parseObject(json, dataClazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Object JSONTOBean(String json, Type type) {
        Object obj = null;
        try {
            obj = JSON.parseObject(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
