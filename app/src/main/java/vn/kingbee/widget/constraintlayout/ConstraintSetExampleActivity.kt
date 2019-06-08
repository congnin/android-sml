package vn.kingbee.widget.constraintlayout

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.appcompat.app.AppCompatActivity
import vn.kingbee.widget.R


class ConstraintSetExampleActivity : AppCompatActivity() {

    /**
     * Whether to show an enlarged image
     */
    private var mShowBigImage = false
    /**
     * The ConstraintLayout that any changes are applied to.
     */
    private var mRootLayout: ConstraintLayout? = null
    /**
     * The ConstraintSet to use for the normal initial state
     */
    private val mConstraintSetNormal = ConstraintSet()
    /**
     * ConstraintSet to be applied on the normal ConstraintLayout to make the Image bigger.
     */
    private val mConstraintSetBig = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.constraintset_example_main)

        mRootLayout = findViewById(R.id.activity_constraintset_example)
        // Note that this can also be achieved by calling
        // `mConstraintSetNormal.load(this, R.layout.constraintset_example_main);`
        // Since we already have an inflated ConstraintLayout in `mRootLayout`, clone() is
        // faster and considered the best practice.
        mConstraintSetNormal.clone(mRootLayout!!)
        // Load the constraints from the layout where ImageView is enlarged.
        mConstraintSetBig.load(this, R.layout.constraintset_example_big)

        if (savedInstanceState != null) {
            val previous = savedInstanceState.getBoolean(SHOW_BIG_IMAGE)
            if (previous != mShowBigImage) {
                mShowBigImage = previous
                applyConfig()
            }
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SHOW_BIG_IMAGE, mShowBigImage)
    }

    // Method called when the ImageView within R.layout.constraintset_example_main
    // is clicked.
    fun toggleMode(v: View) {
        TransitionManager.beginDelayedTransition(mRootLayout)
        mShowBigImage = !mShowBigImage
        applyConfig()
    }

    private fun applyConfig() {
        if (mShowBigImage) {
            mConstraintSetBig.applyTo(mRootLayout!!)
        } else {
            mConstraintSetNormal.applyTo(mRootLayout!!)
        }
    }

    companion object {

        private val SHOW_BIG_IMAGE = "showBigImage"
    }
}