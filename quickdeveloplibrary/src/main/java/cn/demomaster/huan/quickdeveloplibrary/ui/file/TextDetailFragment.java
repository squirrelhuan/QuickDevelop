package cn.demomaster.huan.quickdeveloplibrary.ui.file;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.github.chrisbanes.photoview.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.util.ClipboardUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.QDScrollTextView;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

public class TextDetailFragment extends QuickFragment {
    @Override
    public View onGenerateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_text_detail, null);
        return view;
    }
    QDScrollTextView qdScrollTextView;
    String filePath;
    int dataType =0;//0根据路径记载文件，1根据传递来的data显示
    String dataStr;
    @Override
    public void initView(View rootView) {
        if (getArguments() != null&&getArguments().containsKey("finishWithActivity")) {
            boolean finishWithActivity = getArguments().getBoolean("finishWithActivity");
            setFinishWithActivity(finishWithActivity);
        }
        qdScrollTextView = findViewById(R.id.qdScrollTextView);
        if(getArguments().containsKey("FILE_PATH_KEY")) {
            filePath = getArguments().getString("FILE_PATH_KEY");
            if(!TextUtils.isEmpty(filePath)) {
                readFile(new File(filePath));
                qdScrollTextView.loadTextFile(filePath);
                dataType = 0;
            }
        }

        if(getArguments().containsKey("Data")) {
            dataStr = getArguments().getString("Data");
            if(!TextUtils.isEmpty(dataStr)) {
                qdScrollTextView.setText(dataStr);
                dataType = 1;
            }
        }

        getActionBarTool().setRightOnClickListener(v -> optionsMenu.show(v));
        initOptionsMenu();
    }

    OptionsMenu optionsMenu;
    private OptionsMenu.Builder optionsMenubuilder;
    private void initOptionsMenu() {
        String[] menuNames = {"导出"};
        if(dataType==1){
            menuNames = new String[]{"copy"};
        }
        List<OptionsMenu.Menu> menus = new ArrayList<>();
        for (int i = 0; i < menuNames.length; i++) {
            OptionsMenu.Menu menu = new OptionsMenu.Menu();
            menu.setTitle(menuNames[i]);
            menu.setPosition(i);
            //menu.setIconId(R.mipmap.quickdevelop_ic_launcher);
            //menu.setIconPadding(30);
            //menu.setIconWidth(80);
            menus.add(menu);
        }
        optionsMenubuilder = new OptionsMenu.Builder(mContext);
        optionsMenubuilder.setMenus(menus)
                .setAlpha(.6f)
                .setUsePadding(true)
                .setBackgroundColor(Color.WHITE)
                .setBackgroundRadius(20)
                .setTextColor(Color.BLACK)
                .setTextSize(16)
                .setPadding(0)
                .setWithArrow(true)
                .setArrowHeight(30)
                .setArrowWidth(30)
                .setGravity(GuiderView.Gravity.BOTTOM)
                .setDividerColor(getResources().getColor(R.color.transparent))
                .setAnchor(getActionBarTool().getRightView());
        optionsMenubuilder.setOnMenuItemClicked((position, view) -> {
            switch (position) {
                case 0:
                    optionsMenu.dismiss();
                    if(dataType==0){
                        shareFile(mContext, filePath);
                    }else if(dataType==1){
                        ClipboardUtil.setClip(mContext,dataStr);
                    }
                    break;
            }
        });
        optionsMenu = optionsMenubuilder.creat();
        optionsMenu.setMargin(80);
        optionsMenu.setUsePadding(true);
        optionsMenu.setBackgroundRadiu(20);
        optionsMenu.setBackgroundColor(Color.WHITE);
        optionsMenu.setAnchor(getActionBarTool().getRightView());
    }

    public static void shareFile(Context context, String  fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider",file);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                share.putExtra(Intent.EXTRA_STREAM, contentUri);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }
            share.setType("application/vnd.ms-excel");//此处可发送多种文件
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "分享文件"));
        } else {
            QdToast.showToast(context, "分享文件不存在");
        }
    }

    FileReadTask task;
    private void readFile(File file) {
        if (file == null) {
            return;
        }
        setTitle(file.getName());
        //task = new FileReadTask(this);
        //showLoading();
        //task.execute(file);
    }
    static int maxLines = 100;
    private static class FileReadTask extends AsyncTask<File, String, Void> {
        private WeakReference<TextDetailFragment> mReference;
        boolean flag = true;
        boolean end = false;
        boolean firstLoad = true;

        public FileReadTask(TextDetailFragment fragment) {
            mReference = new WeakReference<>(fragment);
        }
        StringBuffer stringBuffer;
        @Override
        protected Void doInBackground(File... files) {
            stringBuffer = new StringBuffer();
            try {
                FileReader fileReader = new FileReader(files[0]);
                BufferedReader br = new BufferedReader(fileReader);
                String textLine;
                W:while (flag&&(textLine = br.readLine()) != null) {
                    stringBuffer.append(textLine);
                    if (stringBuffer.length()>1024*20&&firstLoad) {
                        publishProgress(stringBuffer.toString());
                        stringBuffer.delete(0, stringBuffer.length());
                    }
                }
                br.close();
                fileReader.close();
            } catch (IOException e) {
                QDLogger.e(e);
            }finally {
                onProgressUpdate(stringBuffer.toString());
                stringBuffer.delete(0, stringBuffer.length());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (flag&&mReference.get() != null &&values[0].length()>1024*20) {
                //QDLogger.println("onProgressUpdate:"+values[0]);
                //mReference.get().tv_content.append(values[0]);
                firstLoad = false;
               // mReference.get().mContentAdapter.append(values[0]);
            }
            if(end){
               // mReference.get().tv_content.append(values[0]);
            }
        }
    }

    @Override
    public void finish() {
        if(task!=null){
            task.flag = false;
        }
        super.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.e(getClass().getName() + "-onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {//默认只处理回退事件
            if (!isRootFragment()) {
                //getActivity().onBackPressed();
                finish();
            } else {//已经是根fragment了
                QDLogger.e(getClass().getName() + "-已经是根fragment了");
                //onKeyDownRootFragment(keyCode,event);
                finish();
            }
            return true;//当返回true时，表示已经完整地处理了这个事件，并不希望其他的回调方法再次进行处理，而当返回false时，表示并没有完全处理完该事件，更希望其他回调方法继续对其进行处理
        } else {//其他事件自行处理
            return false;
        }
    }

}
