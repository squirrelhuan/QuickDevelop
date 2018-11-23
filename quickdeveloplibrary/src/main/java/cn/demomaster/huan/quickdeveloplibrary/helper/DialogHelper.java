package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;

/**
 * @author squirrel桓
 * @date 2018/11/23.
 * description：
 */
public class DialogHelper {


    public static void showDialog(Context context, String title, String message/*, DialogInterface.OnClickListener onClickListener*/){
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        CustomDialog customDialog = builder.setCanTouch(true).create();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent_light_cc));
        linearLayout.setPadding(DisplayUtil.dp2px(context,10),DisplayUtil.dp2px(context,10),DisplayUtil.dp2px(context,10),DisplayUtil.dp2px(context,10));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView titleTextView = new TextView(context);
        titleTextView.setText(title);
        titleTextView.setTextSize(16);
        linearLayout.addView(titleTextView);

        TextView contentTextView = new TextView(context);
        titleTextView.setTextSize(16);
        contentTextView.setText(message);
        linearLayout.addView(contentTextView);


        LinearLayout linearLayout_bottom = new LinearLayout(context);
        linearLayout_bottom.setOrientation(LinearLayout.HORIZONTAL);

        Button button1 = new Button(context);
        button1.setText("取消");
        Button button2 = new Button(context);
        button2.setText("确定");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onClickListener.onClick();
            }
        });
        linearLayout_bottom.addView(button1);
        linearLayout_bottom.addView(button2);

        linearLayout.addView(linearLayout_bottom);


        customDialog.setContentView(linearLayout);

        customDialog.show();
    }

    //public interface on

}
