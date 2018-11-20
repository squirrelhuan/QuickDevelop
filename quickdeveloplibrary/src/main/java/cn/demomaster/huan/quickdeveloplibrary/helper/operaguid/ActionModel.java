package cn.demomaster.huan.quickdeveloplibrary.helper.operaguid;

import android.view.View;

/**
 * @author squirrel桓
 * @date 2018/11/20.
 * description：
 */
public class ActionModel {

    private String tip;//操作文字提示
    private ActionType actionType;
    private View targetView;


    enum ActionType{
        ClICK,TOUCH,MOVE
    }

}
