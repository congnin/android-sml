package vn.kingbee.widget.base.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import vn.kingbee.widget.R
import vn.kingbee.widget.toolbarview.ToolbarButtonType
import vn.kingbee.widget.toolbarview.ToolbarView

abstract class KioskActionBarActivity : KioskBaseActivity() {
    lateinit var toolbarView: ToolbarView
    lateinit var mToolbar: Toolbar

    override val contentResId: Int
        get() = R.layout.activity_action_bar_2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()
    }

    protected fun setUpToolbar() {
        mToolbar = findViewById(R.id.view_actionbar)
        setSupportActionBar(mToolbar)
        toolbarView = ToolbarView(this, mToolbar, object : ToolbarView.ToolbarButtonClickedListener {
            override fun onToolbarButtonClicked(type: ToolbarButtonType?) {
                onMastheadAction(type)
            }
        })
    }

    protected fun onMastheadAction(toolbarButtonType: ToolbarButtonType?) {
        when (toolbarButtonType) {
            ToolbarButtonType.AVATAR,
            ToolbarButtonType.HELP,
                //Todo handleClickHelp()
            ToolbarButtonType.CONTACT,
                //Todo handleClickContact()
            ToolbarButtonType.EXIT ->
                if (isUserLoggedIn()) {
                    resetActionButtonState()
                }
            else -> return
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        resetActionButtonState()
    }

    fun resetActionButtonState() {
        toolbarView.resetActionButtonState()
    }
}