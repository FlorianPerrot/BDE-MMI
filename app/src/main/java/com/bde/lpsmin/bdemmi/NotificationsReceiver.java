package com.bde.lpsmin.bdemmi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by lheido on 17/11/14.
 */
public class NotificationsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        Log.v("onReceive ACTION", action);
        showNotification(context, "Title", "ticker", "contentText");
        if (action.equals(NotificationsService.ACTION_NOTIFICATION)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            long date = preferences.getLong(Utils.PREFERENCES_DATE_KEY, 0L);
            Log.v("date", Utils.rest_get_news+date);
            if(date != 0L) {
                Ion.with(context)
                    .load(Utils.rest_get_news + date)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        public void onCompleted(Exception e, JsonArray result) {
                            showNotification(context, "Title", "ticker", "contentText");
                        }
                    });
            }
        }
    }

    public static void showNotification(Context context, String title, String ticker,
                                        String contentText){
        Intent intent = new Intent(context, BDEMain.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(ticker)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(2)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());
    }
}