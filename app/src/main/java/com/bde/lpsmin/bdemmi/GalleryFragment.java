package com.bde.lpsmin.bdemmi;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by lheido on 15/12/14.
 */
public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private GalleryListViewAdapter adapter;
    private ArrayList<Gallerie> items;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listview_layout, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.defaultColor, R.color.defaultColorDarken, R.color.grey, R.color.black);

        items = new ArrayList<Gallerie>();

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);

        // disable verticalFadingEdge on 2.3
        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
            listView.setVerticalFadingEdgeEnabled(false);
        }

        adapter = new GalleryListViewAdapter(getActivity().getApplicationContext(), items);
        listView.setAdapter(adapter);
        loadItems();

        return rootView;
    }

    public void loadItems(){
        items.clear();
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(true);
        Ion.with(getActivity().getApplicationContext())
                .load(Utils.rest_get_gallery)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result != null) {
                                for (int i = 0; i < result.size(); i++) {
                                    items.add(new Gallerie(
                                            result.get(i).getAsJsonObject().get(Utils.JSON_GALLERY_NAME).getAsString(),
                                            result.get(i).getAsJsonObject().get(Utils.JSON_IMAGES).getAsJsonArray()
                                    ));
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Une erreur est survenue :)", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        swipeLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        loadItems();
    }

}
