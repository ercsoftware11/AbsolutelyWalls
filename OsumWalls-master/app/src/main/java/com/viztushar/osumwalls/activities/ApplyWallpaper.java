package com.viztushar.osumwalls.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.dialogs.ISDialogs;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.PermissionUtils;
import com.viztushar.osumwalls.others.Preferences;
import com.viztushar.osumwalls.others.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Tushar on 15-05-2016.
 */
public class ApplyWallpaper extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    ProgressBar mProgress;
    private FloatingActionButton fab1;
    public String walls, saveWallLocation, wallname;
    Activity context;
    Preferences mPrefs;
    TextView mTextWall;
    LinearLayout wallbg;
    WallpaperItem wallpaperItem;
    LinearLayout btnSave;
    final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    boolean permission;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        context = this;
        mPrefs = new Preferences(this);
        saveWallLocation = Environment.getExternalStorageDirectory().getAbsolutePath()
                + context.getResources().getString(R.string.walls_save_location);
        imageView = (ImageView) findViewById(R.id.walls2);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);
        mTextWall = (TextView) findViewById(R.id.wallname);
        wallbg = (LinearLayout) findViewById(R.id.wallbg);
        fab1 = (FloatingActionButton) findViewById(R.id.fav_fab);
//        if(wallpaperItem.favorite) fab1.setImageResource(R.drawable.ic_favorite_border);
        fab1.setOnClickListener(this);
        btnSave = (LinearLayout) findViewById(R.id.apply);
        btnSave.setOnClickListener(this);
        walls = getIntent().getStringExtra("walls");
        wallname = getIntent().getStringExtra("wallname");
        Log.d("tag", "onCreate() returned: " + walls);
        mTextWall.setText(wallname);

        if (walls != null) {
            Glide.with(this)
                    .load(walls)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop(this))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .thumbnail(0.5f)
                    .into(imageView);
        }

        Glide.with(this)
                .asBitmap()
                .load(walls)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onGenerated(Palette palette) {
                                    setColors(context, palette);
                                }
                            });
                        }
                    }
                });


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setColors(Context colors, Palette palette) {
        // getWindow().setStatusBarColor(ContextCompat.getColor(colors, palette.getLightMutedColor(Color.DKGRAY)));
        fab1.setBackgroundTintList(ColorStateList.valueOf(palette.getDarkVibrantColor(Color.DKGRAY)));
        wallbg.setBackgroundColor(palette.getLightVibrantColor(Color.DKGRAY));

    }
    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return Uri.parse("");
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply:
                if (!PermissionUtils.canAccessStorage(context)) {
                    PermissionUtils.setViewerActivityAction("save");
                    PermissionUtils.requestStoragePermission(context);
                } else {
                   // showDialogs("save");
                }
                break;


            case R.id.fav_fab:

//        fab1.setImageResource(wallpaperItem.favorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
            wallpaperItem.setFav(ApplyWallpaper.this, !wallpaperItem.favorite);
                break;
        }
    }



/*
 final WallpaperManager manager = WallpaperManager.getInstance(this);
                Glide.with(this)
                        .asBitmap()
                        .load(walls)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                try {
                                    manager.setBitmap(resource);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                //Snackbar.make(findViewById(R.id.menu_labels_right), "Wallpaper Set!!", Snackbar.LENGTH_SHORT).show();
*/





}
