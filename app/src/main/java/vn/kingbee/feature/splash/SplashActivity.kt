package vn.kingbee.feature.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import vn.kingbee.feature.base.fragment.BaseKioskFragment
import vn.kingbee.feature.splash.view.SplashFragment
import vn.kingbee.widget.R

class SplashActivity : AppCompatActivity() {

    private var userInteractionListener: UserInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        if (savedInstanceState == null) {
            hostFragment(SplashFragment.newInstance())
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        userInteractionListener?.onUserInteraction()
    }

    private fun hostFragment(fragment: BaseKioskFragment?) {
        if (fragment != null && supportFragmentManager.findFragmentByTag(fragment.getTagName()) == null) {
            val ft = this.supportFragmentManager.beginTransaction()
            ft.replace(
                R.id.fragment_container,
                fragment.getInstance(),
                fragment.getTagFromClassName()
            )
            ft.commit()
        }
    }

    private fun getVisibleFragment(): Fragment? {
        val fragments = supportFragmentManager.fragments as List<Fragment>
        if (fragments.isNotEmpty()) {
            for (fragment in fragments) {
                if (fragment.isVisible) {
                    return fragment
                }
            }
        }
        return null
    }

    fun setUserInteractionListener(userInteractionListener: UserInteractionListener?) {
        this.userInteractionListener = userInteractionListener
    }

    interface UserInteractionListener {
        fun onUserInteraction()
    }
}