package com.andysklyarov.finnotifyfree.ui.fragments

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.preference.*
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.alarm.AlarmServiceManager
import com.google.android.material.button.MaterialButton
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatterBuilder

const val SEEK_BAR_MIN_VALUE = 0
const val SEEK_BAR_MAX_VALUE = 100

private const val TYPE_CLASS_NUMBER_DECIMAL =
    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
private const val INITIAL_EXPANDED_CHILDREN_COUNT_MIN = 2
private const val INITIAL_EXPANDED_CHILDREN_COUNT_MAX = 5

private val TIME_FORMATTER_24: DateTimeFormatter = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .appendPattern("H:mm")
    .toFormatter()

private val TIME_FORMATTER_AM_PM: DateTimeFormatter = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .appendPattern("h:mma")
    .toFormatter()

private const val SETTINGS_INTERNAL_KEY = "settings_key"
private const val ALARM_TIME_INTERNAL_KEY = "alarm_time_key"

class SettingsFragment : PreferenceFragmentCompat(), OnBackPressed {
    private lateinit var alarmManager: AlarmServiceManager
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var settingsCategory: PreferenceCategory
    private lateinit var currencyCode: ListPreference

    private lateinit var alarmSwitch: SwitchPreferenceCompat
    private lateinit var alarmTime: Preference
    private lateinit var topLimit: EditTextPreference
    private lateinit var bottomLimit: EditTextPreference

    private lateinit var maxAbsDynamics: EditTextPreference
    private lateinit var lowDynamics: SeekBarPreference
    private lateinit var highDynamics: SeekBarPreference

    private var isTimeFormat24 = true // todo add getter setter

    private val onBindEditTextListenerToDecimal = EditTextPreference.OnBindEditTextListener {
        it.setRawInputType(TYPE_CLASS_NUMBER_DECIMAL)
    }

    private val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        val time: LocalTime = LocalTime.of(hourOfDay, minute)

        saveLocalTimeToSettings(time)

        alarmTime.summary = if (view.is24HourView) {
            time.format(TIME_FORMATTER_24)
        } else {
            time.format(TIME_FORMATTER_AM_PM)
        }
    }


    private val onTimePreferenceClickListener = Preference.OnPreferenceClickListener {
        val time: LocalTime = loadLocalTimeFromSettings()
        val timeDialog = TimePickerDialog(activity, timeSetListener, time.hour, time.minute, isTimeFormat24)
        timeDialog.show()
        true
    }

    private val onAlarmSwitchPreferenceChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            val newCheckedValue: Boolean = newValue as Boolean

            toggleSettingsCategory(newCheckedValue)

            alarmTime.isVisible = newCheckedValue
            topLimit.isVisible = newCheckedValue
            bottomLimit.isVisible = newCheckedValue
            true
        }

    private val onMaxAbsDynamicsChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            val str: String = (newValue as String)
            val value = str.toFloatOrNull()
            if (value != null && value >= 0) {
                updateDynamicsPreferences(value)
                true
            } else {
                showToastInCenter(R.string.number_format_error)
                false
            }
        }

    private val onLimitChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
        val str: String = (newValue as String)
        val value = str.toFloatOrNull()
        if (value != null && value >= 0) {
            true
        } else {
            showToastInCenter(R.string.number_format_error)
            false
        }
    }

    private val editTexSummaryProvider = Preference.SummaryProvider<EditTextPreference> {
        it.text + " " + getString(R.string.base_currency)
    }

    private val onLowDynamicsPreferenceChangeListener =
        Preference.OnPreferenceChangeListener { preference, newValue ->
            val lowValue = newValue as Int
            val highValue = highDynamics.value

            if (lowValue in SEEK_BAR_MIN_VALUE..highValue) {
                preference.summary = formatDynamicsSummary(lowValue, maxAbsDynamics.text.toFloat())
                true
            } else {
                false
            }
        }

    private val onHighDynamicsPreferenceChangeListener =
        Preference.OnPreferenceChangeListener { preference, newValue ->
            val lowValue = lowDynamics.value
            val highValue = newValue as Int

            if (highValue in lowValue..SEEK_BAR_MAX_VALUE) {
                preference.summary = formatDynamicsSummary(highValue, maxAbsDynamics.text.toFloat())
                true
            } else {
                false
            }
        }

    private val onCodeChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
        val appCompatActivity = (activity as AppCompatActivity)
        val codeTextView: TextView = appCompatActivity.findViewById(R.id.currency_textview)
        codeTextView.text = newValue as String
        true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val appCompatActivity = (activity as AppCompatActivity)

        // Init toolbar buttons
        val infoButton: MaterialButton = appCompatActivity.findViewById(R.id.info_button)
        val alarmButton: MaterialButton = appCompatActivity.findViewById(R.id.settings_button)

        infoButton.visibility = View.INVISIBLE
        alarmButton.icon = AppCompatResources.getDrawable(appCompatActivity, R.drawable.ic_close)
        alarmButton.setOnClickListener {
            startIfEnabledAlarmService()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.common_settings, rootKey)

        alarmManager = AlarmServiceManager(requireContext())
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        if (!tryInitializePreferences()) {
            requireActivity().supportFragmentManager.popBackStack()
            return
        }

        currencyCode.onPreferenceChangeListener = onCodeChangeListener

        val isChecked = alarmSwitch.isChecked
        alarmTime.isVisible = isChecked
        alarmTime.onPreferenceClickListener = onTimePreferenceClickListener

        val time: LocalTime = loadLocalTimeFromSettings()
        if (isTimeFormat24) {
            alarmTime.summary = time.format(TIME_FORMATTER_24)
        } else {
            alarmTime.summary = time.format(TIME_FORMATTER_AM_PM)
        }

        topLimit.isVisible = isChecked
        topLimit.setOnBindEditTextListener(onBindEditTextListenerToDecimal)
        topLimit.summaryProvider = editTexSummaryProvider
        topLimit.onPreferenceChangeListener = onLimitChangeListener
        topLimit.setDefaultValue("0.0")

        bottomLimit.isVisible = isChecked
        bottomLimit.setOnBindEditTextListener(onBindEditTextListenerToDecimal)
        bottomLimit.summaryProvider = editTexSummaryProvider
        bottomLimit.onPreferenceChangeListener = onLimitChangeListener
        bottomLimit.setDefaultValue("0.0")

        toggleSettingsCategory(isChecked)
        alarmSwitch.onPreferenceChangeListener = onAlarmSwitchPreferenceChangeListener

        maxAbsDynamics.setOnBindEditTextListener(onBindEditTextListenerToDecimal)
        maxAbsDynamics.summaryProvider = editTexSummaryProvider
        maxAbsDynamics.onPreferenceChangeListener = onMaxAbsDynamicsChangeListener

        if (maxAbsDynamics.text == null) maxAbsDynamics.text = "1"
        updateDynamicsPreferences(maxAbsDynamics.text.toFloat())

        lowDynamics.onPreferenceChangeListener = onLowDynamicsPreferenceChangeListener
        highDynamics.onPreferenceChangeListener = onHighDynamicsPreferenceChangeListener
    }

    override fun onBackPressed() {
        startIfEnabledAlarmService()
    }

    private fun startIfEnabledAlarmService() {
        if (alarmSwitch.isChecked) {
            val timeInNano = sharedPreferences.getLong(
                requireActivity().getString(R.string.alarm_time_data_long_key),
                0
            )
            val time: LocalTime = LocalTime.ofNanoOfDay(timeInNano)

            alarmManager.startRepeatingService(
                currencyCode.value,
                time.hour,
                time.minute,
                topLimit.text.toFloat(),
                bottomLimit.text.toFloat()
            )
        } else {
            alarmManager.stopRepeatingService()
        }
    }

    private fun tryInitializePreferences(): Boolean {
        return try {
            val activity = requireActivity()
            settingsCategory = findPreference(SETTINGS_INTERNAL_KEY)!!
            currencyCode = findPreference(activity.getString(R.string.currency_code_string_key))!!
            alarmSwitch = findPreference(activity.getString(R.string.enable_alarm_boolean_key))!!
            alarmTime = findPreference(ALARM_TIME_INTERNAL_KEY)!!
            topLimit = findPreference(activity.getString(R.string.top_limit_string_key))!!
            bottomLimit = findPreference(activity.getString(R.string.bottom_limit_string_key))!!
            maxAbsDynamics =
                findPreference(activity.getString(R.string.max_abs_dynamics_string_key))!!
            lowDynamics = findPreference(activity.getString(R.string.low_dynamics_int_key))!!
            highDynamics = findPreference(activity.getString(R.string.high_dynamics_int_key))!!
            true
        } catch (e: Exception) {
            showToastInCenter(R.string.preferences_init_error)
            false
        }
    }

    private fun updateDynamicsPreferences(baseValue: Float) {
        lowDynamics.summary = formatDynamicsSummary(lowDynamics.value, baseValue)
        highDynamics.summary = formatDynamicsSummary(highDynamics.value, baseValue)
    }

    private fun formatDynamicsSummary(value: Int, baseValue: Float): String {
        val current = baseValue * (value.toFloat() / SEEK_BAR_MAX_VALUE)
        return "±%.2f".format(current) + " " + getString(R.string.base_currency)
    }

    private fun toggleSettingsCategory(initChecked: Boolean) {
        if (initChecked) {
            settingsCategory.initialExpandedChildrenCount = INITIAL_EXPANDED_CHILDREN_COUNT_MAX
        } else {
            settingsCategory.initialExpandedChildrenCount = INITIAL_EXPANDED_CHILDREN_COUNT_MIN
        }
    }

    private fun saveLocalTimeToSettings(time: LocalTime) {
        val myEditor = sharedPreferences.edit()
        myEditor.putLong(
            requireActivity().getString(R.string.alarm_time_data_long_key),
            time.toNanoOfDay()
        )
        myEditor.apply()
    }

    private fun loadLocalTimeFromSettings(): LocalTime {
        val timeInNano = sharedPreferences.getLong(
            requireActivity().getString(R.string.alarm_time_data_long_key),
            0
        )
        return LocalTime.ofNanoOfDay(timeInNano)
    }

    private fun showToastInCenter(messageResId: Int) {
        val message = getString(messageResId)
        val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}