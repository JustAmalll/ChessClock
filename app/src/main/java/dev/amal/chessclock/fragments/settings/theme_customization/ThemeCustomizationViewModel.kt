package dev.amal.chessclock.fragments.settings.theme_customization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThemeCustomizationViewModel : ViewModel() {

    private val _currentThemeId = MutableLiveData<Int>()
    val currentThemeId: LiveData<Int> get() = _currentThemeId

    fun setCurrentTheme(currentThemeId: Int) {
        _currentThemeId.value = currentThemeId
    }
}