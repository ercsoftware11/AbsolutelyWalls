package com.viztushar.osumwalls.items;

import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.viztushar.osumwalls.others.Preferences;

/**
 * Created by architjn on 29/07/15.
 */
public class WallpaperItem {

    public boolean favorite;
    private String name, author, url, thumb;

    public WallpaperItem(String name, String author, String url, String thumb) {
        this.name = name;
        this.author = author;
        this.url = url;
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getThumb() {
        return thumb;
    }
}
