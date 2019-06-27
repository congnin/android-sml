package vn.kingbee.widget.imageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import vn.kingbee.widget.R
import vn.kingbee.widget.button.CircleButton
import vn.kingbee.widget.indicator.HorizontalIndicator

class SwitchIconDemoActivity : AppCompatActivity() {
    lateinit var switchIcon1: SwitchIconView
    lateinit var switchIcon2: SwitchIconView
    lateinit var switchIcon3: SwitchIconView
    lateinit var button1: View

    lateinit var button2: View
    lateinit var button3: View

    lateinit var btnBack: CircleButton
    lateinit var btnNext: CircleButton
    lateinit var stepIndicator: HorizontalIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_icon_view_demo)

        switchIcon1 = findViewById(R.id.switchIconView1)
        switchIcon2 = findViewById(R.id.switchIconView2)
        switchIcon3 = findViewById(R.id.switchIconView3)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)

        btnBack = findViewById(R.id.fragment_horizontal_pager_back_button)
        btnNext = findViewById(R.id.fragment_horizontal_pager_next_button)
        stepIndicator = findViewById(R.id.fragment_horizontal_pager_indicator)

        button1.setOnClickListener { switchIcon1.switchState() }
        button2.setOnClickListener { switchIcon2.switchState() }
        button3.setOnClickListener { switchIcon3.switchState() }

        val progressArray = resources.getStringArray(R.array.arr_progress_tracker)
        var currentIndex = 0
        val maxIndex = progressArray.size
        stepIndicator.setCurrentItem(currentIndex)
        btnBack.setOnClickListener {
            if(currentIndex > 0) {
                currentIndex--
                stepIndicator.setCurrentItem(currentIndex)
            }
        }

        btnNext.setOnClickListener {
            if(currentIndex < maxIndex - 1){
                currentIndex++
                stepIndicator.setCurrentItem(currentIndex)
            }
        }
    }

    fun setE17StepIndicator(e17StepIndicatorState: E17StepIndicatorState) {
        when (e17StepIndicatorState) {
            E17StepIndicatorState.CELLPHONE_NUMBER,
            E17StepIndicatorState.IDENTITY, E17StepIndicatorState.SECURITY,
            E17StepIndicatorState.FINGERPRINTS, E17StepIndicatorState.VERIFY,
            E17StepIndicatorState.ADDRESS, E17StepIndicatorState.CARD -> {
                stepIndicator.visibility = View.VISIBLE
                stepIndicator.setCurrentItem(e17StepIndicatorState.id)
            }
        }
    }
}