package edu.upc.mcia.publications;

import android.app.Application;
import android.content.Context;

import jonathanfinerty.once.Once;
import timber.log.Timber;

public class App extends Application {

    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Once.initialise(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
