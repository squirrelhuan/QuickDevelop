package cn.demomaster.huan.quickdeveloplibrary.ui.file;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.model.Member;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.SimpleRecycleViewAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdlogger_library.format.table.Table;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.quickdatabaselibrary.QuickDbHelper;
import cn.demomaster.quickdatabaselibrary.listener.UpgradeInterface;
import cn.demomaster.quickdatabaselibrary.model.SqliteTable;
import cn.demomaster.quickdatabaselibrary.model.TableColumn;
import cn.demomaster.quickdatabaselibrary.model.TableInfo;
import cn.demomaster.quickdatabaselibrary.model.TableItem;
import cn.demomaster.quickdatabaselibrary.view.TableView;

/**
 * 文件浏览
 */
public class DBTableDetailFragment extends QuickFragment implements UpgradeInterface {
    @Override
    public View onGenerateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_db_table_detail, null);
        return view;
    }

    TextView tv_console;
    QuickDbHelper dbHelper;
    TableView tableView;
    TextView btn_add01;
    TextView btn_add02;
    TextView btn_fin1;
    TextView btn_fin02;
    TextView btn_fin03;
    TextView btn_clear;
    TextView btn_del01;
    TextView btn_del02;
    TextView btn_mod01;

    @Override
    public void initView(View rootView) {
        btn_add01 =findViewById(R.id.btn_add01);
        btn_add01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert1(v);
            }
        });
        btn_add02 =findViewById(R.id.btn_add02);
        btn_add02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert2(v);
            }
        });
        btn_fin1 =findViewById(R.id.btn_fin1);
        btn_fin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOne1(v);
            }
        });
        btn_fin02 =findViewById(R.id.btn_fin02);
        btn_fin02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findList2(v);
            }
        });
        btn_fin03 =findViewById(R.id.btn_fin03);
        btn_fin03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findList2(v);
            }
        });
        btn_del01 =findViewById(R.id.btn_del01);
        btn_del01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete01(v);
            }
        });
        btn_del02 =findViewById(R.id.btn_del02);
        btn_del02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll(v);
            }
        });
        btn_mod01 =findViewById(R.id.btn_mod01);
        btn_mod01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify01(v);
            }
        });
        btn_clear =findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(v);
            }
        });

        tv_console = findViewById(R.id.tv_console);
        if (getArguments() != null && getArguments().containsKey("finishWithActivity")) {
            boolean finishWithActivity = getArguments().getBoolean("finishWithActivity");
            setFinishWithActivity(finishWithActivity);
        }
        if (getArguments() != null && getArguments().containsKey("DbFilePath")) {
            String dbPath = getArguments().getString("DbFilePath");
            String tableFileName = getArguments().getString("DbTableFile");
            setTitle(tableFileName);
           //quick_db1.db 在data/data/下生成对应的db文件
            //quick_db2.db 在assets下的db文件
            dbHelper = new QuickDbHelper(mContext, dbPath, dbPath, null, 10, this);
            
            //Member mm = dbHelper.findOne("select * from "+tableFileName, Member.class);
            //System.out.println("mm="+mm);
            //生成表
            findTable(tableFileName);
        }
    }
    TableInfo tableInfo;
    private void findTable(String tableName) {
        print("查询表结构");
        tableInfo = dbHelper.findTableByName(tableName);
        QDLogger.formatArray("字段集",tableInfo.getTableColumns());
        initTableView(tableInfo);
    }
    
    private void initTableView(TableInfo tableInfo) {
        tableView = findViewById(R.id.tableView);
        List<String> columnList = new ArrayList<>();
        for(TableColumn tableColumn:tableInfo.getTableColumns()){
            columnList.add(tableColumn.getName());
        }
        //String[] titles = (String[]) columns.toArray();// new String[]{"id", "name", "age", "sex", "nickname", "header"};
        String[] titles = new String[columnList.size()];
        columnList.toArray(titles);
        tableView.setTitles(titles);
        List<String[]> stringList = new ArrayList<>();
        /*for (int i = 0; i < 500; i++) {
            String[] strings = new String[titles.length];
            for (int j = 0; j < strings.length; j++) {
                strings[j] = i + "-" + titles[j] + "";
            }
            stringList.add(strings);
        }*/
        tableView.setData2(stringList);
    }

    public void findOne1(View view) {
        Member mm = dbHelper.findOne("select * from Member", Member.class);
        print("当前记录：" + (mm == null ? "无" : mm.toString()));
    }

    private void print(String s) {
        System.out.println(s);
        tv_console.append("\n" + s);
    }

    public void findOne2() {
        /*User user_t = new User();
        user_t.setId(3);
        User user2 = dbHelper.findOne(user_t);
        print("：" + (user2 == null ? "" : user2.toString()));*/
    }

    public void findList1(View view) {
        getAllRecord();
    }

    private void getAllRecord() {
        List<TableItem>  tableItemList = dbHelper.findArray(tableInfo.getTableName(),"select * from "+tableInfo.getTableName());
        print("总记录数：" + (tableItemList == null ? "0" : tableItemList.size()));
        tableView.setData(tableItemList);
    }

    public void findList2(View view) {
        List<TableItem>  tableItemList = dbHelper.findArray(tableInfo.getTableName(),"select * from "+tableInfo.getTableName());
        print("结果数量：" + (tableItemList == null ? "0" : tableItemList.size()));
        QDLogger.formatArray("结果集",tableItemList);
        tableView.setData3(tableItemList);
    }

    private void deleteSingle() {
       /* User user_d = new User();
        user_d.setId(2);
        ContentValues values = new ContentValues();
        values.put("id",2);
        dbHelper.delete("user",values);*/
    }

    public void insert2(View view) {
        List<TableItem> tableItemList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            TableItem member1 = new TableItem();
            //member1.set();
            tableItemList.add(member1);
        }
        dbHelper.insertArray(tableItemList);
        getAllRecord();
    }

    public void insert1(View view) {
        /*Member member = new Member();
        member.setName("member single");
        member.setAge(new Random().nextInt());*/

        List<TableColumn> tableColumnList = new ArrayList<>();
        for(TableColumn tableColumn: tableInfo.getTableColumns()){
            tableColumn.setValueObj("1");
            tableColumnList.add(tableColumn);
        }
       boolean b = dbHelper.insert(tableInfo.getTableName(),tableColumnList);
        print("单条数据插入:" + b);
        getAllRecord();
       /* User user = new User();
        user.setName("张三");
        user.setAge(18);
        dbHelper.insert(user);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void clear(View view) {
        tv_console.setText("");
    }

    public void delete01(View view) {
        List<Member> members = new ArrayList<>();
        try {
            members = dbHelper.findArray("select * from member", Member.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (members == null || members.size() < 1) {
            return;
        }
        Member member = members.get(0);
        try {
            ContentValues values = new ContentValues();
            values.put("id", member.getId());
            dbHelper.delete("MEMBER", values);
            //dbHelper.delete(member);
            print("删除记录" + member.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getAllRecord();
    }

    public void deleteAll(View view) {
        dbHelper.deleteAll(Member.class);
        print("删除全部记录");
        getAllRecord();
    }

    public void modify01(View view) {
        List<Member> members = new ArrayList<>();
        try {
            members = dbHelper.findArray("select * from member", Member.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (members == null || members.size() < 1) {
            return;
        }
        Member member = members.get(0);
        member.setName("sb");
        member.setAge(18);
        member.setDescription("小青蛙，呱呱呱");
        dbHelper.modify(member);
        long id = dbHelper.getLastIndex();
        print("修改完成id=" + id);
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
