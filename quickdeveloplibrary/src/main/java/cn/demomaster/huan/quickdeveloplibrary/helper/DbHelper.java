package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author squirrel桓
 * @date 2018/11/19.
 * description：
 */
public class DbHelper {
    Context context;

    void create() {
        //创建StuDBHelper对象
        cn.demomaster.huan.quickdeveloplibrary.db.DbHelper dbHelper = new cn.demomaster.huan.quickdeveloplibrary.db.DbHelper(context, "stu_db", null, 1);
        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getReadableDatabase();
    }

    DbHelper() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.lingdududu.db/databases/stu.db", null);
        query(db);
    }

    private void query(SQLiteDatabase db) {
        //查询获得游标
        Cursor cursor = db.query("usertable", null, null, null, null, null, null);

        //判断游标是否为空
        if (cursor.moveToFirst()) {
            //遍历游标
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.move(i);
                //获得ID
                int id = cursor.getInt(0);
                //获得用户名
                String username = cursor.getString(1);
                //获得密码
                String password = cursor.getString(2);
                //输出用户信息 System.out.println(id+":"+sname+":"+snumber);
            }
        }
    }

}
