package vn.kingbee.widget.constraintlayout

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainConstraintActivity : AppCompatActivity() {
    private var mTag = "activity_constraint_example"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mTag)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setContentView(mTag)
    }

    fun show(v: View) {
        mTag = v.tag as String
        setContentView(mTag)
    }

    override fun onBackPressed() {
        if (mTag != "activity_constraint_example") {
            mTag = "activity_constraint_example"
            setContentView(mTag)
        } else {
            super.onBackPressed()
        }
    }

    fun showConstraintSetExample(view: View) {
        startActivity(Intent(this, ConstraintSetExampleActivity::class.java))
    }

    private fun setContentView(tag: String) {
        val id = resources.getIdentifier(tag, "layout", packageName)
        setContentView(id)
    }
}