package vn.kingbee.widget.dialog.big

import android.app.Dialog
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.big.base.DialogInfo
import vn.kingbee.widget.dialog.big.timeout.ProgressCircleView

class DialogHelperImpl : DialogHelper {
    override fun createDialog(context: Context, dialogInfo: DialogInfo): Dialog {
        return Dialog(context)
    }

    private fun progressDialog(context: Context): MaterialDialog {
        val builder = MaterialDialog.Builder(context)
        builder.customView(R.layout.view_circle_loading, false)
        val materialDialog = builder.build()
        ProgressCircleView(materialDialog)
        return materialDialog
    }


}