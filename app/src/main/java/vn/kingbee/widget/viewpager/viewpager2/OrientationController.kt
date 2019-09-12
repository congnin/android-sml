package vn.kingbee.widget.viewpager.viewpager2

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.viewpager2.widget.ViewPager2

class OrientationController(private val viewPager: ViewPager2, private val spinner: Spinner) {
    fun setUp() {
        val orientation = viewPager.orientation
        val adapter = ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item,
            arrayOf(HORIZONTAL, VERTICAL))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val initialPosition = adapter.getPosition(orientationToString(orientation))
        if (initialPosition >= 0) {
            spinner.setSelection(initialPosition)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                viewPager.orientation = stringToOrientation(parent.selectedItem.toString())
            }
        }
    }

    private fun orientationToString(orientation: Int): String {
        return when (orientation) {
            ViewPager2.ORIENTATION_HORIZONTAL -> HORIZONTAL
            ViewPager2.ORIENTATION_VERTICAL -> VERTICAL
            else -> throw IllegalArgumentException("Orientation $orientation doesn't exist")
        }
    }

    internal fun stringToOrientation(string: String): Int {
        return when (string) {
            HORIZONTAL -> ViewPager2.ORIENTATION_HORIZONTAL
            VERTICAL -> ViewPager2.ORIENTATION_VERTICAL
            else -> throw IllegalArgumentException("Orientation $string doesn't exist")
        }
    }

    companion object {
        private const val HORIZONTAL = "horizontal"
        private const val VERTICAL = "vertical"
    }
}