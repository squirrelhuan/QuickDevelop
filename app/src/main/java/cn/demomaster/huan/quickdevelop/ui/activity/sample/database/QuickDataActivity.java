package cn.demomaster.huan.quickdevelop.ui.activity.sample.database;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.ui.file.FileAdapter;
import cn.demomaster.huan.quickdeveloplibrary.ui.file.FileExplorerFragment;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;
import cn.demomaster.quickdatabaselibrary.listener.UpgradeInterface;

@ActivityPager(name = "数据库",preViewClass = TextView.class,resType = ResType.Resource,iconRes = R.mipmap.ic_database)
public class QuickDataActivity extends QuickActivity implements UpgradeInterface {
    RecyclerView recyclerView;
    FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_data);

        //生成表
        String p = "abc";
        File file = mContext.getDatabasePath(p);
        String path = file.getParentFile().getAbsolutePath();
        QDLogger.println("getDatabasePath="+path);
        /*File file1 = new File(path);
        List<File> fileList = Arrays.asList(file1.listFiles());
        List<FileInfo> fileInfos = new ArrayList<>();
        for (File file2 : fileList) {
            fileInfos.add(new FileInfo(file2));
        }*/

        /*recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new FileAdapter(mContext, fileInfos);
        adapter.setOnItemClickListener((parent, view, position, id) -> {
            String t = QDFileUtil.getFileType(mContext,fileInfos.get(position).file);
            QDLogger.println("type="+t);
            if(!TextUtils.isEmpty(t)&&t.equals("db")) {
                openDataBase(fileInfos.get(position).file);
            }
        });
        adapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //showMenu(position);
                return false;
            }
        });
        recyclerView.setAdapter(adapter);*/
        //adapter.notifyDataSetChanged();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("finishWithActivity", true);
        bundle.putString("DIR_PATH_KEY", path);
        intent.putExtras(bundle);
        startFragment(new FileExplorerFragment(), android.R.id.content, intent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}