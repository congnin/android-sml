package vn.kingbee.widget.toasty

import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.graphics.drawable.Drawable
import android.os.Build
import android.content.Context
import android.graphics.drawable.NinePatchDrawable
import android.graphics.PorterDuff
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.view.View
import vn.kingbee.widget.R


object ToastyUtils {
    fun tintIcon(drawable: Drawable, @ColorInt tintColor: Int): Drawable {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return drawable
    }

    fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable {
        val toastDrawable = getDrawable(context, R.mipmap.toast_frame) as NinePatchDrawable?
        return tintIcon(toastDrawable!!, tintColor)
    }

    fun setBackground(view: View, drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.background = drawable
        else
            view.setBackgroundDrawable(drawable)
    }

    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return AppCompatResources.getDrawable(context, id)
    }

    fun getColor(context: Context, @ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }
}