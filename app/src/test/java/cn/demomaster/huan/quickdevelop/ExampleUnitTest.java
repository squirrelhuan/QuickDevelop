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


    @Test
    public void testClass(){
        String className = this.getClass().getName();
        System.out.println(className);
        Class clazz = null;
        try {
            clazz = Class.forName("cn.demomaster.huan.quickdevelop.ExampleUnitTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("----------");
        }

        System.out.println(clazz.getClass().getName());
        System.out.println(this.getClass().getPackage().getName());
        System.out.println(Test.class.getResource(""));
        System.out.println(Test.class.getResource("/"));
    }
}