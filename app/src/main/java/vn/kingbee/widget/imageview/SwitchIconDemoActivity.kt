package vn.kingbee.widget.imageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import vn.kingbee.widget.R

class SwitchIconDemoActivity : AppCompatActivity() {
    lateinit var switchIcon1: SwitchIconView
    lateinit var switchIcon2: SwitchIconView
    lateinit var switchIcon3: SwitchIconView
    lateinit var button1: View

    lateinit var button2: View
    lateinit var button3: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_icon_view_demo)

        switchIcon1 = findViewById(R.id.switchIconView1)
        switchIcon2 = findViewById(R.id.switchIconView2)
        switchIcon3 = findViewById(R.id.switchIconView3)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)

        button1.setOnClickListener { switchIcon1.switchState() }
        button2.setOnClickListener { switchIcon2.switchState() }
        button3.setOnClickListener { switchIcon3.switchState() }
    }
}