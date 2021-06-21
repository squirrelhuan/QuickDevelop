package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

/**
 * 导航栏样式三种
 */
public enum ACTIONBAR_TYPE {
    //无状态栏，有标题栏，内容填充到标题栏以下部分
    NO_STATUS,
    //有状态栏，无标题栏，内容填充到状态栏以下部分
    NO_ACTION_BAR,
    //无状态栏且无标题栏，内容填充整个屏幕
    NO_ACTION_BAR_NO_STATUS,
    //有状态栏且有标题栏，内容填充到标题栏以下部分
    NORMAL,
    //层叠效果，有状态栏且有标题栏，页面内容可填充到状态栏
    ACTION_STACK,
    //层叠效果，无状态栏、有标题栏，内容填充整个屏幕
    ACTION_STACK_NO_STATUS,
    //层叠效果，有状态栏、无标题栏，内容不会和导航栏出现覆盖效果
    ACTION_STACK_NO_ACTION,
    //透明导航栏，有状态栏且有标题栏，内容不会和导航栏出现覆盖效果
    //ACTION_TRANSPARENT
}
