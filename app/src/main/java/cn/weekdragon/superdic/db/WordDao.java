package cn.weekdragon.superdic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.weekdragon.superdic.bean.WordInfo;

/**
 * �������ݿ��birthInfo����
 *
 * @author WeekDragon
 */
public class WordDao {

    private DBHelper dbHelper;

    public WordDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * 插入
     */
    public void add(WordInfo info) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("word", info.getWord());
        values.put("exp", info.getExp());
        database.insert(DBHelper.Table_BirthInfo, null, values);
        database.close();
    }

    /**
     * ɾ
     */
    public void delete(int id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.Table_BirthInfo, "_id=?", new String[]{id
                + ""});
        database.close();
    }

    /**
     * 得到所有单词
     */
    public List<WordInfo> getAll() {
        List<WordInfo> list = new ArrayList<WordInfo>();
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String sql = "select *  from " + DBHelper.Table_BirthInfo;
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String word = cursor.getString(1);
            String exp = cursor.getString(2);
            WordInfo info = new WordInfo(id, word, exp);
            list.add(info);
        }
        cursor.close();
        database.close();
        return list;
    }
}
