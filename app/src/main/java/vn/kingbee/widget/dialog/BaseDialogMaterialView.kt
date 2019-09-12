package vn.kingbee.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import butterknife.ButterKnife
import vn.kingbee.widget.R

const val NO_ANIMATION = -1
abstract class BaseDialogMaterialView {

    protected var mMaterialDialog: Dialog? = null
    protected var mDialogClickedListener: DialogClickedListener? = null
    private var mContext: Context? = null
    private var mView: View? = null

    constructor(context: Context, buttonListener: DialogClickedListener?) {
        mContext = context
        mDialogClickedListener = buttonListener
        mMaterialDialog = Dialog(mContext!!, android.R.style.Theme_Translucent_NoTitleBar)
        mMaterialDialog?.setCanceledOnTouchOutside(false)
        mMaterialDialog?.setCancelable(false)
        mView = LayoutInflater.from(context).inflate(getLayout(), null)
        mMaterialDialog?.setContentView(mView!!)
        setAnimation(getDefaultAnimation())
        ButterKnife.bind(this, mView!!)
        bindView()
    }

    fun getCustomView(): View {
        return mView!!
    }

    abstract fun getLayout(): Int

    fun getDialog(): Dialog {
        return mMaterialDialog!!
    }

    protected abstract fun bindView()

    protected fun getView(): View {
        return mView!!
    }

    protected fun getDefaultAnimation(): Int {
        return R.style.Dialog_Infade_OutFade_Animation
    }

    protected fun setAnimation(animationResources: Int) {
        if (animationResources != NO_ANIMATION && mMaterialDialog?.window != null) {
            mMaterialDialog?.window!!.setWindowAnimations(animationResources)
        }
    }

    protected fun getContext(): Context {
        return mContext!!
    }

    fun getDialogClickedListener(): DialogClickedListener {
        return mDialogClickedListener!!
    }
}