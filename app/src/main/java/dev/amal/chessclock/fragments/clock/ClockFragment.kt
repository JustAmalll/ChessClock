package dev.amal.chessclock.fragments.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentClockBinding

class ClockFragment : Fragment() {

    private var _binding: FragmentClockBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ClockViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockBinding.inflate(inflater, container, false)

        val application = requireActivity().application
        val factory = ClocksViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(ClockViewModel::class.java)

        viewModel.navigateToSettings.observe(viewLifecycleOwner) {
            if (it == true) navigateToSettings()
        }

        binding.actionGoToSettings.setOnClickListener {
            viewModel.goToSettingsAction()
        }

        return binding.root
    }

    private fun navigateToSettings() {
        findNavController().navigate(R.id.action_clockFragment_to_clockListFragment)
        viewModel.onSettingsNavigated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}