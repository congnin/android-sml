package vn.kingbee.widget.dialog.ext

import android.animation.Animator
import android.os.Bundle
import android.view.*
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import vn.kingbee.utils.AnimationUtil
import vn.kingbee.widget.BuildConfig
import vn.kingbee.widget.R
import vn.kingbee.widget.tabhost.TabPager
import vn.kingbee.widget.tabhost.TabPagerAdapter
import timber.log.Timber
import android.animation.AnimatorListenerAdapter


abstract class BaseMenuContentDialogFragment : CustomWindowDialogFragment() {
    @BindView(R.id.tvCallCenter)
    lateinit var tvCallCenter: TextView
    @BindView(R.id.tabPager)
    lateinit var tabPager: TabPager
    @BindView(R.id.tvVersion)
    lateinit var tvVersion: TextView

    lateinit var mUnbinder: Unbinder

    protected abstract fun getListFragment(): List<TabPagerAdapter.TabPagerItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        if (rootView != null) {
            mUnbinder = ButterKnife.bind(this, rootView)
            initUI()
            dialog?.setOnDismissListener { }
            rootView.animate().translationY(0F).setDuration(getDurationIn().toLong()).start()
        }
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }

    override val windowHeight: Int
        get() = getScreenSize()[1] - resources.getDimension(R.dimen.mas_head_toolbar_header_height).toInt()

    override val windowWidth: Int
        get() = ViewGroup.LayoutParams.MATCH_PARENT

    override fun setWindowParams(window: Window?) {
        super.setWindowParams(window)
        // Make the dialog possible to be outside touch
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override val animationStyle: Int
        get() = R.style.MenuContentDialogFragment

    override val layoutId: Int
        get() = R.layout.menu_base_dialog_fragment

    override val gravity: Int
        get() = Gravity.BOTTOM

    protected fun initUI() {
        updateVersion()
        updateCallCenterFromConstant()
        setUpTabPager()
    }

    private fun updateVersion() {
        tvVersion.text = BuildConfig.VERSION_NAME
    }

    private fun setUpTabPager() {
        tabPager.initTab(childFragmentManager, getListFragment())
    }

    protected fun updateCallCenterFromConstant() {
        tvCallCenter.text = "0860 999 119"
    }

    @OnClick(R.id.btnDone)
    fun onDoneButtonClicked() {
        dismissAnimation()
    }

    fun dismissAnimation() {
        if (view != null) {
            AnimationUtil.translateY(view!!,
                resources.getDimensionPixelSize(R.dimen.menu_base_dialog_translate_y),
                getDurationOut(),
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        dismiss()
                    }
                })
        } else {
            dismiss()
        }
    }

    private fun getDurationOut(): Int {
        return resources.getInteger(android.R.integer.config_mediumAnimTime)
    }

    private fun getDurationIn(): Int {
        return resources.getInteger(android.R.integer.config_longAnimTime)
    }

    fun isShowing(): Boolean {
        return dialog != null && dialog!!.isShowing
    }
}