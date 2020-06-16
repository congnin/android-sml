package vn.kingbee.widget.dialog.big

import android.app.Dialog
import android.content.Context
import vn.kingbee.widget.dialog.big.base.DialogInfo

interface DialogHelper {
    fun createDialog(context: Context, dialogInfo: DialogInfo): Dialog
}