package com.in.den.newsunshine;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by harumi on 27/10/2016.
 */

public class LocationArrayAdapter extends ArrayAdapter<String> {

    LayoutInflater inflater;

    Callback mycallback = null;

    public LocationArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

        inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    public void setCallback(Callback callback) {
        mycallback = callback;
    }

    private class ViewHolder {
        TextView locationName;
       // Button removeButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.listview_locations_item, null);
            holder = new ViewHolder();
            final TextView locview = (TextView) convertView
                    .findViewById(R.id.item_location);
            holder.locationName = locview;


            Button deletebutton = (Button)convertView.findViewById(R.id.btn_deleteloc);
            deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String location = locview.getText().toString();

                    if(mycallback != null) {

                        mycallback.deleteLocation(location);
                    }
                }
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String s = (String)getItem(position);

        holder.locationName.setText(s);

        return convertView;
    }


    interface Callback {

        void deleteLocation(String location);
    }
}
