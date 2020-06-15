package com.example.coinstest.framework.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import java.time.ZonedDateTime;

public class AlarmServiceManager {

    public final static String TIME_IN_MILLIS_KEY = "TIME_IN_MILLIS";
    public final static String TOP_LIMIT_KEY = "TOP_LIMIT";
    public final static String BOTTOM_LIMIT_KEY = "BOTTOM_LIMIT";

    private final static String SERVICE_STATUS_KEY = "SERVICE_STATUS";

    private Context context;
    private AlarmManager manager;
    private PackageManager packageManager;
    private ServiceState state;

    public AlarmServiceManager(@NonNull Context context) {
        this.context = context;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        packageManager = context.getPackageManager();
        loadServiceData();
    }

    public void startRepeatingService(@NonNull ZonedDateTime timeToStart, float topLimit, float bottomLimit) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(TOP_LIMIT_KEY, topLimit);
        intent.putExtra(BOTTOM_LIMIT_KEY, bottomLimit);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        long currentTimeMillis = timeToStart.toInstant().toEpochMilli();

        if (manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, currentTimeMillis, AlarmManager.INTERVAL_DAY, alarmIntent);

            state = new ServiceState(true, currentTimeMillis, topLimit, bottomLimit);
            safeServiceData();

            enableBootReceiver();
        }
    }

    public void stopRepeatingService() {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (manager != null) {

            disableBootReceiver();
            manager.cancel(alarmIntent);

            state = new ServiceState(false, state);
            safeServiceData();
        }
    }

    public ServiceState getServiceState() {
        return state;
    }

    private void loadServiceData() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isStarted = myPreferences.getBoolean(SERVICE_STATUS_KEY, false);
        long timeToStartInMillis = myPreferences.getLong(TIME_IN_MILLIS_KEY, 0);
        float topLimit = myPreferences.getFloat(TOP_LIMIT_KEY, 0);
        float bottomLimit = myPreferences.getFloat(BOTTOM_LIMIT_KEY, 0);

        state = new ServiceState(isStarted, timeToStartInMillis, topLimit, bottomLimit);
    }

    private void safeServiceData() {

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor myEditor = myPreferences.edit();

        myEditor.putBoolean(SERVICE_STATUS_KEY, state.isStarted);
        myEditor.putFloat(TOP_LIMIT_KEY, state.topLimit);
        myEditor.putFloat(BOTTOM_LIMIT_KEY, state.bottomLimit);
        myEditor.putLong(TIME_IN_MILLIS_KEY, state.timeToStartInMillis);

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