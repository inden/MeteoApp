package com.in.den.newsunshine.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by harumi on 18/10/2016.
 */

public class SunshineSyncService extends Service {

    private static Object mLock = new Object();
    private static SunshineSyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {

        synchronized (mLock) {
            syncAdapter = new SunshineSyncAdapter(getApplicationContext(), true);
        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
