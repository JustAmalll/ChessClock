package dev.amal.chessclock.fragments.settings

import android.animation.Animator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentThemeCustomizationBinding
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.THEME_ID


class ThemeCustomizationFragment : Fragment() {

    private var _binding: FragmentThemeCustomizationBinding? = null
    private val binding get() = _binding!!

    val viewModel: ThemeCustomizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeCustomizationBinding.inflate(inflater, container, false)

        val pref = requireActivity().getSharedPreferences(
            SettingsFragment.PREFERENCES_NAME, Context.MODE_PRIVATE
        )

        when (pref.getInt(THEME_ID, 2)) {
            1 -> setFirstTheme()
            2 -> setSecondTheme()
            3 -> setThirdTheme()
            4 -> setFourthTheme()
            5 -> setFifthTheme()
            6 -> setSixthTheme()
        }

        binding.applyButton.setOnClickListener {
            viewModel.currentThemeId.observe(viewLifecycleOwner) {
                pref.edit().putInt(THEME_ID, it).apply()
            }
            findNavController().navigate(R.id.action_themeCustomizationFragment_to_settingsFragment)
        }

        binding.firstThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(1)
            animate(1)
            setFirstTheme()
        }

        binding.secondThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(2)
            animate(2)
            setSecondTheme()
        }

        binding.thirdThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(3)
            animate(3)
            setThirdTheme()
        }

        binding.fourthThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(4)
            animate(4)
            setFourthTheme()
        }

        binding.fifthThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(5)
            animate(5)
            setFifthTheme()
        }

        binding.sixthThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(6)
            animate(6)
            setSixthTheme()
        }

        return binding.root
    }

    private fun setFirstTheme() {
        binding.apply {
            setBackgroundColor(applyButton, R.color.theme_one_main)
            setBackgroundColor(clock1ContainerPreview, R.color.theme_one_main)
            setBackgroundColor(clock2ContainerPreview, R.color.theme_one_secondary)
            clock2TextViewPreview.setTextColor(Color.WHITE)
            clock1TextViewPreview.setTextColor(Color.WHITE)
            animatedCheckMark1.playAnimation()
        }
    }

    private fun setSecondTheme() {
        binding.apply {
            setBackgroundColor(applyButton, R.color.theme_two_main)
            setBackgroundColor(clock1ContainerPreview, R.color.theme_two_main)
            clock2ContainerPreview.setBackgroundColor(Color.WHITE)
            setTextColor(clock2TextViewPreview, R.color.theme_two_main)
            animatedCheckMark2.playAnimation()
        }
    }

    private fun setThirdTheme() {
        binding.apply {
            setBackgroundColor(applyButton, R.color.theme_three_main)
            setBackgroundColor(clock1ContainerPreview, R.color.theme_three_main)
            setBackgroundColor(clock2ContainerPreview, R.color.theme_three_secondary)
            setTextColor(clock2TextViewPreview, R.color.theme_two_main)
            clock1TextViewPreview.setTextColor(Color.WHITE)
            animatedCheckMark3.playAnimation()
        }
    }

    private fun setFourthTheme() {
        binding.apply {
            setBackgroundColor(applyButton, R.color.theme_four_main)
            setBackgroundColor(clock1ContainerPreview, R.color.theme_four_main)
            setBackgroundColor(clock2ContainerPreview, R.color.theme_four_secondary)
            clock2TextViewPreview.setTextColor(Color.WHITE)
            clock1TextViewPreview.setTextColor(Color.WHITE)
            animatedCheckMark4.playAnimation()
        }
    }

    private fun setFifthTheme() {
        binding.apply {
            setBackgroundColor(applyButton, R.color.theme_five_main)
            setBackgroundColor(clock1ContainerPreview, R.color.theme_five_main)
            setBackgroundColor(clock2ContainerPreview, R.color.theme_five_secondary)
            clock2TextViewPreview.setTextColor(Color.WHITE)
            clock1TextViewPreview.setTextColor(Color.WHITE)
            animatedCheckMark5.playAnimation()
        }
    }

    private fun setSixthTheme() {
        binding.apply {
            setBackgroundColor(applyButton, R.color.theme_six_main)
            setBackgroundColor(clock1ContainerPreview, R.color.theme_six_main)
            setBackgroundColor(clock2ContainerPreview, R.color.theme_six_secondary)
            setTextColor(clock2TextViewPreview, R.color.theme_six_main)
            setTextColor(clock1TextViewPreview, R.color.theme_six_secondary)
            animatedCheckMark5.playAnimation()
        }
    }

    // it will be refactored in future
    private fun animate(themeId: Int) {
        binding.apply {
            when (themeId) {
                1 -> {
                    animatedCheckMark1.visibility = View.VISIBLE
                    animatedCheckMark1.playAnimation()
                    animatedCheckMark2.visibility = View.GONE
                    animatedCheckMark3.visibility = View.GONE
                    animatedCheckMark4.visibility = View.GONE
                    animatedCheckMark5.visibility = View.GONE
                    animatedCheckMark6.visibility = View.GONE
                }
                2 -> {
                    animatedCheckMark1.visibility = View.GONE
                    animatedCheckMark2.visibility = View.VISIBLE
                    animatedCheckMark2.playAnimation()
                    animatedCheckMark3.visibility = View.GONE
                    animatedCheckMark4.visibility = View.GONE
                    animatedCheckMark5.visibility = View.GONE
                    animatedCheckMark6.visibility = View.GONE
                }
                3 -> {
                    animatedCheckMark1.visibility = View.GONE
                    animatedCheckMark2.visibility = View.GONE
                    animatedCheckMark3.visibility = View.VISIBLE
                    animatedCheckMark3.playAnimation()
                    animatedCheckMark4.visibility = View.GONE
                    animatedCheckMark5.visibility = View.GONE
                    animatedCheckMark6.visibility = View.GONE
                }
                4 -> {
                    animatedCheckMark1.visibility = View.GONE
                    animatedCheckMark2.visibility = View.GONE
                    animatedCheckMark3.visibility = View.GONE
                    animatedCheckMark4.visibility = View.VISIBLE
                    animatedCheckMark4.playAnimation()
                    animatedCheckMark5.visibility = View.GONE
                    animatedCheckMark6.visibility = View.GONE
                }
                5 -> {
                    animatedCheckMark1.visibility = View.GONE
                    animatedCheckMark2.visibility = View.GONE
                    animatedCheckMark3.visibility = View.GONE
                    animatedCheckMark3.visibility = View.GONE
                    animatedCheckMark4.visibility = View.GONE
                    animatedCheckMark5.visibility = View.VISIBLE
                    animatedCheckMark5.playAnimation()
                    animatedCheckMark6.visibility = View.GONE
                }
                6 -> {
                    animatedCheckMark1.visibility = View.GONE
                    animatedCheckMark2.visibility = View.GONE
                    animatedCheckMark3.visibility = View.GONE
                    animatedCheckMark3.visibility = View.GONE
                    animatedCheckMark4.visibility = View.GONE
                    animatedCheckMark5.visibility = View.VISIBLE
                    animatedCheckMark5.visibility = View.GONE
                    animatedCheckMark6.visibility = View.VISIBLE
                    animatedCheckMark6.playAnimation()
                }
            }
        }
    }

    private fun setBackgroundColor(view: View, @ColorRes color: Int) {
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }

    private fun setTextColor(view: TextView, @ColorRes color: Int) {
        view.setTextColor(ContextCompat.getColor(requireContext(), color))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

