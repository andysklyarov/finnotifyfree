package com.andysklyarov.finnotify.framework.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (action.equals("android.intent.action.BOOT_COMPLETED") ||
                action.equals("android.intent.action.QUICKBOOT_POWERON") ||
                action.equals("com.htc.intent.action.QUICKBOOT_POWERON")) {

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            if (pendingIntent != null) {
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                long millisToStart = loadDateTime(context);
                if (manager != null)
                    manager.setRepeating( AlarmManager.RTC_WAKEUP, millisToStart, AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

    private long loadDateTime(Context context) {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getInt(AlarmServiceManager.TIME_IN_MILLIS_KEY, 0);
    }
}
