package com.in.den.newsunshine.sync;

import android.net.Uri;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by harumi on 19/10/2016.
 */
public class OpenWeatherTest {
    @Test
    public void queryServer() throws Exception {

        Uri.parse("http://www.google.fr").buildUpon().appendQueryParameter("q","titi");

        String s = OpenWeatherService.queryServer("chaudière");

        Log.d("test", s);
        System.out.println(s);

        assertNotEquals("", s);

        OpenWeatherService.OpenWeather ow = OpenWeatherService.parseJSonString(s);

        assertNotEquals(null, ow);


    }

}