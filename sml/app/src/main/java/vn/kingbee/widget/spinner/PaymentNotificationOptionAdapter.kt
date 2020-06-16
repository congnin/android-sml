package vn.kingbee.widget.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import vn.kingbee.widget.R

open class PaymentNotificationOptionAdapter(var context: Context) : BaseAdapter(), MaterialSpinner.SpinnerAdapter {

    var data = arrayOf(PaymentNotificationAdapterModel(R.string.payment_notification_input_email_option, PaymentNotificationType.EMAIL)
        , PaymentNotificationAdapterModel(R.string.payment_notification_input_cellphone_option, PaymentNotificationType.CELLPHONE)
        , PaymentNotificationAdapterModel(R.string.payment_notification_input_email_and_cellphone_option, PaymentNotificationType.CELLPHONE_EMAIL))

    override fun getView(position: Int, container: View?, viewGroup: ViewGroup?): View {
        val view: View

        if (container == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_dropdown_single_content, viewGroup, false)
            view.tag = ItemRowHolder(view)
        } else {
            view = container
        }
        val holder = view.tag as ItemRowHolder
        holder.tvContent.text = context.getString(data[position].resourceId)
        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return data.size
    }

    private class ItemRowHolder(row: View?) {
        val tvContent: TextView = row?.findViewById(R.id.tvDropdownContent) as TextView
    }

    override fun getSelectedMessage(context: Context, position: Int): String {
        return context.getString(data[position].resourceId)
    }
}