package dev.amal.chessclock.fragments.clock

import android.app.Application
import android.content.Context
import android.text.format.DateUtils
import androidx.lifecycle.*
import dev.amal.chessclock.database.ChessClock
import dev.amal.chessclock.database.ChessClockDatabase
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.ALERT_TIME_KEY
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.LOW_TIME_WARNING_KEY
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.PREFERENCES_NAME
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.SOUND_AFTER_MOVE_KEY
import dev.amal.chessclock.fragments.settings.SettingsFragment.Companion.VIBRATE_KEY
import dev.amal.chessclock.utils.ChessUtils.Companion.CURRENT_CLOCK_KEY
import dev.amal.chessclock.utils.CountDownTimer
import dev.amal.chessclock.utils.CountDownTimer.Companion.NOT_STARTED
import dev.amal.chessclock.utils.CountDownTimer.Companion.ONE_MINUTE
import dev.amal.chessclock.utils.CountDownTimer.Companion.ONE_SECOND
import dev.amal.chessclock.utils.CountDownTimer.Companion.PAUSED
import dev.amal.chessclock.utils.CountDownTimer.Companion.RUNNING
import kotlinx.coroutines.launch

class ClockViewModel(application: Application) : ViewModel() {

    private val database = ChessClockDatabase.getInstance(application, viewModelScope)
        .chessClockDao

    private val pref = application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private var clock: ChessClock? = null

    companion object {
        const val TURN_1 = 1
        const val TURN_2 = 2
        const val NO_TURN = 0
        private const val PERCENT_33 = 0.33F
        private const val PERCENT_66 = 0.66F
        private const val PERCENT_50 = 0.50F
    }

    private var clockId: Long = pref.getLong(CURRENT_CLOCK_KEY, -1)

    private var lowTimeWarningActive = pref.getBoolean(LOW_TIME_WARNING_KEY, false)

    private var vibrationActive = pref.getBoolean(VIBRATE_KEY, true)

    private var timeAlert = pref.getLong(ALERT_TIME_KEY, 0)

    private var soundAfterMoveActive = pref.getBoolean(SOUND_AFTER_MOVE_KEY, true)

    // Timer 1
    private lateinit var timer1: CountDownTimer
    private val _timeLeft1 = MutableLiveData<Long>()
    private val timeLeft1: LiveData<Long> get() = _timeLeft1
    val timeLeftString1: LiveData<String> = Transformations.map(timeLeft1) {
        /* elapsedSeconds passed this way allows to set times like
            14sec, 56millis (which it's shown as 00:14) to be 15sec,55millis
            (which it's shown as 00:15). So when the time is 0sec, n-millis, it will
            show 00:01 until the time is 0sec 0millis when it'll be 00:00 (time up)
         */
        DateUtils.formatElapsedTime(((it + ONE_SECOND - 1) / ONE_SECOND))
    }
    private val _playerOneMoves = MutableLiveData<Int>()
    val playerOneMoves: LiveData<Int> get() = _playerOneMoves
    private val _showAlertTimeOne = MutableLiveData<Boolean>()
    val showAlertTimeOne: LiveData<Boolean> get() = _showAlertTimeOne
    private val _timeUpPlayerOne = MutableLiveData<Boolean>()
    val timeUpPlayerOne: LiveData<Boolean> get() = _timeUpPlayerOne
    private val _showHintOne = MutableLiveData<Boolean>()
    val showHintOne: LiveData<Boolean> get() = _showHintOne

    // Timer 2
    private lateinit var timer2: CountDownTimer
    private val _timeLeft2 = MutableLiveData<Long>()
    private val timeLeft2: LiveData<Long> get() = _timeLeft2
    val timeLeftString2: LiveData<String> = Transformations.map(timeLeft2) {
        /* elapsedSeconds passed this way allows to set times like
            14sec, 56millis (which it's shown as 00:14) to be 15sec,55millis
            (which it's shown as 00:15). So when the time is 0sec, n-millis, it will
            show 00:01 until the time is 0sec 0millis when it'll be 00:00 (time up)
         */
        DateUtils.formatElapsedTime(((it + ONE_SECOND - 1) / ONE_SECOND))
    }
    private val _playerTwoMoves = MutableLiveData<Int>()
    val playerTwoMoves: LiveData<Int> get() = _playerTwoMoves
    private val _showAlertTimeTwo = MutableLiveData<Boolean>()
    val showAlertTimeTwo: LiveData<Boolean> get() = _showAlertTimeTwo
    private val _timeUpPlayerTwo = MutableLiveData<Boolean>()
    val timeUpPlayerTwo: LiveData<Boolean> get() = _timeUpPlayerTwo
    private val _showHintTwo = MutableLiveData<Boolean>()
    val showHintTwo: LiveData<Boolean> get() = _showHintTwo

    private val _updateHintText = MutableLiveData<Boolean>()
    val updateHintText: LiveData<Boolean> get() = _updateHintText

    private val _gamePaused = MutableLiveData<Boolean>()
    val gamePaused: LiveData<Boolean> get() = _gamePaused

    private val _navigateToSettings = MutableLiveData<Boolean>()
    val navigateToSettings: LiveData<Boolean> get() = _navigateToSettings

    private val _turn = MutableLiveData<Int>()
    private val turn: LiveData<Int> get() = _turn

    private val _playClockSound = MutableLiveData<Boolean>()
    val playClockSound: LiveData<Boolean> get() = _playClockSound

    val guidelinePercentage: LiveData<Float> = Transformations.map(turn) {
        when (turn.value) {
            TURN_1 -> PERCENT_66
            TURN_2 -> PERCENT_33
            else -> PERCENT_50
        }
    }

    private var timeAlertCheck1: (Long) -> Unit = {}
    private var timeAlertCheck2: (Long) -> Unit = {}

    private val _vibrate = MutableLiveData<Boolean>()
    val vibrate: LiveData<Boolean> get() = _vibrate

    private val _playTimeUpSound = MutableLiveData<Boolean>()
    val playTimeUpSound: LiveData<Boolean> get() = _playTimeUpSound

    init {
        initializeCurrentClock()
        _gamePaused.value = true
        _turn.value = NO_TURN
        setPlayerMovesInitialValues()
        setShowHintInitialValues()
        setShowAlertTimeInitialValues()
    }

    fun checkUpdatedPref() {
        updateAlertPrefs()

        vibrationActive = pref.getBoolean(VIBRATE_KEY, true)

        soundAfterMoveActive = pref.getBoolean(SOUND_AFTER_MOVE_KEY, true)

        updateClock()
    }

    private fun updateAlertPrefs() {
        lowTimeWarningActive = pref.getBoolean(LOW_TIME_WARNING_KEY, false)
        timeAlert = pref.getLong(ALERT_TIME_KEY, 0)
        setAlertTimeChecks()
    }

    private fun updateClock() {
        val clockIdUpdated = pref.getLong(CURRENT_CLOCK_KEY, -1)
        if (clockIdUpdated != clockId) {
            clockId = clockIdUpdated
            if (timer1.state == NOT_STARTED && timer2.state == NOT_STARTED)
                initializeCurrentClock()
        }
    }

    private fun setPlayerMovesInitialValues() {
        _playerOneMoves.value = 0
        _playerTwoMoves.value = 0
    }

    private fun setShowHintInitialValues() {
        _showHintOne.value = true
        _showHintTwo.value = true
    }

    private fun setShowAlertTimeInitialValues() {
        _showAlertTimeOne.value = false
        _showAlertTimeTwo.value = false
    }

    private fun initializeCurrentClock() {
        viewModelScope.launch {
            clock = database.get(clockId)
            initializeTimer1()
            initializeTimer2()
            setAlertTimeChecks()
        }
    }

    private fun initializeTimer1() {
        val firstPlayerTime = clock?.firstPlayerTime ?: ONE_MINUTE * 5
        _timeLeft1.value = firstPlayerTime
        timer1 = object : CountDownTimer(firstPlayerTime, ONE_SECOND / 100) {
            override fun onTickTimer(millisUntilFinished: Long) {
                _timeLeft1.value = millisUntilFinished
                timeAlertCheck1(millisUntilFinished)
            }

            override fun onFinishTimer() {
                onTimeUpPlayerOne()
            }
        }
    }

    private fun initializeTimer2() {
        val secondPlayerTime = clock?.secondPlayerTime ?: ONE_MINUTE * 5
        _timeLeft2.value = secondPlayerTime
        timer2 = object : CountDownTimer(secondPlayerTime, ONE_SECOND / 100) {
            override fun onTickTimer(millisUntilFinished: Long) {
                _timeLeft2.value = millisUntilFinished
                timeAlertCheck2(millisUntilFinished)
            }

            override fun onFinishTimer() {
                onTimeUpPlayerTwo()
            }
        }
    }

    private fun onTimeUpPlayerOne() {
        _timeLeft1.value = 0
        _timeUpPlayerOne.value = true
        _gamePaused.value = true
        vibrate()
        playTimeUpSound()
    }

    private fun onTimeUpPlayerTwo() {
        _timeLeft2.value = 0
        _timeUpPlayerTwo.value = true
        _gamePaused.value = true
        vibrate()
        playTimeUpSound()
    }

    private fun playTimeUpSound() {
        _playTimeUpSound.value = true
        _playTimeUpSound.value = false
    }

    private fun vibrate() {
        if (vibrationActive) {
            _vibrate.value = true
            _vibrate.value = false
        }
    }

    private fun hideHintTexts() {
        _showHintOne.value = false
        _showHintTwo.value = false
    }

    fun onClickClock1() {
        when (timer1.state) {
            NOT_STARTED -> {
                if (timer2.state == NOT_STARTED) {
                    timer2.startTimer()
                    if (turn.value == NO_TURN) _updateHintText.value = true
                    _turn.value = TURN_2
                    _gamePaused.value = false
                    hideHintTexts()
                }
            }
            RUNNING -> {
                timer1.pauseTimer()
                timer2.resumeTimer()
                if (soundAfterMoveActive) _playClockSound.value = true
                _turn.value = TURN_2
                clock?.let {
                    timer1.incrementTime(it.increment)
                    _timeLeft1.value = _timeLeft1.value?.plus(it.increment)
                }
                _playerOneMoves.value = _playerOneMoves.value?.plus(1)
                // check after increment
                timeAlertCheck1(timeLeft1.value!!)
            }
            PAUSED -> {
                if (turn.value == TURN_1) {
                    timer1.resumeTimer()
                    _gamePaused.value = false
                    hideHintTexts()
                }
            }
        }
    }

    fun onClickClock2() {
        when (timer2.state) {
            NOT_STARTED -> {
                if (timer1.state == NOT_STARTED) {
                    timer1.startTimer()
                    if (turn.value == NO_TURN) _updateHintText.value = true
                    _turn.value = TURN_1
                    _gamePaused.value = false
                    hideHintTexts()
                }
            }
            RUNNING -> {
                timer2.pauseTimer()
                timer1.resumeTimer()
                if (soundAfterMoveActive) _playClockSound.value = true
                _turn.value = TURN_1
                clock?.let {
                    timer2.incrementTime(it.increment)
                    _timeLeft2.value = _timeLeft2.value?.plus(it.increment)
                }
                _playerTwoMoves.value = _playerTwoMoves.value?.plus(1)
                // check after increment
                timeAlertCheck2(timeLeft2.value!!)
            }
            PAUSED -> {
                if (turn.value == TURN_2) {
                    timer2.resumeTimer()
                    _gamePaused.value = false
                    hideHintTexts()
                }
            }
        }
    }

    fun goToSettingsAction() {
        _navigateToSettings.value = true
    }

    fun onSettingsNavigated() {
        _navigateToSettings.value = false
    }

    fun donePlayingClockSound() {
        _playClockSound.value = false
    }

    fun onClickPause() {
        pauseTimers()
        _gamePaused.value = true
        when (turn.value) {
            TURN_1 -> _showHintOne.value = true
            TURN_2 -> _showHintTwo.value = true
            NO_TURN -> {
                _showHintOne.value = true
                _showHintTwo.value = true
            }
        }
    }

    private fun setAlertTimeChecks() {
        if (lowTimeWarningActive && timeAlert > 0) {
            if (timeAlert < clock?.firstPlayerTime ?: ONE_MINUTE * 5) {
                timeAlertCheck1 = { currentTime ->
                    if (currentTime < timeAlert && showAlertTimeOne.value == false) {
                        _showAlertTimeOne.value = true
                        vibrate()
                    } else if (currentTime > timeAlert && showAlertTimeOne.value == true) {
                        _showAlertTimeOne.value = false
                    }
                }
            } else {
                timeAlertCheck1 = {}
            }
            if (timeAlert < clock?.secondPlayerTime ?: ONE_MINUTE * 5) {
                timeAlertCheck2 = { currentTime ->
                    if (currentTime < timeAlert && showAlertTimeTwo.value == false) {
                        _showAlertTimeTwo.value = true
                        vibrate()
                    } else if (currentTime > timeAlert && showAlertTimeTwo.value == true) {
                        _showAlertTimeTwo.value = false
                    }
                }
            } else {
                timeAlertCheck2 = {}
            }
        } else {
            timeAlertCheck1 = {}
            timeAlertCheck2 = {}
        }
    }

    private fun pauseTimers() {
        timer1.pauseTimer()
        timer2.pauseTimer()
    }

    override fun onCleared() {
        super.onCleared()
        timer1.cancelTimer()
        timer2.cancelTimer()
    }
}