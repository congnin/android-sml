package vn.kingbee.widget.utils

import android.content.Context
import android.content.ContextWrapper
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import vn.kingbee.widget.R

class FontHelper {
    companion object {
        fun initializeFontConfig() {
            CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath).build())
        }

        fun attachBaseContext(context: Context): ContextWrapper {
            return CalligraphyContextWrapper.wrap(context)
        }
    }
}
