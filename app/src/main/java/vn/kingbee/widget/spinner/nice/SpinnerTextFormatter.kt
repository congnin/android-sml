package vn.kingbee.widget.spinner.nice

import android.text.Spannable

interface SpinnerTextFormatter {
    fun format(text: String): Spannable

    fun format(item: Any): Spannable
}