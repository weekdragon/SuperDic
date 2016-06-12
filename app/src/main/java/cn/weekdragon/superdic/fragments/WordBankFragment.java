package cn.weekdragon.superdic.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.weekdragon.superdic.R;
import cn.weekdragon.superdic.adapter.WordAdapter;
import cn.weekdragon.superdic.bean.WordInfo;


/**
 * Created by WeekDragon on 2016/3/30.
 */
public class WordBankFragment extends Fragment {
    private final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/mydic";
    private final String DATABASE_FILENAME = "dictionary.db";
    private SQLiteDatabase database;
    private ListView wordBank;
    //select _id,province ,name from city order by _id limit perItemNum Offset
    private int page = 0;
    private String sql = "select * from t_words limit 50 offset  ";
    private List<WordInfo> data = new ArrayList<>();
    private Handler mHandler;
    private WordAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wordbank, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordBank = (ListView) getActivity().findViewById(R.id.wordBank);
        mAdapter = new WordAdapter(getActivity(), data);
        wordBank.setAdapter(mAdapter);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mAdapter.notifyDataSetChanged();
            }
        };
        database = openDatabase();
        if (database == null) {
            Log.d("提示", "aaaa");
            return;
        }
        Cursor cursor = database.rawQuery(sql + page * 50, null);

        while (cursor.moveToNext()) {
            String word = cursor.getString(0);
            String exp = cursor.getString(1);
            WordInfo info = new WordInfo(word, exp);
            data.add(info);
        }

        wordBank.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1
                        && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    page++;
                    Cursor cursor = database.rawQuery(sql + page * 50, null);
                    while (cursor.moveToNext()) {
                        String word = cursor.getString(0);
                        String exp = cursor.getString(1);
                        WordInfo info = new WordInfo(word, exp);
                        data.add(info);
                    }
                    wordBank.setSelection(view.getLastVisiblePosition());

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }


    private SQLiteDatabase openDatabase() {
        try {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists())
                dir.mkdir();
            if (!(new File(databaseFilename)).exists()) {
                InputStream is = getResources().openRawResource(
                        R.raw.dictionary);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.close();
                is.close();
            }
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                    databaseFilename, null);
            return database;
        } catch (Exception e) {
        }
        return null;
    }

}
