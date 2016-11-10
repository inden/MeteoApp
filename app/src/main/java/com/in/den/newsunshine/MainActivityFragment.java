package com.in.den.newsunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.in.den.newsunshine.data.SunshineContract;
import com.in.den.newsunshine.sync.SunshineSyncAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    MyCursorAdapter mCursorAdapter;
    ArrayList<String> mPreferedlocList;
    private static final int FORECAST_LOADER = 0;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview =  inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView)rootview.findViewById(R.id.listview_main);

        //It's important to set the last parameter to 0!!
         mCursorAdapter =  new MyCursorAdapter(getContext(),  null,  0 );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Open detail activity
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if(cursor != null) {

                    Long date = cursor.getLong( cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_DATE));

                    Uri uri = SunshineContract.WeatherEntry.buildWeatherLocationWithDate("123", date);
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    //intent.setData(uri);
                    intent.putExtra("date", date);
                    getContext().startActivity(intent);
                }
            }
        });

        listView.setAdapter(mCursorAdapter);

        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        SharedPreferenceOp sharedPreferenceOp = new SharedPreferenceOp(getContext());
        mPreferedlocList = sharedPreferenceOp.getPreferenceLocations();

        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

/*
    @Override
    public void onResume() {
        SharedPreferenceOp sharedPreferenceOp = new SharedPreferenceOp(getContext());
        ArrayList<String> locations = sharedPreferenceOp.getPreferenceLocations();

        if (!mPreferedlocList.containsAll(locations)) {
            //new element added
            SunshineSyncAdapter.syncImmediately(getContext());
            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
        }

        List<String> deletedlist = isLocationsDeleted(locations);
        //async to clean database

        mPreferedlocList = locations;

        super.onResume();
    }*/



    /***************************************************************************
     * ***************************************************************************
    ** Implements 3 méthodes from LoaderCallback
     */

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        ArrayList<String> locations = new SharedPreferenceOp(getContext()).getPreferenceLocations();

        //Uri uri = SunshineContract.WeatherEntry.buildWeatherLocation("123");
        Uri uri = SunshineContract.WeatherEntry.buildWeatherMultiLocations(locations);
        String[] projection = null;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        return new CursorLoader(getContext(),uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
