package com.bde.lpsmin.bdemmi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by lheido on 17/11/14.
 */
public class NotificationsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(NotificationsService.ACTION_NOTIFICATION)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            long date = preferences.getLong(Utils.PREFERENCES_DATE_KEY, 0L);
            if(date != 0L) {
                Ion.with(context)
                    .load(Utils.rest_get_news + date)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        public void onCompleted(Exception e, JsonArray result) {

                        }
                    });
            }
        }
    }
}