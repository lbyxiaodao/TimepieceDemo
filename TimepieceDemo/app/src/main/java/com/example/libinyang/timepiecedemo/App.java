package com.example.libinyang.timepiecedemo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Li Bin Yang on 2017/12/8.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
