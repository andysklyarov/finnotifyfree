package com.andysklyarov.finnotifyfree.alarm


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.andysklyarov.domain.interactors.CurrencyInteractors
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.ui.MainActivity
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class AlarmReceiver @Inject constructor() : DaggerBroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_ID = 333
        private const val NOTIFICATION_CHANNEL_ID = "0"

        private fun drawableToBitmap(drawable: Drawable): Bitmap? {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }

    @Inject
    lateinit var useCase: CurrencyInteractors

    @SuppressLint("CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val app: AppDelegate = context.applicationContext as AppDelegate

        val upLimit = intent.getFloatExtra(AlarmServiceManager.TOP_LIMIT_KEY, 0.0f)
        val downLimit = intent.getFloatExtra(AlarmServiceManager.BOTTOM_LIMIT_KEY, 0.0f)

        var code: String? = intent.getStringExtra(AlarmServiceManager.CURRENCY_CODE_KEY)

        if (code == null || code.isEmpty()) code = app.getString(R.string.default_currency_code)

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

        val app: AppDelegate = context.applicationContext as AppDelegate

        val baseCurrencyCode = app.getString(R.string.base_currency)


        val d: Drawable =
            ResourcesCompat.getDrawable(app.resources, R.mipmap.ic_launcher, app.theme)!!
        val bitmap = drawableToBitmap(d)!!

        if (curs.value > upLimit || curs.value < downLimit) {
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(curs.fullName)
                .setContentText("${curs.nom} ${curs.chCode} = ${curs.value} $baseCurrencyCode")
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
                .setLargeIcon(Bitmap.createScaledBitmap(bitmap, 128, 128, false))
                .setSmallIcon(R.mipmap.ic_launcher)


            val notifyManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var channel: NotificationChannel? = notifyManager.getNotificationChannel(
                    NOTIFICATION_CHANNEL_ID
                )
                if (channel == null) {
                    channel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "title",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notifyManager.createNotificationChannel(channel)
                }
                notifyManager.notify(NOTIFICATION_ID, notification.build())
            } else {
                notifyManager.notify(NOTIFICATION_ID, notification.build())
            }
        }
    }
}