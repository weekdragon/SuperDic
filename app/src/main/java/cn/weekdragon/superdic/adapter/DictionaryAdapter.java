package cn.weekdragon.superdic.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by WeekDragon on 2016/3/29.
 */
public class DictionaryAdapter extends CursorAdapter {

    private Context mContext;

    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.getString(cursor
                .getColumnIndex("_id"));
    }

    private void setView(View view, Cursor cursor) {
        TextView tvWordItem = (TextView) view;
        tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
        tvWordItem.setPadding(15, 10, 10, 15);
        tvWordItem.setTextSize(18);
    }

    public DictionaryAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.mContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        setView(view, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = new TextView(mContext);
        setView(view, cursor);
        return view;
    }

}