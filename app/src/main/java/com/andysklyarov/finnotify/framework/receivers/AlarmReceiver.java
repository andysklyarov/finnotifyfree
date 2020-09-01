package com.andysklyarov.finnotify.framework.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.andysklyarov.finnotify.R;
import com.andysklyarov.finnotify.domain.CurrencyInRub;
import com.andysklyarov.finnotify.framework.MainApplication;
import com.andysklyarov.finnotify.interactors.Interactors;
import com.andysklyarov.finnotify.presentation.MainActivity;

import java.time.LocalDate;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        MainApplication app = (MainApplication) context.getApplicationContext();
        Interactors interactors = app.getInteractors();

        Disposable res = interactors.getLastCurrency()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currencyInRub -> {
                    float upLimit = intent.getFloatExtra(AlarmServiceManager.TOP_LIMIT_KEY, 0);
                    float downLimit = intent.getFloatExtra(AlarmServiceManager.BOTTOM_LIMIT_KEY, 0);

                    notify(context, currencyInRub, upLimit, downLimit);
                });
    }

    private void notify(Context context, CurrencyInRub curs, float upLimit, float downLimit) {
        Intent notificationIntent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (curs.value > upLimit || curs.value < downLimit) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "1")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Dollar alert!!!") // todo change
                    .setContentText(" 1 USD = " + curs.value + " RUB") // todo change
                    .setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setAutoCancel(true)
                    .setOngoing(false);
            NotificationManager notifyManager = (NotificationManager) (context.getSystemService(Context.NOTIFICATION_SERVICE));
            if (notifyManager == null) {
                return;
            }
            NotificationChannel mChannel = notifyManager.getNotificationChannel("0");
            if (mChannel == null) {
                mChannel = new NotificationChannel("1", "title", NotificationManager.IMPORTANCE_HIGH);
                notifyManager.createNotificationChannel(mChannel);
            }
            notifyManager.notify(333, notification.build());
        }
    }
}
