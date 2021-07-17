package cn.demomaster.huan.quickdeveloplibrary;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    public static String numToHex8(int b) {
        return String.format("%02x", b);//2表示需要两个16进制数
    }

    @Test
    public static void TextHex() {
        String a = numToHex8(10);
        QDLogger.println("a=" + a.toUpperCase());
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("cn.demomaster.huan.quickdeveloplibrary.test", appContext.getPackageName());
    }


    @Test
    public void testClass() {
        QDLogger.println(Test.class.getResource("/"));
    }
    
}
