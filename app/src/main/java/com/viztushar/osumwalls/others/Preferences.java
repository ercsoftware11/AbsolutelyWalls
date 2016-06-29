package com.viztushar.osumwalls.others;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String
            PREFERENCES_NAME = "APP_PREFERENCES",
            FAVORITES = "Favorite";

    private final Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

}