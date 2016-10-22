package com.viztushar.osumwalls;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.batch.android.Batch;
import com.viztushar.osumwalls.activities.AboutAppActivity;
import com.viztushar.osumwalls.activities.FavWallaper;
import com.viztushar.osumwalls.fragments.HomeFragment;
import com.viztushar.osumwalls.others.Utils;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Context context;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    //SharedPreferences sharedPreferences = new SharedPreferences(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.content.SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        Utils.darkTheme = useDarkTheme;
        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme);
        }

        //setTheme(R.style.AppTheme_Dark_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        switchFragment(new HomeFragment("walls"), false);

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
                  /*  case R.id.navigation_favourite:
                        item.setChecked(true);
                        startFavSection();
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
                        item.setChecked(false);
                        startAboutSection();
                        mDrawerLayout.closeDrawers();
                        break;

                }
                return true;
            }
        });
    }


    public void switchFragment(Fragment fragment, boolean b) {
        if (!b) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_holder, fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_holder, fragment).commit();
        }
    }

    public void startAboutSection() {
        Intent intent = new Intent(this, AboutAppActivity.class);
        startActivity(intent);
    }


    private void startFavSection() {
        Intent intent = new Intent(this, FavWallaper.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (Utils.darkTheme) menu.findItem(R.id.action_darktheme).setChecked(true);
        else menu.findItem(R.id.action_darktheme).setChecked(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
            case R.id.action_sendemail:
                sendEmail();
                break;
            case R.id.action_changelog:
                //startAboutSection();
                new MaterialDialog.Builder(context)
                        .title("Absolutely Wallpapers " + getAppVersion())
                        .content("New\n \n- All new about section\n- Dark Theme (apply using three dots menu)\n- No connection message if you don't have an internet connection\n- New changelog section (as you can see)\n" +
                                "\nImproved\n \n- All round awesome improvements! \n \n \n If you like the app be sure to leave a rating and/or review! They are very much appreciated ")
                        .positiveText(android.R.string.ok)
                        .show();
                break;
            case R.id.action_darktheme:
                if (item.isChecked()) {
                    item.setChecked(false);
                    toggleTheme(false);
                } else {
                    item.setChecked(true);
                    toggleTheme(true);
                    //sharedPreferences.saveBoolean("dark_theme", true);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getAppVersion() {
        String versionCode = "1.0";
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

    private void toggleTheme(boolean darkTheme) {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"ercsoftware11@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Absolutely Wallpapers");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Type the message you would like to send to the devs");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            //Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onStart() {
        super.onStart();
        Batch.onStart(this);
    }

    @Override
    protected void onStop() {
        Batch.onStop(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Batch.onDestroy(this);

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Batch.onNewIntent(this, intent);

        super.onNewIntent(intent);
    }
}
