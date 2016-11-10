package com.in.den.newsunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import static com.in.den.newsunshine.SunshineUtil.formatTemperature;
import static com.in.den.newsunshine.SunshineUtil.freindlyformatTemperature;

public class DetailActivity extends AppCompatActivity {

    private double iCoordLat;
    private double iCoordLong;
    private static String LOG_TAG = DetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        long idate = intent.getLongExtra("date", 0);
        int icode = intent.getIntExtra("code", 0);
        double iTempday = intent.getDoubleExtra("tempday", 0);
        double iTempmin = intent.getDoubleExtra("tempmin", 0);
        double iTempmax = intent.getDoubleExtra("tempmax", 0);
        double iTempeve = intent.getDoubleExtra("tempeve", 0);
        double iTempmorn = intent.getDoubleExtra("tempmorn", 0);
        double iTempnight = intent.getDoubleExtra("tempnight", 0);
        double iHumidity = intent.getDoubleExtra("humidity", 0);
        double iPressure = intent.getDoubleExtra("pressure", 0);
        iCoordLat = intent.getDoubleExtra("coordlat", 0);
        iCoordLong = intent.getDoubleExtra("coordlong", 0);

        String desc = intent.getStringExtra("desc");
        String location = intent.getStringExtra("location");

        TextView textviewLoc = (TextView)findViewById(R.id.detail_location);
        TextView textviewDate = (TextView)findViewById(R.id.detail_date);
        TextView textviewDesc = (TextView)findViewById(R.id.detail_desc);
        TextView textviewTempmorn = (TextView)findViewById(R.id.detail_tempmorn);
        TextView textviewTempday = (TextView)findViewById(R.id.detail_tempday);
        TextView textviewTempeve = (TextView)findViewById(R.id.detail_tempeve);
        TextView textviewTempnight = (TextView)findViewById(R.id.detail_tempnight);
        TextView textviewTempmax = (TextView)findViewById(R.id.detail_tempmax);
        TextView textviewTempmin = (TextView)findViewById(R.id.detail_tempmin);
        TextView textviewPress = (TextView)findViewById(R.id.detail_pression);
        TextView textviewHumid = (TextView)findViewById(R.id.detail_humidity);
        ImageView imageView = (ImageView)findViewById(R.id.detail_image);

        Locale locale = getResources().getConfiguration().locale;

        SunshineUtil.DateStringHolder dateStringHolder = SunshineUtil.formatLongToDate(idate, locale);

        textviewTempmorn.setText(freindlyformatTemperature(iTempmorn, false));
        textviewTempday.setText(freindlyformatTemperature(iTempday, false));
        textviewTempeve.setText(freindlyformatTemperature(iTempeve, false));
        textviewTempnight.setText(freindlyformatTemperature(iTempnight, false));
        textviewTempmin.setText(freindlyformatTemperature(iTempmin, false));
        textviewTempmax.setText(freindlyformatTemperature(iTempmax, false));
        textviewPress.setText(String.valueOf(iPressure));
        textviewHumid.setText(String.valueOf(iHumidity));
        textviewLoc.setText(location);
        textviewDesc.setText(desc);
        textviewDate.setText(dateStringHolder.dayofweek + " " + dateStringHolder.day +
                    dateStringHolder.month + " " + dateStringHolder.year);
        imageView.setImageResource(SunshineUtil.getArtResourceForWeatherCondition(icode));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map: {
               // Example: "geo:47.6,-122.3"
                Uri uri = Uri.parse("geo:"+ String.valueOf(iCoordLat) + ","
                        + String.valueOf(iCoordLong) + "?z=9");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "Couldn't call map, no receiving apps installed!");
                }

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
