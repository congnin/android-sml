package vn.kingbee.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

@SuppressLint("WrongConstant")
object UIUtils {
    private const val TABLET_97_WIDTH = 1024

    fun linearExpand(targetView: View, duration: Int) {
        targetView.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val targetHeight = targetView.measuredHeight

        targetView.layoutParams.height = 1
        targetView.visibility = View.VISIBLE
        val expandAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                targetView.layoutParams.height = if (interpolatedTime == 1f)
                    LinearLayout.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                targetView.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        expandAnimation.duration = duration.toLong()
        targetView.startAnimation(expandAnimation)
    }

    fun linearCollapse(targetView: View, duration: Int) {
        val initialHeight = targetView.measuredHeight

        val collapseAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    targetView.visibility = View.GONE
                } else {
                    targetView.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    targetView.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        collapseAnimation.duration = duration.toLong()
        targetView.startAnimation(collapseAnimation)
    }

    fun isLegacyDevice(windowManager: WindowManager): Boolean {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels == TABLET_97_WIDTH
    }


    fun showKeyboard(context: Context, v: View?) {
        if (v != null) {
            val imm = context.getSystemService("input_method") as InputMethodManager
            imm.showSoftInput(v, 0)
        }
    }

    fun hideKeyboard(context: Context, v: View?) {
        if (v != null) {
            val imm = context.getSystemService("input_method") as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun hideKeyboard(activity: Activity) {
        try {
            (activity.getSystemService("input_method") as InputMethodManager).hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        } catch (var2: Exception) {
            var2.printStackTrace()
        }

    }

    fun isVisibleFragment(f: Fragment): Boolean {
        return if (f.parentFragment != null && f.parentFragment!!.isVisible && f.isVisible) {
            true
        } else if (f.parentFragment == null && f.isVisible) {
            true
        } else {
            f.userVisibleHint
        }
    }

    fun isKeyboardShown(rootView: View): Boolean {
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm = rootView.resources.displayMetrics
        val heightDiff = rootView.bottom - r.bottom
        return heightDiff.toFloat() > 128.0f * dm.density
    }

    fun setDefaultFont(context: Context, staticTypefaceFieldName: String, fontAssetName: String) {
        val regular = Typeface.createFromAsset(context.assets, fontAssetName)

        try {
            val staticField = Typeface::class.java.getDeclaredField(staticTypefaceFieldName)
            staticField.isAccessible = true
            staticField.set(null as Any?, regular)
        } catch (var5: NoSuchFieldException) {
            var5.printStackTrace()
        } catch (var6: IllegalAccessException) {
            var6.printStackTrace()
        }

    }

    private fun hasNavBar(@NonNull context: Context): Boolean {
        val resources = context.resources
        if (Build.FINGERPRINT.startsWith("generic")) {
            return true
        } else {
            val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
            return id > 0 && resources.getBoolean(id)
        }
    }

    fun getNavigationBarHeight(@NonNull context: Context): Int {
        if (!hasNavBar(context)) {
            return 0
        } else {
            val resources = context.resources
            val orientation = resources.configuration.orientation
            val isSmartphone = resources.configuration.smallestScreenWidthDp < 600
            if (isSmartphone && 2 == orientation) {
                return 0
            } else {
                val id = resources.getIdentifier(if (orientation == 1) "navigation_bar_height" else "navigation_bar_height_landscape", "dimen", "android")
                return if (id > 0) resources.getDimensionPixelSize(id) else 0
            }
        }
    }

    fun getStatusBarHeight(@NonNull context: Context): Int {
        val resources = context.resources
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }

        return result
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun getScreenSize(@NonNull context: Context): IntArray {
        return intArrayOf(context.resources.displayMetrics.widthPixels, context.resources.displayMetrics.heightPixels)
    }

    fun getLocationOnScreen(v: View): IntArray {
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        return location
    }

    fun getLocationInWindow(v: View): IntArray {
        val location = IntArray(2)
        v.getLocationInWindow(location)
        return location
    }

    fun getLocalVisibleRect(v: View): IntArray {
        val location = IntArray(4)
        val rect = Rect()
        v.getLocalVisibleRect(rect)
        location[0] = rect.left
        location[1] = rect.top
        location[2] = rect.right
        location[3] = rect.bottom
        return location
    }

    fun getDrawable(context: Context, resId: Int): Drawable {
        return ResourcesCompat.getDrawable(context.resources, resId, null as Resources.Theme?)!!
    }

    fun getDrawable(context: Context, resId: Int, theme: Resources.Theme): Drawable {
        return ResourcesCompat.getDrawable(context.resources, resId, theme)!!
    }

    fun getColor(context: Context, resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    fun setBackground(v: View, @NonNull backgroundResId: Int?) {
        if (SystemUtil.hasJellyBean()) {
            v.background = getDrawable(v.context, backgroundResId!!)
        } else {
            v.setBackgroundDrawable(getDrawable(v.context, backgroundResId!!))
        }

    }

    fun isTouchInsideEditText(event: MotionEvent, editText: EditText): Boolean {
        val xPoint = event.rawX
        val yPoint = event.rawY
        val l = IntArray(2)
        editText.getLocationOnScreen(l)
        val x = l[0]
        val y = l[1]
        val w = editText.width
        val h = editText.height
        return xPoint >= x.toFloat() && xPoint <= (x + w).toFloat() && yPoint >= y.toFloat() && yPoint <= (y + h).toFloat()
    }

    fun getPixelFromDP(context: Context, dipValue: Int): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(1, dipValue.toFloat(), metrics)
    }

    fun unbindDrawables(ctx: Context, view: View) {
        if (view.background != null) {
            if (view.id != -1) {
                println(ctx.resources.getResourceEntryName(view.id))
            }

            view.background.callback = null
            view.setBackgroundResource(0)
        }

        if (view is ImageView) {
            view.setImageBitmap(null as Bitmap?)
        } else if (view is ViewGroup) {

            for (i in 0 until view.childCount) {
                unbindDrawables(ctx, view.getChildAt(i))
            }

            if (view !is AdapterView<*>) {
                view.removeAllViews()
            }
        }

    }

    fun isFragmentValid(f: Fragment): Boolean {
        return f.activity != null && !f.isDetached
    }
}