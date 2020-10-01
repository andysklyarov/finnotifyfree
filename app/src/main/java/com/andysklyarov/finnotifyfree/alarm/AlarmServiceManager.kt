package com.andysklyarov.finnotifyfree.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class AlarmServiceManager(private val context: Context) {

    companion object {
        const val TOP_LIMIT_KEY = "TOP_LIMIT_KEY"
        const val BOTTOM_LIMIT_KEY = "BOTTOM_LIMIT_KEY"
        const val CURRENCY_CODE_KEY = "CURRENCY_CODE_KEY"
    }

    private val manager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun startRepeatingService(
        currencyCode: String,
        hour: Int,
        minutes: Int,
        topLimit: Float,
        bottomLimit: Float
    ) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(CURRENCY_CODE_KEY, currencyCode)
        intent.putExtra(TOP_LIMIT_KEY, topLimit)
        intent.putExtra(BOTTOM_LIMIT_KEY, bottomLimit)

        val alarmIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmTime = toZonedDateTime(hour, minutes)
        val currentTimeMillis = alarmTime.toInstant().toEpochMilli()
        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            currentTimeMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    fun stopRepeatingService() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        manager.cancel(alarmIntent)
    }

    private fun toZonedDateTime(hour: Int, minutes: Int): ZonedDateTime {

        val startOfDayTime = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault())

        var alarmTime = startOfDayTime.plusHours(hour.toLong()).plusMinutes(minutes.toLong())
        if (alarmTime.isBefore(ZonedDateTime.now())) {
            alarmTime = alarmTime.plusDays(1)
        }
        return alarmTime
    }
}