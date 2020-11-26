package cn.demomaster.huan.quickdeveloplibrary.base.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.ActionButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.OnClickActionListener;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;

public abstract class QdDialogActivity extends Activity {

    private long id;
    public Object data;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null&&bundle.containsKey("QDDialogId")){
            id = bundle.getLong("QDDialogId");
            data = DialogActivityHelper.getDataById(id);
        }
        frameLayout = new FrameLayout(this);
        generateView(getLayoutInflater(),frameLayout);
        setContentView(frameLayout);
        StatusBarUtil.transparencyBar(new WeakReference<Activity>(this));
        getWindow().setBackgroundDrawable(new ColorDrawable(0x55000000));
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    public abstract void generateView(LayoutInflater layoutInflater, ViewGroup viewParent);

    @Override
    protected void onPause() {
        super.onPause();
        DialogActivityHelper.onDialogActivityDismiss(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ActivityDialogHelper.onDialogActivityDismiss(id);
    }
    
    public static class Builder extends QDDialog.Builder{
        
        public Builder(Context context) {
            super(context);
        }
        
        public Builder addButtonAction(String text, OnClickListener onClickListener) {
            //return super.addAction(text, onClickListener);
            ActionButton actionButton = new ActionButton();
            if (text != null) {
                actionButton.setText(text);
            }
            if (onClickListener != null) {
                actionButton.setOnClickListener(onClickListener);
            }
            this.actionButtons.add(actionButton);
            return this;
        }
        
    }
    
    public static abstract class OnClickListener implements OnClickActionListener{
        public abstract void onClick(Activity activity, Object tag);
        
        @Override
        public void onClick(Dialog dialog, Object tag) {
            
        }
    }
}