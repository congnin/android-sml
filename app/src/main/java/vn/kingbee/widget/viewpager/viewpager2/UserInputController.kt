package vn.kingbee.widget.viewpager.viewpager2

import android.widget.CheckBox
import androidx.viewpager2.widget.ViewPager2

class UserInputController(private val viewPager: ViewPager2, private val disableBox: CheckBox) {
    fun setUp() {
        disableBox.isChecked = !viewPager.isUserInputEnabled
        disableBox.setOnCheckedChangeListener { _, isDisabled ->
            viewPager.isUserInputEnabled = !isDisabled
        }
    }
}