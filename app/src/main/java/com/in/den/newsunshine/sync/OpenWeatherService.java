package com.in.den.newsunshine.sync;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.List;
import com.google.gson.Gson;

/**
 * Created by harumi on 19/10/2016.
 */

public class OpenWeatherService {


    public static String queryServer(String locationSetting) throws IOException {
        String jsonString = "";


        final String FORECAST_BASE_URL =
                "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";
        final String APPID_PARAM = "APPID";

        //.appendQueryParameter(AUTORI_PARAM, "4594e014782b299142a29adc95b89c10")


        String format = "json";
        String units = "metric";
        int numDays = 7;

        Uri reqUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationSetting)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
               //.appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .appendQueryParameter(APPID_PARAM, "4594e014782b299142a29adc95b89c10")
                .build();



        URL url = new URL(reqUri.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        InputStream is = urlConnection.getInputStream();
        if(is != null) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                String s;
                StringBuffer sb = new StringBuffer();
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }

                jsonString = sb.toString();
            }
            finally {
                br.close();
            }
        }

        return jsonString;
    }

    public static OpenWeather parseJSonString(String jsonstring) {
        Gson gson = new Gson();
        OpenWeather ow = gson.fromJson(jsonstring, OpenWeather.class);

        return ow;
    }

    public static OpenWeather queryAndparseServer(String localsetting) throws IOException {
        String jsons = queryServer(localsetting);
        OpenWeather ow =  parseJSonString(jsons);
        ow.getCity().setLocationSetting(localsetting);
        return ow;
    }

    /*
    {
"city":
{
	"id":2988507,
	"name":"Paris",
	"coord"{"lon":2.3488,"lat":48.853409},
	"country":"FR",
	"population":0
},
"cod":"200",
"message":0.0122,
"cnt":7,
"list":[
{
"dt":1476874800,
"temp":{"day":12.6,"min":8.61,"max":12.6,"night":8.61,"eve":11.01,"morn":12.6},
"pressure":1022.54,
"humidity":93,
"weather":
[
{
"id":801,
"main":"Clouds",
"description":"few clouds",
"icon":"02d"
}
],
"speed":4.41,
"deg":296,
"clouds":20
},{...}
]
}
     */

    public static class OpenWeather {
        private City city;
        private List<Forcast> list;

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public List<Forcast> getForcast() {
            return list;
        }

        public void setForcast(List<Forcast> list) {
            this.list = list;
        }

    }

    public static class City {
        private String id; //location setting vlaue
        private String name;
        private String country;
        private Coord coord;
        private String locationSetting; //location setting et name pouvent être différents

        public String getCountry() {return country;}

        public void setCountry() {this.country = country;}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }

        public Coord getCoord() {return coord;}

        public void setLocationSetting(String locationSetting) {this.locationSetting = locationSetting;}

        public String getLocationSetting() {return locationSetting;}

    }

    public static class Coord {
        private double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        private double lat;


    }


    public static class Forcast {
        private long dt;
        private Temp temp;
        private String humidity;
        private List<Weather> weather;
        private String pressure;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public Temp getTemp() {
            return temp;
        }

        public void setTemp(Temp temp) {
            this.temp = temp;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getPressure() {
            return pressure;
        }
    }

    public static class Weather {
        private int id;
        private String description;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShortDesc() {
            return description;
        }
        public void setShortDesc(String description) {
            this.description = description;
        }
    }

    public static class Temp {
        private String min;
        private String max;
        private String day;
        private String night;
        private String eve;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getMorn() {
            return morn;
        }

        public void setMorn(String morn) {
            this.morn = morn;
        }

        public String getEve() {
            return eve;
        }

        public void setEve(String eve) {
            this.eve = eve;
        }

        public String getNight() {
            return night;
        }

        public void setNight(String night) {
            this.night = night;
        }

        private String morn;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }


    }


}
