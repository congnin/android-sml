package vn.kingbee.widget.recyclerview.alphabet.demo

import butterknife.ButterKnife
import android.widget.ImageButton
import butterknife.BindView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import vn.kingbee.widget.R


class AlphabetAdapter(private val mDataArray: MutableList<String>?) : RecyclerView.Adapter<AlphabetAdapter.ViewHolder>(), SectionIndexer {
    private var mSectionPositions: ArrayList<Int>? = null

    override fun getItemCount(): Int {
        return mDataArray?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView!!.text = mDataArray!![position]
        holder.mImageButton!!.setOnClickListener {
            mDataArray.removeAt(holder.adapterPosition)
            notifyDataSetChanged()
        }
    }

    override fun getSectionForPosition(position: Int): Int {
        return 0
    }

    override fun getSections(): Array<Any> {
        val sections = ArrayList<String>(26)
        mSectionPositions = ArrayList(26)
        var i = 0
        val size = mDataArray!!.size
        while (i < size) {
            val section = mDataArray[i][0].toString().toUpperCase()
            if (!sections.contains(section)) {
                sections.add(section)
                mSectionPositions!!.add(i)
            }
            i++
        }
        return sections.toTypedArray()
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mSectionPositions!![sectionIndex]
    }

    class ViewHolder : RecyclerView.ViewHolder {
        @BindView(R.id.tv_alphabet)
        lateinit var mTextView: TextView
        @BindView(R.id.ib_alphabet)
        lateinit var mImageButton: ImageButton

        constructor(itemView: View) : super(itemView) {
            ButterKnife.bind(this, itemView)
        }
    }
}