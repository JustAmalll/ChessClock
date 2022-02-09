package dev.amal.chessclock

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.amal.chessclock.fragments.settings.SettingsFragment

class MainActivity : AppCompatActivity(), SetTheme {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        setContentView(R.layout.activity_main)
    }

    override fun setTheme() {
        val pref = getSharedPreferences(SettingsFragment.PREFERENCES_NAME, Context.MODE_PRIVATE)
        when (pref.getInt(SettingsFragment.THEME_ID, 2)) {
            1 -> setTheme(R.style.ThemeFirst)
            2 -> setTheme(R.style.ThemeSecond)
            3 -> setTheme(R.style.ThemeThird)
            4 -> setTheme(R.style.ThemeFourth)
            5 -> setTheme(R.style.ThemeFifth)
            6 -> setTheme(R.style.ThemeSixth)
        }
    }
}

interface SetTheme {
    fun setTheme()
}