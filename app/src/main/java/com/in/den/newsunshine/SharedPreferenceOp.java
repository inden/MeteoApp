package com.in.den.newsunshine;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by harumi on 28/10/2016.
 */

public class SharedPreferenceOp {

    Context mContext;
    public static final String PREFS_NAME = "NEWSUNSHINE_APP";
    public static final String JSONARRAYSTRING = "[]";
    private static SharedPreferences mSettings;
    private Gson gson;
    private boolean bDefaultLocation;
    private String mDefaultLocation;

    public SharedPreferenceOp(Context context) {
        mContext = context;
        mSettings =  mContext.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        gson = new Gson();
        bDefaultLocation = true;
        mDefaultLocation = mContext.getString(R.string.pref_location_default);
    }

    public ArrayList<String> getPreferenceLocations() {

        String jsonstring = mSettings.getString(mContext.getString(R.string.pref_location_key),
                JSONARRAYSTRING);

        ArrayList<String> loclist = new ArrayList<String>();

        try {

            loclist = gson.fromJson(jsonstring, ArrayList.class);
        }
        catch (Exception ex) {}

        if(bDefaultLocation && loclist.size() == 0) {
            loclist.add(mDefaultLocation);
        }

        return loclist;
    }

    private void addOrRemoveLocation(String newloc, boolean badd) {

        ArrayList<String> locations = getPreferenceLocations();

        if(badd) {
            locations.add(newloc);
        }
        else {
            locations.remove(newloc);
        }

        String newjson = gson.toJson(locations);

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(mContext.getString(R.string.pref_location_key), newjson);
        editor.commit();

    }

    public void addNewLocation(String newloc) {
        addOrRemoveLocation(newloc, true);
    }

    public void removeLocation(String newloc) {
        addOrRemoveLocation(newloc, false);
    }


}
