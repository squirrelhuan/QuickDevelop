package cn.demomaster.huan.quickdevelop.util;

public class PasswordGenarator {

    public PasswordGenarator(){
       init();
    }

    public void init(){
        char[] chars = new char[startLenth];
        for(int i=0;i<chars.length;i++){
            chars[i]=(char)startAscii;
        }
        password = new String(chars);
        time =0;
    }
    int startLenth = 8;
    String password="12344";
    long time;
    char[] passwordChars;
    int startAscii = 33;
    int lengthAscii = 94;
    public String genaratPass(){
        int count = lengthAscii;
        int index = (int) (time/count);//char索引
        char newchar = (char) ((int)time%count+startAscii);//
        if(index>startLenth-1){
            startLenth++;
            init();
        }
        passwordChars=password.toCharArray();
        System.out.println("time="+time+",index="+index);
        passwordChars[index] = newchar;
        password = new String(passwordChars);
        time++;
        return password;
    }
}
