package vn.kingbee.widget.viewpager.viewpager2

import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import vn.kingbee.widget.R
import vn.kingbee.widget.viewpager.viewpager2.cards.CardViewAdapter

class FakeDragActivity : FragmentActivity() {
    private lateinit var viewPager: ViewPager2
    private var landscape = false
    private var lastValue: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fakedrag)
        landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = CardViewAdapter()
        viewPager.isUserInputEnabled = false
        UserInputController(viewPager, findViewById(R.id.disable_user_input_checkbox)).setUp()
        OrientationController(viewPager, findViewById(R.id.orientation_spinner)).setUp()

        findViewById<View>(R.id.touchpad).setOnTouchListener { _, event ->
            handleOnTouchEvent(event)
        }
    }

    private fun getValue(event: MotionEvent): Float {
        return if(landscape) event.y else event.x
    }

    private fun handleOnTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastValue = getValue(event)
                viewPager.beginFakeDrag()
            }

            MotionEvent.ACTION_MOVE -> {
                val value = getValue(event)
                val delta = value - lastValue
                viewPager.fakeDragBy(delta)
                lastValue = value
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                viewPager.endFakeDrag()
            }
        }
        return true
    }
}