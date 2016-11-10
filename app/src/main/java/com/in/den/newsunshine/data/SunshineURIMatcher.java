package com.in.den.newsunshine.data;

import android.content.UriMatcher;


public final class SunshineURIMatcher  {

    public static final UriMatcher uriMatcher = buildUriMatcher();

    public static final int WEATHER = 100;
    public static final int WEATHER_WITH_LOCATION = 101;
    public static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    public static final int WEATHER_WITH_MULTILOCATION = 103;
    public static final int WEATHER_WITH_MULTILOCATION_AND_DATE = 104;
    public static final int WEATHER_TODAY = 105;
    public static final int LOCATION = 200;
    public static final int LOCATION_CITY = 201;


    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SunshineContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, SunshineContract.PATH_WEATHER, WEATHER);
        matcher.addURI(authority,  SunshineContract.PATH_WEATHER + "/" +
                SunshineContract.PATH_MULTILOCATION, WEATHER_WITH_MULTILOCATION);
        matcher.addURI(authority,  SunshineContract.PATH_WEATHER + "/" +
                SunshineContract.PATH_TODAY, WEATHER_TODAY);
        matcher.addURI(authority,  SunshineContract.PATH_WEATHER + "/" +
                SunshineContract.PATH_MULTILOCATION + "#", WEATHER_WITH_MULTILOCATION_AND_DATE);
        matcher.addURI(authority, SunshineContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        matcher.addURI(authority, SunshineContract.PATH_WEATHER + "/*/#", WEATHER_WITH_LOCATION_AND_DATE);


        matcher.addURI(authority, SunshineContract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, SunshineContract.PATH_LOCATION + "/*", LOCATION_CITY);
        return matcher;
    }

}
