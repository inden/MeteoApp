package com.in.den.newsunshine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.in.den.newsunshine.sync.SunshineSyncAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Call SyncAdapter
        //For the first usage, create an Account and set automatique synchoronize mecanisme
        //(Every 3 hours) and connect to weatherservice to fill the database
        SunshineSyncAdapter.initSync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Locations: {
                Intent intent = new Intent(this, LocationActivity.class);
                this.startActivity(intent);

                return true;
            }
            case R.id.action_settings : {
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return  true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
