package vn.kingbee.movie.util

import android.database.Cursor
import android.database.DataSetObserver
import androidx.recyclerview.widget.RecyclerView

abstract class CursorRecyclerViewAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH> {
    private var mCursor: Cursor?
    private var mDataValid: Boolean
    private var mRowIdColumn: Int
    private val mDataSetObserver: DataSetObserver?

    constructor(cursor: Cursor?) {
        mCursor = cursor
        mDataValid = cursor != null
        mRowIdColumn = if (mDataValid) mCursor!!.getColumnIndex("_id") else -1
        mDataSetObserver = NotifyingDataSetObserver()
        if (mCursor != null) {
            mCursor?.registerDataSetObserver(mDataSetObserver)
        }
    }

    fun getCursor(): Cursor? = mCursor

    override fun getItemCount(): Int {
        if (mDataValid && mCursor != null) {
            return mCursor!!.count
        }
        return 0
    }

    override fun getItemId(position: Int): Long {
        if (mDataValid && mCursor != null && mCursor!!.moveToPosition(position)) {
            return mCursor!!.getLong(mRowIdColumn)
        }
        return 0
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    abstract fun onBindViewHolder(viewHolder: VH, cursor: Cursor?)

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        if (!mDataValid) {
            throw IllegalStateException("this should only be called when the cursor is valid")
        }
        if (!mCursor!!.moveToPosition(position)) {
            throw IllegalStateException("couldn't move cursor to position $position")
        }
        onBindViewHolder(viewHolder, mCursor)
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    fun changeCursor(cursor: Cursor?) {
        val old = swapCursor(cursor)
        if (old != null) {
            old.close()
        }
    }

    private fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor == mCursor) {
            return null
        }
        val oldCursor = mCursor
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver)
        }
        mCursor = newCursor
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor?.registerDataSetObserver(mDataSetObserver)
            }
            mRowIdColumn = newCursor!!.getColumnIndexOrThrow("_id")
            mDataValid = true
            notifyDataSetChanged()
        } else {
            mRowIdColumn = -1
            mDataValid = false
            notifyDataSetChanged()
        }
        return oldCursor
    }

    inner class NotifyingDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            mDataValid = true
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            mDataValid = false
            notifyDataSetChanged()
        }
    }
}