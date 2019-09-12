package vn.kingbee.widget.dialog.loading

import android.content.Context
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.BaseDialogMaterialView
import vn.kingbee.widget.dialog.DialogClickedListener

class LoadingDialogMaterial : BaseDialogMaterialView {
    @BindView(R.id.progressBar)
    lateinit var mProgressBar: ProgressBar
    @BindView(R.id.tv_progress_message)
    lateinit var tvMessage: TextView
    protected var mMessage: String? = null

    constructor(context: Context, buttonListener: DialogClickedListener?): super(context, buttonListener)

    fun setMessage(message: String) {
        mMessage = message
        tvMessage.text = message
    }

    override fun getLayout(): Int {
        return R.layout.dialog_loading_default
    }

    override fun bindView() {
        mProgressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(
                getContext(), R.color.yellow
            ), PorterDuff.Mode.MULTIPLY
        )
        tvMessage.text = mMessage
    }
}