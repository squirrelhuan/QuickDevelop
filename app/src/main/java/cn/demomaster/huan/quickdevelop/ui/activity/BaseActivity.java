package cn.demomaster.huan.quickdevelop.ui.activity;

import android.view.View;

import java.lang.annotation.Annotation;

import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;

public class BaseActivity extends QDActivity {

    public String getTitleFromAnnotation(){
        Annotation[] annotations = getClass().getAnnotations();
        for (Annotation a : annotations) {
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
