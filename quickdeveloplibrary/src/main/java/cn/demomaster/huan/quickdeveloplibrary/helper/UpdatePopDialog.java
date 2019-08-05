package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;

/**
 * @author squirrel桓
 * @date 2018/12/17.
 * description：
 */
public class UpdatePopDialog {

    private Context context;
    private String contentText;
    private String btnText;
    public TextView contentView;
    public TextView tv_update;
    private View.OnClickListener onCloseListener;

    public UpdatePopDialog(Context context, String contentText) {
        this.context = context;
        this.contentText = contentText;
        this.btnText = "";
        init();
    }

    public UpdatePopDialog(Context context, String contentText, String leftText) {
        this.context = context;
        this.contentText = contentText;
        this.btnText = leftText;
        init();
    }

    public CustomDialog customDialog;

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

        CustomDialog.Builder builder = new CustomDialog.Builder(context, R.layout.item_pop_dialog_update);

        customDialog = builder.setCanTouch(true).create();
        customDialog.setOnDismissListener(onDismissListener);
        View ccustomDialogView = customDialog.getContentView();
        contentView = ccustomDialogView.findViewById(R.id.tv_content);
        contentView.setText(contentText);
        tv_update = ccustomDialogView.findViewById(R.id.tv_update);
        //tv_update.setText(btnText);
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCloseListener != null) {
                    onCloseListener.onClick(v);
                }
                customDialog.dismiss();
            }
        });

    }

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        onDismissListener = listener;
        if (customDialog != null) {
            customDialog.setOnDismissListener(listener);
        }
    }

    public void setOnCloseListener(View.OnClickListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public void show() {
        //View root = getContentView(context);
        //showAtLocation(root,  Gravity.CENTER, 0,0);
        customDialog.show();
    }

    public void dismiss() {
        if (customDialog != null && customDialog.isShowing()) {
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
