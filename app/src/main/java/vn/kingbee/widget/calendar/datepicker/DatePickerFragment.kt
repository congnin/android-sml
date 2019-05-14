package vn.kingbee.widget.calendar.datepicker

import android.app.Dialog
import android.support.v4.app.DialogFragment
import butterknife.Unbinder
import android.widget.Button
import butterknife.BindView
import android.widget.DatePicker
import io.reactivex.subjects.PublishSubject
import vn.kingbee.widget.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import butterknife.ButterKnife
import io.reactivex.Observable
import java.util.*
import butterknife.OnClick

class DatePickerFragment : DialogFragment() {
    @BindView(R.id.date_picker)
    lateinit var datePicker: DatePicker

    @BindView(R.id.btn_cancel)
    lateinit var btnCancel: Button

    @BindView(R.id.btn_ok)
    lateinit var btnOK: Button

    lateinit var unbinder: Unbinder

    private val dateChoose = PublishSubject.create<Date>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_date_picker, container, false)
        unbinder = ButterKnife.bind(this, view)
        init(view)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    protected fun init(view: View) {
        var date = Calendar.getInstance().time

        val args = arguments
        if (args != null && args.containsKey(ARG_DEFAULT_TIME)) {
            date = args.getSerializable(ARG_DEFAULT_TIME) as Date?
        }

        val c = Calendar.getInstance()
        c.time = date

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker.init(year, month, day, null)
    }

    fun getDateChoose(): Observable<Date> {
        return dateChoose.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.btn_cancel, R.id.btn_ok)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_cancel -> dismiss()
            R.id.btn_ok -> {
                val calendar = Calendar.getInstance()
                calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                dateChoose.onNext(calendar.time)
                dismiss()
            }
        }
    }

    companion object {
        protected const val ARG_DEFAULT_TIME = "ARG_DEFAULT_TIME"

        fun newInstance(defaultTime: Date?): DatePickerFragment {
            val fragment = DatePickerFragment()

            if (defaultTime != null) {
                val args = Bundle()
                args.putSerializable(ARG_DEFAULT_TIME, defaultTime)

                fragment.arguments = args
            }

            return fragment
        }
    }
}