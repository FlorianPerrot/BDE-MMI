package com.bde.lpsmin.bdemmi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;


/**
 * Created by lheido on 26/09/14.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> pages;
    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> pages) {
        super(fm);
        this.pages = pages;
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
}
