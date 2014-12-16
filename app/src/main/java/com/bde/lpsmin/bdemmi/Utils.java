package com.bde.lpsmin.bdemmi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by lheido on 01/10/14.
 */
public class Utils {

    public static final String rest_get_actu = "http://bde-mmi.alwaysdata.net/json/actu/all";
    public static final String rest_get_event = "http://bde-mmi.alwaysdata.net/json/event/prochainement";
    public static final String rest_get_event_histo = "http://bde-mmi.alwaysdata.net/json/event/historique";
    public static final String rest_get_gallery = "http://bde-mmi.alwaysdata.net/json/gallerie/all";
    public static final int UPDATE_AM = 10; //10h
    public static final int UPDATE_PM = 16; //16h
    public static String rest_get_news = "http://bde-mmi.alwaysdata.net/json/news/";
    public static final String PREFERENCES_DATE_KEY = "date_key";
    public static final String PREFERENCES_ACTU_BOOL_KEY = "notif_actu_checked";
    public static final String PREFERENCES_EVENT_BOOL_KEY = "notif_event_checked";
    public static final String PREFERENCES_NAVIGATION_STARTUP = "navigation_startup";

    public final static String mail = "bdemmi.grenoble@gmail.com";
    /**
     * get fb_id with image or other way.
     * use to launch fb app or open browser with fb page url.
     */
    public final static String fb_id = "140369639484142";
    /**
     * use to open browser with site web url.
     */
    public final static String site_web = "http://www.google.com";

    /**
     * instagram profile.
     */
    public static final String instagram_profile = "icijapon";

    /**
     * use to format date with right database date string
     */
    public final static String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    /**
     * use to display date on textView
     */
    public static final String DATE_FORMAT_DISPLAY = "EEEE dd MMM HH:mm";

    public static final String JSON_TITLE = "titre";
    public static final String JSON_CONTENT = "contenu";
    public static final String JSON_IMAGE = "image";
    public static final String JSON_GALLERY_NAME = "nom";
    public static final String JSON_IMAGE_TITLE = "titre";
    public static final String JSON_IMAGE_URL = "url";
    public static final String JSON_IMAGES = "images";
    public static final String JSON_PLACE = "lieu";
    public static final String JSON_DATE = "date";
    public static final String JSON_NB_ACTU = "actu";
    public static final String JSON_NB_EVENT = "event";

    public static final String ACTION_NOTIFICATION_AM = "com.bde.lpsmin.bdemmi.action_notification_am";
    public static final String ACTION_NOTIFICATION_PM = "com.bde.lpsmin.bdemmi.action_notification_pm";

    public static void startAlarms(Context context){
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendarAm = Calendar.getInstance();
        calendarAm.setTimeInMillis(System.currentTimeMillis());
        calendarAm.set(Calendar.HOUR_OF_DAY, Utils.UPDATE_AM);
        calendarAm.set(Calendar.MINUTE, 0);
        calendarAm.set(Calendar.SECOND, 0);

        Calendar calendarPm = Calendar.getInstance();
        calendarPm.setTimeInMillis(System.currentTimeMillis());
        calendarPm.set(Calendar.HOUR_OF_DAY, Utils.UPDATE_PM);
        calendarPm.set(Calendar.MINUTE, 0);
        calendarPm.set(Calendar.SECOND, 0);

        Intent intentAm = new Intent(context, NotificationsReceiver.class);
        intentAm.setAction(ACTION_NOTIFICATION_AM);
        PendingIntent pIntentAm = PendingIntent.getBroadcast(context, 0, intentAm, 0);

        Intent intentPm = new Intent(context, NotificationsReceiver.class);
        intentPm.setAction(ACTION_NOTIFICATION_PM);
        PendingIntent pIntentPm = PendingIntent.getBroadcast(context, 0, intentPm, 0);

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarAm.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pIntentAm);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarPm.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pIntentPm);
    }

    public static void stopAlarms(Context context){
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendarAm = Calendar.getInstance();
        calendarAm.setTimeInMillis(System.currentTimeMillis());
        calendarAm.set(Calendar.HOUR_OF_DAY, Utils.UPDATE_AM);
        calendarAm.set(Calendar.MINUTE, 0);
        calendarAm.set(Calendar.SECOND, 0);

        Calendar calendarPm = Calendar.getInstance();
        calendarPm.setTimeInMillis(System.currentTimeMillis());
        calendarPm.set(Calendar.HOUR_OF_DAY, Utils.UPDATE_PM);
        calendarPm.set(Calendar.MINUTE, 0);
        calendarPm.set(Calendar.SECOND, 0);

        Intent intentAm = new Intent(context, NotificationsReceiver.class);
        intentAm.setAction(ACTION_NOTIFICATION_AM);
        PendingIntent pIntentAm = PendingIntent.getBroadcast(context, 0, intentAm, 0);

        Intent intentPm = new Intent(context, NotificationsReceiver.class);
        intentPm.setAction(ACTION_NOTIFICATION_PM);
        PendingIntent pIntentPm = PendingIntent.getBroadcast(context, 0, intentPm, 0);

        mAlarmManager.cancel(pIntentAm);
        mAlarmManager.cancel(pIntentPm);
    }
}
