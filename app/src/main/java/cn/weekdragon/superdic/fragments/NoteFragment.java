package cn.weekdragon.superdic.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.weekdragon.superdic.R;
import cn.weekdragon.superdic.adapter.WordAdapter;
import cn.weekdragon.superdic.bean.WordInfo;
import cn.weekdragon.superdic.db.WordDao;


/**
 * Created by WeekDragon on 2016/3/29.
 */
public class NoteFragment extends Fragment {
    private ListView mListView;
    private WordDao dao;
    private List<WordInfo> data = new ArrayList<WordInfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dao = new WordDao(getActivity());
        mListView = (ListView) getView().findViewById(R.id.mListView);
        data = dao.getAll();
        final WordAdapter mAdapter = new WordAdapter(getActivity(), data);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.dialog_icon);
                builder.setMessage("确认操作");
                builder.setTitle("提示");
                final int location = position;
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dao.delete(data.get(location).getId());
                        data.remove(location);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null).show();
            }
        });

    }
}
