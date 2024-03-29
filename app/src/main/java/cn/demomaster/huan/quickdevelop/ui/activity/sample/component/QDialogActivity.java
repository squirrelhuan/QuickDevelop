package cn.demomaster.huan.quickdevelop.ui.activity.sample.component;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.GroundGlassUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.base.Gravity;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDInputDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDMulSheetDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QuickDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QuickShareDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.adapter.ShareAdapter;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


@ActivityPager(name = "对话框", preViewClass = TextView.class, resType = ResType.Resource,iconRes = R.mipmap.ic_dialog)
public class QDialogActivity extends QuickFragment {
    private int backgroundRadio = 20;
    private ListView mListView;

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_qdialog, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        getActionBarTool().setRightOnClickListener(v -> {
            //getOptionsMenu().show();
            showSheetMenu();
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
                "高度适应键盘升降的对话框",
                "分享",
                "带radiobutton的单选窗",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3"
        };
        /*List<String> data = new ArrayList<>();
        Collections.addAll(data, listItems);*/
        mListView.setAdapter(new ArrayAdapter<>(mContext, R.layout.simple_list_item, listItems));
        mListView.setOnItemClickListener((parent, view, position, id) -> {
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
                case 15://高度适应键盘升降的对话框
                    showShareDialog();
                    break;
                case 16://高度适应键盘升降的对话框
                    showShareDialog();
                    break;
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
                .addAction("连接", (dialog, view, tag) -> {
                    Toast.makeText(mContext, "input = " + tag, Toast.LENGTH_SHORT).show();
                    //连接返回editview的value
                }).addAction("取消").
                setGravity_foot(Gravity.RIGHT)
                .create()
                .show();
    }

    public OptionsMenu optionsMenu;
    //获取自定义菜单
    public OptionsMenu getOptionsMenu() {
        if (optionsMenu == null) {
            optionsMenu = new OptionsMenu(new OptionsMenu.Builder(mContext));
        }
        return optionsMenu;
    }

    private OptionsMenu.Builder optionsMenubuilder;

    //获取自定义菜单
    public OptionsMenu.Builder getOptionsMenuBuilder() {
        if (optionsMenubuilder == null) {
            optionsMenubuilder = new OptionsMenu.Builder(mContext);
        }
        return optionsMenubuilder;
    }
    public PhotoHelper photoHelper;
    public PhotoHelper getPhotoHelper() {
        if (photoHelper == null) {
            photoHelper = new PhotoHelper(mContext,null,mContext.getPackageName()+".fileprovider");
        }
        return photoHelper;
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
        getOptionsMenu().setOnMenuItemClicked((position, view) -> {
            switch (position) {
                case 0:
                    showSheetMenu();
                    break;
                case 1:
                    getPhotoHelper().selectPhotoFromGalleryAndCrop(null, new PhotoHelper.OnTakePhotoResult() {
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
                    //ScreenShotUitl.shot((Activity) mContext);
                    break;
            }
        });
    }

    private void showSheetMenu() {
        String[] menus = {"i1", "item2", "item3"};
        QDSheetDialog dialog = new QDSheetDialog.Builder(mContext)
                .setData(menus)
                .setGravity(Gravity.BOTTOM)
                .setWidthLayoutType(ViewGroup.LayoutParams.MATCH_PARENT)
                .create();
        dialog.show();
    }

    QuickShareDialog shareDialog;
    private void showShareDialog() {
        if (shareDialog == null) {
            ArrayList<ShareAdapter.ShareModel> spinners = new ArrayList<>();
            String[] menus = new String[]{"微信", "微博","朋友圈","QQ空间"};// {"item1", "item2", "item3"};
            for(int i=0;i<menus.length;i++){
                ShareAdapter.ShareModel shareModel = new ShareAdapter.ShareModel();
                shareModel.setName(""+menus[i]);
                shareModel.setImgResId(R.mipmap.meizi);
                spinners.add(shareModel);
            }
            //menus = spinners.toArray(menus);
            shareDialog = new QuickShareDialog.Builder(mContext)
                    .setContentViewLayoutID(R.layout.layout_dialog_share_default)
                    .setData(menus)
                    .setOrientation(LinearLayout.HORIZONTAL)
                    .setGravity(android.view.Gravity.BOTTOM)
                    .setWidthLayoutType(ViewGroup.LayoutParams.MATCH_PARENT)
                    .setBackgroundColor(Color.WHITE)
                    .setBackgroundRadius(DisplayUtil.dp2px(mContext, 10))
                    .create();
            shareDialog.setSheetAdapter(new ShareAdapter(mContext,spinners));
        }
        shareDialog.show();
    }


    QuickDialog radioButtonDialog;
    private void showRadioButtonDialog() {
        if (radioButtonDialog == null) {
            radioButtonDialog = new QuickDialog.Builder(mContext)
                    .setContentView(R.layout.radio_button_dialog_layout)
                    .bindView(R.id.tv_title,"确定要发送吗？")
                    .create();
            ArrayList<ShareAdapter.ShareModel> spinners = new ArrayList<>();
            String[] menus = new String[]{"微信", "微博","朋友圈","QQ空间"};// {"item1", "item2", "item3"};
            for(int i=0;i<menus.length;i++){
                ShareAdapter.ShareModel shareModel = new ShareAdapter.ShareModel();
                shareModel.setName(""+menus[i]);
                shareModel.setImgResId(R.mipmap.meizi);
                spinners.add(shareModel);
            }
            //menus = spinners.toArray(menus);
            shareDialog = new QuickShareDialog.Builder(mContext)
                    .setContentViewLayoutID(R.layout.layout_dialog_share_default)
                    .setData(menus)
                    .setOrientation(LinearLayout.HORIZONTAL)
                    .setGravity(android.view.Gravity.BOTTOM)
                    .setWidthLayoutType(ViewGroup.LayoutParams.MATCH_PARENT)
                    .setBackgroundColor(Color.WHITE)
                    .setBackgroundRadius(DisplayUtil.dp2px(mContext, 10))
                    .create();
            shareDialog.setSheetAdapter(new ShareAdapter(mContext,spinners));
        }
        shareDialog.show();
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

    private void showMessageWithButton(cn.demomaster.huan.quickdeveloplibrary.widget.base.Gravity gravity) {
        QuickDialog qdDialog = new QuickDialog.Builder(mContext)
                .setContentView(R.layout.quick_dialog_layout)
                .bindView(R.id.tv_title,"确定要发送吗？")
                //.setMinHeight_body((int) getResources().getDimension(R.dimen.dp_100))
                //.setGravity_body(Gravity.CENTER).setText_size_body((int) getResources().getDimension(R.dimen.sp_10))
                //.setWidth((int) getResources().getDimension(R.dimen.dp_120))
                //.setText_color_body(Color.BLUE)
                .bindView(R.id.tv_left,"取消")
                .bindView(R.id.tv_right,"确定")//.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                //.setText_size_foot((int) getResources().getDimension(R.dimen.sp_6))
                //.setPadding_foot((int) getResources().getDimension(R.dimen.sp_6))
                //.setActionButtonPadding((int) getResources().getDimension(R.dimen.sp_6))
                //.setText_color_foot(Color.GREEN)
                //.setLineColor(Color.RED)
                //.setBackgroundRadius(backgroundRadio)
                //.setGravity_foot(gravity)
                .create();

        GroundGlassUtil glassUtil = new GroundGlassUtil(qdDialog.getContext());
        /*qdDialog.getContentLinearView().setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                QDLogger.println("setLayoutAnimationListener onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                QDLogger.println("setLayoutAnimationListener onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                QDLogger.println("setLayoutAnimationListener onAnimationRepeat");
            }
        });*/
        //qdDialog.getContentView().addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> QDLogger.println("onLayoutChange left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom));

        //glassUtil.setTargetView(qdDialog.getContentLinearView());
        glassUtil.setBackgroundView(mListView);
        glassUtil.setRadius(100);
        glassUtil.invalidate();
        qdDialog.show();
        //glassUtil.setTargetView(qdDialog.getContentLinearView());
    }

    private void showMessageWithButton2(cn.demomaster.huan.quickdeveloplibrary.widget.base.Gravity gravity) {
        new QDDialog.Builder(mContext).setTitle("标题")
                .setMessage("确定要发送吗？")
                .setBackgroundRadius(backgroundRadio)
                .addAction("确定", (dialog, view, tag) -> dialog.dismiss()).addAction("取消").setGravity_foot(gravity).create().show();
    }

    private void showMenuDialog() {
        String[] menus = {"item1", "item2", "item3"};
        new QDSheetDialog.MenuBuilder(mContext)
                .setData(menus)
                .setOnDialogActionListener((dialog, position, data) -> {
                    dialog.dismiss();
                    QdToast.showToast(mContext, data.get(position));
                }).create().show();
    }

    private void showMulMenuDialog1() {
        String[] menus = {"item1", "item2", "234"};
        new QDMulSheetDialog.MenuBuilder(mContext).setData(menus).setOnDialogActionListener((dialog, position, data) -> {
            dialog.dismiss();
            QdToast.showToast(mContext, data.get(position));
        }).create().show();
    }

    private void showMulMenuDialog() {
        String[] menus = {"item1", "item2", "234", "6565", "656456", "56656", "8888", "item2", "item3", "item2", "item3", "item2", "item3", "item2", "item3", "item2", "234", "6565", "656456", "56656", "8888", "item2",};
        new QDMulSheetDialog.MenuBuilder(mContext)
                .setData(menus)
                .setOnDialogActionListener((dialog, position, data) -> {
                    dialog.dismiss();
                    QdToast.showToast(mContext, data.get(position));
                }).create().show();
    }

}
