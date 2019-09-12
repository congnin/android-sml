package vn.kingbee.widget.spinner

import android.content.Context
import androidx.appcompat.widget.ListPopupWindow
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import vn.kingbee.widget.R
import androidx.core.view.ViewCompat
import androidx.core.content.ContextCompat
import vn.kingbee.utils.StringUtils
import android.widget.AdapterView
import vn.kingbee.utils.CommonUtils

class MaterialSpinner : LinearLayout {
    private var mHint: TextView? = null

    private var mSpinner: TextView? = null

    private var mArrow: View? = null

    private var mError: TextView? = null

    private var mPopup: ListPopupWindow? = null
    private var mAdapter: ListAdapter? = null

    private var mRules: List<BaseSpinnerValidationRule>? = null

    private var mEmptyErrorMessage: String? = null

    private var currentPosition: Int = 0

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        mRules = ArrayList()
        val view = View.inflate(context, R.layout.view_material_spinner_layout, this)
        initView(view)
        initAttrs(attrs)
    }

    fun initView(view: View) {
        mHint = view.findViewById(R.id.vas_hint)
        mSpinner = view.findViewById(R.id.vas_spinner)
        mError = view.findViewById(R.id.vas_error)
        mArrow = view.findViewById(R.id.vas_arrow)
        mPopup = ListPopupWindow(context)
        mPopup?.anchorView = mSpinner
        mPopup?.setDropDownGravity(Gravity.BOTTOM)
        mPopup?.isModal = true
        mPopup?.softInputMode = android.widget.ListPopupWindow.INPUT_METHOD_NOT_NEEDED

        mPopup?.setOnDismissListener {
            mArrow?.animate()?.rotation(0F)?.setDuration(300)?.start()
            isValid()
        }

        mSpinner?.setOnClickListener {
            if (mAdapter == null || mAdapter?.count == 0) {
                return@setOnClickListener
            }
            requestFocus()
            showDropdown()
            mError?.visibility = INVISIBLE
            updateState()
        }
    }

    fun updateState() {
        if (mPopup!!.isShowing) {
            mSpinner?.isEnabled = false
        } else {
            mSpinner?.isEnabled = true
            mSpinner?.isSelected = (mError?.visibility == View.VISIBLE)
        }
    }

    fun showError(message: String) {
        if (!message.isEmpty()) {
            mError?.text = message
            mError?.visibility = View.VISIBLE
        } else {
            mError?.visibility = View.INVISIBLE
        }
    }

    private fun showDropdown() {
        mArrow?.animate()?.rotation(180F)?.setDuration(300)?.start()
        mPopup?.show()
        updateState()
    }

    protected fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MaterialSpinner, 0, 0)
        val firstHint = ta.getString(R.styleable.MaterialSpinner_spinnerFirstHint)
        val secondHint = ta.getString(R.styleable.MaterialSpinner_spinnerSecondHint)
        mEmptyErrorMessage = ta.getString(R.styleable.MaterialSpinner_spinnerEmptyErrorMessage)

        val firstHintColor = ta.getColor(
            R.styleable.MaterialSpinner_spinnerFirstHintColor,
            ContextCompat.getColor(context, R.color.text_color_gray3)
        )
        val secondHintColor = ta.getColor(
            R.styleable.MaterialSpinner_spinnerSecondHintColor,
            ContextCompat.getColor(context, R.color.dark_4)
        )

        val textColor = ta.getColor(
            R.styleable.MaterialSpinner_spinnerTextColor, ContextCompat.getColor(
                context, R.color.light_black_1
            )
        )

        val errorColor = ta.getColor(
            R.styleable.MaterialSpinner_spinnerErrorColor, ContextCompat.getColor(
                context, R.color.red
            )
        )

        val drawableRight = ta.getDrawable(R.styleable.MaterialSpinner_spinnerDrawableRight)
        val background = ta.getDrawable(R.styleable.MaterialSpinner_spinnerBackground)

        mHint?.text = StringUtils.emptyIfNull(firstHint)
        mSpinner?.hint = StringUtils.emptyIfNull(secondHint)
        mError?.text = StringUtils.emptyIfNull(mEmptyErrorMessage)

        mHint?.setTextColor(firstHintColor)
        mSpinner?.setHintTextColor(secondHintColor)
        mSpinner?.setTextColor(textColor)
        mError?.setTextColor(errorColor)

        if (drawableRight != null) {
            mSpinner?.setCompoundDrawables(null, null, drawableRight, null)
        }

        if (background != null) {
            ViewCompat.setBackground(mSpinner!!, background)
        }

        ta.recycle()
    }

    fun setAdapter(adapter: ListAdapter) {
        mAdapter = adapter
        currentPosition = 0
        mPopup?.setAdapter(adapter)
    }

    fun setOnItemClickListener(listener: AdapterView.OnItemClickListener) {
        mPopup?.setOnItemClickListener { parent, view, position, id ->
            listener.onItemClick(parent, view, position, id)
            setCurrent(position)
            mPopup!!.dismiss()
        }
    }

    fun reset() {
        currentPosition = 0
        mSpinner?.text = ""
    }

    fun setCurrent(position: Int) {
        if (mAdapter != null && position >= 0 && position < mAdapter!!.count) {
            currentPosition = position
            if (mAdapter is SpinnerAdapter) {
                mSpinner?.text = (mAdapter as SpinnerAdapter)
                    .getSelectedMessage(mSpinner!!.context, position)
            } else {
                mSpinner?.text = mAdapter?.getItem(position).toString()
            }
        }
    }

    fun getText(): CharSequence = mSpinner!!.text

    fun addValidationRule(rule: BaseSpinnerValidationRule) {
        (mRules as ArrayList).add(rule)
    }

    fun getAdapter(): ListAdapter = mAdapter!!

    fun isValid(): Boolean {
        var isValid = true
        mSpinner?.clearFocus()
        if (mSpinner?.length() == 0) {
            showError(mEmptyErrorMessage!!)
            isValid = false
        } else if (!checkValidationRule()) {
            isValid = false
        } else {
            showError("")
        }
        updateState()
        return isValid
    }

    private fun checkValidationRule(): Boolean {
        if (!CommonUtils.isEmpty(mRules)) {
            for (rule in mRules!!) {
                if (!rule.isValid(this, currentPosition)) {
                    showError(rule.errorMessage)
                    return false
                }
            }
        }
        return true
    }

    interface SpinnerAdapter : ListAdapter {
        fun getSelectedMessage(context: Context, position: Int): String
    }
}