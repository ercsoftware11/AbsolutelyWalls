package com.viztushar.osumwalls;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.viztushar.osumwalls.activities.FavWallaper;
import com.viztushar.osumwalls.adapter.WallAdapter;
import com.viztushar.osumwalls.fragments.HomeFragment;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.tasks.GetWallpapers;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        switchFragment(new HomeFragment("walls"),false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.main_navigationview);
    }

    public void updateToggleButton(Toolbar toolbar) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void setNav(Toolbar toolbar) {
        updateToggleButton(toolbar);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_all:
                        item.setChecked(true);
                        HomeFragment fragment = new HomeFragment("walls");
                        switchFragment(fragment, false);
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.navigation_new:
                        item.setChecked(true);
                        HomeFragment fragment1 = new HomeFragment("new");
                        switchFragment(fragment1, false);
                        mDrawerLayout.closeDrawers();
                        break;
                   /* case R.id.navigation_favorites:
                        item.setChecked(true);
                        Intent intent=new Intent(getApplicationContext(), FavWallaper.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;*/
                    case R.id.navigation_material:
                        item.setChecked(true);
                        HomeFragment fragment3 = new HomeFragment("material");
                        switchFragment(fragment3, false);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_landscape:
                        item.setChecked(true);
                        HomeFragment fragment4 = new HomeFragment("landscape");
                        switchFragment(fragment4, false);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_nature:
                        item.setChecked(true);
                        HomeFragment fragment5 = new HomeFragment("nature");
                        switchFragment(fragment5, false);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_sea:
                        item.setChecked(true);
                        HomeFragment fragment6 = new HomeFragment("sea");
                        switchFragment(fragment6, false);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_city:
                        item.setChecked(true);
                        HomeFragment fragment7 = new HomeFragment("city");
                        switchFragment(fragment7, false);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_vintage:
                        item.setChecked(true);
                        HomeFragment fragment8 = new HomeFragment("vintage");
                        switchFragment(fragment8, false);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_about:
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        startAboutSection();
                        break;

                }
                return true;
            }
        });
        }

    public void startAboutSection(){
        new LibsBuilder()
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutAppName("Absolutely Wallpapers")
                .withAboutDescription("<b>Developers</b><br/>ERC Software and Tushar Parmar<br /><br /><b>Major Authors</b><br/>Zan Cerne <br/>Rutwik Patel<br/>Odney Joseph<br/><br/><b>Changelog</b><br/>Redesigned entire app<br/>Walls are now all server based so new ones can be added every day ")
                //start the activity
                .start(this);
    }
    public void switchFragment(Fragment fragment, boolean b) {
        if (!b)
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_holder, fragment).commit();
        else
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_holder, fragment).commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


}
