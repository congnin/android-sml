package vn.kingbee.widget.recyclerview

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import io.reactivex.Observable
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.utils.FileUtils
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.recyclerview.help.HelpVideoAdapter
import vn.kingbee.widget.recyclerview.help.HelpVideoResponse

class HelpVideoDemo : BaseActivity(), HelpVideoAdapter.HelpVideoClickListener {
    override fun onVideoClick(path: String) {
        Toast.makeText(this, path, Toast.LENGTH_LONG).show()
    }

    lateinit var mTvTitle: TextView
    lateinit var helpVideoList: androidx.recyclerview.widget.RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycleview_demo)

        helpVideoList = findViewById(R.id.help_video_list)
        mTvTitle = findViewById(R.id.help_video_title)
        helpVideoList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        showProgressDialog()
        getHelpVideoFromResource().subscribe({ helpInfo -> updateVideoList(helpInfo) }, { e ->
            hideProgressDialog()
            Timber.d("error load help video: " + e.message)
        }, { hideProgressDialog() })
    }

    private fun getHelpVideoFromResource(): Observable<HelpVideoResponse> {
        return FileUtils.getHelpVideoFromResource(MyApp.getInstance())
    }

    private fun updateVideoList(info: HelpVideoResponse?) {
        if (info != null) {
            mTvTitle.text = info.getHelpTitle()

            //list.
            val adapter = HelpVideoAdapter(this, info.getInstructionVideo()!!, this)
            helpVideoList.adapter = adapter
        }
    }
}