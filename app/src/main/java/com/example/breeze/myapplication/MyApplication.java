package com.example.breeze.myapplication;

import android.app.Application;

import im.fir.sdk.FIR;

/**
 * Created by sunhq on 2015/8/25.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {

        FIR.init(this);
        super.onCreate();
    }
}
