package vn.kingbee.widget.spinner.nice

import android.text.SpannableString
import android.text.Spannable

open class SimpleSpinnerTextFormatter : SpinnerTextFormatter {

    override fun format(text: String): Spannable {
        return SpannableString(text)
    }

    override fun format(item: Any): Spannable {
        return SpannableString(item.toString())
    }
}