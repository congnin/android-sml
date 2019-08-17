package vn.kingbee.widget.dialog.big.timeout

import android.content.DialogInterface
import com.afollestad.materialdialogs.MaterialDialog
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.big.timeout.LoadingCircular

class ProgressCircleView : DialogInterface.OnDismissListener, DialogInterface.OnShowListener {
    private var mMaterialDialog: MaterialDialog
    private var mLoadingCircular: LoadingCircular

    constructor(materialDialog: MaterialDialog) {
        this.mMaterialDialog = materialDialog
        this.mLoadingCircular = materialDialog.customView!!.findViewById(R.id.lv_circular)
        this.mLoadingCircular.isClickable = false
        this.mMaterialDialog.setOnDismissListener(this)
        this.mMaterialDialog.setOnShowListener(this)
        this.mMaterialDialog.window!!.setBackgroundDrawableResource(R.color.c_transparent)
    }

    override fun onDismiss(dialog: DialogInterface) {
        this.mLoadingCircular.stopAnim()
    }

    override fun onShow(dialog: DialogInterface) {
        this.mLoadingCircular.startAnim()
    }
}