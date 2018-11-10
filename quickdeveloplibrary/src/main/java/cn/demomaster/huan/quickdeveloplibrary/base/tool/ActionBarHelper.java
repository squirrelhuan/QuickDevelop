package cn.demomaster.huan.quickdeveloplibrary.base.tool;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * Created by Squirrel桓 on 2018/11/10.
 */
public class ActionBarHelper {

    public static ActivityLayout init(Activity mContext, int layoutResID){

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        //inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup contentView_rel = (ViewGroup)mInflater.inflate(layoutResID, null);
        ActivityLayout.Builder builder = new ActivityLayout.Builder(mContext);
        builder.setContentView(contentView_rel);
        ViewGroup headerView = (ViewGroup) mInflater.inflate(R.layout.activity_actionbar_common, null);
        builder.setHeadView(headerView);
        return  builder.create();
    }
}
