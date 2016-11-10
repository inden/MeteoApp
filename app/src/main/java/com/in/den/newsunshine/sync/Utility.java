package com.in.den.newsunshine.sync;

import android.content.ContentValues;
import android.graphics.Path;

import com.in.den.newsunshine.data.SunshineContract;

/**
 * Created by harumi on 19/10/2016.
 */

public class Utility {

    public static ContentValues getContentValuesForLocation(OpenWeatherService.OpenWeather ow) {
        ContentValues cv = new ContentValues();
        cv.put(SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING, ow.getCity().getLocationSetting().toUpperCase());
        cv.put(SunshineContract.LocationEntry.COLUMN_CITYID, ow.getCity().getId());
        cv.put(SunshineContract.LocationEntry.COLUMN_CITYCOUNTRY, ow.getCity().getCountry());
        cv.put(SunshineContract.LocationEntry.COLUMN_CITYCOORD_LAT, ow.getCity().getCoord().getLat());
        cv.put(SunshineContract.LocationEntry.COLUMN_CITYCOORD_LON, ow.getCity().getCoord().getLon());
        cv.put(SunshineContract.LocationEntry.COLUMN_CITYNAME, ow.getCity().getName());

        return cv;
    }

    public static ContentValues[] getContentValuesForWeather(OpenWeatherService.OpenWeather ow) {

        int size = ow.getForcast().size();
        ContentValues[] listcv = new ContentValues[size];

        for(int i = 0; i < size; i++) {
            ContentValues cv = new ContentValues();
            OpenWeatherService.Forcast fc = ow.getForcast().get(i);

            cv.put(SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY, ow.getCity().getLocationSetting().toUpperCase());
            cv.put(SunshineContract.WeatherEntry.COLUMN_HUMIDITY, fc.getHumidity());
            cv.put(SunshineContract.WeatherEntry.COLUMN_PRESSURE, fc.getPressure());
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_MIN, fc.getTemp().getMin());
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_MAX, fc.getTemp().getMax());
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_DAY, fc.getTemp().getDay());
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_NIGHT, fc.getTemp().getNight());
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_EVE, fc.getTemp().getEve());
            cv.put(SunshineContract.WeatherEntry.COLUMN_TEMP_MORN, fc.getTemp().getMorn());
            cv.put(SunshineContract.WeatherEntry.COLUMN_DATE, fc.getDt());
            cv.put(SunshineContract.WeatherEntry.COLUMN_SHORT_DESC, fc.getWeather().get(0).getShortDesc());
            cv.put(SunshineContract.WeatherEntry.COLUMN_WEATHER_ID, fc.getWeather().get(0).getId());

            listcv[i] = cv;
        }

        return  listcv;
    }
}
