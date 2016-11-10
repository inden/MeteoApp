package com.in.den.newsunshine;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.in.den.newsunshine.data.SunshineContract;
import com.in.den.newsunshine.data.SunshineDbQuery;
import com.in.den.newsunshine.sync.SunshineSyncAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandableListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> ,
MyCursorTreeAdapter.Callback{

    private static final String SELECTED_KEY = "selected_key";
    MyCursorTreeAdapter mCursorTreeAdapter;
    ArrayList<String> mPreferedlocList;
    private static final int FORECAST_LOADER = 0;
    private static final int PREFLOCATIONS_LOADER = -1;

    private static boolean bFirstTime = true;

    private int mPosition = ListView.INVALID_POSITION;
    ExpandableListView expandableListView;

    public ExpandableListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_expandable_list, container, false);

        expandableListView =
                (ExpandableListView)root.findViewById(R.id.expandableListView);

        mCursorTreeAdapter = new MyCursorTreeAdapter(null, getContext());
        mCursorTreeAdapter.setCallback(this);

        expandableListView.setAdapter(mCursorTreeAdapter);

        expandableListView.setSmoothScrollbarEnabled(true);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                expandableListView.smoothScrollToPosition(groupPosition);

                mPosition = groupPosition;

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                mPosition = groupPosition;

                Cursor cursor = (Cursor)parent.getExpandableListAdapter().getChild(groupPosition,childPosition);

                long idate = cursor.getLong(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_DATE));
                int icode = cursor.getInt(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_WEATHER_ID));
                double iTempday = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_DAY));
                double iTempmin = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_MIN));
                double iTempmax = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_MAX));
                double iTempeve = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_EVE));
                double iTempmorn = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_MORN));
                double iTempnight = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_TEMP_NIGHT));

                double iHumidity = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_HUMIDITY));
                double iPressure = cursor.getDouble(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_PRESSURE));
                String desc = cursor.getString(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_SHORT_DESC));
                String location = cursor.getString(cursor.getColumnIndex(SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY));
                double iCoordLat = cursor.getDouble(cursor.getColumnIndex(SunshineContract.LocationEntry.COLUMN_CITYCOORD_LAT));
                double iCoordLon = cursor.getDouble(cursor.getColumnIndex(SunshineContract.LocationEntry.COLUMN_CITYCOORD_LON));



                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("date", idate);
                intent.putExtra("code", icode);
                intent.putExtra("tempday", iTempday);
                intent.putExtra("tempmin", iTempmin);
                intent.putExtra("tempmax", iTempmax);
                intent.putExtra("tempeve", iTempeve);
                intent.putExtra("tempmorn", iTempmorn);
                intent.putExtra("tempnight", iTempnight);
                intent.putExtra("humidity", iHumidity);
                intent.putExtra("pressure", iPressure);
                intent.putExtra("desc", desc);
                intent.putExtra("location", location);
                intent.putExtra("coordlat", iCoordLat);
                intent.putExtra("coordLong", iCoordLon);

                getContext().startActivity(intent);

                return true;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return root;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        if(bFirstTime) {
            SunshineSyncAdapter.syncImmediately(getContext());
            bFirstTime = false;
        }

        SharedPreferenceOp sharedPreferenceOp = new SharedPreferenceOp(getContext());
        mPreferedlocList = sharedPreferenceOp.getPreferenceLocations();

        Loader<Cursor> loader = getLoaderManager().getLoader(PREFLOCATIONS_LOADER);
        if (loader != null && !loader.isReset()) {
            getLoaderManager().restartLoader(PREFLOCATIONS_LOADER, null, this);
        } else {
            getLoaderManager().initLoader(PREFLOCATIONS_LOADER, null, this);
        }

        super.onActivityCreated(savedInstanceState);
    }


    @Override

    public void onResume() {
        SharedPreferenceOp sharedPreferenceOp = new SharedPreferenceOp(getContext());
        ArrayList<String> locations = sharedPreferenceOp.getPreferenceLocations();

        ArrayList<String> newlocations = getNewLocations(locations);
        boolean bDeleted = isLocationDeleted(locations);

        if (newlocations.size() > 0 || bDeleted) {
            //new element added
            if(newlocations.size() > 0) {
                SunshineSyncAdapter.syncImmediately(getContext(), newlocations, true);
            }

            getLoaderManager().restartLoader(PREFLOCATIONS_LOADER, null, this);
            mPreferedlocList = locations;
        }

        super.onResume();
    }

    private boolean isLocationDeleted(ArrayList<String> locations) {
        boolean bnotfound = false;
        for (int i = 0; i < mPreferedlocList.size(); i++) {
            String l = mPreferedlocList.get(i);
            if(!locations.contains(l)) {
               bnotfound = true;
                break;
            }
        }
        return bnotfound;
    }

    private ArrayList<String> getNewLocations(ArrayList<String> locations) {

        ArrayList<String> newloclist = new ArrayList<String>();
        for (int i = 0; i < locations.size(); i++) {
            String l = locations.get(i);
            if(!mPreferedlocList.contains(l)) {
                newloclist.add(l);
            }
        }
        return newloclist;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

     /***************************************************************************
     ****************************************************************************
     ** Implements 3 méthodes from LoaderCallback
     * ****************************************************************************
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id != -1) {
            // child cursor for each city
            String locationname = mCursorTreeAdapter.getLocationSettingMap().get(id);
            Uri uri = SunshineContract.WeatherEntry.buildWeatherLocation(locationname);
            //String[] projection = new String[]{SunshineContract.LocationEntry.COLUMN_CITYNAME, SunshineContract.LocationEntry.COLUMN_CITYCOUNTRY};
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = null;

            return new CursorLoader(getContext(),uri, null, selection, selectionArgs, sortOrder);

        } else {

            Uri uri = SunshineContract.WeatherEntry.CONTENT_URI_TODAY;
            return new CursorLoader(getContext(), uri, null, null, null, null);
        }
     }

    public static String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        if (id != -1) {
            // child cursor
            if (!data.isClosed()) {
                try {
                    int groupPos = mCursorTreeAdapter.getGroupMap().get(id);

                    mCursorTreeAdapter.setChildrenCursor(groupPos, data);

                    if (mPosition != ListView.INVALID_POSITION) {
                        expandableListView.smoothScrollToPosition(mPosition);
                    }


                } catch (NullPointerException e) {}
            }
        } else {
            //parent cursor
            mCursorTreeAdapter.setGroupCursor(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        if (id != -1) {
            // child cursor
            try {
                mCursorTreeAdapter.setChildrenCursor(id, null);
            } catch (NullPointerException e) {}
        } else {
            mCursorTreeAdapter.setGroupCursor(null);
        }
    }
    /**************************************
    call back 4 treeadapter
     *************************************/

    @Override
    public void loadChildCursor(int groupId) {
        Loader<Cursor> loader = getLoaderManager().getLoader(groupId);
        if (loader != null && !loader.isReset()) {
            getLoaderManager()
                    .restartLoader(groupId, null, this);
        } else {
            getLoaderManager().initLoader(groupId, null, this);
        }
    }
}
