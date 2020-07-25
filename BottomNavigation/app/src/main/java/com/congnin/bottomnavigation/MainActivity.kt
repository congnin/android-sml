package com.congnin.bottomnavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.congnin.bottomnavigation.fragment.AfterAcademyFragment
import com.congnin.bottomnavigation.fragment.MindOrksFragment
import com.congnin.bottomnavigation.fragment.UserFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mindOrksFragment = MindOrksFragment()
    private val afterAcademyFragment = AfterAcademyFragment()
    private val userFragment = UserFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = mindOrksFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentManager.beginTransaction().apply {
            add(R.id.container, userFragment, getString(R.string.user)).hide(userFragment)
            add(R.id.container, afterAcademyFragment, getString(R.string.after_academy)).hide(afterAcademyFragment)
            add(R.id.container, mindOrksFragment, getString(R.string.mindOrks))
        }.commit()
        initListeners()
        bottomNavigationView.itemIconTintList = null
    }

    private fun initListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_mindorks -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(mindOrksFragment).commit()
                    activeFragment = mindOrksFragment
                    true
                }

                R.id.navigation_afterAcademy -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(afterAcademyFragment).commit()
                    activeFragment = afterAcademyFragment
                    true
                }

                R.id.navigation_user -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(userFragment).commit()
                    activeFragment = userFragment
                    true
                }

                else -> false
            }
        }
    }
}