package com.bde.lpsmin.bdemmi;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import java.util.ArrayList;

/**
 * Created by lheido on 25/09/14.
 */
public class ActuFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    protected SwipeRefreshLayout swipeLayout;
    protected ExpandableListViewAdapter adapter;
    protected ArrayList<Actu> items;
    protected int currentEvent = 0; //0 pour event futur, 1 pour les anciens.

    public ActuFragment(){}

    public static ActuFragment newInstance(){
        return new ActuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.expandablelistview_layout, container, false);
        Log.e("onCreateView", ""+this.getClass());
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.defaultColor, R.color.defaultColorDarken, R.color.grey, R.color.black);

        items = new ArrayList<Actu>();

        ActionSlideExpandableListView listView = (ActionSlideExpandableListView) rootView.findViewById(R.id.list_actu);

        // disable verticalFadingEdge on 2.3
        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
            listView.setVerticalFadingEdgeEnabled(false);
        }

        adapter = new ExpandableListViewAdapter(getActivity().getApplicationContext(), items);
        listView.setAdapter(
                new SlideExpandableListAdapter(
                        adapter,
                        R.id.toggle_expand,
                        R.id.expandable
                )
        );
        loadItems(currentEvent);

        return rootView;
    }

    public void loadItems(int i){
        currentEvent = i;
        items.clear();
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(true);
        Ion.with(getActivity().getApplicationContext())
            .load(Utils.rest_get_actu)
            .asJsonArray()
            .setCallback(new FutureCallback<JsonArray>(){
                public void onCompleted(Exception e, JsonArray result) {
                    if (result != null && items != null) {
                        for (int i = 0; i < result.size(); i++) {
                            items.add(new Actu(
                                result.get(i).getAsJsonObject().get(Utils.JSON_TITLE).getAsString(),
                                result.get(i).getAsJsonObject().get(Utils.JSON_CONTENT).getAsString(),
                                result.get(i).getAsJsonObject().get(Utils.JSON_IMAGE).getAsString(),
                                result.get(i).getAsJsonObject().get(Utils.JSON_DATE).getAsString()
                            ));
                        }
                        if (adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "Une erreur est survenue :)",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    if (swipeLayout != null) {
                        swipeLayout.setRefreshing(false);
                    }

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
        loadItems(currentEvent);
    }

}
