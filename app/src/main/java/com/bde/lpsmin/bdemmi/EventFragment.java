package com.bde.lpsmin.bdemmi;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lheido on 27/09/14.
 */
public class EventFragment extends ActuFragment {

    public static EventFragment newInstance(){
        return new EventFragment();
    }

    @Override
    public void loadItems(int i){
        currentEvent = i;
        items.clear();
        adapter.notifyDataSetChanged();
        GetEventTask task = new GetEventTask(getActivity(), currentEvent);
        task.execTask();
    }

    protected class GetEventTask extends GetActuTask {

        public GetEventTask(FragmentActivity activity, int histo) {
            super(activity, histo);
        }

        @Override
        public Event create(String title, String description, String imgUri, String place, String date){
            return new Event("Event "+title, description, imgUri, place, date);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(act.get() != null) {
                String url;
                if(historique == 0) {
                    url = Utils.rest_get_event;
                }else {
                    url = Utils.rest_get_event_histo;
                }
                Ion.with(context)
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>(){
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result != null) {
                                for (int i = 0; i < result.size(); i++) {
                                    publishProgress(new Event(
                                            result.get(i).getAsJsonObject().get("title").getAsString(),
                                            result.get(i).getAsJsonObject().get("contenu").getAsString(),
                                            result.get(i).getAsJsonObject().get("image").getAsString(),
                                            result.get(i).getAsJsonObject().get("lieu").getAsString(),
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

    }

}
