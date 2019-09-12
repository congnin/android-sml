package vn.kingbee.feature.splash.presenter

import vn.kingbee.feature.base.presenter.IBasePresenter
import vn.kingbee.feature.splash.view.SplashView

interface SplashPresenter : IBasePresenter<SplashView> {

    fun onScreenClicked()

    fun getAllLovs()

    fun generateAppToken()

    fun getSettingConfig()

    fun loadKioskConfiguration()

    fun getKioskSettingConfig()
}