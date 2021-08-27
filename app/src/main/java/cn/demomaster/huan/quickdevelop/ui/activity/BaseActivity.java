package cn.demomaster.huan.quickdevelop.ui.activity;

import android.view.View;

import java.lang.annotation.Annotation;

import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.quicksticker_annotations.QuickStickerBinder;

public class BaseActivity extends QDActivity {

    @Override
    public void initContentView() {
        super.initContentView();
        QuickStickerBinder.getInstance().bind(this);
    }

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
    public void setContentView(View view) {
        super.setContentView(view);
        setTitle(getTitleFromAnnotation());
    }
}
