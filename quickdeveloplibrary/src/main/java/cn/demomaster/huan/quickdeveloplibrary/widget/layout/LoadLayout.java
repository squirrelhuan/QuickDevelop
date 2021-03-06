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
import cn.demomaster.qdlogger_library.QDLogger;

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
        if(loadViewLayoutId==0){
            loadView = new EmptyLayout(getContext());
            ((EmptyLayout)loadView).btn_retry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EmptyLayout)loadView).hideAll();
                    ((EmptyLayout)loadView).showLodding();
                    loadData();
                }
            });
        }else {
            loadView = mInflater.inflate(loadViewLayoutId, this, false);
        }
        if(loadView!=null) {
            loadView.setTag(viewTag);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(loadView, layoutParams);
        }
        QDLogger.println("count = "+getChildCount());
    }
    String viewTag = "QdLoadLayout";
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        System.out.println("index = "+index+","+child);
        if(child!=null&&child!=loadView){
            contentView = child;
        }
    }

    public View getLoadView() {
        return loadView;
    }
    public void loadData(){
        loadData(null);
    }

    public void loadData(String loadingMessage){
        showLoadMessageView(loadingMessage);
        if(onLoadListener!=null){
            onLoadListener.loadData();
        }
    }

    public void showLoadMessageView() {
        showLoadMessageView(null);
    }
    public void showLoadMessageView(String loadingMessage) {
        if(contentView!=null) {
            contentView.setVisibility(INVISIBLE);
        }
        loadView.setVisibility(VISIBLE);
        if(loadView instanceof EmptyLayout){
            ((EmptyLayout)loadView).hideAll();
            ((EmptyLayout)loadView).showLodding();
            if(!TextUtils.isEmpty(loadingMessage)){
                ((EmptyLayout)loadView).showMessage(loadingMessage);
            }
        }
    }

    public void loadSuccess() {
        if(contentView!=null) {
            contentView.setVisibility(VISIBLE);
        }
        loadView.setVisibility(GONE);
        if(onLoadListener!=null){
            onLoadListener.loadSuccess();
        }
        if(loadView instanceof EmptyLayout){
            ((EmptyLayout)loadView).hideAll();
        }
    }
    public void loadFail(){
        loadFail(null,null);
    }
    public void loadFail(String msg){
        loadFail(msg,null);
    }
    public void loadFail(String title,String msg) {
        if(contentView!=null) {
            contentView.setVisibility(INVISIBLE);
        }
        loadView.setVisibility(VISIBLE);
        if(onLoadListener!=null){
            onLoadListener.loadFail();
        }
        if(loadView instanceof EmptyLayout){
            ((EmptyLayout)loadView).hideAll();
            if(!TextUtils.isEmpty(title)){
                ((EmptyLayout) loadView).showTitle(title);
            }
            if(!TextUtils.isEmpty(msg)){
                ((EmptyLayout)loadView).showMessage(msg);
            }
            ((EmptyLayout)loadView).showRetry();
        }
    }
    OnLoadListener onLoadListener;
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public static interface OnLoadListener{
        void loadData();
        void loadSuccess();
        void loadFail();
    }
}
