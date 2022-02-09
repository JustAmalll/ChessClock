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
import dev.amal.chessclock.MainActivity
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentThemeCustomizationBinding
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.THEME_ID


class ThemeCustomizationFragment : BaseFragment() {

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
            1 -> {
                setFirstTheme()
                binding.animatedCheckMark1.speed = 999f
                binding.animatedCheckMark1.playAnimation()
            }
            2 -> {
                setSecondTheme()
                binding.animatedCheckMark2.speed = 999f
                binding.animatedCheckMark2.playAnimation()
            }
            3 -> {
                setThirdTheme()
                binding.animatedCheckMark3.speed = 999f
                binding.animatedCheckMark3.playAnimation()
            }
            4 -> {
                setFourthTheme()
                binding.animatedCheckMark4.speed = 999f
                binding.animatedCheckMark4.playAnimation()
            }
            5 -> {
                setFifthTheme()
                binding.animatedCheckMark5.speed = 999f
                binding.animatedCheckMark5.playAnimation()
            }
            6 -> {
                setSixthTheme()
                binding.animatedCheckMark6.speed = 999f
                binding.animatedCheckMark6.playAnimation()
            }
        }

        viewModel.currentThemeId.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    1 -> {
                        animatedCheckMark1.visibility = View.VISIBLE
                        animatedCheckMark1.speed = 4f
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
                        animatedCheckMark2.speed = 4f
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
                        animatedCheckMark3.speed = 4f
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
                        animatedCheckMark4.speed = 4f
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
                        animatedCheckMark5.speed = 4f
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
                        animatedCheckMark6.speed = 4f
                        animatedCheckMark6.playAnimation()
                    }
                }
            }
        }

        binding.applyButton.setOnClickListener {
            viewModel.currentThemeId.observe(viewLifecycleOwner) {
                pref.edit().putInt(THEME_ID, it).apply()
            }
            (activity as MainActivity).setTheme()
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
            clockTopContainerPreview.setBgColor(R.color.theme_one_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_one_secondary)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor()
        }
    }

    private fun setSecondTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_two_main)
            clockTopContainerPreview.setBgColor(R.color.theme_two_main)
            clockBottomContainerPreview.setBackgroundColor(Color.WHITE)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor(R.color.theme_two_main)
        }
    }

    private fun setThirdTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_three_main)
            clockTopContainerPreview.setBgColor(R.color.theme_three_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_three_secondary)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor(R.color.theme_two_main)
        }
    }

    private fun setFourthTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_four_main)
            clockTopContainerPreview.setBgColor(R.color.theme_four_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_four_secondary)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor()
        }
    }

    private fun setFifthTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_five_main)
            clockTopContainerPreview.setBgColor(R.color.theme_five_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_five_secondary)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor()
        }
    }

    private fun setSixthTheme() {
        binding.apply {
            applyButton.setBgColor(R.color.theme_six_main)
            clockTopContainerPreview.setBgColor(R.color.theme_six_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_six_secondary)
            clockTopTextViewPreview.setTextViewColor(R.color.theme_six_secondary)
            clockBottomTextViewPreview.setTextViewColor(R.color.theme_six_main)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

