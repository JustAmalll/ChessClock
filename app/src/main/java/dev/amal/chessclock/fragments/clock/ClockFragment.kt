package dev.amal.chessclock.fragments.clock

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.amal.chessclock.BaseFragment
import dev.amal.chessclock.MainActivity
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentClockBinding
import dev.amal.chessclock.fragments.settings.SettingsFragment


class ClockFragment : BaseFragment() {

    private var _binding: FragmentClockBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ClockViewModel

    private lateinit var clockSound: MediaPlayer
    private lateinit var timeUpSound: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockBinding.inflate(inflater, container, false)

        val pref = requireActivity().getSharedPreferences(
            SettingsFragment.PREFERENCES_NAME, Context.MODE_PRIVATE
        )

        when (pref.getInt(SettingsFragment.THEME_ID, 2)) {

        }

        clockSound = MediaPlayer.create(requireContext(), R.raw.chess_clock_sound)
        timeUpSound = MediaPlayer.create(context, R.raw.time_up_sound)

        val application = requireActivity().application
        val factory = ClocksViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(ClockViewModel::class.java)
        viewModel.checkUpdatedPref()

        // Observers...
        viewModel.guidelinePercentage.observe(viewLifecycleOwner) {
            val changeBoundsTransition = ChangeBounds()
            changeBoundsTransition.duration = 200
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
            if (it == true) binding.clock1.textViewHint.visibility = View.VISIBLE
            else binding.clock1.textViewHint.visibility = View.INVISIBLE
        }

        viewModel.showHintTwo.observe(viewLifecycleOwner) {
            if (it == true) binding.clock2.textViewHint.visibility = View.VISIBLE
            else binding.clock2.textViewHint.visibility = View.INVISIBLE
        }

        viewModel.gamePaused.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.actionPause.visibility = View.INVISIBLE
                binding.actionGoToSettings.visibility = View.VISIBLE
                binding.actionRestart.visibility = View.VISIBLE
            } else {
                binding.actionGoToSettings.visibility = View.INVISIBLE
                binding.actionRestart.visibility = View.INVISIBLE
                binding.actionPause.visibility = View.VISIBLE
            }
        }

        viewModel.playerOneMoves.observe(viewLifecycleOwner) {
            binding.clock1.textMovementsCount.text = it.toString()
        }

        viewModel.playerTwoMoves.observe(viewLifecycleOwner) {
            binding.clock2.textMovementsCount.text = it.toString()
        }

        viewModel.showAlertTimeOne.observe(viewLifecycleOwner) {
            if (it == true) binding.clock1.alertTimeIcon.visibility = View.VISIBLE
            else binding.clock1.alertTimeIcon.visibility = View.INVISIBLE
        }

        viewModel.showAlertTimeTwo.observe(viewLifecycleOwner) {
            if (it == true) binding.clock2.alertTimeIcon.visibility = View.VISIBLE
            else binding.clock2.alertTimeIcon.visibility = View.INVISIBLE
        }

        viewModel.timeUpPlayerOne.observe(viewLifecycleOwner) {
            binding.clock1.root.isClickable = false
            binding.clock2.root.isClickable = false
            binding.clock1Container.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.design_default_color_error)
            )
        }

        viewModel.timeUpPlayerTwo.observe(viewLifecycleOwner) {
            binding.clock1.root.isClickable = false
            binding.clock2.root.isClickable = false
            binding.clock2Container.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.design_default_color_error)
            )
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
        binding.clock1.root.setOnClickListener {
            viewModel.onClickClock1()
        }

        binding.clock2.root.setOnClickListener {
            viewModel.onClickClock2()
        }

        binding.actionPause.setOnClickListener {
            viewModel.onClickPause()
        }

        binding.actionRestart.setOnClickListener {
            resetClocksAlertDialog()
        }

        binding.actionGoToSettings.setOnClickListener {
            viewModel.goToSettingsAction()
        }
        //...
        return binding.root
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
        } else {
            vibrator.vibrate(500)
        }
    }

    private fun resetClocksAlertDialog() {
        val restartBuilder = MaterialAlertDialogBuilder(requireContext())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}