package vn.kingbee.feature.splash.presenter

import vn.kingbee.feature.base.presenter.IBasePresenter

interface SplashPresenter<T> : IBasePresenter<T> {

    fun onScreenClicked()

    fun getAllLovs()

    fun generateAppToken()

    fun getSettingConfig()

    fun loadKioskConfiguration()

    fun getKioskSettingConfig()
}