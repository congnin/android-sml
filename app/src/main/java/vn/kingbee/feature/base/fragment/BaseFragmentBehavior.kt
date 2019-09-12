package vn.kingbee.feature.base.fragment

import android.app.Dialog
import vn.kingbee.feature.base.callback.IBaseError

interface BaseFragmentBehavior {

    fun getProgressDialog(): Dialog

    fun showProgressDialog()

    fun hideProgressDialog()

    fun viewLoaded()
}