package com.example.coinstest.framework.receivers;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.coinstest.R;
import com.example.coinstest.domain.CurrencyInRub;
import com.example.coinstest.framework.MainApplication;
import com.example.coinstest.interactors.Interactors;

public class AlarmReceiver extends BroadcastReceiver {

    private float upLimit = 90;
    private float downLimit = 75;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notifyManager = (NotificationManager) (context.getSystemService(Context.NOTIFICATION_SERVICE));

        if (notifyManager != null) {
            NotificationChannel mChannel = notifyManager.getNotificationChannel("0");

            if (mChannel == null) {
                mChannel = new NotificationChannel("1", "title", NotificationManager.IMPORTANCE_HIGH);
                notifyManager.createNotificationChannel(mChannel);
            }

            MainApplication app = (MainApplication)context.getApplicationContext();
            Interactors interactors = app.getInteractors();
            CurrencyInRub curs = interactors.getLastCurrency();

            if(curs.value > upLimit || curs.value < downLimit) {
                NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Dollar alert!!!")
                        .setContentText(" 1 USD = " + curs.value + " RUB")
                        .setAutoCancel(true)
                        .setOngoing(false);

                notifyManager.notify(333, notification.build());
            }
        }
    }
}
