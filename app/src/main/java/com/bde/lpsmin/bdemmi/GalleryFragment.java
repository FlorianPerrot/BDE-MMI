package com.bde.lpsmin.bdemmi;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import java.util.ArrayList;

/**
 * Created by lheido on 16/12/14.
 */
public abstract class GalleryFragment extends Fragment {
    private ArrayList<Gallerie.Image> items;
    private GalleryViewAdapter adapter;

    public static GalleryFragment newInstance(final ArrayList<Gallerie.Image> data) {
        return new GalleryFragment() {
            @Override
            public void setUp() {
                loadItems(data);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gridview_layout, container, false);

        items = new ArrayList<Gallerie.Image>();

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);

        // disable verticalFadingEdge on 2.3
        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
            gridView.setVerticalFadingEdgeEnabled(false);
        }

        adapter = new GalleryViewAdapter(getActivity().getApplicationContext(), items);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lauchDetail(view, i);
            }
        });
        setUp();

        return rootView;
    }

    public void lauchDetail(View view, int position){
        DetailActivity.launch(((BDEMain) getActivity()), view.findViewById(R.id.item_image), items.get(position));
    }

    public abstract void setUp();

    public void loadItems(ArrayList<Gallerie.Image> data){
        items.clear();
        adapter.notifyDataSetChanged();
        items.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}
