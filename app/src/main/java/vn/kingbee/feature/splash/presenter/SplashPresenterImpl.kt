package vn.kingbee.feature.splash.presenter

import android.annotation.SuppressLint
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.kingbee.application.MyApp
import vn.kingbee.domain.dataprocess.AppBus
import vn.kingbee.domain.dataprocess.Runtime
import vn.kingbee.domain.entity.lov.LOV
import vn.kingbee.domain.entity.token.AccessTokenRequest
import vn.kingbee.domain.entity.token.AccessTokenResponse
import vn.kingbee.domain.setting.usecase.SettingUseCase
import vn.kingbee.domain.token.usecase.TokenUseCase
import vn.kingbee.feature.base.presenter.BaseKioskPresenter
import vn.kingbee.feature.splash.view.SplashView
import vn.kingbee.utils.FileUtils
import vn.kingbee.utils.SystemUtil
import javax.inject.Inject
const val AUTHOR = "Basic V0haOEgzV2ZIV2thSTRFYTdwTHlGSG0zVDBzYTplY0Y2c25LMTAwbkpLM05velF1dFFIV3JpaGdh"
class SplashPresenterImpl : BaseKioskPresenter<SplashView>, SplashPresenter {

    private var mSettingUseCase: SettingUseCase
    private var mTokenUseCase: TokenUseCase

    @Inject
    constructor(
        runtime: Runtime,
        appBus: AppBus,
        settingUseCase: SettingUseCase,
        tokenUseCase: TokenUseCase) : super(runtime, appBus) {
        this.mSettingUseCase = settingUseCase
        this.mTokenUseCase = tokenUseCase
    }

    override fun onScreenClicked() {
        generateAppToken()
    }

    override fun getAllLovs() {
        mSettingUseCase.getLOVs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LOV> {
                override fun onComplete() {
                    //do nothing
                }

                override fun onSubscribe(d: Disposable) {
                    //do nothing
                }

                override fun onNext(t: LOV) {
                    getView()?.hideProgressDialog()
                }

                override fun onError(e: Throwable) {
                    //do nothing
                }
            })
    }

    override fun generateAppToken() {
        getView()?.showProgressDialog()
        val kioskConfiguration = runtime.getKioskConfiguration()
        val tokenConfigs = HashMap<String, String>()
        for (tkItem in kioskConfiguration?.tokenConfigurations!!) {
            tokenConfigs[tkItem.name!!.trim()] = tkItem.value!!
        }
        if (tokenConfigs.size == 0) {
            return
        }

        val accessTokenRequest = AccessTokenRequest(tokenConfigs["SCOPE"], tokenConfigs["GRANT_TYPE"])

        accessTokenRequest.authorization = AUTHOR
        mTokenUseCase.getToken(accessTokenRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<AccessTokenResponse> {
                override fun onComplete() {
                    //do nothing
                }

                override fun onSubscribe(d: Disposable) {
                    //do nothing
                }

                override fun onNext(t: AccessTokenResponse) {
                    runtime.setAppToken(t)
                    getAllLovs()
                }

                override fun onError(e: Throwable) {
                    //do nothing
                }
            })
    }

    override fun getSettingConfig() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("CheckResult")
    override fun loadKioskConfiguration() {
        val msisdn = SystemUtil.getSerialNumber(MyApp.getInstance())
        appBus.setMsisdn(msisdn)
        FileUtils.getEnvConfigurationFromResource(MyApp.getInstance())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it != null) {
                    runtime.setKioskConfiguration(it)
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