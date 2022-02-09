package dev.amal.chessclock.fragments.clock_list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ClockListViewModelFactory(
    private val application: Application,
    private val clockId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClockListViewModel::class.java))
            ClockListViewModel(application, clockId) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}