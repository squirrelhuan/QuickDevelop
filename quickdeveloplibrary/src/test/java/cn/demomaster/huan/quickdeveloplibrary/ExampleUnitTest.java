package cn.demomaster.huan.quickdeveloplibrary;

import org.junit.Test;

import cn.demomaster.huan.quickdeveloplibrary.util.xml.QDSaxHandler;

import static org.junit.Assert.*;

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
    public void testSax(){
        QDSaxHandler mySAXApp = new QDSaxHandler();
        //mySAXApp.test();
    }
}