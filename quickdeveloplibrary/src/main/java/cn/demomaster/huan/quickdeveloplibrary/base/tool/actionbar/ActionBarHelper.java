package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * Created by Squirrel桓 on 2018/11/10.
 */
public class ActionBarHelper {

    public static ActionBarLayout init(Activity mContext, int layoutResID,int headLayoutResID){
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        //inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ActionBarLayout.Builder builder = new ActionBarLayout.Builder(mContext);
        // ViewGroup contentView_rel = (ViewGroup)mInflater.inflate(layoutResID, null);
        builder.setContentView(layoutResID);
        // ViewGroup headerView = (ViewGroup) mInflater.inflate(R.layout.activity_actionbar_common_a, null);
        builder.setHeadView(headLayoutResID);
        return  builder.create();
    }
    public static ActionBarLayout init(Activity mContext, int layoutResID){

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        //inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ActionBarLayout.Builder builder = new ActionBarLayout.Builder(mContext);
       // ViewGroup contentView_rel = (ViewGroup)mInflater.inflate(layoutResID, null);
        builder.setContentView(layoutResID);
       // ViewGroup headerView = (ViewGroup) mInflater.inflate(R.layout.activity_actionbar_common_a, null);
        builder.setHeadView(R.layout.quickdevelop_activity_actionbar_common);
        return  builder.create();
    }
    public static ActionBarLayout init(Activity mContext, ViewGroup layoutView){

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        //inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ActionBarLayout.Builder builder = new ActionBarLayout.Builder(mContext);
        // ViewGroup contentView_rel = (ViewGroup)mInflater.inflate(layoutResID, null);
        builder.setContentView(layoutView);
        // ViewGroup headerView = (ViewGroup) mInflater.inflate(R.layout.activity_actionbar_common_a, null);
        builder.setHeadView(R.layout.quickdevelop_activity_actionbar_common);
        return  builder.create();
    }

    /**
     * 导航栏构建者
     */
    public static class Builder {
        private int contentResId;
        private int content2ResId;
        private int headerResId;

        public Builder(int contentResId, int content2ResId, int headerResId) {
            this.contentResId = contentResId;
            this.content2ResId = content2ResId;
            this.headerResId = headerResId;
        }

        public void setContent2ResId(int content2ResId) {
            this.content2ResId = content2ResId;
        }

        public void setContentResId(int contentResId) {
            this.contentResId = contentResId;
        }

        public void setHeaderResId(int headerResId) {
            this.headerResId = headerResId;
        }

        public void build(){

        }
    }
}
