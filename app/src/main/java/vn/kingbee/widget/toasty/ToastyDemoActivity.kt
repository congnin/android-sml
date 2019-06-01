package vn.kingbee.widget.toasty

import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.graphics.Typeface.BOLD_ITALIC
import android.text.style.StyleSpan
import android.text.SpannableStringBuilder
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import vn.kingbee.widget.R


class ToastyDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_toasty)

        findViewById<View>(R.id.button_error_toast).setOnClickListener {
            Toasty.error(this@ToastyDemoActivity, R.string.error_message,
                Toasty.LENGTH_SHORT, true).show()
        }
        findViewById<View>(R.id.button_success_toast).setOnClickListener {
            Toasty.success(this@ToastyDemoActivity, R.string.success_message,
                Toasty.LENGTH_SHORT, true).show()
        }
        findViewById<View>(R.id.button_info_toast).setOnClickListener {
            Toasty.info(this@ToastyDemoActivity, R.string.info_message,
                Toasty.LENGTH_SHORT, true).show()
        }
        findViewById<View>(R.id.button_warning_toast).setOnClickListener {
            Toasty.warning(this@ToastyDemoActivity, R.string.warning_message,
                Toasty.LENGTH_SHORT, true).show()
        }
        findViewById<View>(R.id.button_normal_toast_wo_icon).setOnClickListener {
            Toasty.normal(this@ToastyDemoActivity, R.string.normal_message_without_icon).show()
        }
        findViewById<View>(R.id.button_normal_toast_w_icon).setOnClickListener {
            val icon = resources.getDrawable(R.mipmap.ic_pets_white_48dp)
            Toasty.normal(this@ToastyDemoActivity, R.string.normal_message_with_icon, icon).show()
        }
        findViewById<View>(R.id.button_info_toast_with_formatting).setOnClickListener {
            Toasty.info(this@ToastyDemoActivity, getFormattedMessage()).show()
        }
        findViewById<View>(R.id.button_custom_config).setOnClickListener {
            Toasty.Config.instance
                .setToastTypeface(Typeface.createFromAsset(assets, "fonts/PCap Terminal.otf"))
                .allowQueue(false)
                .apply()
            Toasty.custom(this@ToastyDemoActivity, R.string.custom_message, resources.getDrawable(R.mipmap.laptop512),
                android.R.color.black, android.R.color.holo_green_light, Toasty.LENGTH_SHORT, true, true).show()
            Toasty.Config.reset() // Use this if you want to use the configuration above only once
        }
    }

    private fun getFormattedMessage(): CharSequence {
        val prefix = "Formatted "
        val highlight = "bold italic"
        val suffix = " text"
        val ssb = SpannableStringBuilder(prefix).append(highlight).append(suffix)
        val prefixLen = prefix.length
        ssb.setSpan(StyleSpan(BOLD_ITALIC),
            prefixLen, prefixLen + highlight.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ssb
    }

}