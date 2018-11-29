package cn.demomaster.huan.quickdevelop;

import org.junit.Test;

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
    public void fmate(){
        String code ="3";
        String r = String.format("%0" + 2 + "d", Integer.parseInt(code));

        System.out.println(r);
    }

}