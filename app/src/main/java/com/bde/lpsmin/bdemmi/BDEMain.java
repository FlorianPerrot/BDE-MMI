package com.bde.lpsmin.bdemmi;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
    private int currentPage = PAGE_ACTU;
    private ArrayList<ActuFragment> pages;
    private ViewPagerAdapter mViewPagerAdapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdemain);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        pages = new ArrayList<ActuFragment>();
        pages.add(ActuFragment.newInstance());
        pages.add(EventFragment.newInstance());
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), pages);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mViewPagerAdapter);

        final TextView tab_actu = (TextView) findViewById(R.id.tab_actu);
        tab_actu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(PAGE_ACTU, true);
            }
        });

        final TextView tab_event = (TextView) findViewById(R.id.tab_event);
        tab_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(PAGE_EVENT, true);
            }
        });

        final float scale = getResources().getDisplayMetrics().density;
        final int defaultPadding = (int) (10*scale + 0.5f);
        final int finalPadding = (int) (100*scale + 0.5f);

        tab_actu.setPadding(finalPadding, defaultPadding, defaultPadding, defaultPadding);
        tab_event.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);

        // custom spinner action view
        final View view = getLayoutInflater().inflate(R.layout.spinner_event_action_view, null);
        if(view != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                    R.layout.event_spinner, getResources().getStringArray(R.array.event_spinner));
            adapter.setDropDownViewResource(R.layout.event_spinner_item);
            spinner = (Spinner) view.findViewById(R.id.spinner);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                private boolean firstTime = true;

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!firstTime) {
                        pages.get(PAGE_EVENT).loadItems(i);
                    }
                    firstTime = false;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            int gravity;
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
                gravity = Gravity.END;
            }else{
                gravity = Gravity.RIGHT;
            }
            getSupportActionBar().setCustomView(view, new ActionBar.LayoutParams(gravity));

            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {
                    int padding = (int)(v*100*scale + 0.5f);
                    if(i == PAGE_ACTU){
                        if(v != 0.0){
                            tab_actu.setPadding(finalPadding - padding, defaultPadding, defaultPadding, defaultPadding);
                            tab_event.setPadding(defaultPadding, defaultPadding, padding, defaultPadding);
                        }else{
                            tab_actu.setPadding(finalPadding, defaultPadding, defaultPadding, defaultPadding);
                            tab_event.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
                        }
                    }else{
                        if(v != 0.0){
                            tab_actu.setPadding(padding, defaultPadding, defaultPadding, defaultPadding);
                            tab_event.setPadding(defaultPadding, defaultPadding, finalPadding - padding, defaultPadding);
                        }else{
                            tab_actu.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
                            tab_event.setPadding(defaultPadding, defaultPadding, finalPadding, defaultPadding);
                        }
                    }
                }

                @Override
                public void onPageSelected(int index) {
                    currentPage = index;
                    getSupportActionBar().setDisplayShowCustomEnabled(currentPage == PAGE_EVENT);
                    if(index == PAGE_ACTU){
                        tab_actu.setPadding(finalPadding, defaultPadding, defaultPadding, defaultPadding);
                        tab_event.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
                    }else{
                        tab_actu.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
                        tab_event.setPadding(defaultPadding, defaultPadding, finalPadding, defaultPadding);
                    }
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

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
}
