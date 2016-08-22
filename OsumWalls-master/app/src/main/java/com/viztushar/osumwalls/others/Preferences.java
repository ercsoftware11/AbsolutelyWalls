package com.viztushar.osumwalls.others;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.viztushar.osumwalls.items.WallpaperItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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