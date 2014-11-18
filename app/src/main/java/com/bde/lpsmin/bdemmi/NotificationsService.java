package com.bde.lpsmin.bdemmi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by lheido on 17/11/14.
 */
public class NotificationsService extends Service {

    public static final String ACTION_NOTIFICATION = "action_notification";
    private AlarmManager mAlarmManager;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);

        Calendar calendarAm = Calendar.getInstance();
        calendarAm.setTimeInMillis(System.currentTimeMillis());
        calendarAm.set(Calendar.HOUR_OF_DAY, Utils.UPDATE_AM);

        Calendar calendarPm = Calendar.getInstance();
        calendarPm.setTimeInMillis(System.currentTimeMillis());
        calendarPm.set(Calendar.HOUR_OF_DAY, Utils.UPDATE_PM);


        Intent intentAm = new Intent(mContext, NotificationsReceiver.class);
        intentAm.setAction(ACTION_NOTIFICATION);
        PendingIntent pIntentAm = PendingIntent.getBroadcast(mContext, 0, intentAm, 0);

        Intent intentPm = new Intent(mContext, NotificationsReceiver.class);
        intentPm.setAction(ACTION_NOTIFICATION);
        PendingIntent pIntentPm = PendingIntent.getBroadcast(mContext, 0, intentPm, 0);

        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarAm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntentAm);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarPm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntentPm);

        Log.v("NotificationsService", "started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v("NotificationsService", "destroyed");
    }
}
