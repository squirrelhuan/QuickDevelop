package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * Created by Squirrel桓 on 2018/11/10.
 */
public class ActionBarHelper {

    public static ActionBarLayout init(Activity mContext, int layoutResID){

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        //inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ActionBarLayout.Builder builder = new ActionBarLayout.Builder(mContext);
       // ViewGroup contentView_rel = (ViewGroup)mInflater.inflate(layoutResID, null);
        builder.setContentView(layoutResID);
       // ViewGroup headerView = (ViewGroup) mInflater.inflate(R.layout.activity_actionbar_common, null);
        builder.setHeadView(R.layout.activity_actionbar_common);
        return  builder.create();
    }
}
