package dev.amal.chessclock.fragments.time_control_fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class TimeControlViewModelFactory(
    private val application: Application,
    private val clockId: Long,
    private val editOption: Boolean
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeControlViewModel::class.java))
            TimeControlViewModel(application, clockId, editOption) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}