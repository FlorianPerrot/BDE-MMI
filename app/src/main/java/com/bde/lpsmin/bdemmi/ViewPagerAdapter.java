package com.bde.lpsmin.bdemmi;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;


/**
 * Created by lheido on 26/09/14.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final Context mContext;
    private ArrayList<Fragment> pages;
    public ViewPagerAdapter(Context context, FragmentManager fm, ArrayList<Fragment> pages) {
        super(fm);
        this.pages = pages;
        this.mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }
    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence returnValue = null;
        switch (position) {
            case 0:
                returnValue = mContext.getResources().getString(R.string.navigation_drawer_actu);
                break;
            case 1:
                returnValue = mContext.getResources().getString(R.string.navigation_drawer_event);
                break;
            case 2:
                returnValue = mContext.getResources().getString(R.string.navigation_drawer_gallery);
                break;
        }
        return returnValue;
    }
}
