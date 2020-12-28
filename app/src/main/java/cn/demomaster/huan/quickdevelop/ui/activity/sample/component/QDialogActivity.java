package cn.demomaster.huan.quickdevelop.ui.activity.sample.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.GroundGlassUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.OnClickActionListener;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDInputDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDMulSheetDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.QDLogger;

@ActivityPager(name = "对话框", preViewClass = TextView.class, resType = ResType.Custome)
public class QDialogActivity extends BaseActivity {

    private int backgroundRadio = 20;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdialog);

        getActionBarTool().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getOptionsMenu().show();
                showSheetMenu();
            }
        });

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
                    case 0://简单提示框
                        showMessage();
                        break;
                    case 1://简单提示框(带标题)
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
                    case 8://菜单类型对话框
                        showMenuDialog();
                        break;
                    case 9://带 Checkbox 的消息确认框

                        break;
                    case 10://单选菜单类型对话框
                        showMulMenuDialog1();
                        break;
                    case 11://多选菜单类型对话框
                        showMulMenuDialog();
                        break;
                    case 12://多选菜单类型对话框(item 数量很多)

                        break;
                    case 13://带输入框的对话框

                        break;
                    case 14://高度适应键盘升降的对话框
                        showInputDialog();
                        break;
                }
            }
        });
        initOptionsMenu();
    }

    private void showInputDialog() {
        new QDInputDialog.Builder(mContext).setTitle("连接wifi")
                .setMessage("")
                .setHint("请输入密码")
                .setBackgroundRadius(backgroundRadio)
                .setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                .addAction("连接", new OnClickActionListener() {
                    @Override
                    public void onClick(Dialog dialog, Object tag) {
                        Toast.makeText(mContext, "input = " + tag, Toast.LENGTH_SHORT).show();
                        //连接返回editview的value
                    }
                }).addAction("取消").setGravity_foot(Gravity.RIGHT).create().show();
    }

    //初始化菜单
    private void initOptionsMenu() {
        List<OptionsMenu.Menu> menus = new ArrayList<>();
        String[] menuNames = {"设置圆角"};
        for (int i = 0; i < menuNames.length; i++) {
            OptionsMenu.Menu menu = new OptionsMenu.Menu();
            menu.setTitle(menuNames[i]);
            menu.setPosition(i);
            menus.add(menu);
        }
        getOptionsMenu().setMenus(menus);
        getOptionsMenu().setAlpha(.86f);
        getOptionsMenu().setMargin(2);
        getOptionsMenu().setAnchor(getActionBarTool().getRightView());
        getOptionsMenu().setOnMenuItemClicked(new OptionsMenu.OnMenuItemClicked() {
            @Override
            public void onItemClick(int position, View view) {
                switch (position) {
                    case 0:
                        showSheetMenu();
                        break;
                    case 1:
                        getPhotoHelper().selectPhotoFromGalleryAndCrop(new PhotoHelper.OnTakePhotoResult() {
                            @Override
                            public void onSuccess(Intent data, String path) {
                                /*setImageToView(data);*/
                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        });
                        break;
                    case 2:
                        ScreenShotUitl.shot((Activity) mContext);
                        break;
                }
            }
        });
    }

    private void showSheetMenu() {
        String[] menus = {"item1", "item2", "item3"};
        QDSheetDialog dialog = new QDSheetDialog.Builder(mContext).setData(menus).setGravity(Gravity.BOTTOM).setWidthLayoutType(ViewGroup.LayoutParams.MATCH_PARENT).create();
        dialog.show();
    }
    /*private void showMessagePositiveDialog() {
        DialogHelper.showDialog(this,"title","message");
    }
*/

    // ================================ 生成不同类型的对话框
    private void showMessage() {
        new QDDialog.Builder(mContext).setMessage("这是一个提示").setBackgroundRadius(backgroundRadio).create().show();
    }

    private void showMessage1() {
        new QDDialog.Builder(mContext).setTitle("标题")
                .setMessage("确定要发送吗？").setBackgroundRadius(backgroundRadio).create().show();
    }

    private void showMessageWithButton(int gravity) {
        QDDialog qdDialog = new QDDialog.Builder(mContext)
                .setMessage("确定要发送吗？")
                //.setMinHeight_body((int) getResources().getDimension(R.dimen.dp_100))
                //.setGravity_body(Gravity.CENTER).setText_size_body((int) getResources().getDimension(R.dimen.sp_10))
                //.setWidth((int) getResources().getDimension(R.dimen.dp_120))
                .setText_color_body(Color.BLUE)
                .addAction("确定")//.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setText_size_foot((int) getResources().getDimension(R.dimen.sp_6))
                //.setPadding_foot((int) getResources().getDimension(R.dimen.sp_6))
                //.setActionButtonPadding((int) getResources().getDimension(R.dimen.sp_6))
                .setText_color_foot(Color.GREEN)
                .setLineColor(Color.RED)
                .setBackgroundRadius(backgroundRadio)
                .setGravity_foot(gravity).create();

        GroundGlassUtil glassUtil = null;
        glassUtil = new GroundGlassUtil(qdDialog.getContext());
        qdDialog.getContentLinearView().setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                QDLogger.e("setLayoutAnimationListener onAnimationStart");

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                QDLogger.e("setLayoutAnimationListener onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                QDLogger.e("setLayoutAnimationListener onAnimationRepeat");

            }
        });
        qdDialog.getContentView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                QDLogger.e("onLayoutChange left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom);
            }
        });

        //glassUtil.setTargetView(qdDialog.getContentLinearView());
        //glassUtil.setBackgroundView(mListView, false);
        //glassUtil.setRadius(100);
       // glassUtil.invalidate();
        qdDialog.show();
        //glassUtil.setTargetView(qdDialog.getContentLinearView());
    }

    private void showMessageWithButton2(int gravity) {
        new QDDialog.Builder(mContext).setTitle("标题")
                .setMessage("确定要发送吗？")
                .setBackgroundRadius(backgroundRadio)
                .addAction("确定", new OnClickActionListener() {
                    @Override
                    public void onClick(Dialog dialog, Object tag) {
                        dialog.dismiss();
                    }
                }).addAction("取消").setGravity_foot(gravity).create().show();
    }

    private void showMenuDialog() {
        String[] menus = {"item1", "item2", "item3"};
        new QDSheetDialog.MenuBuilder(mContext).setData(menus).setOnDialogActionListener(new QDSheetDialog.OnDialogActionListener() {
            @Override
            public void onItemClick(QDSheetDialog dialog, int position, List<String> data) {
                dialog.dismiss();
                PopToastUtil.ShowToast(mContext, data.get(position));
            }
        }).create().show();
    }

    private void showMulMenuDialog1() {
        String[] menus = {"item1", "item2", "234"};
        new QDMulSheetDialog.MenuBuilder(mContext).setData(menus).setOnDialogActionListener(new QDMulSheetDialog.OnDialogActionListener() {
            @Override
            public void onItemClick(QDMulSheetDialog dialog, int position, List<String> data) {
                dialog.dismiss();
                PopToastUtil.ShowToast(mContext, data.get(position));
            }
        }).create().show();
    }

    private void showMulMenuDialog() {
        String[] menus = {"item1", "item2", "234", "6565", "656456", "56656", "8888", "item2", "item3", "item2", "item3", "item2", "item3", "item2", "item3", "item2", "234", "6565", "656456", "56656", "8888", "item2",};
        new QDMulSheetDialog.MenuBuilder(mContext).setData(menus).setOnDialogActionListener(new QDMulSheetDialog.OnDialogActionListener() {
            @Override
            public void onItemClick(QDMulSheetDialog dialog, int position, List<String> data) {
                dialog.dismiss();
                PopToastUtil.ShowToast(mContext, data.get(position));
            }
        }).create().show();
    }
}
