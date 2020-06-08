package com.example.coinstest.framework.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import java.time.ZonedDateTime;

public class AlarmServiceManager {

    public final static String TIME_IN_MILLIS_KEY = "TimeInMillis";

    private Context context = null;
    private AlarmManager manager = null;
    private PackageManager packageManager = null;

    public AlarmServiceManager(Context context) {
        this.context = context;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        packageManager = context.getPackageManager();
    }

    public void startRepeatingService(ZonedDateTime timeToStart) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        long currentTimeMillis = timeToStart.toInstant().toEpochMilli();

        if (manager != null)
            manager.setRepeating(AlarmManager.RTC_WAKEUP, currentTimeMillis, AlarmManager.INTERVAL_DAY, alarmIntent);

        enableBootReceiver();
        safeDateTime(timeToStart);
    }

    public void stopRepeatingService() {
        disableBootReceiver();

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (manager != null) {
            manager.cancel(alarmIntent);
        }
    }

    private void safeDateTime(ZonedDateTime timeToStart) {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putLong(TIME_IN_MILLIS_KEY, timeToStart.toInstant().toEpochMilli());
        myEditor.apply();
    }

    private void enableBootReceiver() {
        packageManager.setComponentEnabledSetting(
                new ComponentName(context, BootReceiver.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableBootReceiver() {
        packageManager.setComponentEnabledSetting(
                new ComponentName(context, BootReceiver.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}