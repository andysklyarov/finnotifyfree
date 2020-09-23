package com.andysklyarov.finnotifyfree.alarm


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.domain.interactors.CurrencyInteractors
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.ui.MainActivity
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AlarmReceiver @Inject constructor() : DaggerBroadcastReceiver() {
    @Inject
    lateinit var useCase: CurrencyInteractors

    private lateinit var disposable: Disposable

    @SuppressLint("CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val app: AppDelegate = context.applicationContext as AppDelegate

        val upLimit = intent.getFloatExtra(AlarmServiceManager.TOP_LIMIT_KEY, 0.0f)
        val downLimit = intent.getFloatExtra(AlarmServiceManager.BOTTOM_LIMIT_KEY, 0.0f)

        var code: String? = intent.getStringExtra(AlarmServiceManager.CURRENCY_CODE_KEY)

        if (code == null || code.isEmpty()) {
            code = app.getString(R.string.default_currency_code)
        }

        useCase.getLastCurrency(code)
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
                .setContentTitle(curs.fullName)
                .setContentText(curs.nom.toString() + " " + curs.chCode + " = " + curs.value + " RUB ")
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