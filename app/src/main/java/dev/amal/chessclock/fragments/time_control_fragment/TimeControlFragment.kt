package dev.amal.chessclock.fragments.time_control_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentTimeControlBinding
import dev.amal.chessclock.utils.TimePickerDialog

class TimeControlFragment : Fragment() {

    private var _binding: FragmentTimeControlBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TimeControlViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeControlBinding.inflate(inflater, container, false)

        val args = TimeControlFragmentArgs.fromBundle(requireArguments())
        setAppbarTitle(args.editOption)

        val application = requireActivity().application

        val viewModelFactory =
            TimeControlViewModelFactory(application, args.clockId, args.editOption)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TimeControlViewModel::class.java)

        // OBSERVERS
        viewModel.firstPlayerTime.observe(viewLifecycleOwner) {
            binding.playerOneTime.text = it
        }

        viewModel.secondPlayerTime.observe(viewLifecycleOwner) {
            binding.playerTwoTime.text = it
        }

        viewModel.sameValueSwitch.observe(viewLifecycleOwner) {
            binding.sameTimeSwitch.isChecked = it
        }

        viewModel.incrementTime.observe(viewLifecycleOwner) {
            binding.incrementTime.text = it
        }

        viewModel.closeFragment.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
                viewModel.onCloseDone()
            }
        }

        // UI ACTIONS
        binding.toolbar.setNavigationOnClickListener {
            viewModel.onNavigationClick()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.save_option_menu -> {
                    viewModel.onSaveOptionMenuClick()
                    true
                }
                else -> false
            }
        }

        binding.playerOneTime.setOnClickListener {
            showTimePickerForPlayerOne()
        }

        binding.playerTwoTime.setOnClickListener {
            showTimePickerForPlayerTwo()
        }

        binding.sameTimeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSameValueSwitchChange(isChecked)
        }

        binding.incrementTime.setOnClickListener {
            showTimePickerForIncrement()
        }

        return binding.root
    }

    private fun showTimePickerForPlayerOne() {
        val timePicker = TimePickerDialog()
        timePicker.maxValueHour = 10
        viewModel.chessClock.value?.let { chessClock ->
            timePicker.setInitialTimeMillis(chessClock.firstPlayerTime)
        }
        timePicker.setOnTimeSetOption(getString(R.string.set_time_button)) { h, m, s ->
            viewModel.onFirstPlayerTimeSet(h, m, s)
        }
        timePicker.setTitle(getString(R.string.timer_picker_title))
        timePicker.show(parentFragmentManager, "time_picker")
    }

    private fun showTimePickerForPlayerTwo() {
        val timePicker = TimePickerDialog()
        timePicker.maxValueHour = 10
        viewModel.chessClock.value?.let { chessClock ->
            timePicker.setInitialTimeMillis(chessClock.secondPlayerTime)
        }
        timePicker.setOnTimeSetOption(getString(R.string.set_time_button)) { h, m, s ->
            viewModel.onSecondPlayerTimeSet(h, m, s)
        }
        timePicker.setTitle(getString(R.string.timer_picker_title))
        timePicker.show(parentFragmentManager, "time_picker")
    }

    private fun showTimePickerForIncrement() {
        val timePicker = TimePickerDialog()
        timePicker.includeHours = false
        viewModel.chessClock.value?.let { chessClock ->
            timePicker.setInitialTimeMillis(chessClock.increment)
        }
        timePicker.setOnTimeSetOption(getString(R.string.set_time_button)) { _, m, s ->
            viewModel.onIncrementTimeSet(m, s)
        }
        timePicker.setTitle(getString(R.string.timer_picker_title))
        timePicker.show(parentFragmentManager, "time_picker")
    }

    private fun setAppbarTitle(editOption: Boolean) {
        if (editOption) binding.toolbar.title = getString(R.string.app_bar_edit_title)
        else binding.toolbar.title = getString(R.string.app_bar_create_title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}