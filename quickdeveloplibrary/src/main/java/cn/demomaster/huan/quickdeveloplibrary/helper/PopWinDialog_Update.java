package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;

/**
 * @author squirrel桓
 * @date 2018/12/17.
 * description：
 */
public class PopWinDialog_Update {

    private static Context mContext;
    private String contentText;
    private TextView contentView;
    private ImageTextView btn_close;
    private String btnText;
    private View.OnClickListener onClickListener;
    private static boolean canTouch;


    private static QDActionDialog customDialog;
    private static PopWinDialog_Update instance;

    public static PopWinDialog_Update getInstance(Context context) {
        instance = new PopWinDialog_Update(context);
        return instance;
    }

    PopWinDialog_Update(Context context) {
        customDialog = new QDActionDialog.Builder(context).setContentViewLayout(R.layout.item_pop_dialog_update).setCancelable(canTouch).create();
        init();
    }

    private void init() {
        View ccustomDialogView = customDialog.getContentView();
        contentView = ccustomDialogView.findViewById(R.id.tv_content);
        contentView.setText(contentText);
        btn_close = ccustomDialogView.findViewById(R.id.btn_close);
        btn_close.setText(btnText);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
        contentView.setText(contentText);
    }

    public void setBtn_text(String btn_text) {
        this.btnText = btn_text;
        btn_close.setText(btn_text);
    }

    public void setOnClickListener(View.OnClickListener onClickListener_ok) {
        this.onClickListener = onClickListener_ok;
    }

    public void show() {
        //View root = getContentView(context);
        //showAtLocation(root,  Gravity.CENTER, 0,0);
        if (customDialog != null&&!customDialog.isShowing()) {
            customDialog.show();
        }
    }

    public static View getContentView(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).findViewById(android.R.id.content);
        }
        return null;
    }

    public void dismiss() {
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (customDialog != null) {
            return customDialog.isShowing();
        }
        return false;
    }
}
