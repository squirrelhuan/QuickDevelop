package cn.demomaster.huan.quickdeveloplibrary.ui.file;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

public class FileExplorerFragment extends QuickFragment {
    @Override
    public View onGenerateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_explorer, null);
        return view;
    }

    RecyclerView recy_file;
    List<FileInfo> fileInfos;
    @Override
    public void initView(View rootView) {
        recy_file = findViewById(R.id.recy_file);
        recy_file.setLayoutManager(new LinearLayoutManager(getContext()));
        fileInfos = new ArrayList<>();
        FileAdapter adapter = new FileAdapter(mContext, fileInfos);
        adapter.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if(!fileInfos.get(position).file.isDirectory()) {
                bundle.putString("FILE_PATH_KEY", fileInfos.get(position).file.getAbsolutePath());
                intent.putExtras(bundle);
                startFragment(new TextDetailFragment(), android.R.id.content, intent);
            }else {
                //bundle.putString("DIR_PATH_KEY", mContext.getExternalFilesDir("").getParentFile().getAbsolutePath());
                bundle.putString("DIR_PATH_KEY", fileInfos.get(position).file.getAbsolutePath());
                intent.putExtras(bundle);
                startFragment(new FileExplorerFragment(),android.R.id.content,intent);
            }
        });
        recy_file.setAdapter(adapter);
        List<FileInfo> fileInfoList= initRootFileInfos(getContext());
        fileInfos.addAll(fileInfoList);
        adapter.notifyDataSetChanged();
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
            if (dir != null && dir.exists()) {
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
                if (file != null && file.exists()) {
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
}
