package com.bde.lpsmin.bdemmi;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class BDEMain extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ViewPager mViewPager;
    private static final int PAGE_ACTU = 0;// attention a changer les add de pages si la valeur change
    private static final int PAGE_EVENT = 1;
    private static final int PAGE_GALLERY = 2;
    private int currentPage = PAGE_ACTU;
    private ArrayList<Fragment> pages;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdemain);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean(Utils.PREFERENCES_ACTU_BOOL_KEY, false)
           || preferences.getBoolean(Utils.PREFERENCES_EVENT_BOOL_KEY, false)){
            startNotificationAlarm();
        }

//        long date = preferences.getLong(Utils.PREFERENCES_DATE_KEY, 0L);
//        Log.v("date", Utils.rest_get_news+date);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        pages = new ArrayList<>();
        pages.add(ActuFragment.newInstance());
        pages.add(EventFragment.newInstance());
        pages.add(GalleryFragment.newInstance());
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), pages);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mViewPagerAdapter);

        // custom spinner action view
        final View view = getLayoutInflater().inflate(R.layout.spinner_event_action_view, null);
        if(view != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                    R.layout.event_spinner, getResources().getStringArray(R.array.event_spinner));
            adapter.setDropDownViewResource(R.layout.event_spinner_item);
            Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                private boolean firstTime = true;

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!firstTime) {
                        ((ActuFragment)pages.get(PAGE_EVENT)).loadItems(i);
                    }
                    firstTime = false;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            int gravity = GravityCompat.END;
            getSupportActionBar().setCustomView(view, new ActionBar.LayoutParams(gravity));

            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {
                }

                @Override
                public void onPageSelected(int index) {
                    currentPage = index;
                    getSupportActionBar().setDisplayShowCustomEnabled(currentPage == PAGE_EVENT);
                }

                @Override
                public void onPageScrollStateChanged(int i) {}
            });
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    public void stopNotificationAlarm() {
        Utils.stopAlarms(this);
    }

    @Override
    public void startNotificationAlarm() {
        Utils.startAlarms(this);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(currentPage == PAGE_EVENT);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.bdemain, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(Utils.PREFERENCES_DATE_KEY, System.currentTimeMillis());
        editor.apply();
    }

    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), R.string.double_back_toast, Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
