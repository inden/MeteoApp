package com.in.den.newsunshine.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harumi on 13/10/2016.
 */

public final class SunshineContract {

    public static final String CONTENT_AUTHORITY = "com.in.den.newsunshine.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weather";
    public static final String PATH_LOCATION = "location";
    public static final String SEPARATEUR = "/";
    public static final String PATH_MULTILOCATION="multilocation";
    public static final String PATH_TODAY="today";



    public static class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE = createAutorityPath(ContentResolver.CURSOR_DIR_BASE_TYPE, PATH_LOCATION);
        public static final String COTENT_ITEM_TYPE = createAutorityPath(ContentResolver.CURSOR_ITEM_BASE_TYPE, PATH_LOCATION);

        public static final String TABLE_NAME = "LOCATION";

        public static final String COLUMN_CITYNAME = "CITY_NAME";
        public static final String COLUMN_LOCATION_SETTING = "LOCATION_SETTING"; // searve as foreign key for the weather table
        public static final String COLUMN_CITYID = "CITY_ID";
        public static final String COLUMN_CITYCOUNTRY = "CITY_COUNTRY";
        public static final String COLUMN_CITYCOORD_LON = "CITY_COORD_LON";
        public static final String COLUMN_CITYCOORD_LAT = "CITY_COORD_LAT";


        public static Uri buildLocationUri(String idloc) {
            return CONTENT_URI.buildUpon().appendPath(idloc).build();
        }

        public static String getLocationSettingFromUri(Uri uri) {
            String locationsetting =  uri.getPathSegments().get(1);
            return locationsetting.toUpperCase();
        }
    }

    public static class WeatherEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final Uri CONTENT_URI_MULTILOCATION = CONTENT_URI.buildUpon().appendPath(PATH_MULTILOCATION).build();

        public static final Uri CONTENT_URI_TODAY = CONTENT_URI.buildUpon().appendPath(PATH_TODAY).build();

        public static final String CONTENT_TYPE = createAutorityPath(ContentResolver.CURSOR_DIR_BASE_TYPE, PATH_WEATHER);
        public static final String COTENT_ITEM_TYPE = createAutorityPath(ContentResolver.CURSOR_ITEM_BASE_TYPE, PATH_WEATHER);

        public static final String TABLE_NAME = "WEATHER";

        public static final String COLUMN_LOCATION_KEY = "LOCATION_KEY";
        public static final String COLUMN_DATE = "DATE";
        public static final String COLUMN_TEMP_MAX = "TEMP_MAX";
        public static final String COLUMN_TEMP_MIN = "TEMP_MIN";
        public static final String COLUMN_TEMP_DAY = "TEMP_DAY";
        public static final String COLUMN_TEMP_NIGHT = "TEMP_NIGHT";
        public static final String COLUMN_TEMP_EVE = "TEMP_EVE";
        public static final String COLUMN_TEMP_MORN = "TEMP_MORN";
        public static final String COLUMN_WEATHER_ID = "WEATHER_ID";
        public static final String COLUMN_SHORT_DESC = "SHORT_DESC";
        public static final String COLUMN_HUMIDITY = "HUMIDITY";
        public static final String COLUMN_PRESSURE = "PRESSURE";



        public static Uri buildWeatherUri(long id) {
            return android.content.ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeatherLocation(String locationSetting) {
            return createUriPath(CONTENT_URI, locationSetting);
        }

        public static Uri buildWeatherMultiLocations(List<String> locations) {
            Uri uri = CONTENT_URI_MULTILOCATION;
            for(int i=0; i < locations.size(); i++) {
                uri = appendLocationKeyQueryParameter(uri, locations.get(i));
            }

            return uri;
        }

        private static Uri appendLocationKeyQueryParameter(Uri uri, String location) {
            return uri.buildUpon().appendQueryParameter(COLUMN_LOCATION_KEY, location).build();
        }

        public static Uri buildWeatherLocationWithStartDate(
                String locationSetting, long startDate) {

            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COLUMN_DATE, Long.toString(startDate)).build();
        }

        public static Uri buildWeatherLocationWithDate(String locationSetting, long date) {

            return createUriPathWithDate(CONTENT_URI, locationSetting, date);
        }

        public static String getLocationSettingFromUri(Uri uri) {
            String locationsetting = uri.getPathSegments().get(1);
            return locationsetting.toString();
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }

        public static List<String> getMultiLocationFromUri(Uri uri) {
            List<String> params = new ArrayList<String>() ;
            List<String> list = uri.getQueryParameters(COLUMN_LOCATION_KEY);
             for(int i = 0; i < list.size(); i++) {
                 params.add( list.get(i).toUpperCase());
             }
            return params;
        }

        private static Uri createUriPathWithDate(Uri uri, String elem, long date) {
            return createUriPath(createUriPath(uri,elem), String.valueOf(date));
        }
    }


    private static Uri createUriPath(Uri uri, String elem) {
        return uri.buildUpon().appendPath(elem).build();
    }


    private static String createAutorityPath(String basetype, String location) {
        return basetype + SEPARATEUR + CONTENT_AUTHORITY + location;
    }
}
