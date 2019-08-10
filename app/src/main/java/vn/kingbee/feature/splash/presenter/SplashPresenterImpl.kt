package vn.kingbee.feature.splash.presenter

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.application.appbus.AppBus
import vn.kingbee.application.runtime.Runtime
import vn.kingbee.domain.setting.usecase.SettingUseCase
import vn.kingbee.domain.token.usecase.TokenUseCase
import vn.kingbee.feature.base.presenter.BaseKioskPresenter
import vn.kingbee.feature.splash.view.SplashView
import vn.kingbee.utils.FileUtils
import javax.inject.Inject

class SplashPresenterImpl : BaseKioskPresenter<SplashView>, SplashPresenter<SplashView> {

    private var mSettingUseCase: SettingUseCase
    private var mTokenUseCase: TokenUseCase

    @Inject
    constructor(
        runtime: Runtime,
        appBus: AppBus,
        settingUseCase: SettingUseCase,
        tokenUseCase: TokenUseCase
    ) : super(runtime, appBus) {
        this.mSettingUseCase = settingUseCase
        this.mTokenUseCase = tokenUseCase
    }

    override fun onScreenClicked() {
        generateAppToken()
    }

    override fun getAllLovs() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun generateAppToken() {
        getView()?.showProgressDialog()

    }

    override fun getSettingConfig() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("CheckResult")
    override fun loadKioskConfiguration() {
        FileUtils.getEnvConfigurationFromResource(MyApp.getInstance())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it != null) {
                    runtime.setKioskConfiguration(it)
                    Timber.d("FUCK $it")
                } else {
                    Timber.d("HU HU HU")
                }
            }
    }

    override fun getKioskSettingConfig() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val BASIC = "Basic"
    }
}