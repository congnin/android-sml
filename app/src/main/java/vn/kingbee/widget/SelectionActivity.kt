package vn.kingbee.widget

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import vn.kingbee.rxjava.RxJava2SelectionActivity
import vn.kingbee.widget.animation.demo.ShimmerActivityDemo
import vn.kingbee.widget.button.demo.ButtonDemoActivity
import vn.kingbee.widget.calendar.demo.DatePickerDemoActivity
import vn.kingbee.widget.camera.demo.CameraDemo1Activity
import vn.kingbee.widget.edittext.FormatEditTextActivity
import vn.kingbee.widget.imageview.SwitchIconDemoActivity
import vn.kingbee.widget.keyboard.NumpadActivity
import vn.kingbee.widget.navigationview.ArcNavigationViewDemoActivity
import vn.kingbee.widget.pin.PinActivity
import vn.kingbee.widget.progress.CircleProcessBarActivity
import vn.kingbee.widget.recyclerview.HelpVideoDemo
import vn.kingbee.widget.signature.SignatureDemoActivity
import vn.kingbee.widget.spinner.demo.SpinnerDemoActivity
import vn.kingbee.widget.toasty.ToastyDemoActivity

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

    fun startDatePickerDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, DatePickerDemoActivity::class.java))
    }

    fun startButtonDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, ButtonDemoActivity::class.java))
    }

    fun startSpinnerDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, SpinnerDemoActivity::class.java))
    }

    fun startRecyclerViewDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, HelpVideoDemo::class.java))
    }

    fun startSignatureDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, SignatureDemoActivity::class.java))
    }

    fun startToastyDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, ToastyDemoActivity::class.java))
    }

    fun startArcNavigationViewDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, ArcNavigationViewDemoActivity::class.java))
    }
    fun startSwitchIconViewDemoActivity(v: View) {
        startActivity(Intent(this@SelectionActivity, SwitchIconDemoActivity::class.java))
    }
}
