package com.in.den.newsunshine.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harumi on 14/10/2016.
 */

public class SunshineDbQuery {



    //location.location_setting = ?
    private static final String sLocationSettingSelection =
              SunshineContract.LocationEntry.TABLE_NAME+
                    "." + SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? ";

    //location.location_setting = ? AND date >= ?
    private static final String sLocationSettingWithStartDateSelection =
            SunshineContract.LocationEntry.TABLE_NAME+
                    "." + SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    SunshineContract.WeatherEntry.COLUMN_DATE + " >= ? ";

    //location.location_setting = ? AND date = ?
    private static final String sLocationSettingAndDaySelection =
            SunshineContract.LocationEntry.TABLE_NAME +
                    "." + SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    SunshineContract.WeatherEntry.COLUMN_DATE + " = ? ";

    private static final String sLocationSettingList =
              SunshineContract.LocationEntry.TABLE_NAME +
                    "." + SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING + " in (#)" ;

    private static final String sLocationKeyListIN =
            SunshineContract.WeatherEntry.TABLE_NAME +
                    "." + SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY + " in (#)" ;

    private static final String sLocationKeyListNOTIN =
            SunshineContract.WeatherEntry.TABLE_NAME +
                    "." + SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY + " not in (#)" ;

    private static final String sLocationSettingListNotIn =
            SunshineContract.LocationEntry.TABLE_NAME +
                    "." + SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING + " not in (#)" ;

    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    static{
        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByLocationSettingQueryBuilder.setTables(
                SunshineContract.WeatherEntry.TABLE_NAME + " INNER JOIN " + SunshineContract.LocationEntry.TABLE_NAME +
                        " ON " + SunshineContract.WeatherEntry.TABLE_NAME +
                        "." + SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY +
                        " = " + SunshineContract.LocationEntry.TABLE_NAME +
                        "." + SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING);


    }

    public static Cursor getWeatherByLocationSetting(SQLiteDatabase db, Uri uri, String[] projection, String sortOrder) {
        String locationSetting = SunshineContract.WeatherEntry.getLocationSettingFromUri(uri);


        long startDate = SunshineContract.WeatherEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;

        if (startDate == 0) {
            selection = sLocationSettingSelection;
            selectionArgs = new String[]{locationSetting};


        } else {
            selectionArgs = new String[]{locationSetting, Long.toString(startDate)};
            selection = sLocationSettingWithStartDateSelection;
        }


        Cursor cursor =  sWeatherByLocationSettingQueryBuilder.query(db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );



        return cursor;
    }

    public static Cursor getWeatherByLocationSettingAndDate(
            SQLiteDatabase db,
            Uri uri, String[] projection,
            String sortOrder) {
        String locationSetting = SunshineContract.WeatherEntry.getLocationSettingFromUri(uri);
        long date = SunshineContract.WeatherEntry.getDateFromUri(uri);



        Cursor cursor = sWeatherByLocationSettingQueryBuilder.query(db,
                projection,
                sLocationSettingAndDaySelection,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );


        return cursor;
    }

    public static Cursor getWeather(SQLiteDatabase db, String[] projection, String selection,
                                    String[] selectionArgs, String sortOrder) {


        Cursor cursor = db.query(SunshineContract.WeatherEntry.TABLE_NAME,
                projection, selection, selectionArgs,null,null, sortOrder);

        int i = cursor.getCount();

        return cursor;
    }

    public static Cursor getLocation(SQLiteDatabase db, String[] projection, String selection,
                                    String[] selectionArgs, String sortOrder) {



        Cursor cursor = db.query(SunshineContract.LocationEntry.TABLE_NAME,
                projection, selection, selectionArgs,null,null, sortOrder);

       // db.close();

        return cursor;
    }

    public static Cursor getLocationLocationSetting(SQLiteDatabase db, String locationsetting) {

        locationsetting = locationsetting.toUpperCase();
        Cursor cursor = db.query(SunshineContract.LocationEntry.TABLE_NAME,
                null, sLocationSettingSelection, new String[] {locationsetting},null,null, null);

        return cursor;
    }

    public static Cursor getLocationLocationSetting(SQLiteDatabase db, Uri uri) {

        String locationsetting = SunshineContract.LocationEntry.getLocationSettingFromUri(uri).toUpperCase();

        Cursor cursor = getLocationLocationSetting(db, locationsetting);

        return cursor;
    }

    public static long insertLocation(SQLiteDatabase db, ContentValues values) {
        return insert(db, SunshineContract.LocationEntry.TABLE_NAME, values);
    }

    public static long insertWeather(SQLiteDatabase db, ContentValues values) {
        return insert(db, SunshineContract.WeatherEntry.TABLE_NAME, values);
    }

    public static long insert(SQLiteDatabase db, String tablename, ContentValues values) {

        long res = 0;
        try {

            res = db.insert(tablename, null, values);

        }
        catch(Exception e) {
            System.out.println();
        }

        return res;
    }

    public static int deleteLocation(SQLiteDatabase db, String selection, String[] selectionArgs) {
        return delete(db, SunshineContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
    }

    public static int deleteWeather(SQLiteDatabase db, String selection, String[] selectionArgs) {
        return delete(db, SunshineContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
    }

    public static int delete(SQLiteDatabase db, String tablename, String selection, String[] selectionArgs) {

        int res = -1;
        try {

            res = db.delete(tablename,selection,selectionArgs);

        }
        catch (Exception e) {
            System.out.println(e);
        }


        return res;
    }

    public static int updateLocation(SQLiteDatabase db, ContentValues values, String selection, String[] selectionArgs) {
        return update(db, SunshineContract.LocationEntry.TABLE_NAME, values,selection, selectionArgs);
    }

    public static int updateWeather(SQLiteDatabase db, ContentValues values, String selection, String[] selectionArgs) {
        return update(db, SunshineContract.WeatherEntry.TABLE_NAME, values,selection, selectionArgs);
    }

    public static int update(SQLiteDatabase db, String tablename, ContentValues values, String selection, String[] selectionArgs) {

        int res = -1;
        try {

            res = db.update(tablename,values, selection, selectionArgs);

        }
        catch(Exception e) {
            System.out.println(e);
        }

        return res;
    }


    public static Cursor getWeatherByLocationList(SQLiteDatabase db, Uri uri, String[] projection, String sortOrder) {

        List<String> l = SunshineContract.WeatherEntry.getMultiLocationFromUri(uri);

        String condition = sLocationSettingList.replace("#", makePlaceholders(l.size()));

        String[] elems = l.toArray(new String[0]);


         Cursor cursor = sWeatherByLocationSettingQueryBuilder.query(db,
                projection,
                condition,
               elems,
                null,
                null,
                sortOrder);

        return cursor;
    }

    public static String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    public static String replace4incondtion(String condition, int len) {
        return condition.replace("#", makePlaceholders(len));
    }

    public static String prepareLocationInCondition(int len) {
        return replace4incondtion(sLocationSettingList, len);
    }

    public static String prepareLocationotNotInCondition(int len) {
        return replace4incondtion(sLocationSettingListNotIn, len);
    }

    public static String prepareLocationKeyNotInCondition(int len) {
        return replace4incondtion(sLocationKeyListNOTIN, len);
    }

    public static String[] toArrayLocationList(List<String> locations) {
        String[] elems = new String[locations.size()];
        for(int i=0; i < locations.size(); i++) {
            elems[i] = locations.get(i).toUpperCase();
        }
        return elems;
    }

    public static Cursor getWeatherByLocationListAndDate(SQLiteDatabase db, Uri uri, String[] projection, String sortOrder) {
        throw new UnsupportedOperationException("");
    }

    public static Cursor getWeatherToday(SQLiteDatabase db, ArrayList<String> locations, String[] projection, String sortOrder) {

        String rowq = "select * from weather a, " +
                "(select location_key, min(date) as date from weather group by location_key) b, " +
                "location c " +
                "where a.location_key = b.location_key and a.location_key = c.location_setting and " +
                "a.date = b.date and a.location_key in (#) order by location_key ";


        rowq = replace4incondtion(rowq, locations.size());

        Cursor cursor = db.rawQuery(rowq, toArrayLocationList(locations));

        return cursor;
    }
}
