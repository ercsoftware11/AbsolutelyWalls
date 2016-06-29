package com.viztushar.osumwalls;

import android.app.Application;

import com.batch.android.Batch;
import com.batch.android.Config;

/**
 * Created by CANNO-EM12W on 6/28/2016.
 */
public class AbsolutelyWallsClass extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        Batch.Push.setGCMSenderId("417024638503");
        Batch.Push.setSmallIconResourceId(R.mipmap.ic_launcher_aw);
        // TODO : switch to live Batch Api Key before shipping
        // Batch.setConfig(new Config("DEV574BF15290398ADAFCB5365BD16")); // devloppement
        Batch.setConfig(new Config("574BF1528E3A2DF1FA33340F2784A8")); // live
    }
}

