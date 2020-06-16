package vn.kingbee.widget.base.activity

import vn.kingbee.widget.base.fragment.BaseFragmentImpl

interface BaseActivity {

    val contentResId: Int

    val containerFragment: BaseFragmentImpl?

    fun setupActivityComponent()

    fun onForeground()

    fun onBackground()
}