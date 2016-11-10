package com.in.den.newsunshine.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.in.den.newsunshine.MainActivity;
import com.in.den.newsunshine.R;
import com.in.den.newsunshine.SharedPreferenceOp;
import com.in.den.newsunshine.data.SunshineContract;
import com.in.den.newsunshine.data.SunshineDbQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by harumi on 18/10/2016.
 */

public class SunshineSyncAdapter extends AbstractThreadedSyncAdapter {

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    private static final String NEWLOCATIONS= "newlocations";
    private static final String NOTDELETENOW = "notdeletenow";

    public SunshineSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SunshineSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    /*
    This method is called by the sync manager when it’s sync time. This method operates from a
    background thread, so no worries while doing network calls.
    A sync adapter can be tied to one account type on the device, but there could be multiple users
    signed in to this account type. For example, you can connect with more than one Google account
    on your device, and the “Calendar” for both accounts will be synced to your device, using
    sync adapters. This method receives as an argument the current account the sync manager has
    requested the sync for.

    */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        try {

            SharedPreferenceOp sharedPreferenceOp = new SharedPreferenceOp(getContext());
            List<String> preflocations = sharedPreferenceOp.getPreferenceLocations();

            List<String> locations;
            List<String> newloc = new ArrayList<String >();
            String snewlocations = extras.getString(NEWLOCATIONS);

            if(snewlocations != null && !snewlocations.isEmpty()) {
                String[] tloc = snewlocations.split("#");
                newloc = new ArrayList<String>(Arrays.asList(tloc));
            }

            if(newloc.size() > 0) {
                locations = newloc;
            }
            else {
                locations = preflocations;
            }

            for (int i = 0; i < locations.size(); i++) {
                addWeather(locations.get(i));
            }

            //delete places here
            Boolean b = extras.getBoolean(NOTDELETENOW);

            if((b == null || (b != null && b!=true)) && preflocations.size() > 0) {


                    getContext().getContentResolver().delete(SunshineContract.WeatherEntry.CONTENT_URI,
                            SunshineDbQuery.prepareLocationKeyNotInCondition(preflocations.size()),
                            SunshineDbQuery.toArrayLocationList(preflocations));

                    getContext().getContentResolver().delete(SunshineContract.LocationEntry.CONTENT_URI,
                            SunshineDbQuery.prepareLocationotNotInCondition(preflocations.size()),
                            SunshineDbQuery.toArrayLocationList(preflocations));

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addWeather(String location) {
        try {

            OpenWeatherService.OpenWeather ow = OpenWeatherService.queryAndparseServer(location);
            ContentValues cv = Utility.getContentValuesForLocation(ow);
            ContentValues[] cvlist = Utility.getContentValuesForWeather(ow);

            addLocation(cv);

            addWeather(cvlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addLocation(ContentValues cv) {
        String locid = cv.getAsString(SunshineContract.LocationEntry.COLUMN_LOCATION_SETTING);
        Uri uri = SunshineContract.LocationEntry.CONTENT_URI.buildUpon().appendPath(locid).build();
        Cursor cur = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cur.getCount() == 0) {
            getContext().getContentResolver().insert(SunshineContract.LocationEntry.CONTENT_URI, cv);
        }
        cur.close();
    }

    private void addWeather(ContentValues[] cvlist) {
        String key = cvlist[0].getAsString(SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY);

        getContext().getContentResolver().delete(SunshineContract.WeatherEntry.CONTENT_URI,
                SunshineContract.WeatherEntry.COLUMN_LOCATION_KEY + "=?", new String[]{key});

        getContext().getContentResolver().bulkInsert(SunshineContract.WeatherEntry.CONTENT_URI, cvlist);


    }

    /***************************************************************
     * Methods to set synchronize mecanism
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));


        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SunshineSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        syncImmediately(context, null, false);
    }

    public static void syncImmediately(Context context, ArrayList<String> locations, boolean bnotdelete) {
        Bundle bundle = new Bundle();

        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        if(locations != null && locations.size() !=0) {
            String slocation= "";
            for(int i= 0; i < locations.size(); i++ ) {
                slocation += locations.get(i);
                if(i != locations.size() - 1) {
                    slocation += "#";
                }
            }

            bundle.putString(NEWLOCATIONS, slocation);
            //the line below throws an exception
            //java.lang.IllegalArgumentException: unexpected value type: java.util.ArrayList
            // bundle.putStringArrayList(NEWLOCATIONS, locations);
        }

        if(bnotdelete) {
            bundle.putBoolean(NOTDELETENOW, bnotdelete);
        }

        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static void initSync(Context context) {
        getSyncAccount(context);
    }


}
