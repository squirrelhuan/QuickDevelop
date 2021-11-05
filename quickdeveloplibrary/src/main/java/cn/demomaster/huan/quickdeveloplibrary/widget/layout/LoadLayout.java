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

import cn.demomaster.huan.quickdeveloplibrary.R;

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

    View loadView;
    View contentView;
    private void init(AttributeSet attrs) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        int loadViewLayoutId = 0;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            loadViewLayoutId = a.getInt(R.styleable.Loadlayout_loadlayout, loadViewLayoutId);
            a.recycle();
        }
        if (loadViewLayoutId == 0) {
            loadView = new EmptyLayout(getContext());
            resetRetryButton();
        } else {
            loadView = mInflater.inflate(loadViewLayoutId, this, false);
        }
        if (loadView != null) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(loadView, layoutParams);
        }
        //QDLogger.println("count = " + getChildCount());
    }

    public void resetRetryButton() {
        ((EmptyLayout) loadView).btn_retry.setOnClickListener(v -> {
            ((EmptyLayout) loadView).hideAll();
            ((EmptyLayout) loadView).showLodding();
            loadData();
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
        loadView = null;
        contentView = null;
        onLoadListener = null;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        //System.out.println("index = " + index + "," + child);
        if (child != null && child != loadView) {
            contentView = child;
        }
    }
    
    public View getLoadView() {
        return loadView;
    }
    
    public void loadData() {
        loadData(null);
    }
    
    public void loadData(String loadingMessage) {
        showLoadMessageView(loadingMessage);
        if (onLoadListener != null) {
            onLoadListener.loadData();
        }
    }
    
    public void showLoadMessageView() {
        showLoadMessageView(null);
    }

    public void showLoadMessageView(String loadingMessage) {
        if (contentView != null) {
            contentView.setVisibility(INVISIBLE);
        }
        if(loadView!=null) {
            loadView.setVisibility(VISIBLE);
            if (loadView instanceof EmptyLayout) {
                ((EmptyLayout) loadView).hideAll();
                ((EmptyLayout) loadView).showLodding();
                if (!TextUtils.isEmpty(loadingMessage)) {
                    ((EmptyLayout) loadView).showMessage(loadingMessage);
                }
            }
        }
    }

    public void loadSuccess() {
        if (contentView != null) {
            contentView.setVisibility(VISIBLE);
        }
        if(loadView!=null) {
            loadView.setVisibility(GONE);
            if (loadView instanceof EmptyLayout) {
                ((EmptyLayout) loadView).hideAll();
            }
        }
        if (onLoadListener != null) {
            onLoadListener.loadSuccess();
        }
    }

    public void setRetryText(String text){
        if (loadView!=null && loadView instanceof EmptyLayout) {
            ((EmptyLayout) loadView).setRetryText(text);
        }
    }

    public void loadFailWithRetry(String retryText, String msg,OnClickListener onClickListener) {
        setRetryText(retryText);
        ((EmptyLayout) loadView).btn_retry.setOnClickListener(onClickListener);
        loadFail(msg, null);
    }

    public void loadFail() {
        loadFail(null, null);
    }

    public void loadFail(String msg) {
        loadFail(msg, null);
    }

    public void loadFail(String title, String msg) {
        if (contentView != null) {
            contentView.setVisibility(INVISIBLE);
        }
        if (loadView!=null) {
            loadView.setVisibility(VISIBLE);
        }
        if (onLoadListener != null) {
            onLoadListener.loadFail();
        }
        if (loadView!=null && loadView instanceof EmptyLayout) {
            ((EmptyLayout) loadView).hideAll();
            if (!TextUtils.isEmpty(title)) {
                ((EmptyLayout) loadView).showTitle(title);
            }
            if (!TextUtils.isEmpty(msg)) {
                ((EmptyLayout) loadView).showMessage(msg);
            }
            ((EmptyLayout) loadView).showRetry();
        }
    }

    public void showError(String err) {
        
    }

    public void showEmpty(String msg) {
        if (contentView != null) {
            contentView.setVisibility(INVISIBLE);
        }
        if(loadView!=null) {
            if(loadView instanceof EmptyLayout) {
                ((EmptyLayout) loadView).hideAll();
                ((EmptyLayout) loadView).showMessage(msg);
            }
            loadView.setVisibility(VISIBLE);
        }
    }
    
    OnLoadListener onLoadListener;
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public interface OnLoadListener {
        void loadData();

        void loadSuccess();

        void loadFail();
    }
}
