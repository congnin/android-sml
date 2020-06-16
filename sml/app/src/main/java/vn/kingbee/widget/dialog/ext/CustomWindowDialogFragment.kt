package vn.kingbee.widget.dialog.ext

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import vn.kingbee.utils.UIUtils
import vn.kingbee.widget.R

abstract class CustomWindowDialogFragment : DialogFragment() {

    protected val marginTop: Int
        get() = 0

    protected open val animationStyle: Int
        get() = R.style.AccountRightDialogAnim

    protected abstract val layoutId: Int

    protected abstract val windowHeight: Int

    protected abstract val windowWidth: Int

    protected abstract val gravity: Int

    protected fun getScreenSize(): IntArray {
        return UIUtils.getScreenSize(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialog = InterceptTouchDialog(activity!!, theme)
        customDialogStyle(dialog)
        return inflater.inflate(layoutId, null)
    }

    protected fun customDialogStyle(dialog: Dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        isCancelable = false
        setWindowParams(dialog.window)
    }

    protected open fun setWindowParams(window: Window?) {
        if (window != null) {
            window.setGravity(gravity)
            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val animStyle = animationStyle
            if (animStyle != ANIMATION_NO_CHANGE) {
                window.setWindowAnimations(animStyle)
            }
            window.attributes.y = marginTop
        }
    }

    override fun onStart() {
        super.onStart()
        // Default dialog layout param will wrap_content
        // So just reset it when fragment start to show when onStart()
        val window = dialog?.window
        if (window != null) {
            window.setLayout(windowWidth, windowHeight)
        }
    }

    companion object {

        const val ANIMATION_NO_CHANGE = 0
    }
}