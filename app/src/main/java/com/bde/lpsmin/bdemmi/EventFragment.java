package com.bde.lpsmin.bdemmi;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
                ArrayList<Event> actuList = Event.getActuListFromJson((historique == 0), context);
                for (Event anActuList : actuList) {
                    publishProgress(anActuList);
                }
                return true;
            }
            return false;
        }

    }

}
