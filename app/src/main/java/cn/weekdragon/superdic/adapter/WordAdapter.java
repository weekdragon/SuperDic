package cn.weekdragon.superdic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.weekdragon.superdic.R;
import cn.weekdragon.superdic.bean.WordInfo;


/**
 * Created by WeekDragon on 2016/3/29.
 */
public class WordAdapter extends BaseAdapter {
    private Context mContext;
    private List<WordInfo> data;

    public void setData(List<WordInfo> data) {
        this.data = data;
    }

    public WordAdapter(Context mContext, List<WordInfo> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.word_item, null);
            holder = new Holder();
            holder.tv_word = (TextView) convertView.findViewById(R.id.tv_word);
            holder.tv_exp = (TextView) convertView.findViewById(R.id.tv_exp);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }
        WordInfo info = data.get(position);
        holder.tv_word.setText(info.getWord());
        holder.tv_exp.setText(info.getExp());

        return convertView;
    }

    class Holder {
        TextView tv_word;
        TextView tv_exp;
    }
}
