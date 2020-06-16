package vn.kingbee.widget.base.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import vn.kingbee.utils.FontHelper
import vn.kingbee.utils.SystemUtil
import vn.kingbee.utils.UIUtils
import vn.kingbee.utils.foreground.Foreground
import vn.kingbee.widget.R
import vn.kingbee.widget.base.fragment.BaseFragmentImpl

abstract class BaseActivityImpl : AppCompatActivity(), BaseActivity {
    protected var unbinder: Unbinder? = null

    private val backgroundListener = object : Foreground.Listener {
        override fun onBecameForeground() {
            this@BaseActivityImpl.onForeground()
        }

        override fun onBecameBackground() {
            this@BaseActivityImpl.onBackground()
        }
    }

    abstract fun getFragmentToHost(): BaseFragmentImpl?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivityComponent()
        setContentView(contentResId)
        Foreground.get(this).addListener(backgroundListener)
        if (savedInstanceState == null && getFragmentToHost() != null) {
            hostFragment(getFragmentToHost())
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        this.unbinder = ButterKnife.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
        Foreground.get(this).removeListener(backgroundListener)
    }

    override fun setupActivityComponent() {}

    override val contentResId: Int
        get() = R.layout.activity_base

    override fun onForeground() {}

    override fun onBackground() {}

    override val containerFragment: BaseFragmentImpl?
        get() = getActiveFragment()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(FontHelper.attachBaseContext(newBase!!))
    }

    override fun onBackPressed() {
        val activeFragment = getActiveFragment()
        if(activeFragment != null && !activeFragment.onBackPressed(isBackButtonVisibility())) {
            super.onBackPressed()
            SystemUtil.overridePendingTransactionOnPressed(this)
        }
    }

    protected fun hostFragment(fragment: BaseFragmentImpl?) {
        if (fragment != null && supportFragmentManager.findFragmentByTag(fragment.getTagName()) == null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, fragment.getInstance(), fragment.getTagFromClassName())
            ft.commit()
        }
    }

    protected fun isBackButtonVisibility() = false

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN && currentFocus != null) {
            val view = currentFocus
            val beforeDispatch = UIUtils.isKeyboardShown(view!!.rootView)
            val ret = super.dispatchTouchEvent(ev)
            if (view is EditText) {
                val isAfterDispatch = UIUtils.isKeyboardShown(view.rootView)
                val isTouchInsideEditTextField = UIUtils.isTouchInsideEditText(ev, view)
                if (ev.action == MotionEvent.ACTION_DOWN && isAfterDispatch && beforeDispatch && !isTouchInsideEditTextField) {
                    UIUtils.hideKeyboard(this, this.window.currentFocus)
                }
            }

            return ret
        }
        return super.dispatchTouchEvent(ev)
    }

    protected fun getActiveFragment(): BaseFragmentImpl? {
        var f = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (f == null) {
            val fs = supportFragmentManager.fragments
            if (fs.isNotEmpty()) {
                val lastFragment = fs[fs.size - 1]
                if (lastFragment is BaseFragmentImpl && lastFragment.isVisible) {
                    f = lastFragment
                }
            }
        }

        return f as BaseFragmentImpl
    }
}