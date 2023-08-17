package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import cn.demomaster.huan.quickdeveloplibrary.view.loading.EmoticonView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionStateType;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdrouter_library.actionbar.LoadStateType;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

@ActivityPager(name = "提示框",preViewClass = TextView.class,resType = ResType.Custome)
public class ActionDialogFragment extends QuickFragment {

    private ListView mListView;
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_qdaction_dialog, null);
        return mView;
    }

    @Override
    public String getTitle() {
        return "提示框";
    }

    @Override
    public void initView(View rootView) {
        initOptionsMenu();

        mListView = findViewById(R.id.listview);
        final String[] listItems = new String[]{
                "Loading 类型提示框",
                "成功提示类型提示框",
                "失败提示类型提示框",
                "信息提示类型提示框",
                "顶部图片类型提示框",
                "左侧图片类型提示框",
                "单独图片类型提示框",
                "单独文字类型提示框",
                "自定义内容提示框View",
                "自定义内容提示框LayoutId",
                "顶部View类型提示框",
                "左侧View类型提示框",
                "背景色提示框",
                "透明度提示框"
        };
       /* List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);*/
        mListView.setAdapter(new ArrayAdapter<>(mContext, R.layout.simple_list_item, listItems));
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            QDActionDialog qdActionDialog =null;
            qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setMessage(listItems[position]).setMessageTextSize(24).setPadding(50).create();
            switch (position) {
                case 0://Loading 类型提示框
                    qdActionDialog = new QDActionDialog
                            .Builder(mContext)
                            .setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc))
                            .setStateType(QDActionStateType.LOADING)
                            .setMessage(listItems[position])
                            .setDelayMillis(-1)
                            .create();
                    break;
                case 1://成功提示类型提示框
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionStateType.COMPLETE).setMessage(listItems[position]).setDelayMillis(2000).create();
                    break;
                case 2://失败提示类型提示框
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionStateType.ERROR).setMessage(listItems[position]).setDelayMillis(2000).create();
                    break;
                case 3://信息提示类型提示框
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionStateType.WARNING).setMessage(listItems[position]).setDelayMillis(2000).create();
                    break;
                case 4://顶部图片类型提示框
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setTopImage(R.mipmap.quickdevelop_ic_launcher).setMessage(listItems[position]).create();
                    break;
                case 5://左侧图片类型提示框
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setLeftImage(R.mipmap.quickdevelop_ic_launcher).setMessage(listItems[position]).create();
                    break;
                case 6:
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setTopImage(R.mipmap.quickdevelop_ic_launcher).create();
                    break;
                case 7:
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setMessage(listItems[position]).create();
                    break;
                case 8:
                    TextView textView = new TextView(mContext);
                    textView.setText(listItems[position]);
                    qdActionDialog = new QDActionDialog.Builder(mContext).setBackgroundRadius(50).setContentView(textView).setCancelable(true).create();
                    break;
                case 9:
                    //TextView textView = new TextView(mContext);
                    //textView.setText(listItems[position]);
                    qdActionDialog = new QDActionDialog.Builder(mContext).setBackgroundRadius(50).setContentViewLayout(R.layout.item_warp2).create();
                    EmoticonView emoticonView = qdActionDialog.getContentView().findViewById(R.id.ev_emtion);
                    emoticonView.setStateType(LoadStateType.COMPLETE);
                    break;
                case 10:
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setCancelable(true).setBackgroundRadius(50).setTopViewClass(LoadingCircleView.class).setMessage(listItems[position]).setDelayMillis(5000).create();
                    break;
                case 11:
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setCancelable(true).setBackgroundRadius(50).setLeftViewClass(LoadingCircleView.class).setMessage(listItems[position]).setDelayMillis(5000).create();
                    break;
                case 12:
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundColor(0x33ff0600).setBackgroundRadius(50).setTopViewClass(LoadingCircleView.class).setDelayMillis(5000).create();
                    break;
                case 13:
                    qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setDimAmount(0.5f).setLeftViewClass(LoadingCircleView.class).setMessage(listItems[position]).setDelayMillis(5000).create();
                    break;
            }
            qdActionDialog.show();
            mListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //qdActionDialog.dismiss();
                }
            }, 1500);
        });
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
                    getPhotoHelper().selectPhotoFromGalleryAndCrop(null,new PhotoHelper.OnTakePhotoResult() {
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
                    //ScreenShotUitl.shot(mContext);
                    break;
            }
        });
    }

    private void showSheetMenu() {
        String[] menus ={"item1","item2","item3"};
        new QDSheetDialog.Builder(mContext).setData(menus).create().show();
    }

}
