package vn.kingbee.widget.navigationview

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import com.google.android.material.navigation.NavigationView
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.text.TextUtils
import android.widget.TextView
import android.view.Gravity
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.graphics.*
import androidx.annotation.RequiresApi
import android.view.ViewOutlineProvider
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.internal.ScrimInsetsFrameLayout
import vn.kingbee.utils.CommonUtils

@SuppressLint("ObsoleteSdkInt")
class ArcNavigationView : NavigationView {

    lateinit var settings: ArcViewSettings
    private var height: Int? = 0
    private var width: Int? = 0
    lateinit var clipPath: Path
    lateinit var arcPath: Path

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        settings = ArcViewSettings(context, attrs)
        settings.elevation = ViewCompat.getElevation(this)

        /**
         * If hardware acceleration is on (default from API 14), clipPath worked correctly
         * from API 18.
         *
         * So we will disable hardware Acceleration if API < 18
         *
         * https://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported
         * Section #Unsupported Drawing Operations
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        setBackgroundColor(Color.TRANSPARENT)
        setInsetsColor(Color.TRANSPARENT)
        THRESHOLD = Math.round(CommonUtils.dpToPx(getContext(), 15)) //some threshold for child views while remeasuring them
    }

    private fun setInsetsColor(color: Int) {
        try {
            val insetForegroundField = ScrimInsetsFrameLayout::class.java.getDeclaredField("mInsetForeground")
            insetForegroundField.isAccessible = true
            val colorDrawable = ColorDrawable(color)
            insetForegroundField.set(this, colorDrawable)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun createClipPath(): Path {
        val path = Path()
        arcPath = Path()

        val arcWidth = settings.arcWidth
        val layoutParams = layoutParams as androidx.drawerlayout.widget.DrawerLayout.LayoutParams
        if (settings.cropInside) {
            if (layoutParams.gravity == Gravity.START || layoutParams.gravity == Gravity.LEFT) {
                arcPath.moveTo(width!!.toFloat(), 0F)
                arcPath.quadTo(width!! - arcWidth, height!! / 2F,
                    width!!.toFloat(), height!!.toFloat())
                arcPath.close()

                path.moveTo(0F, 0F)
                path.lineTo(width!!.toFloat(), 0F)
                path.quadTo(width!! - arcWidth, height!! / 2F,
                    width!!.toFloat(), height!!.toFloat())
                path.lineTo(0F, height!!.toFloat())
                path.close()
            } else if (layoutParams.gravity == Gravity.END || layoutParams.gravity == Gravity.RIGHT) {
                arcPath.moveTo(0F, 0F)
                arcPath.quadTo(arcWidth, height!! / 2F,
                    0F, height!!.toFloat())
                arcPath.close()

                path.moveTo(width!!.toFloat(), 0F)
                path.lineTo(0F, 0F)
                path.quadTo(arcWidth, height!! / 2F,
                    0F, height!!.toFloat())
                path.lineTo(width!!.toFloat(), height!!.toFloat())
                path.close()
            }
        } else {
            if (layoutParams.gravity == Gravity.START || layoutParams.gravity == Gravity.LEFT) {
                arcPath.moveTo(width!! - arcWidth / 2, 0F)
                arcPath.quadTo(width!! + arcWidth / 2, height!! / 2F,
                    width!! - arcWidth / 2, height!!.toFloat())
                arcPath.close()

                path.moveTo(0F, 0F)
                path.lineTo(width!! - arcWidth / 2, 0F)
                path.quadTo(width!! + arcWidth / 2, height!! / 2F,
                    width!! - arcWidth / 2, height!!.toFloat())
                path.lineTo(0F, height!!.toFloat())
                path.close()
            } else if (layoutParams.gravity == Gravity.END || layoutParams.gravity == Gravity.RIGHT) {
                arcPath.moveTo(arcWidth / 2, 0F)
                arcPath.quadTo(-arcWidth / 2, height!! / 2F,
                    arcWidth / 2, height!!.toFloat())
                arcPath.close()

                path.moveTo(width!!.toFloat(), 0F)
                path.lineTo(arcWidth / 2, 0F)
                path.quadTo(-arcWidth / 2, height!! / 2F,
                    arcWidth / 2, height!!.toFloat())
                path.lineTo(width!!.toFloat(), height!!.toFloat())
                path.close()
            }
        }
        return path
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            calculateLayoutAndChildren()
        }
    }

    override fun measureChild(child: View, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        if (child is NavigationMenuView) {
            child.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth,
                MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                measuredHeight, MeasureSpec.EXACTLY))
        } else {
            super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec)
        }
    }

    private fun calculateLayoutAndChildren() {
        height = measuredHeight
        width = measuredWidth
        if (width!! > 0 && height!! > 0) {
            clipPath = createClipPath()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                ViewCompat.setElevation(this, settings.elevation)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    outlineProvider = object : ViewOutlineProvider() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        override fun getOutline(view: View, outline: Outline) {
                            if (clipPath.isConvex) {
                                outline.setConvexPath(clipPath)
                            }
                        }
                    }
                }
            }

            val count = childCount
            for (i in 0 until count) {
                val v = getChildAt(i)

                if (v is NavigationMenuView) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        v.setBackground(settings.backgroundDrawable)
                    } else {
                        v.setBackgroundDrawable(settings.backgroundDrawable)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ViewCompat.setElevation(v, settings.elevation)
                    }
                    //TODO: adjusting child views to new width in their rightmost/leftmost points related to path
                    //                    adjustChildViews((ViewGroup) v);
                }
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun adjustChildViews(container: ViewGroup) {
        val containerChildCount = container.childCount
        val pathMeasure = PathMeasure(arcPath, false)
        val layoutParams = layoutParams as androidx.drawerlayout.widget.DrawerLayout.LayoutParams

        for (i in 0 until containerChildCount) {
            val child = container.getChildAt(i)
            if (child is ViewGroup) {
                adjustChildViews(child)
            } else {
                val pathCenterPointForItem = floatArrayOf(0f, 0f)
                val location = locateView(child)
                val halfHeight = location.height() / 2

                pathMeasure.getPosTan((location.top + halfHeight).toFloat(), pathCenterPointForItem, null)
                if (layoutParams.gravity == Gravity.END || layoutParams.gravity == Gravity.RIGHT) {
                    val centerPathPoint = measuredWidth - Math.round(pathCenterPointForItem[0])
                    if (child.measuredWidth > centerPathPoint) {
                        child.measure(View.MeasureSpec.makeMeasureSpec(centerPathPoint - THRESHOLD,
                            MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                            child.measuredHeight, MeasureSpec.EXACTLY))
                        child.layout(centerPathPoint + THRESHOLD, child.top, child.right, child.bottom)
                    }
                } else if (layoutParams.gravity == Gravity.START || layoutParams.gravity == Gravity.LEFT) {
                    if (child.measuredWidth > pathCenterPointForItem[0]) {
                        child.measure(View.MeasureSpec.makeMeasureSpec(Math.round(pathCenterPointForItem[0]) - THRESHOLD,
                            MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                            child.measuredHeight, MeasureSpec.EXACTLY))
                        child.layout(child.left, child.top, Math.round(pathCenterPointForItem[0]) - THRESHOLD, child.bottom)
                    }
                }
                //set text ellipsize to end to prevent it from overlapping edge
                if (child is TextView) {
                    child.ellipsize = TextUtils.TruncateAt.END
                }
            }
        }
    }

    private fun locateView(view: View?): Rect {
        val loc = Rect()
        val location = IntArray(2)
        if (view == null) {
            return loc
        }
        view.getLocationOnScreen(location)

        loc.left = location[0]
        loc.top = location[1]
        loc.right = loc.left + view.width
        loc.bottom = loc.top + view.height
        return loc
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()

        canvas.clipPath(clipPath)
        super.dispatchDraw(canvas)

        canvas.restore()
    }

    companion object {
        private var THRESHOLD: Int = 0
    }
}