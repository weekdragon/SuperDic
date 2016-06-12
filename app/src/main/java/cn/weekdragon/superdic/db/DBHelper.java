package cn.weekdragon.superdic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static int version = 1;
    public static final String Table_BirthInfo = "wordNote";

    public DBHelper(Context context) {
        super(context, "MyDic.db", null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("create table wordNote (_id integer PRIMARY KEY AUTOINCREMENT,word,exp)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
