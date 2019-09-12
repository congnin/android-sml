package vn.kingbee.widget.calendar.ggdatepicker

import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import vn.kingbee.widget.R
import java.util.ArrayList

class YearPickerView(context: Context, datePickerController: DatePickerController) : ListView(
    context
), AdapterView.OnItemClickListener, DatePickerDialog.OnDateChangedListener {
    private var mController: DatePickerController = datePickerController
    private var mAdapter: YearAdapter? = null
    private var mChildSize: Int
    private var mSelectedView: TextViewWithCircularIndicator? = null
    private var mViewSize: Int

    init {
        mController.registerOnDateChangedListener(this)
        layoutParams = ViewGroup.LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        )
        val resources = context.resources
        mViewSize = resources.getDimensionPixelOffset(R.dimen.date_picker_view_animator_height)
        mChildSize = resources.getDimensionPixelOffset(R.dimen.year_label_height)
        isVerticalFadingEdgeEnabled = true
        setFadingEdgeLength(mChildSize / 3)
        init(context)
        onItemClickListener = this
        selector = StateListDrawable()
        dividerHeight = 0
        onDateChanged()
    }

    private fun getYearFromTextView(view: TextView): Int {
        return Integer.valueOf(view.text.toString())
    }

    private fun init(context: Context) {
        val years = ArrayList<String>()
        for (year in mController.getMinYear()..mController.getMaxYear()) {
            years.add(String.format("%d", year))
        }
        mAdapter = YearAdapter(context, R.layout.year_label_text_view, years)
        adapter = mAdapter
    }

    fun getFirstPositionOffset(): Int {
        val firstChild = getChildAt(0) ?: return 0
        return firstChild.top
    }

    override fun onDateChanged() {
        mAdapter!!.notifyDataSetChanged()
        postSetSelectionCentered(mController.getSelectedDay().year - mController.getMinYear())
    }


    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        mController.tryVibrate()
        val clickedView = view as TextViewWithCircularIndicator
        if (clickedView !== mSelectedView) {
            if (mSelectedView != null) {
                mSelectedView!!.drawIndicator(false)
                mSelectedView!!.requestLayout()
            }
            clickedView.drawIndicator(true)
            clickedView.requestLayout()
            mSelectedView = clickedView
        }
        mController.onYearSelected(getYearFromTextView(clickedView))
        mAdapter!!.notifyDataSetChanged()
    }

    fun postSetSelectionCentered(position: Int) {
        postSetSelectionFromTop(position, mViewSize / 2 - mChildSize / 2)
    }

    fun postSetSelectionFromTop(position: Int, y: Int) {
        post {
            setSelectionFromTop(position, y)
            requestLayout()
        }
    }

    private inner class YearAdapter(context: Context, resource: Int, years: List<String>) :
        ArrayAdapter<String>(context, resource, years) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v = super.getView(position, convertView, parent) as TextViewWithCircularIndicator
            v.requestLayout()
            val year = getYearFromTextView(v)
            val selected = mController.getSelectedDay().year == year
            v.drawIndicator(selected)
            if (selected) {
                mSelectedView = v
            }
            return v
        }
    }
}