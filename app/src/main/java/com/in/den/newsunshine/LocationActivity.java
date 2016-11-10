package com.in.den.newsunshine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements LocationArrayAdapter.Callback {

    LocationArrayAdapter arrayAdapter;
    private SharedPreferenceOp sharedPreferenceOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        sharedPreferenceOp = new SharedPreferenceOp(this);

        ListView listlocation = (ListView) findViewById(R.id.listview_location);
        final EditText newlocation = (EditText)findViewById(R.id.edit_newlocation);
        Button addbutton = (Button)findViewById(R.id.btn_newlocation);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newlocation.getText().toString();
                if(!name.isEmpty()) {
                    //json string here
                    sharedPreferenceOp.addNewLocation(name);
                    updateLocations();

                    newlocation.setText("");
                }
            }
        });

        arrayAdapter =
                new LocationArrayAdapter(this, R.layout.listview_locations_item, new ArrayList<String>());

        arrayAdapter.setCallback(this);
        listlocation.setAdapter(arrayAdapter);
    }


    @Override
    protected void onStart() {
        updateLocations();
        super.onStart();
    }

    public void updateLocations() {

        LocationFetchTask task = new LocationFetchTask(this);
        task.execute();
    }

    /*************************
     * Callback method from Location ArrayAdapter
     */
    @Override
    public void deleteLocation(String location) {
        sharedPreferenceOp.removeLocation(location);
        updateLocations();
    }

    public class LocationFetchTask extends AsyncTask<Void, Void, List<String>> {

        Context context;
        public LocationFetchTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            return sharedPreferenceOp.getPreferenceLocations();
        }

        @Override
        protected void onPostExecute(List<String> res) {
            arrayAdapter.clear();
            arrayAdapter.addAll(res);
        }
    }

}
