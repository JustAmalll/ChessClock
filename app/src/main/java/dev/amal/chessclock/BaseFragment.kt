package dev.amal.chessclock

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun View.setBgColor(@ColorRes color: Int) {
        this.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }

    fun TextView.setTextViewColor(@ColorRes color: Int = R.color.white) {
        this.setTextColor(ContextCompat.getColor(requireContext(), color))
    }

}