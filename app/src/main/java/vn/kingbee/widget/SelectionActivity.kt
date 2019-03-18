package vn.kingbee.widget

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import vn.kingbee.widget.edittext.FormatEditTextActivity
import vn.kingbee.widget.progress.CircleProcessBarActivity

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
    }

    fun startCircleProcessBarActivity() {
        startActivity(Intent(this@SelectionActivity, CircleProcessBarActivity::class.java))
    }

    fun startFormatEditTextActivity() {
        startActivity(Intent(this@SelectionActivity, FormatEditTextActivity::class.java))
    }
}
