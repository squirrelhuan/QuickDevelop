package cn.demomaster.huan.quickdevelop.activity.sample.component;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.DialogHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;

@ActivityPager(name = "QDialog",preViewClass = StateView.class,resType = ResType.Custome)
public class QDialogActivity extends BaseActivityParent {

    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdialog);


        mListView = findViewById(R.id.listview);
        String[] listItems = new String[]{
                "简单提示框",
                "简单提示框(带标题)",
                "简单提示框(1个button，居右)",
                "简单提示框(1个button，居中)",
                "简单提示框(1个button，居左)",
                "简单提示框(2个button，居右)",
                "简单提示框(2个button，居中)",
                "简单提示框(2个button，居左)",
                "菜单类型对话框",
                "带 Checkbox 的消息确认框",
                "单选菜单类型对话框",
                "多选菜单类型对话框",
                "多选菜单类型对话框(item 数量很多)",
                "带输入框的对话框",
                "高度适应键盘升降的对话框"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        mListView.setAdapter(new ArrayAdapter<>(mContext, R.layout.simple_list_item, data));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showMessage();
                        break;
                    case 1:
                        showMessage1();
                        break;
                    case 2:
                        showMessageWithButton(Gravity.RIGHT);
                        break;
                    case 3:
                        showMessageWithButton(Gravity.CENTER);
                        break;
                    case 4:
                        showMessageWithButton(Gravity.LEFT);
                        break;
                    case 5:
                        showMessageWithButton2(Gravity.RIGHT);
                        break;
                    case 6:
                        showMessageWithButton2(Gravity.CENTER);
                        break;
                    case 7:
                        showMessageWithButton2(Gravity.LEFT);
                        break;
                }
            }
        });
    }

    /*private void showMessagePositiveDialog() {
        DialogHelper.showDialog(this,"title","message");
    }
*/

    // ================================ 生成不同类型的对话框
    private void showMessage() {
        new QDDialog.Builder(mContext).setMessage("这是一个提示").create().show();
    }
    private void showMessage1() {
        new QDDialog.Builder(mContext).setTitle("标题")
                .setMessage("确定要发送吗？").create().show();
    }
    private void showMessageWithButton(int gravity) {
        new QDDialog.Builder(mContext).setTitle("标题")
                .setMessage("确定要发送吗？").addAction("确定").setGravity_foot(gravity).create().show();
    }

    private void showMessageWithButton2(int gravity) {
        new QDDialog.Builder(mContext).setTitle("标题")
                .setMessage("确定要发送吗？")
                .addAction("确定", new QDDialog.OnClickActionListener() {
            @Override
            public void onClick(QDDialog dialog) {
                dialog.dismiss();
            }
        }).addAction("取消").setGravity_foot(gravity).create().show();
    }


}