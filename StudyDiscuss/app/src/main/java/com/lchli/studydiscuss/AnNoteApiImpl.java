package com.lchli.studydiscuss;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.api.AnNoteApi;

/**
 * Created by lichenghang on 2017/3/30.
 */

public class AnNoteApiImpl implements AnNoteApi{


    @Override
    public void onAppCreate(Application application) {
        StudyApp.instance().onCreate(application);
    }

    @Override
    public void launch(Context context) {

        Intent it=new Intent(context,SplashActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);

    }
}
