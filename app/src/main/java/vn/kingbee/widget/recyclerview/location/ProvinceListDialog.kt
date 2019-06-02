package vn.kingbee.widget.recyclerview.location

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.Window
import android.widget.ImageView
import vn.kingbee.model.Province
import vn.kingbee.utils.LOCATION_LIST_DIALOG_VERTICAL_POSITION
import vn.kingbee.widget.R

class ProvinceListDialog : Dialog, ProvinceListAdapter.ProvinceListAdapterListener {

    private var provinceListAdapter: ProvinceListAdapter? = null
    private var mListener: ProvinceDialogListener? = null
    private var mItems: List<Province>

    constructor(context: Context, items: List<Province>, listener: ProvinceDialogListener) : super(
        context
    ) {
        mItems = items

        mListener = listener
        initialize()
    }

    private fun initialize() {
        val layoutParams = this.window!!.attributes
        layoutParams.y = LOCATION_LIST_DIALOG_VERTICAL_POSITION
        this.window!!.setBackgroundDrawableResource(R.drawable.bg_location_dialog)
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_province_list)

        val provinceListView =
            findViewById<View>(R.id.recycler_view_location_list_dialog_province) as androidx.recyclerview.widget.RecyclerView
        val dismissIcon = findViewById<View>(R.id.icon_dismiss_dialog) as ImageView

        provinceListAdapter = ProvinceListAdapter(context, mItems, this)
        provinceListView.adapter = this.provinceListAdapter

        dismissIcon.setOnClickListener { view -> this.dismiss() }
        provinceListView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    override fun onProvinceClick(province: Province) {
        if (mListener != null) {
            dismiss()
            mListener?.onProvinceSelected(province)
        }
    }

    interface ProvinceDialogListener {
        fun onProvinceSelected(province: Province)
    }
}