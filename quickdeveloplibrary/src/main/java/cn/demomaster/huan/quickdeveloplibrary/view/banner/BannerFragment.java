package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import cn.demomaster.huan.quickdeveloplibrary.helper.cache.CacheInfo;
import cn.demomaster.huan.quickdeveloplibrary.helper.cache.QuickCache;
import cn.demomaster.huan.quickdeveloplibrary.helper.cache.UrlHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout.QdVideo;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.QdLoadingView;
import cn.demomaster.qdlogger_library.QDLogger;

import static android.view.View.VISIBLE;
import static cn.demomaster.huan.quickdeveloplibrary.view.banner.BannerContentType.img;
import static cn.demomaster.huan.quickdeveloplibrary.view.banner.BannerContentType.video;

/**
 * 广告轮播单条信息流
 */
public class BannerFragment extends Fragment implements BannerFragmentInterface {
    public QdVideo qdVideo;
    public ImageView imageView;
    public TextView titleView;
    public QdLoadingView loadingView;
    BannerFrameLayout bannerFrameLayout;
    AdsResource adsResource;
    BannerAdapter.OnPlayingListener mOnloadingListener;
    BannerAdapter adsAdapter;
    int fragmentCode;

    public BannerFragment() {
    }

    public BannerFragment(AdsResource adsResource, BannerAdapter.OnPlayingListener onloadingListener, BannerAdapter adsAdapter, int fragmentCode) {
        this.adsResource = adsResource;
        this.mOnloadingListener = onloadingListener;
        this.adsAdapter = adsAdapter;
        this.fragmentCode = fragmentCode;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        /*View view = inflater.inflate(R.layout.fragment_layout_ads, null);
        bannerFrameLayout = view.findViewById(R.id.bannerFrameLayout);
        loadingView = bannerFrameLayout.findViewById(R.id.loadingView);
        bannerFrameLayout.setBannerRadius(adsResource.getRadius());*/
        bannerFrameLayout = new BannerFrameLayout(getContext());
        if (adsResource != null) {
            int color = adsResource.getBackgroundColor();
            bannerFrameLayout.setBackgroundColor(color);
            bannerFrameLayout.setBannerRadius(adsResource.getRadius());
        }
        loadingView = new QdLoadingView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(50, 50);
        layoutParams.gravity = Gravity.CENTER;
        bannerFrameLayout.addView(loadingView, layoutParams);
        bannerFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return bannerFrameLayout;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        QDLogger.println("onHiddenChanged=" + this.getClass().getSimpleName() + ",hidden=" + hidden);
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        QDLogger.println("setUserVisibleHint=" + isVisibleToUser);//可见//不可见
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adsAdapter != null && fragmentCode == adsAdapter.getCurrentItem()) {
            //System.out.println("fragment["+fragmentCode+"] =>"+hashCode()+" onresume ");
            adsAdapter.onFragmentActived(hashCode());
        }
    }
    
    private void loadResource() {
        if (bannerFrameLayout == null) {
            return;
        }
        loadingView.setVisibility(VISIBLE);
        BannerFileType adsResourceType = BannerFileType.getEnum(adsResource.getFrom());
        //QDLogger.i("fragment[" + fragmentCode + "] =>"+hashCode()+" 加载数据,类型：" + BannerContentType.getEnum(adsResource.getType())+","+adsResourceType);
        switch (adsResourceType) {
            case local://本地资源
                //QDLogger.i("本地资源 >> "+fragmentCode);
                resetView();
                break;
            case remote://网络资源
                loadRemoteResource();
                break;
        }
    }

    /**
     * 加载本地资源
     */
    private void activeFragment() {
        BannerContentType resourceType = BannerContentType.getEnum(adsResource.getType());
        switch (resourceType) {
            case img:
                break;
            case video:
                if (qdVideo.isPlaying()) {
                    qdVideo.pause();
                }
                System.out.println("播放视频 " + fragmentCode);
                loadingView.setVisibility(View.GONE);
                qdVideo.start();
                break;
            case text:
                if (titleView != null) {
                    titleView.setTextColor(adsResource.getTextColor());
                    loadingView.setVisibility(View.GONE);
                }
                break;
        }
    }
    
    /**
     * 加载网络资源
     */
    private void loadRemoteResource() {
        //QDLogger.i("loadRemoteResource >> "+fragmentCode);
        //获取url类型
        final String urlString = adsResource.getUrl();
        if (QuickCache.enable() && QuickCache.containsUrl(urlString)) {
            CacheInfo fileInfo = QuickCache.getCacheInfoByUrl(urlString);
            if (fileInfo != null) {
                if (!TextUtils.isEmpty(fileInfo.getFilePath())) {
                    adsResource.setFrom(BannerFileType.local.value());
                    adsResource.setFilePath(fileInfo.getFilePath());
                    adsResource.setType(getFileType(fileInfo.getFileType()));
                    System.out.println("从缓存中获取 " + fileInfo.getFileType() + "," + fileInfo.getFilePath());
                    resetView();
                    return;
                } else {
                    System.out.println("缓存文件路徑不存在: " + urlString);
                }
            } else {
                System.out.println("缓存中不存在2: " + urlString);
            }
        } else {
            System.out.println("缓存中不存在1: " + urlString);
        }

        UrlHelper.analyUrl(urlString, new UrlHelper.AnalyResult() {
            @Override
            public void success(String url, String fileType, int fileLength) {
                QDLogger.i(" " + fragmentCode + ",网络资源类型：" + fileType + ",当前类型：" + BannerContentType.getEnum(adsResource.getType()));
                if (adsResource.getType() != getFileType(fileType)) {
                    QuickCache.addFile(urlString, fileType, null);
                    adsResource.setType(getFileType(fileType));
                    if (adsResource.getType() == video.value()) {
                        QuickCache.downCacheFile(urlString, fileType);
                    }
                }
                resetView();
            }

            @Override
            public void error() {

            }
        });
    }

    public int getFileType(String fileType) {
        if (fileType.startsWith("image/")) {
            return img.value();
        } else if (fileType.startsWith("video/")) {
            return video.value();
        }
        return -1;
    }

    /**
     * 重置布局
     */
    private void resetView() {
        //QDLogger.d("resetView >> "+fragmentCode);
        if (bannerFrameLayout == null) {
            QDLogger.i("fragment[" + fragmentCode + "] bannerFrameLayout =null ");
            return;
        }
        BannerContentType resourceType = BannerContentType.getEnum(adsResource.getType());
        if (resourceType == null) {
            QDLogger.i("fragment[" + fragmentCode + "] resourceType =null ");
            return;
        }
        if (titleView != null) {
            bannerFrameLayout.removeView(titleView);
            titleView = null;
        }
        releaseUnUsedView(resourceType);
        switch (resourceType) {
            case img:
                if (imageView == null) {
                    imageView = new ImageView(bannerFrameLayout.getContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    bannerFrameLayout.addView(imageView);
                }
                Object imgObj = adsResource.getUrl();
                if (adsResource.getFrom() == BannerFileType.local.value()) {
                    imgObj = new File(adsResource.getFilePath());
                }
                Glide.with(getContext()).load(imgObj).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //setAutoTheme();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //setAutoTheme();
                        if (adsResource.getFrom() == BannerFileType.remote.value()) {
                            if (resource instanceof BitmapDrawable) {
                                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                                QuickCache.saveBitmap(adsResource.getUrl(), bitmap);
                            }
                            loadingView.setVisibility(View.GONE);
                        }
                        return false;
                    }
                }).into(imageView);

                if (!TextUtils.isEmpty(adsResource.getDesc())) {
                    titleView = new TextView(bannerFrameLayout.getContext());
                    titleView.setBackgroundColor(adsResource.getTextBackgroundColor());
                    titleView.setTextColor(adsResource.getTextColor());
                    titleView.setGravity(adsResource.getTextGravity());
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = adsResource.getLayout_gravity();
                    bannerFrameLayout.addView(titleView, layoutParams);
                    titleView.setTextSize(adsResource.getTextSize());
                    titleView.setText(TextUtils.isEmpty(adsResource.getDesc()) ? "" : adsResource.getDesc());
                }
                break;
            case video:
                qdVideo = new QdVideo(bannerFrameLayout.getContext());
                bannerFrameLayout.addView(qdVideo);
                qdVideo.setVideoPath(adsResource.getUrl());
                qdVideo.setOnCompletionListener(mp -> {
                    System.out.println("播放完成 video/mp4");
                    mOnloadingListener.onVideoComplete();
                });//监听播放完成的事件。
                qdVideo.setOnErrorListener((mp, what, extra) -> false);//监听播放发生错误时候的事件。
                qdVideo.setOnPreparedListener(mp -> {

                });

                if (adsResource.getFrom() == BannerFileType.local.value()) {
                    qdVideo.setVideoPath(adsResource.getFilePath());
                } else {
                    qdVideo.setVideoURI(Uri.parse(adsResource.getUrl()));
                }

                if (!TextUtils.isEmpty(adsResource.getDesc())) {
                    titleView = new TextView(bannerFrameLayout.getContext());
                    titleView.setBackgroundColor(adsResource.getTextBackgroundColor());
                    titleView.setTextColor(adsResource.getTextColor());
                    titleView.setGravity(adsResource.getTextGravity());
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = adsResource.getLayout_gravity();
                    layoutParams.leftMargin = 0;
                    layoutParams.topMargin = 0;
                    layoutParams.rightMargin = 0;
                    layoutParams.bottomMargin = 0;
                    titleView.setIncludeFontPadding(false);
                    bannerFrameLayout.addView(titleView, layoutParams);
                    titleView.setTextSize(adsResource.getTextSize());
                    titleView.setText(TextUtils.isEmpty(adsResource.getDesc()) ? "" : adsResource.getDesc());
                }
                break;
            case html:
                break;
            case text:
                titleView = new TextView(bannerFrameLayout.getContext());
                titleView.setBackgroundColor(adsResource.getTextBackgroundColor());
                titleView.setTextColor(adsResource.getTextColor());
                titleView.setText(TextUtils.isEmpty(adsResource.getDesc()) ? "" : adsResource.getDesc());
                titleView.setTextSize(adsResource.getTextSize());
                titleView.setGravity(adsResource.getTextGravity());
                titleView.setLines(1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    titleView.setAutoSizeTextTypeUniformWithPresetSizes(new int[]{6, 100}, 1);
                    titleView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                }
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = adsResource.getLayout_gravity();
                layoutParams.leftMargin = 0;
                layoutParams.topMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                titleView.setIncludeFontPadding(false);
                bannerFrameLayout.addView(titleView, layoutParams);
                break;
        }

        if (fragmentCode == adsAdapter.getCurrentItem()) {
            activeFragment();
        }
    }
    
    /**
     * 移除无用控件
     *
     * @param resourceType
     */
    private void releaseUnUsedView(BannerContentType resourceType) {
        switch (resourceType) {
            case video:
                if (qdVideo != null) {
                    if (qdVideo.isPlaying()) {
                        qdVideo.pause();
                    }
                    qdVideo.stopPlayback();
                    qdVideo.setOnCompletionListener(null);
                    qdVideo.setOnPreparedListener(null);
                    bannerFrameLayout.removeView(qdVideo);
                    qdVideo = null;
                }
                break;
        }
        BannerContentType[] enumArray = BannerContentType.values();
        for (BannerContentType bannerContentType : enumArray) {
            if (bannerContentType != resourceType) {
                releaseUnUsedView2(bannerContentType);
            }
        }
    }

    private void releaseUnUsedView2(BannerContentType resourceType) {
        switch (resourceType) {
            case text:
            case html:
                break;
            case video:
                if (qdVideo != null) {
                    if (qdVideo.isPlaying()) {
                        qdVideo.pause();
                    }
                    qdVideo.stopPlayback();
                    qdVideo.setOnCompletionListener(null);
                    qdVideo.setOnPreparedListener(null);
                    bannerFrameLayout.removeView(qdVideo);
                    qdVideo = null;
                }
                break;
            case img:
                if (imageView != null) {
                    bannerFrameLayout.removeView(imageView);
                    imageView = null;
                }
                break;
        }
    }

    /*
        void loading();
        void complete();
        void error();*/
    boolean isActived;

    @Override
    public void onActiveChanged(int activedHash) {
        //QDLogger.i("状态变化 " + fragmentCode + "=> " + hashCode() +"=?"+activedHash);
        if (activedHash == this.hashCode()) {
            isActived = true;
            loadResource();
        } else if (isActived) {
            isActived = false;
            pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    @Override
    public void onDestroyView() {
        pause();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        if (adsAdapter != null) {
            adsAdapter.unregisterFragment(this.hashCode());
        }
        super.onDetach();
    }

    /**
     * 页面暂停播放，
     */
    public void pause() {
       /* if(fragmentCode!=-1) {
            QDLogger.i("fragment[" + fragmentCode + "] =>"+hashCode()+" 暂停 " + hashCode());
        }*/
        if (qdVideo != null) {
            if (qdVideo.isPlaying()) {
                qdVideo.pause();
            }
            qdVideo.stopPlayback();
            qdVideo.setOnCompletionListener(null);
            qdVideo.setOnPreparedListener(null);
            bannerFrameLayout.removeView(qdVideo);
            qdVideo = null;
        }
    }

}