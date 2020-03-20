package cn.demomaster.huan.quickdeveloplibrary;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches {
    @Test
    public void test1(  ){

        // 按指定模式在字符串查找
        String line = "This order was placed for QT3000! OK?";
        String pattern = "(\\D*)(\\d+)(.*)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
            System.out.println("Found value: " + m.group(3) );
        } else {
            System.out.println("NO MATCH");
        }
    }

    private static final String REGEX = "\\bcat\\b";
    private static final String INPUT =
            "cat cat cat cattie cat";

    public void test2( ){
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT); // 获取 matcher 对象
        int count = 0;

        while(m.find()) {
            count++;
            System.out.println("Match number "+count);
            System.out.println("start(): "+m.start());
            System.out.println("end(): "+m.end());
        }
    }

    /**
     * 匹配字符串，并且是以该字符串开头或结尾
     * 字符串边界的元字符有两个：一个是用来匹配字符串开头的^，另一个是用来匹配字符串结尾的$
     * @date 2016-04-20 15:19:14
     * @author sgl
     */

    @Test
    public void stringBoundary(){
        String str="hello world,hello java,hello java";

        System.out.println("===========匹配字符串===========");
        //匹配str中所有字符串hello，这时str中3个hello都能匹配上，通过下面打印的匹配上的字符串的位置可以看出
        Pattern p=Pattern.compile("hello");
        Matcher m=p.matcher(str);
        while(m.find()){
            System.out.println(m.group()+"   位置：["+m.start()+","+m.end()+"]");
        }
        System.out.println("===========匹配字符串，并且该字符串是在开头的位置===========");
        // ^表示匹配字符串的开头，但是如何在[]里面则表示非，如[^a-f] 不匹配a-f
        // "hello"和"^hello"的区别就是：前者匹配时不管是不是在开头位置，只要能匹配就行，后者则是不但要能匹配而且还要是在开头的位置。这时str中3个hello只有第1个能匹配上。
        p=Pattern.compile("^hello");
        m=p.matcher(str);
        while(m.find()){
            System.out.println(m.group()+"   位置：["+m.start()+","+m.end()+"]");
        }

        System.out.println("===========匹配字符串===========");
        //这时str中两个java都能匹配上
        p=Pattern.compile("java");
        m=p.matcher(str);
        while(m.find()){
            System.out.println(m.group()+"   位置：["+m.start()+","+m.end()+"]");
        }
        System.out.println("===========匹配字符串，并且是该字符串是在末尾的位置===========");
        //这时str中两个java只有第2个才能匹配上
        p=Pattern.compile("java$");
        m=p.matcher(str);
        while(m.find()){
            System.out.println(m.group()+"   位置：["+m.start()+","+m.end()+"]");
        }

        System.out.println("===========匹配字符串，并且是该字符串是在start的位置===========");
        str="\"CSBOX:\"003A64181D886514990502524C3148940012910403524C33489B2000700404524C3148940026060405524C33489B2000660406524C33489B20007104\"\r\n"
        +"\"CSBOXGET:\"003A64181D886514990502524C3148940012910403524C33489B2000700404524C3148940026060405524C33489B2000660406524C33489B20007104\"\r\n"
        +"\"CSBOX:\"003A64181D886514990502524C3148940012910403524C33489B2000700404524C3148940026060405524C33489B2000660406524C33489B20007104\"\r\n";
           // p=Pattern.compile("\"CSBOX:\"(.+?)(?=\"\n\r)");

        String headerStr = "CSBOX:\"";
        String headerextStr = "CSBOXGET:\"";
        String footerStr = "\r\n";
        p=Pattern.compile("("+headerStr+"(.+?)(?="+footerStr+")"+")|("+headerextStr+"(.+?)(?="+footerStr+"))");
        m=p.matcher(str);
        while(m.find()){
            System.out.println(m.group()+"   位置：["+m.start()+","+m.end()+"]");
        }
    }


}
