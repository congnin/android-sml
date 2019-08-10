package vn.kingbee.feature.splash.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vn.kingbee.application.MyApp
import vn.kingbee.feature.base.fragment.BaseKioskFragmentImpl
import vn.kingbee.feature.splash.presenter.SplashPresenter
import vn.kingbee.feature.splash.presenter.SplashPresenterImpl
import vn.kingbee.widget.base.fragment.BaseFragmentImpl
import vn.kingbee.widget.base.presenter.BasePresenter
import javax.inject.Inject

class SplashFragment : BaseKioskFragmentImpl(), SplashView {

    @Inject
    lateinit var presenter: SplashPresenter<SplashView>

    override fun setupFragmentComponent() {
        MyApp.getInstance().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)

        presenter.loadKioskConfiguration()
    }

    override fun goToNextScreen() {
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