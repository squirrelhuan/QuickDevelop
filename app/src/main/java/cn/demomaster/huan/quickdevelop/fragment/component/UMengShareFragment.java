package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.helper.AudioRecordHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "UMengShareFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class UMengShareFragment extends BaseFragment {
    //Components
    ViewGroup mView;



    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_umengshare, null);
        }

        final UMShareListener umShareListener = new UMShareListener() {
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
        });

        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarLayout actionBarLayout) {

        Bundle bundle = getArguments();
        //actionBarLayout.setTitle(title+"---------ASDFGGHHJ");
        actionBarLayout.setHeaderBackgroundColor(Color.BLUE);
    }



}