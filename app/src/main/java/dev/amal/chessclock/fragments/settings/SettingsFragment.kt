package dev.amal.chessclock.fragments.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentSettingsBinding
import dev.amal.chessclock.utils.CountDownTimer.Companion.ONE_MINUTE
import dev.amal.chessclock.utils.CountDownTimer.Companion.ONE_SECOND
import dev.amal.chessclock.utils.TimePickerDialog


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    private lateinit var pref: SharedPreferences

    companion object {
        const val PREFERENCES_NAME = "dev.amal.chessclock.PREF_NAME"
        const val SOUND_AFTER_MOVE_KEY = "sound_after_move"
        const val VIBRATE_KEY = "vibrate"
        const val LOW_TIME_WARNING_KEY = "low_time_warning"
        const val ALERT_TIME_KEY = "alert_time"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        pref = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        loadData()

        // UI ACTIONS
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.soundSettingContainer.setOnClickListener {
            binding.soundSwitch.isChecked = !binding.soundSwitch.isChecked
        }

        binding.soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            pref.edit().putBoolean(SOUND_AFTER_MOVE_KEY, isChecked).apply()
        }

        binding.vibrateSettingContainer.setOnClickListener {
            binding.vibrateSwitch.isChecked = !binding.vibrateSwitch.isChecked
        }

        binding.vibrateSwitch.setOnCheckedChangeListener { _, isChecked ->
            pref.edit().putBoolean(VIBRATE_KEY, isChecked).apply()
        }

        binding.lowTimeWarningSettingContainer.setOnClickListener {
            binding.lowTimeWarningSwitch.isChecked = !binding.lowTimeWarningSwitch.isChecked
        }

        binding.lowTimeWarningSwitch.setOnCheckedChangeListener { _, isChecked ->
            pref.edit().putBoolean(LOW_TIME_WARNING_KEY, isChecked).apply()
        }

        binding.alertTimeSettingContainer.setOnClickListener {
            showTimePickerForAlertTime()
        }

        return binding.root
    }

    private fun loadData() {
        val soundAfterMove = pref.getBoolean(SOUND_AFTER_MOVE_KEY, true)
        binding.soundSwitch.isChecked = soundAfterMove

        val vibrate = pref.getBoolean(VIBRATE_KEY, true)
        binding.vibrateSwitch.isChecked = vibrate

        val lowTimeWarning = pref.getBoolean(LOW_TIME_WARNING_KEY, false)
        binding.lowTimeWarningSwitch.isChecked = lowTimeWarning

        val alertTime = pref.getLong(ALERT_TIME_KEY, 0) / ONE_SECOND
        val alertTimeText = DateUtils.formatElapsedTime(alertTime)
        binding.alertTimeSummary.text = alertTimeText
    }

    private fun showTimePickerForAlertTime() {
        val timePicker = TimePickerDialog()
        timePicker.includeHours = false
        timePicker.setInitialTimeMillis(pref.getLong(ALERT_TIME_KEY, 0))
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
        pref.edit().putLong(ALERT_TIME_KEY, alertTime).apply()
    }

    private fun updateTimeAlertText(alertTime: Long) {
        val alertTimeText = DateUtils.formatElapsedTime(alertTime / ONE_SECOND)
        binding.alertTimeSummary.text = alertTimeText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}