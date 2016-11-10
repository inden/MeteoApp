package com.in.den.newsunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by harumi on 14/10/2016.
 */

public class SunshineDbHelper extends SQLiteOpenHelper {

    public static final String databasename = "newsunshine.db";
    public static final int version = 5;

    public static final String createtable_location = createloc();
    public static final String createtable_weather = createweather();

    private static String createloc() {
        String sql = "create table %s (" +
                "%s integer primary key autoincrement, " +
                "%s text not null, " +
                "%s text, " +
                "%s integer not null, " +
                "%s text, "+
                "%s real, "+
                "%s real,  "+
                "CONSTRAINT citydateunique UNIQUE (LOCATION_SETTING)" +
                ");";
        return String.format(sql, SunshineContract.LocationEntry.TABLE_NAME,
                SunshineContract.LocationEntry._ID,
                SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING, //may not be same as cityname
                SunshineContract.LocationEntry.COLUMN_CITYNAME,
                SunshineContract.LocationEntry.COLUMN_CITYID,
                SunshineContract.LocationEntry.COLUMN_CITYCOUNTRY,
                SunshineContract.LocationEntry.COLUMN_CITYCOORD_LAT,
                SunshineContract.LocationEntry.COLUMN_CITYCOORD_LON);

    }

    private static String createweather() {
        String sql = "create table %s (" +
                "%s integer primary key autoincrement, " +
                "%s text not null, " +    //clef etrangere
                "%s integer not null, " +
                "%s text, "+
                "%s real, "+
                "%s real, "+
                "%s real, "+
                "%s real, "+
                "%s real, "+
                "%s real, "+
                "%s real, "+
                "%s real, "+
                "%s integer, " +
                "CONSTRAINT citydateunique UNIQUE (LOCATION_KEY, DATE)" +
                ");";
        return String.format(
                sql, SunshineContract.WeatherEntry.TABLE_NAME,
                SunshineContract.WeatherEntry._ID,
                SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY, //fkey location setting
                SunshineContract.WeatherEntry.COLUMN_DATE,
                SunshineContract.WeatherEntry.COLUMN_SHORT_DESC,
                SunshineContract.WeatherEntry.COLUMN_TEMP_MAX,
                SunshineContract.WeatherEntry.COLUMN_TEMP_MIN,
                SunshineContract.WeatherEntry.COLUMN_TEMP_DAY,
                SunshineContract.WeatherEntry.COLUMN_TEMP_NIGHT,
                SunshineContract.WeatherEntry.COLUMN_TEMP_EVE,
                SunshineContract.WeatherEntry.COLUMN_TEMP_MORN,
                SunshineContract.WeatherEntry.COLUMN_HUMIDITY,
                SunshineContract.WeatherEntry.COLUMN_PRESSURE,
                SunshineContract.WeatherEntry.COLUMN_WEATHER_ID);

    }


    public SunshineDbHelper(Context context) {
        super(context,databasename,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createtable_location);
        db.execSQL(createtable_weather);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + SunshineContract.WeatherEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + SunshineContract.LocationEntry.TABLE_NAME);

        onCreate(db);

    }
}
