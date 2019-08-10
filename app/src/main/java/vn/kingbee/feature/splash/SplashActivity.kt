package vn.kingbee.feature.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import vn.kingbee.feature.base.fragment.BaseKioskFragment
import vn.kingbee.feature.splash.view.SplashFragment
import vn.kingbee.widget.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        if (savedInstanceState == null) {
            hostFragment(SplashFragment.newInstance())
        }
    }

    private fun hostFragment(fragment: BaseKioskFragment?) {
        if (fragment != null && supportFragmentManager.findFragmentByTag(fragment.getTagName()) == null) {
            val ft = this.supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, fragment.getInstance(), fragment.getTagFromClassName())
            ft.commit()
        }
    }
}