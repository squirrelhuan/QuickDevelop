package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.model.Member;
import cn.demomaster.huan.quickdevelop.model.User;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;
import cn.demomaster.quickdatabaselibrary.QuickDbHelper;
import cn.demomaster.quickdatabaselibrary.listener.UpgradeInterface;
import cn.demomaster.quickdatabaselibrary.view.TableView;

@ActivityPager(name = "数据库",preViewClass = TextView.class,resType = ResType.Resource,iconRes = R.mipmap.ic_database)
public class QuickDataActivity2 extends QuickActivity implements UpgradeInterface {
    TextView tv_console;
    QuickDbHelper dbHelper;
    TableView tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_data);

        tv_console = findViewById(R.id.tv_console);

        dbHelper = new QuickDbHelper(this, "quick_db.db", "quick_db.db", null, 10, this);
        //quick_db1.db 在data/data/下生成对应的db文件
        //quick_db2.db 在assets下的db文件

        dbHelper.getTables();

        //生成表
        creatTable();

        initTableView();
    }

    private void initTableView() {
        tableView = findViewById(R.id.tableView);
        String[] titles = new String[]{"id", "name", "age", "sex", "nickname", "header"};

        tableView.setTitles(titles);
        List<String[]> stringList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            String[] strings = new String[titles.length];
            for (int j = 0; j < strings.length; j++) {
                strings[j] = i + "-" + titles[j] + "";
            }
            stringList.add(strings);
        }
        tableView.setData(stringList);
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
        User user_t = new User();
        user_t.setId(3);
        User user2 = dbHelper.findOne(user_t);
        print("：" + (user2 == null ? "" : user2.toString()));
    }

    public void findList1(View view) {
        getAllRecord();
    }

    private void getAllRecord() {
        List<Member> members = new ArrayList<>();
        members = dbHelper.findArray("select * from member", Member.class);
        print("总记录数：" + (members == null ? "0" : members.size()));
        tableView.setData(members);
    }

    public void findList2(View view) {
        // Member member1 = new Member();
        // member1.setDescription("描述");
        List<Member> members = dbHelper.findArray("select * from member where description=\"描述\"", Member.class);
        print("结果数量：" + (members == null ? "0" : members.size()));
    }

    private void deleteSingle() {
        User user_d = new User();
        user_d.setId(2);
        ContentValues values = new ContentValues();
        values.put("id", 2);
        dbHelper.delete("user", values);
    }

    public void insert2(View view) {
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Member member1 = new Member();
            member1.setAge(i);
            member1.setName("M_" + i);
            member1.setDescription("描述");
            members.add(member1);
        }
        dbHelper.insertArray(members);
        getAllRecord();
    }

    public void insert1(View view) {
        Member member = new Member();
        member.setName("member single");
        member.setAge(new Random().nextInt());
        dbHelper.insert(member);
        print("单条数据插入:" + member.toString());
        getAllRecord();
       /* User user = new User();
        user.setName("张三");
        user.setAge(18);
        dbHelper.insert(user);*/
    }

    private void creatTable() {
        print("创建表");
        try {
            dbHelper.createTable(Member.class);
            dbHelper.createTable(User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}