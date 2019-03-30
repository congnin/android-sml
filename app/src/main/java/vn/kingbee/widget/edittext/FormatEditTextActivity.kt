package vn.kingbee.widget.edittext

import android.os.Bundle
import android.widget.AdapterView
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.spinner.MaterialSpinner
import vn.kingbee.widget.spinner.PaymentNotificationOptionAdapter

class FormatEditTextActivity : BaseActivity() {
    private var materialSpinner: MaterialSpinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_format_edittext)

        addViews()
        addEvents()
    }

    private fun addViews() {
        materialSpinner = findViewById(R.id.spPaymentNotificationMethod)
    }

    private fun addEvents() {
        val adapter = PaymentNotificationOptionAdapter(this)
        materialSpinner?.setAdapter(adapter)
        materialSpinner?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

        })
    }
}