package com.example.yurja.wallpaper;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "0150a7cd28ca576cc5ad218796b3a353");
    }
}
