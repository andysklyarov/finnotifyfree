package com.andysklyarov.finnotifyfree.framework.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

private const val SERVICE_STATUS_KEY = "SERVICE_STATUS_KEY"

class AlarmServiceManager(val context: Context) {

    companion object {
        const val TIME_IN_MILLIS_KEY = "TIME_IN_MILLIS_KEY"
        const val TOP_LIMIT_KEY = "TOP_LIMIT_KEY"
        const val BOTTOM_LIMIT_KEY = "BOTTOM_LIMIT_KEY"
        const val CURRENCY_CODE_KEY = "CURRENCY_CODE_KEY"
    }

    private val manager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private lateinit var state: ServiceState
    private lateinit var alarmTime: ZonedDateTime

    init {
        loadServiceData()
    }

    fun getAlarmTime(): ZonedDateTime {
        return alarmTime
    }

    fun setAlarmTime24(hour: Int, minutes: Int) {
        val startOfDayTime = ZonedDateTime.now()
            .toLocalDate()
            .atStartOfDay(ZoneId.systemDefault())

        alarmTime = startOfDayTime.plusHours(hour.toLong()).plusMinutes(minutes.toLong())
        if (alarmTime.isBefore(ZonedDateTime.now())) {
            alarmTime = alarmTime.plusDays(1)
        }
    }

    fun startRepeatingService(currencyCode: String, topLimit: Float, bottomLimit: Float) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(CURRENCY_CODE_KEY, currencyCode)
        intent.putExtra(TOP_LIMIT_KEY, topLimit)
        intent.putExtra(BOTTOM_LIMIT_KEY, bottomLimit)

        val alarmIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val currentTimeMillis = alarmTime.toInstant().toEpochMilli()

        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            currentTimeMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

        state = ServiceState(true, currentTimeMillis, topLimit, bottomLimit)
        safeServiceData()
    }

    fun stopRepeatingService() {
        val intent = Intent(context, AlarmReceiver::class.java)

        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        manager.cancel(alarmIntent)
        state = ServiceState(false, state)
        safeServiceData()
    }

    fun getServiceState(): ServiceState {
        return state
    }

    private fun loadServiceData() {
        val myPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        val isStarted = myPreferences.getBoolean(SERVICE_STATUS_KEY, false)
        val timeToStartInMillis = myPreferences.getLong(TIME_IN_MILLIS_KEY, 0)
        val topLimit = myPreferences.getFloat(TOP_LIMIT_KEY, 0f)
        val bottomLimit = myPreferences.getFloat(BOTTOM_LIMIT_KEY, 0f)

        state = ServiceState(isStarted, timeToStartInMillis, topLimit, bottomLimit)

        val i: Instant = Instant.ofEpochMilli(timeToStartInMillis)
        alarmTime = ZonedDateTime.ofInstant(i, ZoneId.systemDefault())
    }

    private fun safeServiceData() {
        val myPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val myEditor = myPreferences.edit()
        myEditor.putBoolean(SERVICE_STATUS_KEY, state.isStarted)
        myEditor.putFloat(TOP_LIMIT_KEY, state.topLimit)
        myEditor.putFloat(BOTTOM_LIMIT_KEY, state.bottomLimit)
        myEditor.putLong(TIME_IN_MILLIS_KEY, state.timeToStartInMillis)
        myEditor.apply()
    }
}