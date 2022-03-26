package cn.demomaster.huan.quickdeveloplibrary.ui.file;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.SimpleRecycleViewAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.quickdatabaselibrary.QuickDbHelper;
import cn.demomaster.quickdatabaselibrary.listener.UpgradeInterface;
import cn.demomaster.quickdatabaselibrary.model.SqliteTable;

/**
 * 数据库文件浏览
 */
public class DBTablesFragment extends QuickFragment {
    @Override
    public View onGenerateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_explorer, null);
        return view;
    }

    RecyclerView recy_file;
    List<String> tableList;
    SimpleRecycleViewAdapter adapter;
    QuickDbHelper dbHelper;
    String dbPath;
    @Override
    public void initView(View rootView) {
        if (getArguments() != null&&getArguments().containsKey("finishWithActivity")) {
            boolean finishWithActivity = getArguments().getBoolean("finishWithActivity");
            setFinishWithActivity(finishWithActivity);
        }
        tableList = new ArrayList<>();
        if(getArguments()!=null&&getArguments().containsKey("DbFilePath")){
            dbPath = getArguments().getString("DbFilePath");
            dbHelper = new QuickDbHelper(getContext(), dbPath, dbPath, null, 10, (UpgradeInterface) getContext());
            //quick_db1.db 在data/data/下生成对应的db文件
            //quick_db2.db 在assets下的db文件
            List<SqliteTable> sqliteTableList = dbHelper.getTables();
            for(SqliteTable item:sqliteTableList){
                //QDLogger.println("表："+item.getName());
                tableList.add(item.getName());
            }
        }

        recy_file = findViewById(R.id.recy_file);
        recy_file.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SimpleRecycleViewAdapter(mContext, tableList);
        adapter.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("DbFilePath",dbPath);
            bundle.putString("DbTableFile",tableList.get(position));
            intent.putExtras(bundle);
            startFragment(new DBTableDetailFragment(), android.R.id.content, intent);
        });
        adapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showMenu(position);
                return false;
            }
        });
        recy_file.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    QDSheetDialog qdSheetDialog;
    private void showMenu(int dataPosition) {
        /*String[] menus = {"删除"};
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
        qdSheetDialog.show();*/
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
