package dev.amal.chessclock.fragments.settings

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.amal.chessclock.BaseFragment
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentThemeCustomizationBinding
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.THEME_ID


class ThemeCustomizationFragment : BaseFragment() {

    private var _binding: FragmentThemeCustomizationBinding? = null
    private val binding get() = _binding!!

    val viewModel: ThemeCustomizationViewModel by viewModels()

    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeCustomizationBinding.inflate(inflater, container, false)

        pref = requireActivity().getSharedPreferences(
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

        viewModel.currentThemeId.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
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
                        animatedCheckMark4.visibility = View.GONE
                        animatedCheckMark5.visibility = View.VISIBLE
                        animatedCheckMark5.playAnimation()
                        animatedCheckMark6.visibility = View.GONE
                    }
                    6 -> {
                        animatedCheckMark1.visibility = View.GONE
                        animatedCheckMark2.visibility = View.GONE
                        animatedCheckMark3.visibility = View.GONE
                        animatedCheckMark4.visibility = View.GONE
                        animatedCheckMark5.visibility = View.GONE
                        animatedCheckMark6.visibility = View.VISIBLE
                        animatedCheckMark6.playAnimation()
                    }
                }
            }
        }

        binding.applyButton.setOnClickListener {
            viewModel.currentThemeId.observe(viewLifecycleOwner) {
                pref.edit().putInt(THEME_ID, it).apply()
            }
            findNavController().navigate(R.id.action_themeCustomizationFragment_to_settingsFragment)
        }

        binding.firstThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(1)
            setFirstTheme()
        }

        binding.secondThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(2)
            setSecondTheme()
        }

        binding.thirdThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(3)
            setThirdTheme()
        }

        binding.fourthThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(4)
            setFourthTheme()
        }

        binding.fifthThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(5)
            setFifthTheme()
        }

        binding.sixthThemeCv.setOnClickListener {
            viewModel.setCurrentTheme(6)
            setSixthTheme()
        }

        return binding.root
    }

    private fun setFirstTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_one_main)
            clock1ContainerPreview.setBgColor(R.color.theme_one_main)
            clock2ContainerPreview.setBgColor(R.color.theme_one_secondary)
            clock2TextViewPreview.setTextViewColor()
            clock1TextViewPreview.setTextViewColor()
            animatedCheckMark1.playAnimation()
        }
    }

    private fun setSecondTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_two_main)
            clock1ContainerPreview.setBgColor(R.color.theme_two_main)
            clock2ContainerPreview.setBackgroundColor(Color.WHITE)
            clock2TextViewPreview.setTextViewColor(R.color.theme_two_main)
            animatedCheckMark2.playAnimation()
        }
    }

    private fun setThirdTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_three_main)
            clock1ContainerPreview.setBgColor(R.color.theme_three_main)
            clock2ContainerPreview.setBgColor(R.color.theme_three_secondary)
            clock2TextViewPreview.setTextViewColor(R.color.theme_two_main)
            clock1TextViewPreview.setTextViewColor()
            animatedCheckMark3.playAnimation()
        }
    }

    private fun setFourthTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_four_main)
            clock1ContainerPreview.setBgColor(R.color.theme_four_main)
            clock2ContainerPreview.setBgColor(R.color.theme_four_secondary)
            clock2TextViewPreview.setTextViewColor()
            clock1TextViewPreview.setTextViewColor()
            animatedCheckMark4.playAnimation()
        }
    }

    private fun setFifthTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_five_main)
            clock1ContainerPreview.setBgColor(R.color.theme_five_main)
            clock2ContainerPreview.setBgColor(R.color.theme_five_secondary)
            clock2TextViewPreview.setTextViewColor()
            clock1TextViewPreview.setTextViewColor()
            animatedCheckMark5.playAnimation()
        }
    }

    private fun setSixthTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_six_main)
            clock1ContainerPreview.setBgColor(R.color.theme_six_main)
            clock2ContainerPreview.setBgColor(R.color.theme_six_secondary)
            clock2TextViewPreview.setTextViewColor(R.color.theme_six_main)
            clock1TextViewPreview.setTextViewColor(R.color.theme_six_secondary)
            animatedCheckMark6.playAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

