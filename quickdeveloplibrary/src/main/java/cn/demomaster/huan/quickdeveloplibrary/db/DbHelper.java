package cn.demomaster.huan.quickdeveloplibrary.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * @author squirrel桓
 * @date 2018/11/19.
 * description：
 */
public class DbHelper extends SQLiteOpenHelper {
    private Context mContext;
    private SQLiteDatabase db;

    public SQLiteDatabase getDb() {
        if(db==null){
            getReadableDatabase();
        }
        return db;
    }

    //必须要有构造函数
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,DbHelperInterface dbHelperInterface) {
        super(context, name, factory, version);
        this.dbHelperInterface = dbHelperInterface;
        this.mContext = context.getApplicationContext();
        this.DATABASE_NAME = name;
        initLocalDB();//初始化本地的db文件
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    private static final String TAG = "TagSQLite";

    //当第一次创建数据库的时候，调用该方法
    public void onCreate(SQLiteDatabase db) {
       // String sql = "create table stu_table(id int,sname varchar(20),sage int,ssex varchar(10))";
        //输出创建数据库的日志信息
       // QDLogger.i(TAG, "create Database------------->");
        //execSQL函数用于执行SQL语句
       // db.execSQL(sql);
    }

    //当更新数据库的时候执行该方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //输出更新数据库的日志信息
        QDLogger.println(TAG, "update Database------------->oldVersion="+oldVersion+",newVersion="+newVersion);
        if(dbHelperInterface!=null){
            dbHelperInterface.onUpgrade(db,oldVersion,newVersion);
        }
    }

    private static String DATABASE_NAME = "yidao.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SP_KEY_DB_VER = "db_ver";
    private void initLocalDB() {
        if (databaseExists()) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            if (DATABASE_VERSION != dbVersion) {
                File dbFile = mContext.getDatabasePath(DATABASE_NAME);
                if (!dbFile.delete()) {
                    QDLogger.println(TAG, "Unable to update database");
                }
            }
        }
        if (!databaseExists()) {
            createDatabase();
        }
    }

    private boolean databaseExists() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    /**
     * Creates database by copying it from assets directory.
     */
    private void createDatabase() {
        String parentPath = mContext.getDatabasePath(DATABASE_NAME).getParent();
        String path = mContext.getDatabasePath(DATABASE_NAME).getPath();

        File file = new File(parentPath);
        if (!file.exists()) {
            if (!file.mkdir()) {
                QDLogger.println(TAG, "Unable to create database directory");
                return;
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = mContext.getAssets().open(DATABASE_NAME);
            os = new FileOutputStream(path);

            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SP_KEY_DB_VER, DATABASE_VERSION);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        db = super.getReadableDatabase();
        return db;
    }

    /**
     * 检查某表列是否存在
     * @param db
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public static boolean checkColumnExist(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
                    , null );
            result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
            Log.e(TAG,"checkColumnExists1..." + e.getMessage()) ;
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }

        return result ;
    }

    DbHelperInterface dbHelperInterface;
    public void setDbHelperInterface(DbHelperInterface dbHelperInterface) {
        this.dbHelperInterface = dbHelperInterface;
    }

    public static interface DbHelperInterface {
        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }
}
