package vn.kingbee.feature.base.fragment

import android.app.Dialog
import androidx.fragment.app.Fragment

interface BaseKioskFragment {
    fun getTagName(): String

    fun getTagFromClassName(): String

    fun getInstance(): Fragment

    fun setupFragmentComponent()

    fun showNetworkErrorDialog()

    fun showTimeoutDialog()

    fun showLoggedInTimeoutDialog()
}