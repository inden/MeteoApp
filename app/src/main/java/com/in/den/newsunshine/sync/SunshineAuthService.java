package com.in.den.newsunshine.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by harumi on 18/10/2016.
 */

public class SunshineAuthService extends Service {
    private SunshineAuthenticator authenticator;

    @Override
    public void onCreate() {
        authenticator = new SunshineAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
