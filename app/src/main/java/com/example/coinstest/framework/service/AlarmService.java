package com.example.coinstest.framework.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.coinstest.R;
import com.example.coinstest.presentation.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmService extends Service {

    private NotificationCompat.Builder notification = null;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getAction() == "stop") {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(333);
            stopSelf();
            return START_NOT_STICKY;
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                notification.setContentText("sek 1000");
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(
                        333,
                        notification.build()
                );
            }
        }, 1000, 1000);


        Intent notificationIntent = new Intent(
                this,
                MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent iStop = new Intent(this, AlarmService.class).setAction("stop");
        PendingIntent piStop = PendingIntent.getService(this, 0, iStop, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notifManager = (NotificationManager) (getSystemService(Context.NOTIFICATION_SERVICE));

        int importance = NotificationManager.IMPORTANCE_HIGH;


        assert notifManager != null;
        NotificationChannel mChannel = notifManager.getNotificationChannel("0");

        if (mChannel == null) {
            mChannel = new NotificationChannel("1", "title", importance);
            notifManager.createNotificationChannel(mChannel);
        }

        notification = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm service")
                .setContentText("My text")
                .setContentIntent(
                        PendingIntent.getActivity(
                                this,
                                0,
                                notificationIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        )
                )
                .addAction(R.mipmap.ic_launcher, "Stop", piStop)
                .setAutoCancel(true)
                .setOngoing(false);

        NotificationManager notifyService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notifyService != null) {
            notifyService.notify(333, notification.build());
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
