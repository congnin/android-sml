package vn.kingbee.widget.toolbarview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import vn.kingbee.widget.R
import vn.kingbee.widget.imageview.circle.AvatarImageView

class ToolbarView : View.OnClickListener {

    private var toolbarButtonClickedListener: ToolbarView.ToolbarButtonClickedListener
    private var context: Context
    private var containerAvatar: View
    private var toolbar: Toolbar
    private var btnExit: ActionButton
    private var btnHelp: ActionButton
    private var btnContact: ActionButton
    private var imgAvatar: AvatarImageView
    private var imageLogo: ImageView
    private var tvProfileName: TextView
    private var latestSelectView: View? = null
    private var containerToolbarView: RelativeInterceptTouchEvent
    private var isPreventCallbackForSameButton = true

    constructor(context: Context, toolbar: Toolbar, toolbarButtonClickedListener: ToolbarButtonClickedListener) {
        this.context = context
        this.toolbar = toolbar
        this.toolbarButtonClickedListener = toolbarButtonClickedListener
        this.toolbar.setContentInsetsAbsolute(0, 0)
        this.toolbar.removeAllViews()
        val view = LayoutInflater.from(context).inflate(R.layout.mashead_view_standalone_toolbar, toolbar, true)
        this.containerToolbarView = view.findViewById(R.id.containerToolbarView)
        this.tvProfileName = view.findViewById(R.id.tvProfileName)
        this.imageLogo = view.findViewById(R.id.view_toolbar_logo)
        this.imgAvatar = view.findViewById(R.id.imgAvatar)
        this.btnExit = view.findViewById(R.id.view_toolbar_btn_exit)
        this.btnHelp = view.findViewById(R.id.view_toolbar_btn_help)
        this.btnContact = view.findViewById(R.id.view_toolbar_btn_contact)
        this.containerAvatar = view.findViewById(R.id.containerAvatar)
        this.containerAvatar.setOnClickListener(this)
        this.btnExit.setOnClickListener(this)
        this.btnHelp.setOnClickListener(this)
        this.btnContact.setOnClickListener(this)
        this.imageLogo.setOnClickListener(this)
        this.tvProfileName.isActivated = true
    }

    fun toggleInterceptTouchEventToolbar(enable: Boolean) {
        this.containerToolbarView.setInterceptTouchEvent(enable)
    }

    fun getContainerAvatar(): View {
        return this.containerAvatar
    }

    fun getButtonHelp(): ActionButton {
        return this.btnHelp
    }

    fun getButtonContact(): ActionButton {
        return this.btnContact
    }

    fun getExitButton(): ActionButton {
        return this.btnExit
    }

    fun getImgAvatar(): AvatarImageView {
        return this.imgAvatar
    }

    fun setHelpButtonVisibility(isShown: Boolean) {
        this.btnHelp.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    fun setExitButtonVisibility(isShown: Boolean) {
        this.btnExit.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    fun setLogoVisibility(isShown: Boolean) {
        this.imageLogo.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    fun setToolbarVisibility(isShown: Boolean) {
        this.toolbar.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    fun setUserProfileName(name: String) {
        this.tvProfileName.text = name
        this.imgAvatar.text = name
    }

    fun setExitButtonText(exit: String) {
        this.btnExit.setText(exit)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.view_toolbar_logo) {
            this.toolbarButtonClickedListener.onToolbarButtonClicked(ToolbarButtonType.LOGO)
        } else if (v?.id == R.id.view_toolbar_btn_exit && this.btnExit.visibility == View.VISIBLE) {
            this.toolbarButtonClickedListener.onToolbarButtonClicked(ToolbarButtonType.EXIT)
        } else if (v != null) {
            this.onActionButtonClicked(v)
        }
    }

    private fun onActionButtonClicked(viewSelected: View) {
        if (!this.isPreventCallbackForSameButton || viewSelected != this.latestSelectView) {
            if (this.latestSelectView != null && this.latestSelectView!!.isSelected) {
                this.latestSelectView?.isSelected = false
            }

            this.latestSelectView = viewSelected
            this.latestSelectView?.isSelected = true
            this.toolbarButtonClickedListener.onToolbarButtonClicked(this.getButtonTypeFromId(viewSelected.id))
        }
    }

    protected fun getButtonTypeFromId(viewId: Int): ToolbarButtonType? {
        return if (viewId == R.id.view_toolbar_btn_help) {
            ToolbarButtonType.HELP
        } else if (viewId == R.id.view_toolbar_btn_contact) {
            ToolbarButtonType.CONTACT
        } else if (viewId == R.id.view_toolbar_btn_notifications) {
            ToolbarButtonType.NOTIFICATION
        } else {
            if (viewId == R.id.containerAvatar) ToolbarButtonType.AVATAR else null
        }
    }

    fun setToolbarMode(mode: ToolbarMode) {
        when (mode) {
            ToolbarMode.LOGGED_IN -> {
                this.imageLogo.visibility = View.GONE
                this.containerAvatar.visibility = View.VISIBLE
                this.btnContact.visibility = View.VISIBLE
                this.setUpButtonYellow(true)
            }
            ToolbarMode.LAUNCHER -> {
                this.imageLogo.visibility = View.VISIBLE
                this.containerAvatar.visibility = View.GONE
                this.btnContact.visibility = View.VISIBLE
                this.setUpButtonYellow(false)
                this.setExitButtonVisibility(false)
            }
            ToolbarMode.LOGGED_OUT -> {
                this.imageLogo.visibility = View.VISIBLE
                this.containerAvatar.visibility = View.GONE
                this.btnContact.visibility = View.VISIBLE
                this.setUpButtonYellow(false)
            }
            else -> {
                this.imageLogo.visibility = View.VISIBLE
                this.containerAvatar.visibility = View.GONE
                this.btnContact.visibility = View.VISIBLE
                this.setUpButtonYellow(false)
            }
        }
    }

    private fun setUpButtonYellow(isLoggedIn: Boolean) {
        val textDisplay: String
        val minWidth: Float
        if (isLoggedIn) {
            minWidth = this.context.resources.getDimension(R.dimen.mas_head_action_button_min_width)
            textDisplay = this.context.getString(R.string.mashead_txt_logout)
        } else {
            minWidth = this.context.resources.getDimension(R.dimen.mas_head_toolbar_button_exit_width)
            textDisplay = this.context.getString(R.string.mashead_txt_exit)
        }

        this.btnExit.setDrawablePadding(this.context.resources.getDimensionPixelSize(R.dimen.default_margin_medium))
        this.btnExit.setText(textDisplay)
        this.btnExit.minimumWidth = minWidth.toInt()
    }

    protected fun visibleViewForLoggedInMode(visibility: Int) {
        this.containerAvatar.visibility = visibility
        this.btnContact.visibility = visibility
    }

    fun resetActionButtonState() {
        if (this.latestSelectView != null && this.latestSelectView!!.isSelected()) {
            this.latestSelectView?.setSelected(false)
        }

        this.latestSelectView = null
    }

    protected fun setPreventCallbackForSameButton(isPreventCallbackForSameButton: Boolean) {
        this.isPreventCallbackForSameButton = isPreventCallbackForSameButton
    }

    interface ToolbarButtonClickedListener {
        fun onToolbarButtonClicked(type: ToolbarButtonType?)
    }
}