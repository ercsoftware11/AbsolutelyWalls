package com.viztushar.osumwalls.others;

import android.content.Context;

public class SharedPreferences {

    android.content.SharedPreferences prefs;
    android.content.SharedPreferences.Editor editor;

    private String file = "preferences";
    Context context;

    public SharedPreferences(Context context) {
        this.context = context;
    }

    public SharedPreferences(String file, Context context) {
        this.file = file;
        this.context = context;
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        prefs = context.getSharedPreferences(file, 0);
        return prefs.getBoolean(name, defaultValue);
    }

    public int getInteger(String name, int DefaultValue) {
        prefs = context.getSharedPreferences(file, 0);
        return prefs.getInt(name, DefaultValue);
    }

    public String getString(String name, String defaultValue) {
        prefs = context.getSharedPreferences(file, 0);
        return prefs.getString(name, defaultValue);
    }

    public void saveBoolean(String name, boolean value) {
        prefs = context.getSharedPreferences(file, 0);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public void saveInteger(String name, int value) {
        prefs = context.getSharedPreferences(file, 0);
        editor = prefs.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public void saveString(String name, String value) {
        prefs = context.getSharedPreferences(file, 0);
        editor = prefs.edit();
        editor.putString(name, value);
        editor.apply();
    }
}