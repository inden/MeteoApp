package com.in.den.newsunshine;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.in.den.newsunshine.data.SunshineContract;

import java.util.HashMap;
import java.util.Locale;

import static android.R.attr.textAppearanceSmall;

/**
 * Created by harumi on 01/11/2016.
 */

public class MyCursorTreeAdapter extends CursorTreeAdapter {

    private HashMap<Integer, Integer> mGroupMap;
    private HashMap<Integer, String> mLocationSettingMap;
    Callback mycallback = null;
    LayoutInflater layoutInflater;


    public MyCursorTreeAdapter(Cursor cursor, Context context) {

        super(cursor, context);
        mGroupMap = new HashMap<Integer, Integer>();
        mLocationSettingMap = new HashMap<Integer, String>();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCallback(Callback callback) {
        mycallback = callback;
    }

    public HashMap<Integer, Integer> getGroupMap() {
        return mGroupMap;
    }

    public HashMap<Integer, String> getLocationSettingMap() {
        return mLocationSettingMap;
    }

    public static class ViewHolder {

        public final TextView textViewDateDay;
        public final TextView textViewDateDayWeek;
        public final TextView textViewDateMonth;
        public final TextView textViewTempDay;
        public final TextView textViewTempMin;
        public final TextView textViewTempMax;
        public final ImageView imgViewDay;


        public ViewHolder(View view) {

            textViewDateDay = (TextView) view.findViewById(R.id.list_item_dateday);

            textViewDateMonth = (TextView) view.findViewById(R.id.list_item_datemonth);

            textViewDateDayWeek = (TextView) view.findViewById(R.id.list_item_datedayofweek);

            textViewTempDay = (TextView) view.findViewById(R.id.list_item_tempday);

            textViewTempMin = (TextView) view.findViewById(R.id.list_item_tempmin);

            textViewTempMax = (TextView) view.findViewById(R.id.list_item_tempmax);

            imgViewDay = (ImageView) view.findViewById(R.id.list_item_icon);


        }
    }


    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        int groupPos = groupCursor.getPosition();
        int groupId = groupCursor.getInt(groupCursor
                .getColumnIndex(SunshineContract.LocationEntry._ID));


        mGroupMap.put(groupId, groupPos);
        //loanch loader for child
        if (mycallback != null) {
            mycallback.loadChildCursor(groupId);
        }

        return null;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        View newview = layoutInflater.inflate(R.layout.listviewmain_item, parent, false);

        return newview;
    }
/*
    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        int groupid = cursor.getInt(cursor.getColumnIndex(SunshineContract.LocationEntry._ID));
        String locationsetting = cursor.getString(cursor.getColumnIndex(SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING));
        String countryname = cursor.getString(cursor.getColumnIndex(SunshineContract.LocationEntry.COLUMN_CITYCOUNTRY));

        TextView textView1 = (TextView) view.findViewById(R.id.item_cityname);
        TextView textView2 = (TextView) view.findViewById(R.id.item_country);

        textView1.setText(locationsetting);
        textView2.setText(countryname);

        mLocationSettingMap.put(groupid, locationsetting);
    }*/

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        int groupid = cursor.getInt(cursor.getColumnIndex(SunshineContract.LocationEntry._ID));
        String locationsetting = cursor.getString(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY));
        String countryname = cursor.getString(cursor.getColumnIndex(SunshineContract.LocationEntry.COLUMN_CITYCOUNTRY));

        int icode = cursor.getInt(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_WEATHER_ID));
        double iTempday = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_DAY));
        double iTempmin = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_MIN));
        double iTempmax = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_MAX));

        TextView textViewCity = (TextView) view.findViewById(R.id.glist_item_cityname);
        TextView textViewCountry = (TextView) view.findViewById(R.id.glist_item_country);
        ImageView imgIcon = (ImageView) view.findViewById(R.id.glist_item_icon);
        TextView textViewTempDay = (TextView) view.findViewById(R.id.glist_item_tempday);
        TextView textViewTempMin = (TextView) view.findViewById(R.id.glist_item_tempmin);
        TextView textViewTempMax = (TextView) view.findViewById(R.id.glist_item_tempmax);

        textViewTempDay.setText(SunshineUtil.freindlyformatTemperature(iTempday, true));
        textViewTempMax.setText(SunshineUtil.freindlyformatTemperature(iTempmax, false));
        textViewTempMin.setText(SunshineUtil.freindlyformatTemperature(iTempmin, false));

        if(locationsetting.length() > 5) {
            textViewCity.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        }

        textViewCity.setText(locationsetting);
        textViewCountry.setText(countryname);
        imgIcon.setImageResource(SunshineUtil.getArtResourceForWeatherCondition(icode));

        mLocationSettingMap.put(groupid, locationsetting);
    }


    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        View newview = layoutInflater.inflate(R.layout.list_item_forecast, parent, false);

        ViewHolder vh = new ViewHolder(newview);
        newview.setTag(vh);

        return newview;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        MyCursorTreeAdapter.ViewHolder vh = (MyCursorTreeAdapter.ViewHolder) view.getTag();

        long idate = cursor.getLong(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_DATE));
        int icode = cursor.getInt(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_WEATHER_ID));
        double iTempday = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_DAY));
        double iTempmin = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_MIN));
        double iTempmax = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_MAX));

        Locale locale = context.getResources().getConfiguration().locale;

        SunshineUtil.DateStringHolder dateStringHolder = SunshineUtil.formatLongToDate(idate, locale);

        vh.imgViewDay.setImageResource(SunshineUtil.getArtResourceForWeatherCondition(icode));
        vh.textViewDateDay.setText(dateStringHolder.day);
        vh.textViewDateDayWeek.setText(dateStringHolder.dayofweek);
        vh.textViewDateMonth.setText(dateStringHolder.month);
        vh.textViewTempDay.setText(SunshineUtil.freindlyformatTemperature(iTempday, true));
        vh.textViewTempMax.setText(SunshineUtil.freindlyformatTemperature(iTempmax, false));
        vh.textViewTempMin.setText(SunshineUtil.freindlyformatTemperature(iTempmin, false));


    }

    /********************************************/
    interface Callback {

        public void loadChildCursor(int groupid);

    }
}
