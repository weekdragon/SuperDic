package cn.weekdragon.superdic.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.weekdragon.superdic.R;
import cn.weekdragon.superdic.ShowActivity;
import cn.weekdragon.superdic.adapter.DictionaryAdapter;
import cn.weekdragon.superdic.bean.Const;
import cn.weekdragon.superdic.bean.Word;
import cn.weekdragon.superdic.util.Http;

/**
 * Created by WeekDragon on 2016/4/8.
 */
public class SerchFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private ImageView doSearch;
    private ImageView clear;
    private AutoCompleteTextView auto_text;
    private final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/SuperDic";
    private final String DATABASE_FILENAME = "dictionary.db";
    private SQLiteDatabase database;

    private Handler handler;
    private String path;
    private Word word;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler = new Handler();
        initView();
        database = openDatabase();
        if (database == null) {
            Log.d("----提--示----", "赋值失败");
        }
    }

    private void initView() {
        clear = (ImageView) getView().findViewById(R.id.clear);
        doSearch = (ImageView) getView().findViewById(R.id.doSearch);

        auto_text = (AutoCompleteTextView) getView().findViewById(R.id.auto_text);
        auto_text.addTextChangedListener(this);
        auto_text.setHintTextColor(Color.parseColor("#77ffffff"));

        doSearch.setOnClickListener(this);
        clear.setOnClickListener(this);

    }

    private void CopyDBFile() {
        try {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists())
                dir.mkdir();
            else return;
            if (!(new File(databaseFilename)).exists()) {
                InputStream is = getResources().openRawResource(
                        R.raw.dictionary);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                Log.d("----提--示----", "文件复制成功");
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            Log.d("----提--示----", "文件复制失败" + e.toString());
        }
    }

    private SQLiteDatabase openDatabase() {
        try {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            CopyDBFile();
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                    databaseFilename, null);
            return database;
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                // 获得下载并解析的Word对象
                word = Http.getHttp(path);
                // 将下载获取的Word对象赋值给Const类中word的引用
                if (word != null) {
                    Const.word = word;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    System.out.println("--runnable--" + word.getKey());
                    Intent intent = new Intent(getActivity(),
                            ShowActivity.class);
                    startActivity(intent);
                }
            };
            handler.post(r);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == clear) {
            auto_text.setText("");
        } else if (v == doSearch) {
            String editinfo = auto_text.getText().toString();
            if (editinfo != null && !editinfo.equals("")) {
                path = "http://dict-co.iciba.com/api/dictionary.php?w="
                        + editinfo + "&key=4FF796261C88AFE22A720A605A6906E3";
                Runnable runnable = new MyThread();
                Thread myThread = new Thread(runnable);
                myThread.start();
            } else {
                Toast.makeText(getActivity(), "请入内容不能为空", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (database == null) {
            return;
        }
        Cursor cursor = database.rawQuery("select english as _id from t_words where english like ?",
                new String[]{s.toString() + "%"});
        DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(getActivity(), cursor, true);
        auto_text.setAdapter(dictionaryAdapter);
        if (s.toString().length() > 0) {
            clear.setVisibility(View.VISIBLE);
        } else {
            clear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (database != null)
            database.close();
    }
}
