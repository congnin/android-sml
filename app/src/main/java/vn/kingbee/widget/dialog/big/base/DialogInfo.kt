package vn.kingbee.widget.dialog.big.base

import android.text.Spannable
import vn.kingbee.widget.dialog.DialogClickedListener

class DialogInfo {
    var imageResId: Int? = null
    var title: Spannable? = null
    var content: Spannable? = null
    var phoneNumber: String? = null
    var buttonTitles: Array<String>? = null
    var buttonDrawableInfos: Array<ButtonDrawableInfo>? = null
    var hasCloseButton = false
    var listener: DialogClickedListener? = null
    var dialogType: DialogType? = null
    var url: String? = null
    var errorCode: String? = null
}