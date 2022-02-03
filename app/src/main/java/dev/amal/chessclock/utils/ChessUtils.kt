package dev.amal.chessclock.utils

import android.content.res.Resources
import dev.amal.chessclock.R
import dev.amal.chessclock.database.ChessClock
import dev.amal.chessclock.utils.CountDownTimer.Companion.ONE_MINUTE
import dev.amal.chessclock.utils.CountDownTimer.Companion.ONE_SECOND

class ChessUtils {

    companion object {
        const val BULLET = 5
        const val BLITZ = 10
        const val RAPID = 60
        const val CLASSIC = 100
        const val CURRENT_CLOCK_KEY = "CURRENT_CLOCK_KEY"

        /**
         * Receives two times for first and second player, and an increment.
         * Return a String formatted as:
         * "'firstPlayerTime' vs 'secondPlayerTime' | 'increment'"
         * All parameter have to be in milliseconds
         */
        fun getTimesText(
            resources: Resources,
            firstPlayerTime: Long,
            secondPlayerTime: Long,
            increment: Long
        ): String {
            val firstText = getTimeFormatted(firstPlayerTime)
            val secondText = getTimeFormatted(secondPlayerTime)
            var incrementText = resources.getString(R.string.no_increment_clock)
            if (increment > 0) {
                val incrementTime = getTimeFormatted(increment)
                incrementText = resources.getString(R.string.increment_clock, incrementTime)
            }
            return "$firstText vs $secondText | $incrementText"
        }

        private fun getTimeFormatted(time: Long): String {
            val hour = time / (ONE_MINUTE * 60)
            val minute = (time % (ONE_MINUTE * 60)) / ONE_MINUTE
            val second = ((time % (ONE_MINUTE * 60)) % ONE_MINUTE) / ONE_SECOND
            var timeFormatted = ""
            if (hour > 0) timeFormatted = "${hour}h"
            if (minute > 0) timeFormatted += "${minute}m"
            if (second > 0) timeFormatted += "${second}s"
            return timeFormatted
        }
    }
}


class DataUtil {
    companion object {
        fun getDefaultClocks(): List<ChessClock> {
            return arrayListOf(
                ChessClock(firstPlayerTime = ONE_MINUTE, secondPlayerTime = ONE_MINUTE),
                ChessClock(firstPlayerTime = 2 * ONE_MINUTE, secondPlayerTime = 2 * ONE_MINUTE),
                ChessClock(firstPlayerTime = 5 * ONE_MINUTE, secondPlayerTime = 5 * ONE_MINUTE),
                ChessClock(firstPlayerTime = 10 * ONE_MINUTE, secondPlayerTime = 10 * ONE_MINUTE),
                ChessClock(firstPlayerTime = 15 * ONE_MINUTE, secondPlayerTime = 15 * ONE_MINUTE),
                ChessClock(firstPlayerTime = 60 * ONE_MINUTE, secondPlayerTime = 60 * ONE_MINUTE)
            )
        }
    }
}
