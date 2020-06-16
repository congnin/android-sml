package vn.kingbee.widget.dialog.big.base

import android.app.Dialog

interface DialogClickedListener {
    fun onDialogClicked(dialog: Dialog, buttonType: DialogType)
}