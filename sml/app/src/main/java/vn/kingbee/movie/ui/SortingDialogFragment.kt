package vn.kingbee.movie.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import vn.kingbee.application.MyApp
import vn.kingbee.movie.model.Sort
import vn.kingbee.movie.model.SortHelper
import vn.kingbee.widget.R
import javax.inject.Inject
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.Intent

class SortingDialogFragment : DialogFragment() {

    @Inject
    lateinit var sortHelper: SortHelper

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        MyApp.getInstance().networkComponent.inject(this)

        val builder = AlertDialog.Builder(context!!, R.style.DialogStyle)
        builder.setTitle(getString(R.string.sort_dialog_title))
        builder.setNegativeButton(getString(R.string.action_cancel), null)
        builder.setSingleChoiceItems(R.array.pref_sort_by_labels,
            sortHelper.getSortByPreference().ordinal) { dialog, which ->
            sortHelper.saveSortByPreference(Sort.values()[which])
            sendSortPreferenceChangedBroadcast()
            dialog?.dismiss()
        }
        return builder.create()
    }

    private fun sendSortPreferenceChangedBroadcast() {
        val intent = Intent(BROADCAST_SORT_PREFERENCE_CHANGED)
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
    }

    companion object {
        const val BROADCAST_SORT_PREFERENCE_CHANGED = "SortPreferenceChanged"

        const val TAG = "SortingDialogFragment"
    }
}