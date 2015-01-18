package com.bde.lpsmin.bdemmi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static String facebookId;
    private static String instagramProfile;

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mUserLearnedDrawer;
    private CheckedTextView notifActuCheckBox;
    private CheckedTextView notifEventCheckBox;
    private SharedPreferences preferences;
    private CheckedTextView navigationStartupCheckBox;
    private String bdeMail;

    public NavigationDrawerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_drawer, container, false);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        TextView actu = (TextView) getActivity().findViewById(R.id.go_to_actu);
        actu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(0);
            }
        });
        TextView event = (TextView) getActivity().findViewById(R.id.go_to_event);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(1);
            }
        });
        TextView gallery = (TextView) getActivity().findViewById(R.id.go_to_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(2);
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        notifActuCheckBox = (CheckedTextView) getActivity().findViewById(R.id.notif_actu_checkbox);
        notifActuCheckBox.setChecked(preferences.getBoolean(Utils.PREFERENCES_ACTU_BOOL_KEY, false));
        notifActuCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifActuCheckBox.toggle();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Utils.PREFERENCES_ACTU_BOOL_KEY, notifActuCheckBox.isChecked());
                editor.apply();
                if(mCallbacks != null) {
                    if (!preferences.getBoolean(Utils.PREFERENCES_ACTU_BOOL_KEY, false)
                            && !preferences.getBoolean(Utils.PREFERENCES_EVENT_BOOL_KEY, false)) {
                        mCallbacks.stopNotificationAlarm();
                    }else{
                        mCallbacks.startNotificationAlarm();
                    }
                }
            }
        });
        notifEventCheckBox = (CheckedTextView) getActivity().findViewById(R.id.notif_event_checkbox);
        notifEventCheckBox.setChecked(preferences.getBoolean(Utils.PREFERENCES_EVENT_BOOL_KEY, false));
        notifEventCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifEventCheckBox.toggle();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Utils.PREFERENCES_EVENT_BOOL_KEY, notifEventCheckBox.isChecked());
                editor.apply();
                if(mCallbacks != null) {
                    if (!preferences.getBoolean(Utils.PREFERENCES_ACTU_BOOL_KEY, false)
                            && !preferences.getBoolean(Utils.PREFERENCES_EVENT_BOOL_KEY, false)) {
                        mCallbacks.stopNotificationAlarm();
                    }else{
                        mCallbacks.startNotificationAlarm();
                    }
                }
            }
        });

        navigationStartupCheckBox = (CheckedTextView) getActivity().findViewById(R.id.navigation_startup_checkbox);
        navigationStartupCheckBox.setChecked(preferences.getBoolean(Utils.PREFERENCES_NAVIGATION_STARTUP, true));
        navigationStartupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationStartupCheckBox.toggle();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Utils.PREFERENCES_NAVIGATION_STARTUP, navigationStartupCheckBox.isChecked());
                editor.apply();
            }
        });

        Ion.with(getActivity().getApplicationContext())
                .load(Utils.rest_get_contact_info)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>(){
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            bdeMail = result.get(Utils.JSON_EMAIL).getAsString();
                            facebookId = result.get(Utils.JSON_ID_FACEBOOK).getAsString();
                            instagramProfile = result.get(Utils.JSON_PROFILE_INSTAGRAM).getAsString();
                        }  else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Une erreur est survenue :)",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

        ImageButton menu_facebook = (ImageButton) getActivity().findViewById(R.id.menu_facebook);
        menu_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(getOpenFacebookIntent(getActivity().getApplicationContext()));
                }catch (Exception e){}
            }
        });

        ImageButton menu_mail = (ImageButton) getActivity().findViewById(R.id.menu_mail);
        menu_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{bdeMail});
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.contact_bde_title)));
                }catch (Exception e){}
            }
        });

        ImageButton menu_firefox = (ImageButton) getActivity().findViewById(R.id.menu_firefox);
        menu_firefox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.site_web));
                    startActivity(browserIntent);
                }catch (Exception e){}
            }
        });

        ImageButton menu_instagram = (ImageButton) getActivity().findViewById(R.id.menu_instagram);
        menu_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(getOpenInstagramIntent(getActivity().getApplicationContext()));
                }catch (Exception e){}
            }
        });

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        if(preferences.getBoolean(Utils.PREFERENCES_NAVIGATION_STARTUP, true)){
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+facebookId));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+facebookId));
        }
    }

    public static Intent getOpenInstagramIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.instagram.android", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/"+instagramProfile));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/"+instagramProfile));
        }
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
        void stopNotificationAlarm();
        void startNotificationAlarm();
    }
}
