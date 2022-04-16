package cn.demomaster.huan.quickdeveloplibrary.ui.file;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

/**
 * 文件浏览
 */
public class FileExplorerFragment extends QuickFragment {
    @Override
    public View onGenerateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_explorer, null);
        return view;
    }

    RecyclerView recy_file;
    List<FileInfo> fileInfos;
    FileAdapter adapter;
    @Override
    public void initView(View rootView) {
        if (getArguments() != null&&getArguments().containsKey("finishWithActivity")) {
            boolean finishWithActivity = getArguments().getBoolean("finishWithActivity");
            setFinishWithActivity(finishWithActivity);
        }

        recy_file = findViewById(R.id.recy_file);
        recy_file.setLayoutManager(new LinearLayoutManager(getContext()));
        fileInfos = new ArrayList<>();
        adapter = new FileAdapter(mContext, fileInfos);
        adapter.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if(!fileInfos.get(position).file.isDirectory()) {
                String t = QDFileUtil.getFileType(mContext,fileInfos.get(position).file);
                QDLogger.println("type="+t);
                if(!TextUtils.isEmpty(t)&&t.equals("db")) {
                    openDataBase(fileInfos.get(position).file);
                }else {
                    bundle.putString("FILE_PATH_KEY", fileInfos.get(position).file.getAbsolutePath());
                    intent.putExtras(bundle);
                    startFragment(new TextDetailFragment(), android.R.id.content, intent);
                }
            }else {
                //bundle.putString("DIR_PATH_KEY", mContext.getExternalFilesDir("").getParentFile().getAbsolutePath());
                bundle.putString("DIR_PATH_KEY", fileInfos.get(position).file.getAbsolutePath());
                intent.putExtras(bundle);
                startFragment(new FileExplorerFragment(),android.R.id.content,intent);
            }
        });
        adapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showMenu(position);
                return false;
            }
        });
        recy_file.setAdapter(adapter);
        List<FileInfo> fileInfoList= initRootFileInfos(getContext());
        fileInfos.addAll(fileInfoList);
        adapter.notifyDataSetChanged();
    }

    QDSheetDialog qdSheetDialog;
    private void showMenu(int dataPosition) {
        String[] menus = {"删除"};
        qdSheetDialog = new QDSheetDialog.MenuBuilder(mContext)
                .setData(menus)
                .setOnDialogActionListener((dialog, position, data) -> {
                    FileInfo fileInfo = fileInfos.get(dataPosition);
                    QDFileUtil.delete(fileInfo.file.getAbsolutePath());
                    List<FileInfo> fileInfoList= initRootFileInfos(getContext());
                    fileInfos.clear();
                    fileInfos.addAll(fileInfoList);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }).create();
        qdSheetDialog.show();
    }

    private List<FileInfo> initRootFileInfos(Context context) {
        List<File> rootFiles = getRootFiles();
        if (rootFiles == null) {
            rootFiles = getRootFiles2();
        }
        if (rootFiles != null) {
            List<FileInfo> fileInfos = new ArrayList<>();
            for (File file : rootFiles) {
                fileInfos.add(new FileInfo(file));
            }
            return fileInfos;
        }
        return initDefaultRootFileInfos(context);
    }

    private List<File> getRootFiles() {
        String key = "DIR_PATH_KEY";
        if (getArguments() != null&&getArguments().containsKey(key)) {
            File dir = new File(getArguments().getString(key));
            if (dir.exists()) {
                setTitle(dir.getName());
                return Arrays.asList(dir.listFiles());
            }
        }
        return null;
    }
    private List<File> getRootFiles2() {
        String key = "DIR_PATHS_KEY";
        if (getArguments() != null&&getArguments().containsKey(key)) {
            ArrayList<String> paths = getArguments().getStringArrayList(key);
            List<File> fileList = new ArrayList<>();
            for(String str:paths){
                File file = new File(str);
                if (file.exists()) {
                    fileList.add(file);
                }
            }
            return fileList;
        }
        return null;
    }

    private List<FileInfo> initDefaultRootFileInfos(Context context) {
        List<FileInfo> fileInfos = new ArrayList<>();
        fileInfos.add(new FileInfo(context.getFilesDir().getParentFile()));
        fileInfos.add(new FileInfo(context.getExternalCacheDir()));
        fileInfos.add(new FileInfo(context.getExternalFilesDir(null)));
        return fileInfos;
    }

    private boolean isRootFile(Context context, File file) {
        if (file == null) {
            return false;
        }
        List<File> rootFiles = getRootFiles();
        if (rootFiles != null) {
            for (File rootFile : rootFiles) {
                return file.equals(rootFile);
            }
        }
        return file.equals(context.getExternalCacheDir())
                || file.equals(context.getExternalFilesDir(null))
                || file.equals(context.getFilesDir().getParentFile());
    }

    /**
     * 打开数据库文件
     * @param file
     */
    private void openDataBase(File file) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("DbFilePath",file.getAbsolutePath());
        intent.putExtras(bundle);
        startFragment(new DBTablesFragment(),android.R.id.content,intent);
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

    @Override
    public void onDestroy() {
        if(qdSheetDialog!=null){
            qdSheetDialog.dismiss();
        }
        super.onDestroy();
    }
}
