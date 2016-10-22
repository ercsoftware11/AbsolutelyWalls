package com.viztushar.osumwalls.items;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

/**
 * Created by architjn on 29/07/15.
 */
public class WallpaperItem implements Parcelable {

    private boolean favorite;
    private String name, author, url, thumb;

    public WallpaperItem(Context context, String name, String author, String url, String thumb) {
        this.name = name;
        this.author = author;
        this.url = url;
        this.thumb = thumb;

        favorite = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(url, false);
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(Context context, boolean favorite) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(url, favorite).apply();
        this.favorite = favorite;
    }

    protected WallpaperItem(Parcel in) {
        favorite = in.readByte() != 0;
        name = in.readString();
        author = in.readString();
        url = in.readString();
        thumb = in.readString();
    }

    public static final Creator<WallpaperItem> CREATOR = new Creator<WallpaperItem>() {
        @Override
        public WallpaperItem createFromParcel(Parcel in) {
            return new WallpaperItem(in);
        }

        @Override
        public WallpaperItem[] newArray(int size) {
            return new WallpaperItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(url);
        dest.writeString(thumb);
    }
}
