package com.andysklyarov.finnotify.framework.receivers



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.andysklyarov.finnotify.R
import com.andysklyarov.finnotify.domain.CurrencyInRub
import com.andysklyarov.finnotify.framework.MainApplication
import com.andysklyarov.finnotify.interactors.Interactors
import com.andysklyarov.finnotify.presentation.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        val app: MainApplication = context.applicationContext as MainApplication
        val interactors: Interactors = app.getInteractors()

        val upLimit = intent.getFloatExtra(AlarmServiceManager.TOP_LIMIT_KEY, 0.0f)
        val downLimit = intent.getFloatExtra(AlarmServiceManager.TOP_LIMIT_KEY, 0.0f)

        var code: String? = intent.getStringExtra(AlarmServiceManager.CURRENCY_CODE_KEY)

        if (code == null || code.isEmpty()) {
            code = app.getString(R.string.default_currency_code)
        }

        val res = interactors.getLastCurrency(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { currencyInRub: CurrencyInRub ->
                notify(context, currencyInRub, upLimit, downLimit)
            }
    }

    private fun notify(context: Context, curs: CurrencyInRub, upLimit: Float, downLimit: Float) {

        val intent = Intent(context, MainActivity::class.java)
        val notificationIntent =
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        if (curs.value > upLimit || curs.value < downLimit) {
            val notification = NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(curs.name.fullName)
                .setContentText(curs.denomination.toString() + " " + curs.name.code + " " + curs.value + " RUB ")
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )
                )
                .setAutoCancel(true)
                .setOngoing(false)

            val notifyManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            var channel: NotificationChannel? = notifyManager.getNotificationChannel("0")

            if (channel == null) {
                channel = NotificationChannel("1", "title", NotificationManager.IMPORTANCE_DEFAULT)
                notifyManager.createNotificationChannel(channel)
            }

            notifyManager.notify(333, notification.build())
        }


    }


}