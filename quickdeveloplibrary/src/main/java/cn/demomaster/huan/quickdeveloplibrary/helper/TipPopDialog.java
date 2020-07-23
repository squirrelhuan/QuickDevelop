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
public class TipPopDialog {

    private Context context;
    private String contentText;
    private String btnText;
    public TextView contentView;
    public ImageTextView btn_close;
    private View.OnClickListener onCloseListener;

    public TipPopDialog(Context context, String contentText) {
        this.context = context;
        this.contentText = contentText;
        this.btnText = "";
        init();
    }

    public TipPopDialog(Context context, String contentText, String leftText) {
        this.context = context;
        this.contentText = contentText;
        this.btnText = leftText;
        init();
    }

    public QDActionDialog customDialog;

    private void init() {
        /*View v = LayoutInflater.from(context).inflate(R.layout.item_pop_dialog_common, null, false);
        leftView = v.findViewById(R.id.btn_left);
        rightView = v.findViewById(R.id.btn_left);
        leftView.setOnClickListener(onClickListener_left);
        rightView.setOnClickListener(onClickListener_right);
        setContentView(v);

        setTouchable(false);
        setFocusable(false);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setClippingEnabled(false);*/

        QDActionDialog.Builder builder = new QDActionDialog.Builder(context).setContentViewLayout( R.layout.item_pop_dialog_tip);
        customDialog = builder.setCancelable(true).create();
        View ccustomDialogView = customDialog.getContentView();
        contentView = ccustomDialogView.findViewById(R.id.tv_content);
        contentView.setText(contentText);
        btn_close = ccustomDialogView.findViewById(R.id.btn_close);
        btn_close.setText(btnText);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                if (onCloseListener != null) {
                    onCloseListener.onClick(v);
                }
            }
        });

    }

    public void setOnCloseListener(View.OnClickListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public void show() {
        //View root = getContentView(context);
        //showAtLocation(root,  Gravity.CENTER, 0,0);
        customDialog.show();
    }

    public void dismiss(){
        if(customDialog!=null&& customDialog.isShowing()){
        customDialog.dismiss();
        }
    }
    public static View getContentView(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).findViewById(android.R.id.content);
        }
        return null;
    }
}
