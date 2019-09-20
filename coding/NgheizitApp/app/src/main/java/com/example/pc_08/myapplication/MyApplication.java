package com.example.pc_08.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by PC-00 on 2019/9/16.
 */

public class MyApplication extends Application {
    public static Context context;
    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
    }
}
