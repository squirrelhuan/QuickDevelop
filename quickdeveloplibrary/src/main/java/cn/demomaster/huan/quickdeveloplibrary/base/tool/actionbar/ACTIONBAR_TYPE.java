package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

/**
 * 导航栏样式三种
 */
public enum ACTIONBAR_TYPE {
    NO_STATUS,
    //无导航栏
    NO_ACTION_BAR,
    //无导航栏并且内容可填充到状态栏
    NO_ACTION_BAR_NO_STATUS,
    //有导航栏
    NORMAL,
    //层叠
    ACTION_STACK,
    //层叠并且内容可填充到状态栏
    ACTION_STACK_NO_STATUS,
    //透明导航栏
    ACTION_TRANSPARENT
}
