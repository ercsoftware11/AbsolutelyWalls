package com.viztushar.osumwalls.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.adapter.WallAdapter;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.SharedPreferences;
import com.viztushar.osumwalls.tasks.GetWallpapers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tushar on 06-06-2016.
 */

public class FavWallaper extends AppCompatActivity implements GetWallpapers.Callbacks {

    public WallAdapter mAdapter;
    SharedPreferences mPre;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<WallpaperItem> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_wall);
        mPre = new SharedPreferences(this);
        toolbar = (Toolbar) findViewById(R.id.fav_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        items = new ArrayList<>();
        new GetWallpapers(this, this).execute();


    }

    @Override
    public void onListLoaded(String jsonResult, boolean newWalls) {
        try {
            if (jsonResult != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonResult);
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("walls");
                    SharedPreferences sharedPreferences = new SharedPreferences(getApplicationContext());
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        if (sharedPreferences.getBoolean(jsonChildNode.getString("name").toLowerCase().replaceAll(" ", "_").trim(), false)) {
                            items.add(new WallpaperItem(jsonChildNode.optString("name"),
                                    jsonChildNode.optString("author"),
                                    jsonChildNode.optString("url"),
                                    jsonChildNode.optString("thumb")));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_fav);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new WallAdapter(getApplicationContext(), items));
    }
}
