package dev.amal.chessclock.fragments.time_control_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.amal.chessclock.databinding.FragmentTimeControlBinding


class TimeControlFragment : Fragment() {

    private var _binding: FragmentTimeControlBinding? = null
    private val binding: FragmentTimeControlBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeControlBinding.inflate(inflater, container, false)

        return binding.root
    }

}