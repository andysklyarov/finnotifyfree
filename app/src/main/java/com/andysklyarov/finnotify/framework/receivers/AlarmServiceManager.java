package com.andysklyarov.finnotify.framework.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AlarmServiceManager {

    public final static String TIME_IN_MILLIS_KEY = "TIME_IN_MILLIS_KEY";
    public final static String TOP_LIMIT_KEY = "TOP_LIMIT_KEY";
    public final static String BOTTOM_LIMIT_KEY = "BOTTOM_LIMIT_KEY";
    public final static String CURRENCY_CODE_KEY = "CURRENCY_CODE_KEY";
    private final static String SERVICE_STATUS_KEY = "SERVICE_STATUS_KEY";

    private Context context;
    private AlarmManager manager;
    private ServiceState state;
    private ZonedDateTime alarmTime;

    public AlarmServiceManager(@NonNull Context context) {
        this.context = context;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        loadServiceData();
    }

    public ZonedDateTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime24(int hour, int minutes) {
        ZonedDateTime startOfDayTime = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault());
        alarmTime = startOfDayTime.plusHours(hour).plusMinutes(minutes);

        if (alarmTime.isBefore(ZonedDateTime.now())) {
            alarmTime = alarmTime.plusDays(1);
        }
    }


    public void startRepeatingService(String currencyCode, float topLimit, float bottomLimit) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        intent.putExtra(CURRENCY_CODE_KEY, currencyCode);
        intent.putExtra(TOP_LIMIT_KEY, topLimit);
        intent.putExtra(BOTTOM_LIMIT_KEY, bottomLimit);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long currentTimeMillis = alarmTime.toInstant().toEpochMilli();
        if (manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, currentTimeMillis, AlarmManager.INTERVAL_DAY, alarmIntent);

            state = new ServiceState(true, currentTimeMillis, topLimit, bottomLimit);
            safeServiceData();
        }
    }

    public void stopRepeatingService() {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (manager != null) {
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

        Instant i = Instant.ofEpochMilli(timeToStartInMillis);
        alarmTime = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
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


}