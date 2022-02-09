package dev.amal.chessclock.fragments.clock_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.amal.chessclock.database.ChessClockDatabase
import kotlinx.coroutines.launch

class ClockListViewModel(application: Application, clockId: Long) : ViewModel() {

    private val database = ChessClockDatabase
        .getInstance(application, viewModelScope).chessClockDao

    val clocks = database.getAllChessClocks()

    private val _currentClockId = MutableLiveData<Long>()
    val currentClockId: LiveData<Long> get() = _currentClockId

    init {
        _currentClockId.value = clockId
    }

    fun setCurrentClockId(id: Long) {
        _currentClockId.value = id
    }

    fun onRemoveClick(): Boolean = clocks.value?.size!! > 1

    fun removeItem(id: Long) {
        viewModelScope.launch {
            database.deleteById(id)
            if (id == currentClockId.value) {
                val chessClock = database.getAny()
                _currentClockId.value = chessClock.id
            }
        }
    }
}