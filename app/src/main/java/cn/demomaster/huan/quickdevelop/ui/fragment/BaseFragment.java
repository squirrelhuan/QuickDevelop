package cn.demomaster.huan.quickdevelop.ui.fragment;

import android.text.TextUtils;
import android.view.View;

import java.lang.annotation.Annotation;

import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;


public abstract class BaseFragment extends QDFragment {

    public String getTitleFromAnnotation(){
        //获取注解
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
    public void initCreatView(View mView) {
        super.initCreatView(mView);
        if(TextUtils.isEmpty(getTitle())){
            setTitle(getTitleFromAnnotation());
        }
    }

}
