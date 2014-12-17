package com.bde.lpsmin.bdemmi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lheido on 17/12/14.
 */
public class GalleryRootFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.root_fragment, container, false);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        /*
        * When this container fragment is created, we fill it with our first
        * "real" fragment
        */
        transaction.replace(R.id.root_frame, ListGallerieFragment.newInstance());
        transaction.commit();
        return view;
    }
}
