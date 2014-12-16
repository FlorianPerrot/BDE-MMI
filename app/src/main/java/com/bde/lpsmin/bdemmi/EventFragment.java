package com.bde.lpsmin.bdemmi;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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
                                            result.get(i).getAsJsonObject().get(Utils.JSON_TITLE).getAsString(),
                                            result.get(i).getAsJsonObject().get(Utils.JSON_CONTENT).getAsString(),
                                            result.get(i).getAsJsonObject().get(Utils.JSON_IMAGE).getAsString(),
                                            result.get(i).getAsJsonObject().get(Utils.JSON_PLACE).getAsString(),
                                            result.get(i).getAsJsonObject().get(Utils.JSON_DATE).getAsString()
                                    ));
                                }
                            } else {
                                Toast.makeText(context, "Une erreur est survenue :)", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }catch (Exception ex){ex.printStackTrace();}
                        if(act.get() != null) {
                            swipeLayout.setRefreshing(false);
                        }
                    }
                });
                return true;
            }
            return false;
        }

    }

}
