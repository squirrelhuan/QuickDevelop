package cn.demomaster.huan.quickdeveloplibrary.helper.cache;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;

/**
 * 广告帮助类
 */
public class UrlHelper {

    /**
     * 分析url文件类型
     * @param urlString
     * @param analyResult
     */
    public static void analyUrl(String urlString,AnalyResult analyResult){
        //获取url类型
        QdThreadHelper.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                BufferedInputStream bis = null;
                HttpURLConnection urlconnection = null;
                URL url = null;
                try {
                    url = new URL(urlString);
                    urlconnection = (HttpURLConnection) url.openConnection();
                    urlconnection.connect();
                    bis = new BufferedInputStream(urlconnection.getInputStream());
                    String fileTypeStr = HttpURLConnection.guessContentTypeFromStream(bis);
                    if (TextUtils.isEmpty(fileTypeStr)) {
                        fileTypeStr = urlconnection.guessContentTypeFromName(urlString);
                    }
                    final int fileLength = urlconnection.getContentLength();
                    final String fileType = fileTypeStr;
                    //System.out.println("文件类型:" + fileType+",文件大小:"+fileLength+","+Thread.currentThread().getName());//+ ",urlString=" + urlString);
                    if (!TextUtils.isEmpty(fileType)) {
                        //根据类型加载图片/视频
                        QdThreadHelper.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(analyResult!=null){
                                    analyResult.success(urlString,fileType,fileLength);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(analyResult!=null){
                        analyResult.error();
                    }
                }
            }
        });
    }

    public static interface AnalyResult{
        void success(String url,String fileType,int fileLength);
        void error();
    }

}
