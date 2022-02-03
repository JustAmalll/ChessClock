package dev.amal.chessclock.fragments.clock

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentClockBinding

class ClockFragment : Fragment() {

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

        clockSound = MediaPlayer.create(requireContext(), R.raw.chess_clock_sound)
        timeUpSound = MediaPlayer.create(context, R.raw.time_up_sound)

        val application = requireActivity().application
        val factory = ClocksViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(ClockViewModel::class.java)
        viewModel.checkUpdatedPref()

        /** Observers */
        viewModel.timeLeftString1.observe(viewLifecycleOwner) {
            binding.textViewClockTop.text = it
        }

        viewModel.timeLeftString2.observe(viewLifecycleOwner) {
            binding.textViewClockBottom.text = it
        }

        viewModel.navigateToSettings.observe(viewLifecycleOwner) {
            if (it == true) navigateToSettings()
        }

        viewModel.updateHintText.observe(viewLifecycleOwner) {
            binding.textViewHintTop.text = getString(R.string.paused_clock_hint)
            binding.textViewHintBottom.text = getString(R.string.paused_clock_hint)
        }

        viewModel.showHintOne.observe(viewLifecycleOwner) {
            if (it == true) binding.textViewHintTop.visibility = View.VISIBLE
            else binding.textViewHintTop.visibility = View.INVISIBLE
        }

        viewModel.showHintTwo.observe(viewLifecycleOwner) {
            if (it == true) binding.textViewHintBottom.visibility = View.VISIBLE
            else binding.textViewHintBottom.visibility = View.INVISIBLE
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
            binding.textMovementsCountTop.text = it.toString()
        }

        viewModel.playerTwoMoves.observe(viewLifecycleOwner) {
            binding.textMovementsCountBottom.text = it.toString()
        }

        viewModel.showAlertTimeOne.observe(viewLifecycleOwner) {
            if (it == true) binding.alertTimeIconTop.visibility = View.VISIBLE
            else binding.alertTimeIconTop.visibility = View.INVISIBLE
        }

        viewModel.showAlertTimeTwo.observe(viewLifecycleOwner) {
            if (it == true) binding.alertTimeIconBottom.visibility = View.VISIBLE
            else binding.alertTimeIconBottom.visibility = View.INVISIBLE
        }

        viewModel.timeUpPlayerOne.observe(viewLifecycleOwner) {
            binding.llBackgroundTop.isClickable = false
            binding.llBackgroundBottom.isClickable = false
            binding.llBackgroundTop.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.design_default_color_error)
            )
        }

        viewModel.timeUpPlayerTwo.observe(viewLifecycleOwner) {
            binding.llBackgroundTop.isClickable = false
            binding.llBackgroundBottom.isClickable = false
            binding.llBackgroundBottom.setCardBackgroundColor(
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

        /** UI Actions */
        binding.llBackgroundTop.setOnClickListener {
            viewModel.onClickClock1()
            setCardBgColor(binding.llBackgroundBottom, R.color.orange)
            setCardBgColor(binding.llBackgroundTop, R.color.light_gray)
        }

        binding.llBackgroundBottom.setOnClickListener {
            viewModel.onClickClock2()
            setCardBgColor(binding.llBackgroundTop, R.color.orange)
            setCardBgColor(binding.llBackgroundBottom, R.color.light_gray)
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
            setNegativeButton(R.string.cancel_button) { _, _ -> // nothing }
            }.create().show()
        }
    }

    private fun resetTimer() {
        val navController: NavController = requireActivity().findNavController(R.id.navHostFragment)
        navController.run {
            popBackStack()
            navigate(R.id.clockFragment)
        }
    }

    private fun navigateToSettings() {
        findNavController().navigate(R.id.action_clockFragment_to_clockListFragment)
        viewModel.onSettingsNavigated()
    }

    private fun setCardBgColor(cardView: CardView, @ColorRes color: Int) {
        cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
