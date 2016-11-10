package com.in.den.newsunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.in.den.newsunshine.data.SunshineContract;

/**
 * Created by harumi on 20/10/2016.
 */

public class MyCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;

    public static class ViewHolder {

        public final TextView textView1;
        public final TextView textView2;
        public final TextView textView3;

        public ViewHolder(View view) {
            textView1 = (TextView) view.findViewById(R.id.item_content);
            textView2 = (TextView) view.findViewById(R.id.item_content2);
            textView3 = (TextView) view.findViewById(R.id.item_content3);
        }
    }


    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int position = cursor.getPosition();
        View newview;

        if(position == 0) {
             newview = layoutInflater.inflate(R.layout.listviewmain_item0, parent, false);
        }
        else {
            newview = layoutInflater.inflate(R.layout.listviewmain_item, parent, false);
        }

        ViewHolder vh = new ViewHolder(newview);
        newview.setTag(vh);

        return newview;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder vh = (ViewHolder)view.getTag();


        String title = cursor.getString( cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_DATE));
        if(title.isEmpty()) {
            title = "---";
        }
        vh.textView1.setText(title);


        String title2 = cursor.getString( cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY));
        if(title2.isEmpty()) {
            title = "---";
        }
        vh.textView2.setText(title2);


        if(vh.textView3!=null) {
            String title3 = cursor.getString(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_MAX));
            if (title3.isEmpty()) {
                title = "---";
            }
            vh.textView3.setText(title3);
        }

    }
}
