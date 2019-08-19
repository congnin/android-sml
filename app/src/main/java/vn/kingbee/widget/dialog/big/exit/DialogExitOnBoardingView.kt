package vn.kingbee.widget.dialog.big.exit

import android.content.Context
import android.widget.Button
import com.afollestad.materialdialogs.MaterialDialog
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.big.base.DialogInfo

class DialogExitOnBoardingView {
    lateinit var exitOnBoardingNoBtn: Button
    lateinit var exitOnBoardingYesBtn: Button

    internal var mMaterialDialog: MaterialDialog
    internal var mDialogInfo: DialogInfo
    internal var mContext: Context

    constructor(context: Context, materialDialog: MaterialDialog, dialogInfo: DialogInfo) {
        mMaterialDialog = materialDialog
        mMaterialDialog.window!!.setBackgroundDrawableResource(R.color.c_transparent)
        mMaterialDialog.window!!.setLayout(
            context.resources.getDimensionPixelOffset(R.dimen.exit_on_boarding_dialog_width),
            context.resources.getDimensionPixelOffset(R.dimen.exit_on_boarding_dialog_height)
        )
        mDialogInfo = dialogInfo
        mContext = context
    }

}