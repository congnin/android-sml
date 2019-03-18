package vn.kingbee.widget

import android.content.Context
import android.support.v7.app.AppCompatActivity
import vn.kingbee.widget.utils.FontHelper

abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(FontHelper.attachBaseContext(newBase!!))
    }
}