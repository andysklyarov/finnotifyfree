package com.andysklyarov.finnotifyfree.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.databinding.SettingsFragmentBinding
import com.andysklyarov.finnotifyfree.ui.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SettingsFragment : Fragment(), OnBackPressed {

    private lateinit var viewModel: MainViewModel
    private lateinit var settingsView: View
    private var isSettingsSaved: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SettingsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        binding.viewModel = viewModel
        settingsView = binding.root
        return settingsView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initTimePicker()
        initAlarmSwitch()
        initToolbarButtons()
    }

    override fun onBackPressed() {
        if (isSettingsSaved) saveSettings() else cancelSettings()
    }

    override fun onDetach() {
        viewModel.dispatchDetach()
        super.onDetach()
    }

    private fun initTimePicker() {
        val timePicker: TimePicker = settingsView.findViewById(R.id.time_picker)
        timePicker.setIs24HourView(true)
        timePicker.hour = viewModel.getAlarmTime().getHour()
        timePicker.minute = viewModel.getAlarmTime().getMinute()
    }

    private fun initAlarmSwitch() {
        val alarmSwitch: SwitchCompat = settingsView.findViewById(R.id.alarm_switch)
        isSettingsSaved = viewModel.isServiceStarted.get()!!

        alarmSwitch.isChecked = isSettingsSaved

        setSettingsVisibility(alarmSwitch.isChecked)
        alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            setSettingsVisibility(isChecked)
            isSettingsSaved = isChecked
        }
    }

    private fun initToolbarButtons() {
        val appCompatActivity = (activity as AppCompatActivity)

        val infoButton: MaterialButton = appCompatActivity.findViewById(R.id.info_button)
        infoButton.visibility = View.INVISIBLE

        val codeButton: MaterialButton = appCompatActivity.findViewById(R.id.currency_button)
        codeButton.setOnClickListener(null)

        val alarmButton: MaterialButton = appCompatActivity.findViewById(R.id.alarm_button)
        alarmButton.icon = AppCompatResources.getDrawable(appCompatActivity, R.drawable.ic_close)
        alarmButton.setOnClickListener {
            if (isSettingsSaved) {
                saveSettings()
            } else {
                cancelSettings()
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun saveSettings() {
        if (viewModel.isServiceStarted.get()!!)
            viewModel.disableAlarm()

        val textInputTopLimit: TextInputEditText =
            settingsView.findViewById(R.id.textInput_top_limit)
        val textInputBottomLimit: TextInputEditText =
            settingsView.findViewById(R.id.textInput_bottom_limit)

        var topLimit: Float = textInputTopLimit.text.toString().toFloat()
        var bottomLimit: Float = textInputBottomLimit.text.toString().toFloat()
        val timePicker: TimePicker = settingsView.findViewById(R.id.time_picker)

        if (topLimit < 0) topLimit = 0f
        if (bottomLimit < 0) bottomLimit = 0f

        val hours = timePicker.hour
        val minutes = timePicker.minute

        viewModel.enableAlarm(hours, minutes, topLimit, bottomLimit)
    }

    private fun cancelSettings() {
        viewModel.disableAlarm()
    }

    private fun setSettingsVisibility(isVisible: Boolean) {
        val views: ArrayList<View> = ArrayList()
        views.add(settingsView.findViewById(R.id.time_picker))
        views.add(settingsView.findViewById(R.id.image_separator_1))
        views.add(settingsView.findViewById(R.id.image_separator_2))
        views.add(settingsView.findViewById(R.id.image_separator_3))
        views.add(settingsView.findViewById(R.id.image_separator_4))
        views.add(settingsView.findViewById(R.id.text_top_limit))
        views.add(settingsView.findViewById(R.id.textInput_top_limit))
        views.add(settingsView.findViewById(R.id.text_bottom_limit))
        views.add(settingsView.findViewById(R.id.textInput_bottom_limit))

        val visibility: Int = if (isVisible) View.VISIBLE else View.INVISIBLE

        for (v in views) {
            v.visibility = visibility
        }
    }

}