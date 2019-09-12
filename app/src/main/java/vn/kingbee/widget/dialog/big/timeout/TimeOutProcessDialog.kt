package vn.kingbee.widget.dialog.big.timeout

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import at.grabner.circleprogress.CircleProgressView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.functions.Action
import timber.log.Timber
import vn.kingbee.widget.R

class TimeOutProcessDialog {
    @BindView(R.id.btn_exit)
    lateinit var btnExit: Button

    @BindView(R.id.btn_yes)
    lateinit var btnYes: Button

    @BindView(R.id.tv_numberCounter)
    lateinit var tvNumberCounter: TextView

    @BindView(R.id.timeProgress)
    lateinit var timeProgress: CircleProgressView

    private var materialDialog: MaterialDialog? = null

    private var positiveEvent: DialogEvent? = null
    private var negativeEvent: DialogEvent? = null

//    private var finishTask: Consumer? = null

    private var context: Context
    private var countDownTimer: CountDownTimer? = null
    private var timeCountDownValue = 30

    constructor(builder: Builder) {
        positiveEvent = builder.positiveEvent
        negativeEvent = builder.negativeEvent
//        finishTask = builder.finishTask

        context = builder.context

        materialDialog = MaterialDialog.Builder(context)
            .customView(R.layout.dialog_timeout_process, false)
            .build()
        materialDialog?.setCanceledOnTouchOutside(false)

        timeCountDownValue = context.resources.getInteger(R.integer.time_countdown_dialog)

        ButterKnife.bind(this, materialDialog!!)
        initView(context, materialDialog)
    }

    private fun initView(context: Context, materialDialog: MaterialDialog?) {

        materialDialog?.window!!.setBackgroundDrawableResource(R.color.c_transparent)
        materialDialog.window!!.setLayout(
            context.resources.getDimensionPixelOffset(R.dimen.exit_on_boarding_dialog_width),
            context.resources.getDimensionPixelOffset(R.dimen.exit_on_boarding_dialog_height)
        )

        timeProgress.setText(timeCountDownValue.toString())

        countDownTimer = object : CountDownTimer((timeCountDownValue * 1000L) + 200L, 1000L) {
            override fun onFinish() {
                Timber.d("TimeOutProcessDialog onFinish")
            }

            override fun onTick(millisUntilFinished: Long) {
                tvNumberCounter.text = (millisUntilFinished / 1000).toString()
                timeProgress.setValue((timeCountDownValue - (millisUntilFinished / 1000)).toFloat())
            }

        }
    }

    fun show() {
        if (materialDialog != null && !materialDialog?.isShowing!!) {
            materialDialog?.show()
            countDownTimer?.start()
        }
    }

    fun dismiss() {
        if (materialDialog != null && materialDialog?.isShowing!!) {
            materialDialog?.dismiss()
            countDownTimer?.cancel()
        }
    }

    fun isShowing(): Boolean = materialDialog?.isShowing!!

    @OnClick(R.id.btn_yes)
    fun onPositiveButtonClicked() {
        if (materialDialog != null) {
            positiveEvent?.onClick(materialDialog!!)
            countDownTimer?.cancel()

            materialDialog?.dismiss()
        }
    }

    @OnClick(R.id.btn_exit)
    fun onNegativeButtonClicked() {
        if (materialDialog != null) {
            negativeEvent?.onClick(materialDialog!!)
            countDownTimer?.cancel()

            materialDialog?.dismiss()
        }
    }

    class Builder {
        internal var positiveEvent: DialogEvent? = null
        internal var negativeEvent: DialogEvent? = null
        internal var finishTask: Action? = null
        internal val context: Context

        constructor(context: Context) {
            this.context = context
        }

        fun positiveEvent(value: DialogEvent?): Builder {
            this.positiveEvent = value
            return this
        }

        fun negativeEvent(value: DialogEvent?): Builder {
            this.negativeEvent = value
            return this
        }

        fun finishTask(value: Action?): Builder {
            finishTask = value
            return this
        }

        fun build(): TimeOutProcessDialog {
            return TimeOutProcessDialog(this)
        }
    }

    interface DialogEvent {
        fun onClick(dialog: Dialog)
    }
}