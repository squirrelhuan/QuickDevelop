package cn.demomaster.huan.quickdevelop.ui.activity.sample.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class NetWorkUtils {
	
	private static final String PINGURL = "rp.belle.cn";
	
	private static final String URL = "http://rp.belle.cn:9090/blsf-mesV1-web/bas_workshop_group/listAll.json";
	
	public static boolean isNetWork(String pingUrl){
		try {
		    Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 100 www.baid.com");
		    int status = process.waitFor();
		    return status==0;
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		
		return false;
	}
	
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static boolean isNetWork(){
		 HttpURLConnection connection = null;
	        InputStream is = null;
	        BufferedReader br = null;
	        String result = null;// 返回结果字符串
	        try {
	            // 创建远程url连接对象
	            URL url = new URL(URL);
	            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
	            connection = (HttpURLConnection) url.openConnection();
	            // 设置连接方式：get
	            connection.setRequestMethod("GET");
	            // 设置连接主机服务器的超时时间：15000毫秒
	            connection.setConnectTimeout(15000);
	            // 设置读取远程返回的数据时间：60000毫秒
	            connection.setReadTimeout(60000);
	            // 发送请求
	            connection.connect();
	            // 通过connection连接，获取输入流
	            if (connection.getResponseCode() == 200) {
	                is = connection.getInputStream();
	                // 封装输入流is，并指定字符集
	                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	                // 存放数据
	                StringBuffer sbf = new StringBuffer();
	                String temp = null;
	                while ((temp = br.readLine()) != null) {
	                    sbf.append(temp);
	                    sbf.append("\r\n");
	                }
	                result = sbf.toString();
	                Log.d("NetWorkUtils", "=== result:" + result);
	                return true;
	            }
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            // 关闭资源
	            if (null != br) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }

	            if (null != is) {
	                try {
	                    is.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }

	            connection.disconnect();// 关闭远程连接
	        }

	        return false;
	}
	
	 /**
     * Convert a IPv4 address from an integer to an InetAddress.
     *
     * @param hostAddress
     *            an int corresponding to the IPv4 address in network byte order
     */
    public static InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = { (byte) (0xff & hostAddress), (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)), (byte) (0xff & (hostAddress >> 24)) };

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    /**
     * Convert a IPv4 address from an InetAddress to an integer
     *
     * @param inetAddr
     *            is an InetAddress corresponding to the IPv4 address
     * @return the IP address as an integer in network byte order
     */
    public static int inetAddressToInt(InetAddress inetAddr) throws IllegalArgumentException {
        byte[] addr = inetAddr.getAddress();
        return ((addr[3] & 0xff) << 24) | ((addr[2] & 0xff) << 16) | ((addr[1] & 0xff) << 8) | (addr[0] & 0xff);
    }
	
}
