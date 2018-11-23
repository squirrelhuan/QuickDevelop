package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;

/**
 * @author squirrel桓
 * @date 2018/11/23.
 * description：
 */
public class DialogHelper {


    public static void showDialog(Context context,String title,String message){
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        CustomDialog customDialog = builder.create();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView titleTextView = new TextView(context);
        titleTextView.setText(title);
        linearLayout.addView(titleTextView);

        TextView contentTextView = new TextView(context);
        contentTextView.setText(message);
        linearLayout.addView(contentTextView);


        LinearLayout linearLayout_bottom = new LinearLayout(context);
        linearLayout_bottom.setOrientation(LinearLayout.HORIZONTAL);

        Button button1 = new Button(context);
        button1.setText("取消");
        Button button2 = new Button(context);
        button2.setText("确定");
        linearLayout_bottom.addView(button1);
        linearLayout_bottom.addView(button2);

        linearLayout.addView(linearLayout_bottom);


        customDialog.setContentView(linearLayout);

        customDialog.show();
    }

}
