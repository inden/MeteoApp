package com.in.den.newsunshine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.test.RenamingDelegatingContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by harumi on 15/10/2016.
 */
public class SunshineDbQueryTest {

    Context mMockContext;
    SunshineDbHelper dbHelper;
    SQLiteDatabase dbw;



    @Before
    public void setUp() {
       // mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(),"");
        dbHelper = new SunshineDbHelper(mMockContext);


        dbw = dbHelper.getWritableDatabase();

    }

    @After
    public void tearDown() throws Exception {

        dbw.close();
    }

    @Test
    public void queryMin() throws Exception {
        insertWeatherLocation4oneweek();

            Cursor c= dbw.rawQuery("select * from weather a, (select location_key, min(date) as date from weather group by location_key) b " +
                    "where a.location_key = b.location_key and a.date = b.date", null);


        int i =c.getCount();

    }

    @Test
    public void queryAll() throws Exception  {

        Uri u = SunshineContract.LocationEntry.CONTENT_URI;
        Cursor c = mMockContext.getContentResolver().query(u, null, null, null,null);
        int i = c.getCount();
        c.close();
    }

    @Test
    public void contentProviderquery() throws Exception {
        insertWeatherLocation4oneweek();
/*
        Cursor cur = SunshineDbQuery.getLocationLocationSetting(dbw, "123");
        assertEquals(cur.getCount(),1);
        cur.close();

        Cursor cur2 = SunshineDbQuery.getWeather(dbw, null, "location_key=?", new String[]{"123"},null);
        int icur2 = cur2.getCount();
        assertEquals(icur2,7);
        cur2.close();*/

        Uri u = SunshineContract.WeatherEntry.CONTENT_URI.buildUpon().appendPath("tokyo").build();
        Cursor c = mMockContext.getContentResolver().query(u, null, null, null,null);
        int ic = c.getCount();
        assertEquals(ic,7);
        c.close();
    }

    @Test
    public void cpDeleteWeather() throws Exception {
        insertWeatherLocation4oneweek();
        Uri u = SunshineContract.WeatherEntry.CONTENT_URI;
        mMockContext.getContentResolver().delete(u, null, null);

        Cursor c = mMockContext.getContentResolver().query(u, null, null, null,null);
        int ic = c.getCount();
        assertEquals(ic,0);
        c.close();

    }


    @Test
    public void cpQueryWeatherLocFromDate() throws Exception {
        insertWeatherLocation4oneweek();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date day = sdf.parse(getTomorrowString());
        long l = day.getTime();

        Uri u = SunshineContract.WeatherEntry.buildWeatherLocationWithStartDate("123",l);
        Cursor c = mMockContext.getContentResolver().query(u, null, null, null,null);
        int ic = c.getCount();
        assertEquals(ic,6);
        c.close();
    }

    private void deleteAllRecords() {
        SunshineDbQuery.deleteWeather(dbw, null, null);
        SunshineDbQuery.deleteLocation(dbw, null, null);

    }

    private long[] getOneWeek(String yyyyMMddDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date day = sdf.parse(yyyyMMddDate);

        Calendar c = Calendar.getInstance();
        c.setTime(day);
        long[] list = new long[7];
        for(int i =0; i < 7; i++) {

            c.add(Calendar.DATE, i);
            list[i] = c.getTimeInMillis();
        }

        return list;
    }

    private String getTodayString(){
        Calendar c = Calendar.getInstance();

       return getDateString(c);
    }

    private String getTomorrowString(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        return getDateString(c);
    }

    private String getDateString(Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(c.getTime());
    }

    private void insertWeatherLocation4oneweek() throws ParseException {
        String today = getTodayString();
        insertWeatherLocation4oneweek(today);
    }

    private void insertWeatherLocation4oneweek(String yyyyMMddDate) throws ParseException {

        deleteAllRecords();

        ContentValues cvl = new ContentValues();
        cvl.put(SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING,"tokyo");
        cvl.put(SunshineContract.LocationEntry.COLUMN_CITYID,"123");
        cvl.put(SunshineContract.LocationEntry.COLUMN_CITYCOUNTRY,"JP");
        cvl.put(SunshineContract.LocationEntry.COLUMN_CITYCOORD_LAT,"0.123");
        cvl.put(SunshineContract.LocationEntry.COLUMN_CITYCOORD_LON,"1.23");
        cvl.put(SunshineContract.LocationEntry.COLUMN_CITYNAME,"edo");

        long nb = SunshineDbQuery.insertLocation(dbw, cvl);

        long[] daylist = getOneWeek(yyyyMMddDate);

       ContentValues[] listcv = new ContentValues[7];

        for(int i = 0; i < 7; i++) {

            ContentValues cv = new ContentValues();
            cv.put(SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY, "tokyo");
            cv.put(SunshineContract.WeatherEntry.COLUMN_DATE, daylist[i]);
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_MAX, 12 + i);
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_MIN, 10 + i);
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_EVE, 12 + i);
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_MORN, 10 + i);
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_NIGHT, 10 + i);
            cv.put(SunshineContract.WeatherEntry.COLUMN_HUMIDITY, 5 + i);
            cv.put(SunshineContract.WeatherEntry.COLUMN_PRESSURE, 5 + i);
            cv.put(SunshineContract.WeatherEntry.COLUMN_SHORT_DESC, "cloudy");
            cv.put(SunshineContract.WeatherEntry.COLUMN_WEATHER_ID, 801);

            listcv[i] = cv;
        }

        //long nb2 =SunshineDbQuery.insertWeather(dbw, cv);

        mMockContext.getContentResolver().bulkInsert(SunshineContract.WeatherEntry.CONTENT_URI, listcv);
    }



    @Test
    public void getWeather() throws Exception {

        Cursor cur = SunshineDbQuery.getWeather(dbw, null,null,null,null);

        cur.moveToFirst();
        int index = cur.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_DATE);
        long date = cur.getLong(index);

        cur.close();

        SunshineDbQuery.deleteWeather(dbw, null, null);

        cur = SunshineDbQuery.getLocation(dbw, null,null,null,null);

        int i = cur.getCount();

        assertEquals(i, 0);

        cur.close();

    }

    @Test
    public void getLocation() throws Exception {

        ContentValues cv = new ContentValues();
        cv.put(SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING,123);
        cv.put(SunshineContract.LocationEntry.COLUMN_CITYNAME,"tokyo");

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long r =SunshineDbQuery.insertLocation(db, cv);

        Cursor cur = SunshineDbQuery.getLocation(db, null,null,null,null);

        cur.moveToFirst();
        int index = cur.getColumnIndex(SunshineContract.LocationEntry.COLUMN_CITYNAME);
        String name = cur.getString(index);

        assertEquals(name, "tokyo");

        cur.close();

        SunshineDbQuery.deleteLocation(db, null, null);

        cur = SunshineDbQuery.getLocation(db, null,null,null,null);

        int i = cur.getCount();

        assertEquals(i, 0);

        cur.close();

    }


    @Test
    public void updateLocation() throws Exception {

    }

    @Test
    public void updateWeather() throws Exception {

    }



}