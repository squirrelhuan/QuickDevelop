package cn.demomaster.huan.quickdeveloplibrary.widget.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
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
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnSingleClickListener;
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
    OnSingleClickListener onClickListener = new OnSingleClickListener() {
        @Override
        public void onClickEvent(View v) {
            if (onLoadListener != null) {
                //QDLogger.println("onClick errorCode="+errorCode);
                onLoadListener.onRetry(LoadLayout.this,requestCode);
            }
        }
    };
    private void init(AttributeSet attrs) {
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
    public void loadData() {
        loadData(requestCode);
    }
    public void loadData(int code) {
        if (contentView != null) {
            contentView.setVisibility(INVISIBLE);
        }
        inflateView(LoadStateType.Loading,code);
        if (onLoadListener != null) {
            onLoadListener.onLoadData(code);
        }
    }

    public void loadSuccess() {
        if (contentView != null) {
            contentView.setVisibility(VISIBLE);
        }
        if (loadView != null) {
            loadView.setVisibility(GONE);
        }
        if (onLoadListener != null) {
            onLoadListener.onLoadSuccess();
        }
    }

    public void loadFinish(int code,Object... args) {
        inflateView(LoadStateType.LoadComplete,code,args);
    }

    private void inflateView(LoadStateType loadStateType,int code, Object... args) {
        //QDLogger.println("inflateView code="+code);
        requestCode = code;
        ViewBuilder loadViewBuilder = null;
        if(onLoadListener!=null) {
            loadViewBuilder = onLoadListener.inflateView(getContext(),loadStateType,code, args);
        }else {
            if(loadStateType==LoadStateType.LoadComplete) {
                loadViewBuilder = new ViewBuilder();
                loadViewBuilder.setButtonText(getContext().getResources().getString(R.string.retry));
                loadViewBuilder.setShowImageView(true);
                loadViewBuilder.setMsgText("error");
            }else if(loadStateType==LoadStateType.Loading) {
                loadViewBuilder = new ViewBuilder();
                loadViewBuilder.setShowLoadingView(true);
                loadViewBuilder.setMsgText("Loading...");
            }
        }
        if(loadViewBuilder!=null){
            loadViewInterface.onStateChanged(loadViewBuilder,code);
        }
    }

    OnLoadListener onLoadListener;
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public interface OnLoadListenerInterface {
        void onLoadData(int type);
        void onLoadSuccess();
        void onLoadFail();
        void onRetry(LoadLayout loadLayout, int code);
        ViewBuilder inflateView(Context context,LoadStateType loadStateType,int code, Object[] args);
    }

    public static abstract class OnLoadListener implements OnLoadListenerInterface {
        @Override
        public void onLoadFail() {

        }

        @Override
        public void onRetry(LoadLayout loadLayout, int code) {
            loadLayout.loadData();
        }

        @Override
        public ViewBuilder inflateView(Context context,LoadStateType loadStateType,int code, Object[] args) {
            ViewBuilder loadViewBuilder = null;
            if(loadStateType==LoadStateType.LoadComplete) {
                loadViewBuilder = new ViewBuilder();
                loadViewBuilder.setButtonText(context.getResources().getString(R.string.retry));
                loadViewBuilder.setShowImageView(true);
                //loadViewBuilder.setMsgText("Loading error");
            }else if(loadStateType==LoadStateType.Loading) {
                loadViewBuilder = new ViewBuilder();
                loadViewBuilder.setShowLoadingView(true);
                //loadViewBuilder.setMsgText("Loading...");
            }
            return loadViewBuilder;
        }
    }

    public enum LoadStateType {
        Loading,//加载中
        LoadSuccess,//加载成功
        LoadComplete,//加载出错
        //LoadEmpty,//加载结果为空
    }
    //加载中
    //加载成功 隐藏加载界面 显示内容页面
    //加载失败  显示重试按钮 不显示重试按钮
    public static class ViewBuilder {
        boolean showButton;
        boolean showTextView;
        boolean showImageView;
        boolean showLoadingView;
        String buttonText;
        String msgText;

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

        public boolean isShowLoadingView() {
            return showLoadingView;
        }

        public void setShowLoadingView(boolean showLoadingView) {
            this.showLoadingView = showLoadingView;
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
            showTextView = true;
            this.msgText = msgText;
        }
    }
}
