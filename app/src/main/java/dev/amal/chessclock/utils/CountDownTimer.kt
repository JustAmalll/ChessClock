package dev.amal.chessclock.utils

import android.os.CountDownTimer

abstract class CountDownTimer(millisInFuture: Long, private var countDownInterval: Long) {

    companion object {
        const val NOT_STARTED = 0
        const val RUNNING = 1
        const val PAUSED = 2
        const val ONE_MINUTE = 60000L
        const val ONE_SECOND = 1000L
    }

    var state = NOT_STARTED
    private var timeLeft: Long = millisInFuture
    private var timer: CountDownTimer

    init {
        timer = initializeTimer(millisInFuture, countDownInterval)
    }

    private fun initializeTimer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                onTickTimer(millisUntilFinished)
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                onFinishTimer()
            }
        }
    }

    abstract fun onTickTimer(millisUntilFinished: Long)

    abstract fun onFinishTimer()

    fun startTimer() {
        timer.start()
        state = RUNNING
    }

    fun pauseTimer() {
        timer.cancel()
        state = PAUSED
    }

    fun resumeTimer() {
        timer = initializeTimer(timeLeft, countDownInterval).start()
        state = RUNNING
    }

    fun incrementTime(increment: Long) {
        timeLeft += increment
    }

    fun cancelTimer() {
        timer.cancel()
        state = NOT_STARTED
    }

}