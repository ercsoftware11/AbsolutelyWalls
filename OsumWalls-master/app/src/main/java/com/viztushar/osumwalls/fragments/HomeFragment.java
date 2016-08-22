package com.viztushar.osumwalls.fragments;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.viztushar.osumwalls.BuildConfig;
import com.viztushar.osumwalls.MainActivity;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.adapter.WallAdapter;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.tasks.GetWallpapers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements GetWallpapers.Callbacks {

    private View mainView;
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<WallpaperItem> items;
    private DrawerLayout drawer;
    String wall;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.wall_fragment, container, false);
        context = mainView.getContext();
        init();

        items = new ArrayList<>();
        new GetWallpapers(context,this).execute();
        return mainView;
    }

    @SuppressLint("ValidFragment")
    public HomeFragment(String wall){
        this.wall=wall;
        if(wall != null){
            Log.i("wallpaper", "onCreateView: "+wall);
        }
    }


    private void init() {
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBarTitle(wall);
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.TaskDescription taskDescription = new
                    ActivityManager.TaskDescription(getResources().getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                    getResources().getColor(R.color.colorPrimary));
            getActivity().setTaskDescription(taskDescription);
        }

        getHomeActivity().setNav(toolbar);

    }

    private MainActivity getHomeActivity() {
        return ((MainActivity) getActivity());
    }

    private ActionBar getActionBar() {
        return ((MainActivity) getActivity()).getSupportActionBar();
    }

    private void setActionBar(Toolbar toolbar) {
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
    }
    private void setActionBarTitle(String title)
    {
        if(title != null){
            getActionBar().setTitle(title);
        }
        else
        {
            getActionBar().setTitle("Absolutely Wallpapers");
        }
    }

    private void getActionBarTitle(String wall)
    {
        if(wall != null)
        {
            if(wall == "walls")
            {
                setActionBarTitle("All");
            }
            else if (wall == "new")
            {
                setActionBarTitle("New");
            }
            else if (wall == "material")
            {
                setActionBarTitle("Material");
            }
            else if (wall == "landscape")
            {
                setActionBarTitle("Landscapes");
            }
            else if (wall == "nature")
            {
                setActionBarTitle("Nature");
            }
            else if (wall == "sea")
            {
                setActionBarTitle("Sea");
            }
            else if (wall == "city")
            {
                setActionBarTitle("City");
            }
            else if (wall == "vintage")
            {
                setActionBarTitle("Vintage");
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            drawer.openDrawer(Gravity.LEFT);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setDrawer(DrawerLayout drawer) {
        this.drawer = drawer;
    }

    @Override
    public void onListLoaded(String jsonResult) {
        try {
            if (jsonResult != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonResult);
                    JSONArray jsonMainNode = jsonResponse.optJSONArray(wall);
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        items.add(new WallpaperItem(getContext(),jsonChildNode.optString("name"),
                                jsonChildNode.optString("author"),
                                jsonChildNode.optString("url"),
                                jsonChildNode.optString("thumb")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView)mainView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(new WallAdapter(context,items));
    }
}
