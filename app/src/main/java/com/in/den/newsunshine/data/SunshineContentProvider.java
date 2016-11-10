package com.in.den.newsunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.in.den.newsunshine.SharedPreferenceOp;

import java.util.ArrayList;
import java.util.List;


public class SunshineContentProvider extends ContentProvider {
    SunshineDbHelper mDbHelper;
    public static UriMatcher uriMatcher = SunshineURIMatcher.uriMatcher;
    SharedPreferenceOp sharedPreferenceOp;

    @Override
    public boolean onCreate() {
        mDbHelper = new SunshineDbHelper(getContext());
        sharedPreferenceOp = new SharedPreferenceOp(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int i = uriMatcher.match(uri);
        Cursor cursor;

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        switch(i) {

            case SunshineURIMatcher.LOCATION :
                cursor = SunshineDbQuery.getLocation(
                        db, projection, selection, selectionArgs, sortOrder);
                break;
            case SunshineURIMatcher.LOCATION_CITY :
                cursor = SunshineDbQuery.getLocationLocationSetting(
                        db, uri);
                break;
            case SunshineURIMatcher.WEATHER:
                cursor = SunshineDbQuery.getWeather(
                        db, projection, selection, selectionArgs, sortOrder);
                break;
            case SunshineURIMatcher.WEATHER_WITH_MULTILOCATION: {
                cursor = SunshineDbQuery.getWeatherByLocationList(
                        db, uri, projection,  sortOrder);
                break;
            }
            case SunshineURIMatcher.WEATHER_WITH_MULTILOCATION_AND_DATE: {
                cursor = SunshineDbQuery.getWeatherByLocationListAndDate(db, uri, projection, sortOrder);
                break;
            }
            case SunshineURIMatcher.WEATHER_TODAY: {
                ArrayList<String> locations = sharedPreferenceOp.getPreferenceLocations();
                cursor = SunshineDbQuery.getWeatherToday(db, locations, projection, sortOrder);
                int test = cursor.getCount();
                break;
            }
            case SunshineURIMatcher.WEATHER_WITH_LOCATION:
                cursor = SunshineDbQuery.getWeatherByLocationSetting(db, uri,projection,sortOrder);
                int test = cursor.getCount();
                break;
            case SunshineURIMatcher.WEATHER_WITH_LOCATION_AND_DATE:
                cursor = SunshineDbQuery.getWeatherByLocationSettingAndDate(db, uri,projection,sortOrder);
                 break;
            default: throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {

        int i = uriMatcher.match(uri);
        String type;

        switch(i) {

            case SunshineURIMatcher.LOCATION :
                type = SunshineContract.LocationEntry.CONTENT_TYPE; break;
            case SunshineURIMatcher.WEATHER:
                type = SunshineContract.WeatherEntry.CONTENT_TYPE; break;
            case SunshineURIMatcher.WEATHER_WITH_LOCATION:
                 type = SunshineContract.WeatherEntry.CONTENT_TYPE; break;
            case SunshineURIMatcher.WEATHER_WITH_LOCATION_AND_DATE:
                type = SunshineContract.WeatherEntry.COTENT_ITEM_TYPE; break;
            default: throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int i = uriMatcher.match(uri);

        Uri resulturi = null;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch(i) {
            case SunshineURIMatcher.LOCATION : {
                long l = SunshineDbQuery.insertLocation(db, values);
                if (l > 0) {
                    resulturi = uri;
                }
                break;
            }
            case SunshineURIMatcher.WEATHER: {
                long l = SunshineDbQuery.insertWeather(db, values);
                if (l > 0) {
                    resulturi = uri;
                }
                break;
            }
            default : throw new UnsupportedOperationException("Unknown uri" + uri);
        }

        if(resulturi == null) {
            throw new SQLiteException("unable to insert for " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return resulturi;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (match) {
            case SunshineURIMatcher.WEATHER:
                rowsDeleted = SunshineDbQuery.deleteWeather(db, selection, selectionArgs);
                break;
            case SunshineURIMatcher.LOCATION:
                rowsDeleted = SunshineDbQuery.deleteLocation(db, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        int rowsUpdated;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (match) {
            case SunshineURIMatcher.WEATHER:
                rowsUpdated = SunshineDbQuery.updateWeather(db, values, selection, selectionArgs);
                break;
            case SunshineURIMatcher.LOCATION:
                rowsUpdated = SunshineDbQuery.updateLocation(db, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case SunshineURIMatcher.WEATHER:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(SunshineContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            default:
                returnCount = super.bulkInsert(uri, values); break;

        }
        return returnCount;
    }
}
