package com.lchli.studydiscuss;

import android.app.Application;

/**
 * Created by lichenghang on 2017/3/30.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StudyApp.instance().onCreate(this);
    }
}
