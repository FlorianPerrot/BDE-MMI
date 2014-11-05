package com.bde.lpsmin.bdemmi;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lheido on 25/09/14.
 */
public class ActuFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ActionSlideExpandableListView listView;
    protected SwipeRefreshLayout swipeLayout;
    protected ListViewAdapter adapter;
    protected ArrayList<Actu> items;
    protected int currentEvent = 0; //0 pour event futur, 1 pour les anciens.

    public ActuFragment(){}

    public static ActuFragment newInstance(){
        return new ActuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listview_layout, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.defaultColor, R.color.defaultColorDarken, R.color.grey, R.color.black);

        items = new ArrayList<Actu>();

        listView = (ActionSlideExpandableListView) rootView.findViewById(R.id.list_actu);

        // disable verticalFadingEdge on 2.3
        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
            listView.setVerticalFadingEdgeEnabled(false);
        }

        adapter = new ListViewAdapter(getActivity().getApplicationContext(), items);
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
        GetActuTask task = new GetActuTask(getActivity(), currentEvent);
        task.execTask();
    }

    @Override
    public void onRefresh() {
        loadItems(currentEvent);
    }

    protected class GetActuTask extends AsyncTask<Void, Actu, Boolean>{

        protected WeakReference<FragmentActivity> act = null;
        protected Context context = null;
        protected int historique;

        public GetActuTask(FragmentActivity activity, int historique) {
            link(activity);
            this.historique = historique;
        }

        public void link (FragmentActivity pActivity) {
            act = new WeakReference<FragmentActivity>(pActivity);
        }

        @Override
        protected void onPreExecute () {
            if(act.get() != null){
                context = act.get().getApplicationContext();
                swipeLayout.setRefreshing(true);
            }
        }

        public Actu create(String title, String description, String imgUri, String place, String date){
            return new Actu("Actu "+title, description, imgUri, date);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(act.get() != null) {
                Ion.with(context)
                .load(Utils.rest_get_actu)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result != null) {
                                for (int i = 0; i < result.size(); i++) {
                                    publishProgress(new Actu(
                                            result.get(i).getAsJsonObject().get("title").getAsString(),
                                            result.get(i).getAsJsonObject().get("contenu").getAsString(),
                                            result.get(i).getAsJsonObject().get("image").getAsString(),
                                            result.get(i).getAsJsonObject().get("date").getAsString()
                                    ));
                                }
                            } else {
                                Toast.makeText(context, "Une erreur est survenue :)", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            swipeLayout.setRefreshing(false);
                        }catch (Exception ex){ex.printStackTrace();}
                    }
                });
                return true;
            }
            return false;
        }

        @Override
        public void onProgressUpdate(Actu... prog){
            if(act.get() != null){
                items.add(prog[0]);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onPostExecute (Boolean result) {
            if (act.get() != null) {
                if(!result)
                    Toast.makeText(context, "Oups petit problème", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Plusieurs asynctask peuvent être exécutés en parallèle sur les versions 3+ d'Android.
         */
        public void execTask(){
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                execute();
            }
        }
    }

}
