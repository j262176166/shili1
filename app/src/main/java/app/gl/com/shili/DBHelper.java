package app.gl.com.shili;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 连接数据库
 * Created by Administrator on 2018/3/22.
 */

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context, int version) {
        super(context, "abc.db", null, 1);

    }


    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE person (ID INTEGER PRIMARY KEY AUTOINCREMENT not null, name NVARCHAR(100) NOT NULL)";
        sqLiteDatabase.execSQL(sql);

    }

    //软件版本号发生改变时候更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
