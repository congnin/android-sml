package vn.kingbee.widget.dialog.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import vn.kingbee.widget.R

class CommonDialog(context: Context, private val mTitle: Int?, private val mMessage: Int?) {

    private var mAlertDialog: Dialog? = null
    private var mListener: ClickListener? = null
    private var mDialogView: View? = null

    private fun createDialog(context: Context) {

        mAlertDialog = Dialog(context)
        mAlertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mAlertDialog?.setCancelable(false)
        mAlertDialog?.setCanceledOnTouchOutside(false)

        val layoutInflater = LayoutInflater.from(context)
        val lay = RelativeLayout(context)
        mDialogView = layoutInflater!!.inflate(R.layout.dialog_common, lay)
        mAlertDialog?.setContentView(mDialogView!!)

        if (mTitle != null)
            this.mDialogView?.findViewById<TextView>(R.id.tvTitle)?.setText(mTitle)
        if (mMessage != null)
            this.mDialogView?.findViewById<TextView>(R.id.tvMessage)?.setText(mMessage)

        mAlertDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog?.window?.setGravity(Gravity.CENTER)

        val okButton = this.mAlertDialog?.findViewById<Button>(R.id.btn_ok_dialog)
        okButton?.setOnClickListener {
            if (mListener != null) {

                mListener!!.onClickPositive()

            }
            mAlertDialog?.dismiss()
        }

        val cancelButton = this.mAlertDialog?.findViewById<Button>(R.id.btn_cancel_dialog)
        cancelButton?.setOnClickListener {
            if (mListener != null) {

                mListener!!.onClickNegative()

            }
            mAlertDialog?.dismiss()
        }
    }

    fun showNegativeButton(): CommonDialog {
        mDialogView?.findViewById<Button>(R.id.btn_cancel_dialog)?.visibility = View.VISIBLE
        return this
    }

    fun setTitleString(string: String): CommonDialog {
        this.mDialogView?.findViewById<TextView>(R.id.tvTitle)?.text = string
        return this
    }

    fun setMessageString(string: String): CommonDialog {
        this.mDialogView?.findViewById<TextView>(R.id.tvMessage)?.text = string
        return this
    }

    fun setCode(string: String): CommonDialog {
        this.mDialogView?.findViewById<TextView>(R.id.tvCode)?.text = "Kode ref: $string"
        this.mDialogView?.findViewById<TextView>(R.id.tvCode)?.visibility = View.VISIBLE
        return this
    }

    fun setNegativeButtonString(string: Int): CommonDialog {
        this.mDialogView?.findViewById<Button>(R.id.btn_cancel_dialog)?.setText(string)
        return this
    }

    fun setPositiveButtonString(string: Int): CommonDialog {
        this.mDialogView?.findViewById<Button>(R.id.btn_ok_dialog)?.setText(string)
        return this
    }

    fun setNegativeButtonColor(colorCode: Int): CommonDialog {
        this.mDialogView?.findViewById<Button>(R.id.btn_cancel_dialog)?.setTextColor(
            ContextCompat.getColor(mDialogView?.context!!, colorCode))
        return this
    }

    fun setPositiveButtonColor(colorCode: Int): CommonDialog {
        this.mDialogView?.findViewById<Button>(R.id.btn_ok_dialog)?.setTextColor(
            ContextCompat.getColor(mDialogView?.context!!, colorCode))
        return this
    }

    fun setClickListener(listener: ClickListener): CommonDialog {
        this.mListener = listener
        return this
    }

    fun showDialog() {
        mAlertDialog?.show()
    }

    interface ClickListener {
        fun onClickPositive() {}
        fun onClickNegative() {}
    }

    init {
        createDialog(context)
    }
}