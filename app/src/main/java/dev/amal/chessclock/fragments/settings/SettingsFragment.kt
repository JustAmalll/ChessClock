package dev.amal.chessclock.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

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

        return binding.root
    }

}