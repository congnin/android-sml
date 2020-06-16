package vn.kingbee.feature.pagetransformer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import vn.kingbee.widget.R

class IntroActivity : AppCompatActivity() {
    lateinit var mViewPager: ViewPager;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.intro_layout);

        mViewPager = findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mViewPager.adapter = IntroAdapter(supportFragmentManager)

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, IntroPageTransformer())
    }
}