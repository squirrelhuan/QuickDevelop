package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.Application;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.quicksticker_annotations.BindView;
import cn.demomaster.quicksticker_annotations.QuickStickerBinder;
@ActivityPager(name = "数据库",preViewClass = TextView.class,resType = ResType.Resource)
public class CsqliteActivity extends BaseActivity {


    /** Called when the activity is first created. */
//声明各个按钮
    @BindView(R.id.createDatabase)
    Button createBtn;
    @BindView(R.id.insert)
    public Button insertBtn;
    @BindView(R.id.updateDatabase)
    public Button updateBtn;
    @BindView(R.id.query)
    public Button queryBtn;
    @BindView(R.id.delete)
    public Button deleteBtn;
    @BindView(R.id.update)
    public Button ModifyBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csqlite);
        QuickStickerBinder.getInstance().bind(this);
        setListener();
    }

    //为按钮注册监听的方法
    private void setListener(){
        //createBtn.setOnClickListener();
        updateBtn.setOnClickListener(v -> {
            // 数据库版本的更新,由原来的1变为2
            //CBHelper dbHelper = new CBHelper(mContext,"yidao.db",null,2);
            //SQLiteDatabase db =dbHelper.getReadableDatabase();
            PopToastUtil.showToast(mContext,"更新未实现");
        });
        insertBtn.setOnClickListener(v -> {
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
            PopToastUtil.showToastBottom(mContext,"插入数据的方法");
        });
        ModifyBtn.setOnClickListener(v -> {
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
        });
        queryBtn.setOnClickListener(v -> {
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
            Cursor cursor = Application.getInstance().getDbHelper().getReadableDatabase().query("inner_department_category", new String[]{"id","name","code"}, "id=?", new String[]{"1"}, null, null, null);
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String code = cursor.getString(cursor.getColumnIndex("code"));
                // Log.i(TAG, "query------->" + "name："+name+" "+",code："+code);
            }
            //关闭数据库
            //db.close();
            PopToastUtil.showToastCenter(mContext,"插入数据的方法");
        });
        deleteBtn.setOnClickListener(v -> {
            //CBHelper dbHelper = new CBHelper(mContext,"stu_db",null,1);
            //得到一个可写的数据库
            //db =dbHelper.getReadableDatabase();
            String whereClauses = "id=?";
            String [] whereArgs = {String.valueOf(2)};
            //调用delete方法，删除数据
            Application.getInstance().getDbHelper().getReadableDatabase().delete("inner_department_category", whereClauses, whereArgs);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuickStickerBinder.getInstance().unBind(this);
    }

}
