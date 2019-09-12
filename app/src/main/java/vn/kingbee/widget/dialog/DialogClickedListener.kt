package vn.kingbee.widget.dialog

import android.app.Dialog

interface DialogClickedListener {
    fun onDialogClicked(dialog: Dialog, buttonType: ButtonType): Boolean
}