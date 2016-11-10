package com.in.den.newsunshine.sync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.test.RenamingDelegatingContext;

import com.in.den.newsunshine.data.SunshineDbHelper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by harumi on 31/10/2016.
 */
public class SunshineSyncAdapterTest {

    Context mMockContext;
    SunshineSyncAdapter sunshineSyncAdapter;



    @Before
    public void setUp() {
        // mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(),"");
        sunshineSyncAdapter = new SunshineSyncAdapter(mMockContext,false);
    }
    @Test
    public void onPerformSync() throws Exception {
        sunshineSyncAdapter.onPerformSync(null, null, null, null, null);
    }

}