package vn.kingbee.rxjava

import android.content.Intent
import android.os.Bundle
import vn.kingbee.widget.BaseActivity
import android.view.View
import vn.kingbee.widget.R

class RxJava2SelectionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava2_selection);
    }

    fun startOperatorsActivity(view: View) {
        startActivity(Intent(this@RxJava2SelectionActivity, OperatorsActivity::class.java))
    }

    fun startNetworkingActivity(view: View) {
//        startActivity(Intent(this@SelectionActivity, NetworkingActivity::class.java))
    }

    fun startCacheActivity(view: View) {
//        startActivity(Intent(this@RxJava2SelectionActivity, CacheExampleActivity::class.java))
    }

    fun startRxBusActivity(view: View) {
//        (application as MyApplication).sendAutoEvent()
//        startActivity(Intent(this@RxJava2SelectionActivity, RxBusActivity::class.java))
    }

    fun startPaginationActivity(view: View) {
//        startActivity(Intent(this@RxJava2SelectionActivity, PaginationActivity::class.java))
    }

    fun startComposeOperator(view: View) {
//        startActivity(Intent(this@RxJava2SelectionActivity, ComposeOperatorExampleActivity::class.java))
    }

    fun startSearchActivity(view: View) {
//        startActivity(Intent(this@RxJava2SelectionActivity, SearchActivity::class.java))
    }
}