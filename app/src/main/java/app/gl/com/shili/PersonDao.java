package app.gl.com.shili;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class PersonDao {
    private DBHelper dbHelper;
   /* public PersonDao(Context context,int i ,String position , String coding){
        dbHelper = new DBHelper(context,i);
    }*/
    public PersonDao(Context context){
        dbHelper = new DBHelper(context,1);
    }

    //获取所有数据表名
    public void getALlTableName(){

    }
    //新增
    public void add(Person person){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",person.getName());
        database.insert("person",null,values);
        database.close();
    }
    //删除
    public void deleteById(int id){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        database.delete("person","id=?",new String[]{id+""});
        database.close();

    }
    //修改
    public void update (Person person){

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",person.getName());
        database.update("",values,"id="+person.getId(),null);
        database.close();
    }
    //查询并按id反序排列
    public List<Person> queryAll(){
        List<Person> list = new ArrayList<Person>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query("person",null,null,null,null,null,"id desc");
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            list.add(new Person(id,name));
        }
        database.close();
        return list;
    }

}
