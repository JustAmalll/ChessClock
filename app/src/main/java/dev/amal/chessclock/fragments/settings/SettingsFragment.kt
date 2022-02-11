package dev.amal.chessclock.fragments.settings

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.navigation.fragment.findNavController
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentSettingsBinding
import dev.amal.chessclock.utils.BaseFragment
import dev.amal.chessclock.utils.CountDownTimer.Companion.ONE_MINUTE
import dev.amal.chessclock.utils.CountDownTimer.Companion.ONE_SECOND
import dev.amal.chessclock.utils.TimePickerDialog


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    companion object {
        const val PREFERENCES_NAME = "dev.amal.chessclock.PREF_NAME"
        const val SOUND_AFTER_MOVE_KEY = "sound_after_move"
        const val VIBRATE_KEY = "vibrate"
        const val LOW_TIME_WARNING_KEY = "low_time_warning"
        const val ALERT_TIME_KEY = "alert_time"
        const val THEME_ID = "theme_id"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        // UI ACTIONS
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.soundSettingContainer.setOnClickListener {
            binding.soundSwitch.isChecked = !binding.soundSwitch.isChecked
        }

        binding.soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean(SOUND_AFTER_MOVE_KEY, isChecked).apply()
        }

        binding.vibrateSettingContainer.setOnClickListener {
            binding.vibrateSwitch.isChecked = !binding.vibrateSwitch.isChecked
        }

        binding.vibrateSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean(VIBRATE_KEY, isChecked).apply()
        }

        binding.lowTimeWarningSettingContainer.setOnClickListener {
            binding.lowTimeWarningSwitch.isChecked = !binding.lowTimeWarningSwitch.isChecked
        }

        binding.lowTimeWarningSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean(LOW_TIME_WARNING_KEY, isChecked).apply()
            renderLowTimeWarningSwitch(isChecked)
        }

        binding.alertTimeSettingContainer.setOnClickListener { showTimePickerForAlertTime() }

        binding.themeSettingsContainer.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_themeCustomizationFragment)
        }
    }

    private fun renderLowTimeWarningSwitch(boolean: Boolean) {
        if (boolean) {
            binding.apply {
                vibrateSettingContainer.isClickable = true
                vibrateSwitch.isEnabled = true
                alertTimeSettingContainer.isEnabled = true
                vibrateSettingsTitle.setTextViewColor(R.color.black)
                alertTimeTitle.setTextViewColor(R.color.black)
                alertTimeSummary.setTextViewColor(R.color.help_text)
            }
        } else {
            binding.apply {
                vibrateSettingContainer.isClickable = false
                vibrateSwitch.isEnabled = false
                alertTimeSettingContainer.isEnabled = false
                vibrateSettingsTitle.setTextViewColor(R.color.inactive_text)
                alertTimeTitle.setTextViewColor(R.color.inactive_text)
                alertTimeSummary.setTextViewColor(R.color.inactive_text)
            }
        }
    }

    private fun loadData() {
        val soundAfterMove = preferences.getBoolean(SOUND_AFTER_MOVE_KEY, true)
        binding.soundSwitch.isChecked = soundAfterMove

        val vibrate = preferences.getBoolean(VIBRATE_KEY, true)
        binding.vibrateSwitch.isChecked = vibrate

        val lowTimeWarning = preferences.getBoolean(LOW_TIME_WARNING_KEY, false)
        binding.lowTimeWarningSwitch.isChecked = lowTimeWarning
        renderLowTimeWarningSwitch(lowTimeWarning)

        val alertTime = preferences.getLong(ALERT_TIME_KEY, 0) / ONE_SECOND
        val alertTimeText = DateUtils.formatElapsedTime(alertTime)
        binding.alertTimeSummary.text = alertTimeText
    }

    private fun showTimePickerForAlertTime() {
        val timePicker = TimePickerDialog()
        timePicker.includeHours = false
        timePicker.setInitialTimeMillis(preferences.getLong(ALERT_TIME_KEY, 0))
        timePicker.setOnTimeSetOption(getString(R.string.set_time_button)) { _, m, s ->
            onTimeAlertSet(m, s)
        }
        timePicker.setTitle(getString(R.string.timer_picker_title))
        timePicker.show(parentFragmentManager, "time_picker")
    }

    private fun onTimeAlertSet(minutes: Int, seconds: Int) {
        val alertTime = getTimeInMillis(minutes, seconds)
        saveAlertTimePreference(alertTime)
        updateTimeAlertText(alertTime)
    }

    private fun getTimeInMillis(
        minutes: Int, seconds: Int
    ): Long = (minutes * ONE_MINUTE + seconds * ONE_SECOND)

    private fun saveAlertTimePreference(alertTime: Long) {
        preferences.edit().putLong(ALERT_TIME_KEY, alertTime).apply()
    }

    private fun updateTimeAlertText(alertTime: Long) {
        val alertTimeText = DateUtils.formatElapsedTime(alertTime / ONE_SECOND)
        binding.alertTimeSummary.text = alertTimeText
    }
}