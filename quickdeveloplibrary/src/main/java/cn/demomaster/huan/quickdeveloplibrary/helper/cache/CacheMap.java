package cn.demomaster.huan.quickdeveloplibrary.helper.cache;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

//import com.alibaba.fastjson.JSON;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences;

/**
 * 缓存文件Map管理
 */
public class CacheMap {

    static String QuickCacheMap = "QuickCacheMap";
    static Map<String, String> md5Map = new HashMap<>();//key 文件目錄，value 文件名称md5码
    static Map<String, CacheInfo> cacheMap = new HashMap<>();//key url，value 文件名

    String cacheFolderPath = null;

    public void loadData(String folderPath) {
        this.cacheFolderPath = folderPath;
        //匹配缓存文件目录
        findCacheFolder();
        //从shareprefrence读取缓存
        loadSharePrefrence();
    }

    /**
     * 从shareprefrence读取缓存
     */
    private void loadSharePrefrence() {
        String string = QDSharedPreferences.getInstance().getString(QuickCacheMap, null);
        if (!TextUtils.isEmpty(string)) {
            Gson gson = new Gson();
            Type userListType = new TypeToken<ArrayList<CacheInfo>>(){}.getType();
            List<CacheInfo> cacheInfoList = gson.fromJson(string, userListType);
            if (cacheInfoList != null) {
                for (CacheInfo cacheInfo : cacheInfoList) {
                    String url = cacheInfo.getUrl();
                    String filePath = cacheInfo.getFilePath();
                    boolean fileExists = true;
                    //排除不存在的文件
                    if (!TextUtils.isEmpty(filePath)) {
                        if (!md5Map.containsKey(filePath)) {//以文件md5map为标准，对不存在的文件进行过滤
                            fileExists = false;//缓存目录中未找到
                        }

                        if (!fileExists) {//当前缓存文件夹中未找到缓存文件，匹配真实路径。判断文件是否存在
                            File file = new File(filePath);
                            if (file.exists()) {
                                String md5 = null;
                                if (!TextUtils.isEmpty(cacheInfo.getMd5())) {
                                    md5 = cacheInfo.getMd5();
                                } else {
                                    md5 = stringToMD5(filePath);
                                }
                                updateMd5(filePath, md5);
                                cacheInfo.setMd5(md5);
                                fileExists = true;//非缓存目录中找到了
                            }
                        }
                    }
                    if (fileExists) {
                        cacheMap.put(cacheInfo.getUrl(), cacheInfo);
                    }
                }
            }
        }
    }

    private void findCacheFolder() {
        File cacheFolderFile = new File(cacheFolderPath);
        findCacheFolder(cacheFolderFile);
    }

    /**
     * 从缓存文件中遍历
     *
     * @param file
     */
    private void findCacheFolder(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if(files!=null) {
                    for (File file1 : files) {
                        if (file1.isDirectory()) {
                            findCacheFolder(file1);
                        } else {
                            //这里只做初始化，不对value赋值
                            md5Map.put(file1.getAbsolutePath(), null);
                        }
                    }
                }
            }
        }
    }

    /**
     * 更新md5链表
     *
     * @param absolutePath
     */
    private void updateMd5(String absolutePath, String md5) {
        if (TextUtils.isEmpty(md5)) {
            if (md5Map.containsKey(absolutePath)) {
                md5 = md5Map.get(absolutePath);
            }
        }
        md5Map.put(absolutePath, md5);
    }

    @Nullable
    public void put(String url, CacheInfo cacheInfo) {
        String md5 = stringToMD5(url);
        cacheInfo.setMd5(md5);
        cacheInfo.setUrl(url);
        addCacheInfo(cacheInfo);
    }

    private void addCacheInfo(CacheInfo cacheInfo) {
        updateMd5(cacheInfo.filePath, cacheInfo.getMd5());
        cacheMap.put(cacheInfo.getUrl(), cacheInfo);
        updateCache();
    }

    /**
     * 更新缓存
     */
    private static void updateCache() {
        List<CacheInfo> cacheInfoList = new ArrayList<>();
        for (Map.Entry entry : cacheMap.entrySet()) {
            CacheInfo cacheInfo = (CacheInfo) entry.getValue();
            cacheInfo.setUrl((String) entry.getKey());
            cacheInfoList.add(cacheInfo);
        }
        Gson gson = new Gson();
        QDSharedPreferences.getInstance().putString(QuickCacheMap, gson.toJson(cacheInfoList));
    }

    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String stringToMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    public boolean containsURL(String url) {
        return cacheMap.containsKey(url);
    }

    public CacheInfo getCacheInfoByUrl(String url) {
        return cacheMap.get(url);
    }
}
