package dev.amal.chessclock.fragments.clock

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClockViewModel(application: Application) : ViewModel() {

    private val _navigateToSettings = MutableLiveData<Boolean>()
    val navigateToSettings: LiveData<Boolean> get() = _navigateToSettings

    fun goToSettingsAction() {
        _navigateToSettings.value = true
    }

    fun onSettingsNavigated() {
        _navigateToSettings.value = false
    }

}