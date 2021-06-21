package cn.demomaster.huan.quickdeveloplibrary.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActivityPager {
    String name() default "";

    Class activityClass() default void.class;

    //预览类型
    ResType resType() default ResType.Resource;

    //预览view
    Class preViewClass() default void.class;

    //预览资源
    int iconRes() default 0;

}
