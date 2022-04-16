package cn.demomaster.huan.quickdeveloplibrary;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpClient;
import cn.demomaster.huan.quickdeveloplibrary.socket.QDTcpServer;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSax() {
        // QDSaxHandler mySAXApp = new QDSaxHandler();
        //mySAXApp.test();
    }

    @Test
    public void testSocket() {
        QDTcpServer qdTcpServer = new QDTcpServer();
        qdTcpServer.setOnReceiveMessageListener((clientId, qdMessage) -> {
            System.out.println("shou dao le " + qdMessage);
            if (qdMessage != null) {
                if (qdMessage.getData().equals("你好")) {
                    qdTcpServer.sendMessage(clientId, qdMessage.getTime(), "懒得理你");
                }
            }
        });
        qdTcpServer.start();
    }

    @Test
    public void testSocket2() {
        QDTcpClient qdTcpClient = new QDTcpClient("",111);
        qdTcpClient.send("a43654765876");
    }

    @Test
    public void testMD5() {
        System.out.println(generate("abcd"));
    }

    public static String generate(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();
        for (byte b : byteArray) {
            if (Integer.toHexString(0xFF & b).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & b));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & b));
        }
        return md5StrBuff.toString();
    }
}