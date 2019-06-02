package vn.kingbee.widget.button.counterfab

import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import vn.kingbee.widget.R

class CounterFabExample : AppCompatActivity() {
    private var mCounterMode: RadioGroup? = null
    private var mCounterFab: CounterFab? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_fab)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mCounterMode = findViewById(R.id.counter_mode)
        mCounterMode!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.increase_button) {
                mCounterFab!!.setImageResource(R.drawable.ic_menu_manage)
            } else {
                mCounterFab!!.setImageResource(R.drawable.ic_clear_white_24dp)
            }
        }

        mCounterFab = findViewById(R.id.fab)
        mCounterFab!!.setOnClickListener {
            if (mCounterMode!!.checkedRadioButtonId == R.id.increase_button) {
                mCounterFab!!.increase()
            } else {
                mCounterFab!!.decrease()
            }
        }
    }
}