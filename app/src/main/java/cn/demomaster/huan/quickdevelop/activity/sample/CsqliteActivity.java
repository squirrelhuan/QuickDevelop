package cn.demomaster.huan.quickdevelop.activity.sample;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.demomaster.huan.quickdevelop.Application;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;

public class CsqliteActivity extends BaseActivityParent {


    /** Called when the activity is first created. */
//声明各个按钮
    private Button createBtn;
    private Button insertBtn;
    private Button updateBtn;
    private Button queryBtn;
    private Button deleteBtn;
    private Button ModifyBtn;
   /* private CBHelper dbHelper;
    private SQLiteDatabase db;*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csqlite);

//调用creatView方法
        creatView();
//setListener方法
        setListener();
    }

    //通过findViewById获得Button对象的方法
    private void creatView(){
        createBtn = (Button)findViewById(R.id.createDatabase);
        updateBtn = (Button)findViewById(R.id.updateDatabase);
        insertBtn = (Button)findViewById(R.id.insert);
        ModifyBtn = (Button)findViewById(R.id.update);
        queryBtn = (Button)findViewById(R.id.query);
        deleteBtn = (Button)findViewById(R.id.delete);

        getActionBarLayoutOld().setTitle("数据库操作");
    }

    //为按钮注册监听的方法
    private void setListener(){
        createBtn.setOnClickListener(new CreateListener());
        updateBtn.setOnClickListener(new UpdateListener());
        insertBtn.setOnClickListener(new InsertListener());
        ModifyBtn.setOnClickListener(new ModifyListener());
        queryBtn.setOnClickListener(new QueryListener());
        deleteBtn.setOnClickListener(new DeleteListener());
    }

    //创建数据库的方法
    class CreateListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            //Application.instance.dbHelper = new CBHelper(mContext,"yidao.db",null,1);
            //得到一个可读的SQLiteDatabase对象
            //Application.instance.db =dbHelper.getReadableDatabase();
        }
    }

    //更新数据库的方法
    class UpdateListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // 数据库版本的更新,由原来的1变为2
            //CBHelper dbHelper = new CBHelper(mContext,"yidao.db",null,2);
            //SQLiteDatabase db =dbHelper.getReadableDatabase();


            PopToastUtil.ShowToast(mContext,"更新未实现");
        }
    }

    //插入数据的方法
    class InsertListener implements OnClickListener{

        @Override
        public void onClick(View v) {

           // CBHelper dbHelper = new CBHelper(mContext,"yidao.db",null,1);
            //得到一个可写的数据库
            //db =dbHelper.getWritableDatabase();

            //生成ContentValues对象 //key:列名，value:想插入的值
            ContentValues cv = new ContentValues();
            //往ContentValues对象存放数据，键-值对模式
            cv.put("id", 2);
            cv.put("name", "xiaoming");
            cv.put("code", 21);
            //调用insert方法，将数据插入数据库
           // Application.instance.db.insert("inner_department_category", null, cv);
            //关闭数据库
            //db.close();

            PopToastUtil.ShowToastBottom(mContext,"插入数据的方法");
        }
    }

    //查询数据的方法
    class QueryListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            //CBHelper dbHelper = new CBHelper(mContext,"yidao",null,1);
            //得到一个可写的数据库
            //db =dbHelper.getReadableDatabase();
            //参数1：表名
            //参数2：要想显示的列
            //参数3：where子句
            //参数4：where子句对应的条件值
            //参数5：分组方式
            //参数6：having条件
            //参数7：排序方式
            Cursor cursor = Application.getInstance().db.query("inner_department_category", new String[]{"id","name","code"}, "id=?", new String[]{"1"}, null, null, null);
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String code = cursor.getString(cursor.getColumnIndex("code"));
               // Log.i(TAG, "query------->" + "name："+name+" "+",code："+code);
            }
            //关闭数据库
            //db.close();
            PopToastUtil.ShowToastCenter(mContext,"插入数据的方法");
        }
    }

    //修改数据的方法
    class ModifyListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            //CBHelper dbHelper = new CBHelper(mContext,"stu_db",null,1);
            //得到一个可写的数据库
            //db =dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("sage", "23");
            //where 子句 "?"是占位符号，对应后面的"1",
            String whereClause="id=?";
            String [] whereArgs = {String.valueOf(1)};
            //参数1 是要更新的表名
            //参数2 是一个ContentValeus对象
            //参数3 是where子句
            //Application.instance.db.update("inner_department_category", cv, whereClause, whereArgs);
        }
    }

    //删除数据的方法
    class DeleteListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            //CBHelper dbHelper = new CBHelper(mContext,"stu_db",null,1);
            //得到一个可写的数据库
            //db =dbHelper.getReadableDatabase();
            String whereClauses = "id=?";
            String [] whereArgs = {String.valueOf(2)};
            //调用delete方法，删除数据
            Application.getInstance().db.delete("inner_department_category", whereClauses, whereArgs);
        }
    }
}
