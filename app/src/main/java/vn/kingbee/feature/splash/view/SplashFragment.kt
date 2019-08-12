package vn.kingbee.feature.splash.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.feature.base.fragment.BaseKioskFragmentImpl
import vn.kingbee.feature.splash.presenter.SplashPresenter
import vn.kingbee.feature.splash.presenter.SplashPresenterImpl
import vn.kingbee.widget.R
import javax.inject.Inject

class SplashFragment : BaseKioskFragmentImpl(), SplashView {

    @Inject
    lateinit var presenter: SplashPresenter

    override fun setupFragmentComponent() {
        MyApp.getInstance().mAppComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        presenter.loadKioskConfiguration()
    }

    override fun goToNextScreen() {
    }

    override fun onResume() {
        super.onResume()

    }

    @OnClick(R.id.layout_container)
    fun onScreenClicked() {
        presenter.onScreenClicked()
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