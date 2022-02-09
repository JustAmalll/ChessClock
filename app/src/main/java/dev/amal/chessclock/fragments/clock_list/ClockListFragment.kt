package dev.amal.chessclock.fragments.clock_list

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentClockListBinding
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.PREFERENCES_NAME
import dev.amal.chessclock.utils.ChessUtils.Companion.CURRENT_CLOCK_KEY

class ClockListFragment : Fragment() {

    private var _binding: FragmentClockListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ClockListViewModel
    private lateinit var preferences: SharedPreferences

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockListBinding.inflate(inflater, container, false)

        val application = requireActivity().application

        preferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        val currentClockId = preferences.getLong(CURRENT_CLOCK_KEY, -1)

        val viewModelFactory = ClockListViewModelFactory(application, currentClockId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClockListViewModel::class.java)

        val adapter = ClockListAdapter(currentClockId, getClockItemListener())
        binding.clockList.adapter = adapter

        // OBSERVERS...
        viewModel.clocks.observe(viewLifecycleOwner) { adapter.data = it }

        viewModel.currentClockId.observe(viewLifecycleOwner) {
            preferences.edit().putLong(CURRENT_CLOCK_KEY, it).apply()
            adapter.currentClockId = it
            adapter.notifyDataSetChanged()
        }

        // UI ACTIONS
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sound_settings -> {
                    val action =
                        ClockListFragmentDirections.actionClockListFragmentToSettingsFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }

        binding.addClock.setOnClickListener {
            val action = ClockListFragmentDirections.actionClockListFragmentToTimeControlFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun getClockItemListener(): ClockItemListener = object : ClockItemListener {

        override fun onClickItem(clockId: Long) {
            viewModel.setCurrentClockId(clockId)
            findNavController().navigateUp()
        }

        override fun onEditItem(clockId: Long) {
            val action =
                ClockListFragmentDirections.actionClockListFragmentToTimeControlFragment()
            action.clockId = clockId
            action.editOption = true
            findNavController().navigate(action)
        }

        override fun onRemoveItem(clockId: Long) {
            val removeItem = viewModel.onRemoveClick()
            if (removeItem) showConfirmDeleteDialog(clockId)
            else showSnackBarOneClock()
        }
    }

    private fun showSnackBarOneClock() {
        Snackbar.make(binding.root, R.string.empty_clock_list_advice, Snackbar.LENGTH_SHORT).show()
    }

    private fun showConfirmDeleteDialog(clockId: Long) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle(R.string.delete_clock_title)
        dialog.setMessage(R.string.delete_clock_message)
        dialog.setPositiveButton(R.string.delete_clock_confirm_button) { _, _ -> viewModel.removeItem(clockId) }
        dialog.setNegativeButton(R.string.cancel_button) { _, _ -> }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}