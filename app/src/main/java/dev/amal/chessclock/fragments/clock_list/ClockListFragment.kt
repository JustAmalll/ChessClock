package dev.amal.chessclock.fragments.clock_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.amal.chessclock.databinding.FragmentClockListBinding

class ClockListFragment : Fragment() {

    private var _binding: FragmentClockListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockListBinding.inflate(inflater, container, false)

        // UI ACTIONS
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.addClock.setOnClickListener{
            val action = ClockListFragmentDirections.actionClockListFragmentToTimeControlFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}