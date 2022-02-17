package dev.amal.chessclock.fragments.clock

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentClockBinding
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.THEME_ID
import dev.amal.chessclock.utils.BaseFragment


class ClockFragment : BaseFragment<FragmentClockBinding>(
    FragmentClockBinding::inflate
) {

    private lateinit var viewModel: ClockViewModel

    private lateinit var clockSound: MediaPlayer
    private lateinit var timeUpSound: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBarColor()
        setButtonColor()

        clockSound = MediaPlayer.create(requireContext(), R.raw.chess_clock_sound)
        timeUpSound = MediaPlayer.create(context, R.raw.time_up_sound)

        val application = requireActivity().application
        val factory = ClocksViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(ClockViewModel::class.java)
        viewModel.checkUpdatedPref()

        // Observers...
        viewModel.guidelinePercentage.observe(viewLifecycleOwner) {
            val changeBoundsTransition = ChangeBounds()
            changeBoundsTransition.duration = 250
            TransitionManager.beginDelayedTransition(binding.root, changeBoundsTransition)
            binding.guideline.setGuidelinePercent(it)
        }

        viewModel.timeLeftString1.observe(viewLifecycleOwner) {
            binding.clock1.textViewClock.text = it
        }

        viewModel.timeLeftString2.observe(viewLifecycleOwner) {
            binding.clock2.textViewClock.text = it
        }

        viewModel.navigateToSettings.observe(viewLifecycleOwner) {
            if (it == true) navigateToSettings()
        }

        viewModel.updateHintText.observe(viewLifecycleOwner) {
            binding.clock1.textViewHint.text = getString(R.string.paused_clock_hint)
            binding.clock2.textViewHint.text = getString(R.string.paused_clock_hint)
        }

        viewModel.showHintOne.observe(viewLifecycleOwner) {
            if (it == true) binding.clock1.textViewHint.visible()
            else binding.clock1.textViewHint.gone()
        }

        viewModel.showHintTwo.observe(viewLifecycleOwner) {
            if (it == true) binding.clock2.textViewHint.visible()
            else binding.clock2.textViewHint.gone()
        }

        viewModel.gamePaused.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.actionPause.gone()
                binding.actionGoToSettings.visible()
                binding.actionRestart.visible()
            } else {
                binding.actionGoToSettings.gone()
                binding.actionRestart.gone()
                binding.actionPause.visible()
            }
        }

        viewModel.playerOneMoves.observe(viewLifecycleOwner) {
            binding.clock1.textMovementsCount.text = it.toString()
        }

        viewModel.playerTwoMoves.observe(viewLifecycleOwner) {
            binding.clock2.textMovementsCount.text = it.toString()
        }

        viewModel.showAlertTimeOne.observe(viewLifecycleOwner) {
            if (it == true) binding.clock1.alertTimeIcon.visible()
            else binding.clock1.alertTimeIcon.gone()
        }

        viewModel.showAlertTimeTwo.observe(viewLifecycleOwner) {
            if (it == true) binding.clock2.alertTimeIcon.visible()
            else binding.clock2.alertTimeIcon.gone()
        }

        viewModel.timeUpPlayerOne.observe(viewLifecycleOwner) {
            binding.apply {
                clock1.root.isClickable = false
                clock2.root.isClickable = false
                clock1.textViewClock.gone()
                clock1.imageViewLose.visible()
            }
        }

        viewModel.timeUpPlayerTwo.observe(viewLifecycleOwner) {
            binding.clock1.root.isClickable = false
            binding.clock2.root.isClickable = false
            binding.clock2.textViewClock.gone()
            binding.clock2.imageViewLose.visible()
        }

        viewModel.playClockSound.observe(viewLifecycleOwner) {
            if (it == true) {
                playClockSound()
                viewModel.donePlayingClockSound()
            }
        }

        viewModel.vibrate.observe(viewLifecycleOwner) {
            if (it == true) makeVibrate()
        }

        viewModel.playTimeUpSound.observe(viewLifecycleOwner) {
            if (it == true) playTimeUpSound()
        }

        // UI actions
        binding.clock1.root.setOnClickListener { viewModel.onClickClock1() }

        binding.clock2.root.setOnClickListener { viewModel.onClickClock2() }

        binding.actionPause.setOnClickListener { viewModel.onClickPause() }

        binding.actionRestart.setOnClickListener { resetClocksAlertDialog() }

        binding.actionGoToSettings.setOnClickListener { viewModel.goToSettingsAction() }
    }

    private fun playClockSound() {
        if (clockSound.isPlaying) {
            clockSound.pause()
            clockSound.seekTo(0)
        }
        clockSound.start()
    }

    private fun playTimeUpSound() {
        if (timeUpSound.isPlaying) {
            timeUpSound.pause()
            timeUpSound.seekTo(0)
        }
        timeUpSound.start()
    }

    @Suppress("DEPRECATION")
    private fun makeVibrate() {
        val vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.cancel() // cancel any other current vibration
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else vibrator.vibrate(500)
    }

    private fun resetClocksAlertDialog() {
        val restartBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        restartBuilder.apply {
            setTitle(R.string.reset_timer_title)
            setPositiveButton(R.string.reset_button) { _, _ -> resetTimer() }
            setNegativeButton(R.string.cancel_button) { _, _ -> }
        }.create().show()
    }

    private fun resetTimer() {
        val navController: NavController = requireActivity().findNavController(R.id.navHostFragment)
        navController.run {
            popBackStack()
            navigate(R.id.clockFragment)
        }
    }

    private fun navigateToSettings() {
        val action = ClockFragmentDirections.actionClockFragmentToClockListFragment()
        this.findNavController().navigate(action)
        viewModel.onSettingsNavigated()
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            when (preferences.getInt(THEME_ID, 2)) {
                1 -> statusBarColor = getContextCompactColor(R.color.theme_one_main)
                2 -> statusBarColor = getContextCompactColor(R.color.theme_two_main)
                3 -> statusBarColor = getContextCompactColor(R.color.theme_three_main)
                4 -> statusBarColor = getContextCompactColor(R.color.theme_four_main)
                5 -> statusBarColor = getContextCompactColor(R.color.theme_five_main)
                6 -> statusBarColor = getContextCompactColor(R.color.theme_six_main)
            }
        }
    }

    private fun setButtonColor() {
        binding.apply {
            when (preferences.getInt(THEME_ID, 2)) {
                1 -> {
                    actionGoToSettings.setImageResource(R.drawable.ic_settings_btn_first_theme)
                    actionPause.setImageResource(R.drawable.ic_pause_btn_first_theme)
                    actionRestart.setImageResource(R.drawable.ic_restart_btn_first_theme)
                }
                2 -> {
                    actionGoToSettings.setImageResource(R.drawable.ic_settings_btn_second_theme)
                    actionPause.setImageResource(R.drawable.ic_pause_btn_second_theme)
                    actionRestart.setImageResource(R.drawable.ic_restart_btn_second_theme)
                }
                3 -> {
                    actionGoToSettings.setImageResource(R.drawable.ic_settings_btn_third_theme)
                    actionPause.setImageResource(R.drawable.ic_pause_btn_third_theme)
                    actionRestart.setImageResource(R.drawable.ic_restart_btn_third_theme)
                }
                4 -> {
                    actionGoToSettings.setImageResource(R.drawable.ic_settings_btn_fourth_theme)
                    actionPause.setImageResource(R.drawable.ic_pause_btn_fourth_theme)
                    actionRestart.setImageResource(R.drawable.ic_restart_btn_fourth_theme)
                }
                5 -> {
                    actionGoToSettings.setImageResource(R.drawable.ic_settings_btn_fifth_theme)
                    actionPause.setImageResource(R.drawable.ic_pause_btn_fifth_theme)
                    actionRestart.setImageResource(R.drawable.ic_restart_btn_fifth_theme)
                }
                6 -> {
                    actionGoToSettings.setImageResource(R.drawable.ic_settings_btn_sixth_theme)
                    actionPause.setImageResource(R.drawable.ic_pause_btn_sixth_theme)
                    actionRestart.setImageResource(R.drawable.ic_restart_btn_sixth_theme)
                }
            }
        }
    }
}