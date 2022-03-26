package cn.demomaster.huan.quickdeveloplibrary.widget.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 加载布局
 */
public class LoadLayout extends FrameLayout {
    public LoadLayout(@NonNull Context context) {
        super(context);
        init(null);
    }

    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    LoadViewInterface loadViewInterface;
    LayoutInflater mInflater;
    View loadView;
    View contentView;
    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onLoadListener != null) {
                QDLogger.println("onClick errorCode="+errorCode);
                onLoadListener.onRetry(LoadLayout.this,errorCode);
            }
        }
    };
    private void init(AttributeSet attrs) {
        viewBuilders = new ArrayList<>();
        mInflater = LayoutInflater.from(getContext());
        int loadViewLayoutId = 0;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            loadViewLayoutId = a.getInt(R.styleable.Loadlayout_loadlayout, loadViewLayoutId);
            a.recycle();
        }
        if (loadViewLayoutId == 0) {
            setLoadViewInterface(new EmptyLayoutLoader());
        } else {
            loadView = mInflater.inflate(loadViewLayoutId, this, false);
        }
    }

    public void setLoadViewInterface(LoadViewInterface loadViewInterface) {
        this.loadViewInterface = loadViewInterface;
        if (loadView != null) {
            removeView(loadView);
        }
        loadView = loadViewInterface.onCreateLoadView(mInflater, this,onClickListener);
        loadView.setTag("loading");
        addView(loadView, loadViewInterface.getLayoutParams());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
        contentView = null;
        onLoadListener = null;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        //System.out.println("index = " + index + "," + child);
        if (getChildCount() > 2) {
            throw new IllegalStateException("LoadLayout can host more than tow direct child");
        }
        if (child == null || child.getTag() == null || !child.getTag().equals("loading")) {
            contentView = child;
        }
    }

    int requestCode = 0;
    int errorCode;
    public void loadData() {
        loadData(requestCode);
    }

    public void loadData(int code) {
        requestCode = code;
        if (contentView != null) {
            contentView.setVisibility(INVISIBLE);
        }
        LoadViewBuilder loadViewBuilder = getLoadViewBuilder(LoadStateType.Loading, code);
        loadViewInterface.onStateChanged(loadViewBuilder, errorCode);
        if (onLoadListener != null) {
            onLoadListener.onLoadData();
        }
    }

    public void loadSuccess() {
        if (contentView != null) {
            contentView.setVisibility(VISIBLE);
        }
        loadViewInterface.onStateChanged(getLoadViewBuilder(LoadStateType.LoadSuccess, 0), 0);
        if (onLoadListener != null) {
            onLoadListener.onLoadSuccess();
        }
        if (loadView != null) {
            loadView.setVisibility(GONE);
        }
    }

    public void loadFail() {
        loadFail(errorCode, null,null);
    }

    public void loadFail(String errorMsg,String btnText) {
        loadFail(errorCode, errorMsg,btnText);
    }
    public void loadFail(int errorCode, String errorMsg,String btnText) {
        this.errorCode = errorCode;
        if (contentView != null) {
            contentView.setVisibility(INVISIBLE);
        }
        LoadViewBuilder loadViewBuilder = getLoadViewBuilder(LoadStateType.LoadFail, requestCode);
        if(!TextUtils.isEmpty(errorMsg)) {
            loadViewBuilder.setMsgText(errorMsg);
        }
        if(!TextUtils.isEmpty(btnText)) {
            loadViewBuilder.setButtonText(btnText);
        }
        loadViewInterface.onStateChanged(loadViewBuilder,errorCode);
    }

    public void loadEmpty(int errorCode) {
        loadEmpty(errorCode,null );
    }
    public void loadEmpty(String msg) {
        loadEmpty(errorCode,msg );
    }
    public void loadEmpty(int errorCode,String msg) {
        this.errorCode = errorCode;
        if (contentView != null) {
            contentView.setVisibility(INVISIBLE);
        }
        LoadViewBuilder loadViewBuilder =getLoadViewBuilder(LoadStateType.LoadEmpty, requestCode);
        if(!TextUtils.isEmpty(msg)) {
            loadViewBuilder.setMsgText(msg);
        }
        loadViewInterface.onStateChanged(loadViewBuilder, errorCode);
    }

    OnLoadListener onLoadListener;
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public static interface OnLoadListenerInterface {
        void onLoadData();
        void onLoadSuccess();
        void onLoadFail();
        void onRetry(LoadLayout loadLayout, int code);
    }

    public static abstract class OnLoadListener implements OnLoadListenerInterface {
        @Override
        public void onLoadFail() {

        }

        @Override
        public void onRetry(LoadLayout loadLayout, int code) {
            loadLayout.loadData();
        }
    }

    public static enum LoadStateType {
        Loading,//加载中
        LoadSuccess,//加载成功
        LoadFail,//加载出错
        LoadEmpty,//加载结果为空
    }
    //加载中
    //加载成功 隐藏加载界面 显示内容页面
    //加载失败  显示重试按钮 不显示重试按钮
    List<LoadViewBuilder> viewBuilders;
    public void addLoadView(LoadViewBuilder viewBuilder) {
        removeLoadView(viewBuilder.getLoadStateType(), viewBuilder.getRequestCode());
        viewBuilders.add(viewBuilder);
    }

    public void removeLoadView(LoadStateType loadStateType, int code) {
        for (LoadViewBuilder viewBuilder : viewBuilders) {
            if (viewBuilder.getRequestCode() == code && viewBuilder.getLoadStateType() == loadStateType) {
                viewBuilders.remove(viewBuilder);
                return;
            }
        }
    }

    public LoadViewBuilder getLoadViewBuilder(LoadStateType loadStateType, int requestCode) {
        for (LoadViewBuilder viewBuilder : viewBuilders) {
            if (viewBuilder.getRequestCode() == requestCode && viewBuilder.getLoadStateType() == loadStateType) {
                return viewBuilder;
            }
        }

        LoadViewBuilder loadViewBuilder = new LoadViewBuilder(loadStateType,requestCode);
        if(loadStateType==LoadStateType.Loading){
            loadViewBuilder.setShowLoadingView(true);
        }else if(loadStateType==LoadStateType.LoadEmpty){
            loadViewBuilder.setMsgText("no data");
            loadViewBuilder.setShowEmptyImage(true);
        }else if(loadStateType==LoadStateType.LoadFail){
            loadViewBuilder.setButtonText(getContext().getResources().getString(R.string.retry));
            loadViewBuilder.setShowErrorImage(true);
            loadViewBuilder.setMsgText("Loading error");
        }
        addLoadView(loadViewBuilder);
        return loadViewBuilder;
    }

    public static class LoadViewBuilder {
        LoadStateType loadStateType;
        int requestCode;
        boolean showButton;
        boolean showTextView;
        boolean showImageView;
        boolean showLoadingView;
        boolean showEmptyImage;
        boolean showErrorImage;
        String buttonText;
        String msgText;

        public LoadViewBuilder(LoadStateType loadStateType, int requestCode) {
            this.loadStateType = loadStateType;
            this.requestCode = requestCode;
        }

        public LoadStateType getLoadStateType() {
            return loadStateType;
        }

        public void setLoadStateType(LoadStateType loadStateType) {
            this.loadStateType = loadStateType;
        }

        public boolean isShowEmptyImage() {
            return showEmptyImage;
        }

        public void setShowEmptyImage(boolean showEmptyImage) {
            this.showEmptyImage = showEmptyImage;
        }

        public boolean isShowErrorImage() {
            return showErrorImage;
        }

        public void setShowErrorImage(boolean showErrorImage) {
            this.showErrorImage = showErrorImage;
        }

        public boolean isShowLoadingView() {
            return showLoadingView;
        }

        public void setShowLoadingView(boolean showLoadingView) {
            this.showLoadingView = showLoadingView;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public boolean isShowButton() {
            return showButton;
        }

        public void setShowButton(boolean showButton) {
            this.showButton = showButton;
        }

        public boolean isShowTextView() {
            return showTextView;
        }

        public void setShowTextView(boolean showTextView) {
            this.showTextView = showTextView;
        }

        public boolean isShowImageView() {
            return showImageView;
        }

        public void setShowImageView(boolean showImageView) {
            this.showImageView = showImageView;
        }

        public String getButtonText() {
            return buttonText;
        }

        public void setButtonText(String buttonText) {
            showButton=true;
            this.buttonText = buttonText;
        }

        public String getMsgText() {
            return msgText;
        }

        public void setMsgText(String msgText) {
            showTextView= true;
            this.msgText = msgText;
        }
    }
}
