package vn.kingbee.feature.splash.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import vn.kingbee.application.MyApp
import vn.kingbee.feature.base.fragment.BaseKioskFragmentImpl
import vn.kingbee.feature.splash.SplashActivity
import vn.kingbee.feature.splash.presenter.SplashPresenter
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.big.timeout.TimeOutProcessDialog
import java.util.*
import javax.inject.Inject

class SplashFragment : BaseKioskFragmentImpl(), SplashView, SplashActivity.UserInteractionListener {

    @Inject
    lateinit var presenter: SplashPresenter

    protected var timeOutProcessDialog: TimeOutProcessDialog? = null

    override fun setupFragmentComponent() {
        MyApp.getInstance().mAppComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SplashActivity) {
            context.setUserInteractionListener(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)

        setupTimeoutDialog()
        setupTimeoutService()

        presenter.loadKioskConfiguration()
    }

    override fun goToNextScreen() {
    }

    override fun onResume() {
        super.onResume()
        timeoutProcessingService.start()
    }

    override fun onPause() {
        super.onPause()
        timeoutProcessingService.start()
    }

    @OnClick(R.id.layout_container)
    fun onScreenClicked() {
        presenter.onScreenClicked()
    }

    override fun onUserInteraction() {
        timeoutProcessingService.start()
    }

    private fun setupTimeoutService() {
        timeoutProcessingService.registerAction(object : TimerTask() {
            override fun run() {
                if (this@SplashFragment.activity!!.hasWindowFocus()) {
                    timeOutProcessDialog?.show()
                }
            }
        })
    }

    private fun setupTimeoutDialog() {
        timeOutProcessDialog = TimeOutProcessDialog.Builder(activity!!)
            .positiveEvent(object : TimeOutProcessDialog.DialogEvent {
                override fun onClick(dialog: Dialog) {
                    this@SplashFragment.returnAppAndResetTimeout(dialog)
                }
            })
            .build()

    }

    private fun returnAppAndResetTimeout(dialog: Dialog) {
        dialog.dismiss()
        timeoutProcessingService.start()
    }

    companion object {
        fun newInstance(): SplashFragment {
            val fragment = SplashFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}