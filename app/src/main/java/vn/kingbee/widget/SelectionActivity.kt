package vn.kingbee.widget

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import vn.kingbee.rxjava.RxJava2SelectionActivity
import vn.kingbee.widget.animation.demo.ShimmerActivityDemo
import vn.kingbee.widget.camera.demo.CameraDemo1Activity
import vn.kingbee.widget.edittext.FormatEditTextActivity
import vn.kingbee.widget.keyboard.NumpadActivity
import vn.kingbee.widget.pin.PinActivity
import vn.kingbee.widget.progress.CircleProcessBarActivity

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
    }

    fun startRxJavaSelectionActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, RxJava2SelectionActivity::class.java))
    }

    fun startCircleProcessBarActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, CircleProcessBarActivity::class.java))
    }

    fun startFormatEditTextActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, FormatEditTextActivity::class.java))
    }

    fun startPinActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, PinActivity::class.java))
    }

    fun startNumpadActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, NumpadActivity::class.java))
    }

    fun startCameraDemo1Activity(v: View) {
        startActivity(Intent(this@SelectionActivity, CameraDemo1Activity::class.java))
    }

    fun startShimmerDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, ShimmerActivityDemo::class.java))
    }
}
