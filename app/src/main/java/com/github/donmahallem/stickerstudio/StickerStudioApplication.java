package com.github.donmahallem.stickerstudio;

import android.app.Application;

import timber.log.Timber;

/**
 * Created on 13.10.2018.
 */
public class StickerStudioApplication extends Application {
@Override
    public void onCreate(){
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
}
}
