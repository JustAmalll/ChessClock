package dev.amal.chessclock.fragments.settings.theme_customization

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.amal.chessclock.MainActivity
import dev.amal.chessclock.R
import dev.amal.chessclock.databinding.FragmentThemeCustomizationBinding
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.THEME_ID
import dev.amal.chessclock.utils.BaseFragment


class ThemeCustomizationFragment : BaseFragment<FragmentThemeCustomizationBinding>(
    FragmentThemeCustomizationBinding::inflate
) {

    val viewModel: ThemeCustomizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (preferences.getInt(THEME_ID, 2)) {
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
                        animatedCheckMark1.visible()
                        animatedCheckMark1.speed = 4f
                        animatedCheckMark1.playAnimation()
                        animatedCheckMark2.gone()
                        animatedCheckMark3.gone()
                        animatedCheckMark4.gone()
                        animatedCheckMark5.gone()
                        animatedCheckMark6.gone()
                    }
                    2 -> {
                        animatedCheckMark1.gone()
                        animatedCheckMark2.visible()
                        animatedCheckMark2.speed = 4f
                        animatedCheckMark2.playAnimation()
                        animatedCheckMark3.gone()
                        animatedCheckMark4.gone()
                        animatedCheckMark5.gone()
                        animatedCheckMark6.gone()
                    }
                    3 -> {
                        animatedCheckMark1.gone()
                        animatedCheckMark2.gone()
                        animatedCheckMark3.visible()
                        animatedCheckMark3.speed = 4f
                        animatedCheckMark3.playAnimation()
                        animatedCheckMark4.gone()
                        animatedCheckMark5.gone()
                        animatedCheckMark6.gone()
                    }
                    4 -> {
                        animatedCheckMark1.gone()
                        animatedCheckMark2.gone()
                        animatedCheckMark3.gone()
                        animatedCheckMark4.visible()
                        animatedCheckMark4.speed = 4f
                        animatedCheckMark4.playAnimation()
                        animatedCheckMark5.gone()
                        animatedCheckMark6.gone()
                    }
                    5 -> {
                        animatedCheckMark1.gone()
                        animatedCheckMark2.gone()
                        animatedCheckMark3.gone()
                        animatedCheckMark4.gone()
                        animatedCheckMark5.visible()
                        animatedCheckMark5.speed = 4f
                        animatedCheckMark5.playAnimation()
                        animatedCheckMark6.gone()
                    }
                    6 -> {
                        animatedCheckMark1.gone()
                        animatedCheckMark2.gone()
                        animatedCheckMark3.gone()
                        animatedCheckMark4.gone()
                        animatedCheckMark5.gone()
                        animatedCheckMark6.visible()
                        animatedCheckMark6.speed = 4f
                        animatedCheckMark6.playAnimation()
                    }
                }
            }
        }

        binding.applyButton.setOnClickListener {
            viewModel.currentThemeId.observe(viewLifecycleOwner) {
                preferences.edit().putInt(THEME_ID, it).apply()
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
    }


    private fun setFirstTheme() {
        binding.apply {
            applyButton.setCardViewBgColor(R.color.theme_one_main)
            clockTopContainerPreview.setBgColor(R.color.theme_one_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_one_secondary)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor()
        }
    }

    private fun setSecondTheme() {
        binding.apply {
            applyButton.setCardViewBgColor(R.color.theme_two_main)
            clockTopContainerPreview.setBgColor(R.color.theme_two_main)
            clockBottomContainerPreview.setBackgroundColor(Color.WHITE)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor(R.color.theme_two_main)
        }
    }

    private fun setThirdTheme() {
        binding.apply {
            applyButton.setCardViewBgColor(R.color.theme_three_main)
            clockTopContainerPreview.setBgColor(R.color.theme_three_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_three_secondary)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor(R.color.theme_two_main)
        }
    }

    private fun setFourthTheme() {
        binding.apply {
            applyButton.setCardViewBgColor(R.color.theme_four_main)
            clockTopContainerPreview.setBgColor(R.color.theme_four_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_four_secondary)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor()
        }
    }

    private fun setFifthTheme() {
        binding.apply {
            applyButton.setCardViewBgColor(R.color.theme_five_main)
            clockTopContainerPreview.setBgColor(R.color.theme_five_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_five_secondary)
            clockTopTextViewPreview.setTextViewColor()
            clockBottomTextViewPreview.setTextViewColor()
        }
    }

    private fun setSixthTheme() {
        binding.apply {
            applyButton.setCardViewBgColor(R.color.theme_six_main)
            clockTopContainerPreview.setBgColor(R.color.theme_six_main)
            clockBottomContainerPreview.setBgColor(R.color.theme_six_secondary)
            clockTopTextViewPreview.setTextViewColor(R.color.theme_six_secondary)
            clockBottomTextViewPreview.setTextViewColor(R.color.theme_six_main)
        }
    }
}