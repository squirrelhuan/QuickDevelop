package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/*
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;*/

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.view.colorpicker.ColorPicker;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "ColorPickerFragment",preViewClass = TextView.class,resType = ResType.Custome)
public class ColorPickerFragment extends QDFragment {
    //Components
    ViewGroup mView;

    TextView tv_color;
    ColorPicker cp_color;
    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_colorpicker, null);
        }

        /*final UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        };

        Button btn_share_01 = mView.findViewById(R.id.btn_share_01);
        btn_share_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(mContext).withText("hello")
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener).open();
            }
        });

        Button btn_share_02 = mView.findViewById(R.id.btn_share_02);
        btn_share_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(mContext)
                        .setPlatform(SHARE_MEDIA.QQ)//传入平台
                        .withText("hello")//分享内容
                        .setCallback(umShareListener)//回调监听器
                        .share();
            }
        });*/
        cp_color = mView.findViewById(R.id.cp_color);
        tv_color = mView.findViewById(R.id.tv_color);
        handler.postDelayed(runnable,1000);

        return mView;
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tv_color.setText(""+cp_color.getColor());
            handler.postDelayed(runnable,1000);
        }
    };

    @Override
    public void initView(View rootView, ActionBar actionBarLayoutOld) {

        Bundle bundle = getArguments();
        //actionBarLayoutOld.setTitle(title+"---------ASDFGGHHJ");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.BLUE);
    }



}