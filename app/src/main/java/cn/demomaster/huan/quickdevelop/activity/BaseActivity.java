package cn.demomaster.huan.quickdevelop.activity;

import android.view.View;

import java.lang.annotation.Annotation;

import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;

public class BaseActivity extends QDActivity {

    public String getTitleFromAnnotation(){
        //获取注释
        Annotation[] annotations = getClass().getAnnotations();
        for (Annotation a : annotations) {
            //如果是@Login注释，则强制转化，并调用username方法，和password方法。
            if (a != null && a instanceof ActivityPager) {
                String name = ((ActivityPager) a).name();
                return name;
            }
        }
        return null;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setTitle(getTitleFromAnnotation());
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setTitle(getTitleFromAnnotation());
    }
}
