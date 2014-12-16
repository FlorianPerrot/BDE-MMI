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
    private static final int NOTIFICATION_ID_ACTU = 0;
    private static final int NOTIFICATION_ID_EVENT = 1;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Utils.ACTION_NOTIFICATION_AM) || action.equals(Utils.ACTION_NOTIFICATION_PM)) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            long date = preferences.getLong(Utils.PREFERENCES_DATE_KEY, 0L);
//            Log.v("date", Utils.rest_get_news+date);
//            showNotification(context, R.string.notif_actu_title, ""+date, NOTIFICATION_ID_ACTU);
            if(date != 0L) {
                Ion.with(context)
                    .load(Utils.rest_get_news + date)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        public void onCompleted(Exception e, JsonArray result) {
//                            showNotification(context, R.string.notif_event_title, "test "+e, NOTIFICATION_ID_EVENT);
                            if(e != null) {
                                e.printStackTrace();
                            }
                            if(result != null && result.size() != 0){
                                try {
                                    int nb_actu = result.get(0).getAsJsonObject().get(Utils.JSON_NB_ACTU).getAsInt();
                                    if (nb_actu != 0 && preferences.getBoolean(Utils.PREFERENCES_ACTU_BOOL_KEY, true)) {
                                        String ticker = String.format(context.getResources().getString(R.string.notif_actu_ticker), nb_actu);
                                        showNotification(context, R.string.notif_actu_title, ticker, NOTIFICATION_ID_ACTU);
                                    }
                                    int nb_event = result.get(0).getAsJsonObject().get(Utils.JSON_NB_EVENT).getAsInt();
                                    if (nb_event != 0 && preferences.getBoolean(Utils.PREFERENCES_EVENT_BOOL_KEY, true)) {
                                        String ticker = String.format(context.getResources().getString(R.string.notif_event_ticker), nb_event);
                                        showNotification(context, R.string.notif_event_title, ticker, NOTIFICATION_ID_EVENT);
                                    }
                                }catch (Exception ex){ex.printStackTrace();}
                            }
                        }
                    });
            }
        }
    }

    public static void showNotification(Context context, int title, String ticker, int id){
        Intent intent = new Intent(context, BDEMain.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(ticker)
                .setContentTitle(context.getResources().getString(title))
                .setContentText(ticker)
                .setPriority(2)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(id, builder.build());
    }
}