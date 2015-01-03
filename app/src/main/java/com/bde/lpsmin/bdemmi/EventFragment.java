package com.bde.lpsmin.bdemmi;

import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by lheido on 27/09/14.
 */
public class EventFragment extends ActuFragment {

    public static EventFragment newInstance(){
        return new EventFragment();
    }

    @Override
    public void loadItems(int historique){
        currentEvent = historique;
        items.clear();
        adapter.notifyDataSetChanged();
        String url = (currentEvent == 0) ? Utils.rest_get_event : Utils.rest_get_event_histo;
        Ion.with(getActivity().getApplicationContext())
            .load(url)
            .asJsonArray()
            .setCallback(new FutureCallback<JsonArray>() {
                public void onCompleted(Exception e, JsonArray result) {
                    if (result != null && items != null) {
                        for (int i = 0; i < result.size(); i++) {
                            items.add(new Event(
                                result.get(i).getAsJsonObject().get(Utils.JSON_TITLE).getAsString(),
                                result.get(i).getAsJsonObject().get(Utils.JSON_CONTENT).getAsString(),
                                result.get(i).getAsJsonObject().get(Utils.JSON_IMAGE).getAsString(),
                                result.get(i).getAsJsonObject().get(Utils.JSON_PLACE).getAsString(),
                                result.get(i).getAsJsonObject().get(Utils.JSON_DATE).getAsString()
                            ));
                        }
                        if (adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
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
}
