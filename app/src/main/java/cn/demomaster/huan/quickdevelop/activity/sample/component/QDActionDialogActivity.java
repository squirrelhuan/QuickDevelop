package cn.demomaster.huan.quickdevelop.activity.sample.component;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.EmoticonView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadStateType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionStateType;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;

@ActivityPager(name = "QDActionDialog",preViewClass = TextView.class,resType = ResType.Custome)
public class QDActionDialogActivity extends QDActivity {

    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdaction_dialog);
        getActionBarLayout().setHeaderBackgroundColor(Color.RED);
        getActionBarLayout().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getOptionsMenu().show();
                //showSheetMenu();
            }
        });
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
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);
        mListView.setAdapter(new ArrayAdapter<>(mContext, R.layout.simple_list_item, data));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QDActionDialog qdActionDialog =null;
                qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setMessage(listItems[position]).setMessageTextSize(24).setPadding(50).create();
                switch (position) {
                    case 0://Loading 类型提示框
                        qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionStateType.LOADING).setMessage(listItems[position]).setDelayMillis(-1).create();
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
            }
        });
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
        getOptionsMenu().setAnchor(getActionBarLayout().getRightView());
        getOptionsMenu().setOnMenuItemClicked(new OptionsMenu.OnMenuItemClicked() {
            @Override
            public void onItemClick(int position, View view) {
                switch (position) {
                    case 0:
                        showSheetMenu();
                        break;
                    case 1:
                        photoHelper.selectPhotoFromGalleryAndCrop(new PhotoHelper.OnTakePhotoResult() {
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
        String[] menus ={"item1","item2","item3"};
        new QDSheetDialog.Builder(mContext).setData(menus).create().show();
    }
}
