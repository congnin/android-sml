package vn.kingbee.kiosk

import vn.kingbee.widget.base.activity.KioskActionBarActivity
import vn.kingbee.widget.base.fragment.BaseFragmentImpl
import vn.kingbee.widget.toolbarview.ToolbarMode

class LoginPagerActivity : KioskActionBarActivity() {

    protected fun initToolbarMode() {
        toolbarView.setToolbarMode(ToolbarMode.LOGGED_OUT)
    }

    override fun getFragmentToHost(): BaseFragmentImpl? {
        return null
    }
}