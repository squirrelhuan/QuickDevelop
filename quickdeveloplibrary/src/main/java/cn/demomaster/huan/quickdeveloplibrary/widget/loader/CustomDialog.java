package cn.demomaster.huan.quickdeveloplibrary.widget.loader;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class CustomDialog extends Dialog {

    private View contentView;
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public View getContentView(){
        return contentView;
    };



    public static class Builder {
        private String message;
        private View contentView;
        private boolean canTouch;
        private LayoutInflater inflater;
        private CustomDialog dialog;
        private int showType;//显示模式

        public final static int Loading_Builer_Only_Image = 0;
        public final static int Loading_Builer_Image_And_Text = 1;

        public Builder(Context context,int layoutID) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new CustomDialog(context);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(layoutID, null);
            this.contentView = layout;
            //dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        public Builder(Context context,View view) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new CustomDialog(context);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.contentView = view;
        }


        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
        public Builder setCanTouch(Boolean canTouch){
            this.canTouch = canTouch;
            return this;
        }

        public CustomDialog create() {
            if (contentView == null) {//默认loading视图
                contentView = inflater.inflate(R.layout.layout_dialog_loading_default, null);
                if (message != null) {
                    ((TextView)contentView.findViewById(R.id.tv_message)).setText(message);
                    showType = 1;
                }
                switch (showType) {
                    case Loading_Builer_Only_Image://加载动画
                        break;
                    case Loading_Builer_Image_And_Text://图文并茂
                        break;
                }
            }
            if (!canTouch) {
                dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
                dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
            }
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(contentView);
            return dialog;
        }


    }

}
