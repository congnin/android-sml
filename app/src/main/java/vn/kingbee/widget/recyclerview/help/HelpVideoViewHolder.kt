package vn.kingbee.widget.recyclerview.help

import android.content.Context
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import org.apache.commons.lang3.StringUtils
import timber.log.Timber
import vn.kingbee.utils.FileUtils
import vn.kingbee.widget.R
import vn.kingbee.widget.recyclerview.base.BaseRecyclerViewAdapter

class HelpVideoViewHolder : BaseRecyclerViewAdapter.GenericViewHolder {
    @BindView(R.id.help_video_thumb)
    lateinit var ivThumb: ImageView
    @BindView(R.id.help_video_icon_play)
    lateinit var ivPlay: ImageView
    @BindView(R.id.help_video_item_title)
    lateinit var tvTitle: TextView
    @BindView(R.id.help_video_item_description)
    lateinit var tvDescription: TextView
    private var mContext: Context
    private var mListener: HelpVideoAdapter.HelpVideoClickListener

    constructor(context: Context,
                itemView: View,
                listener: HelpVideoAdapter.HelpVideoClickListener) : super(itemView) {

        mContext = context
        mListener = listener
    }

    override fun bindItem(position: Int, item: Any) {
        item as HelpVideo
        ivThumb.tag = item.path
        ivPlay.tag = item.path
        displayThumbBitmap(mContext, item, ivThumb)
        tvTitle.text = item.title
        tvDescription.text = item.description
    }

    private fun displayThumbBitmap(context: Context, item: HelpVideo, imageView: ImageView) {
        if (!StringUtils.isEmpty(item.thumb)) {
            Timber.d("VIDEO", "VIDEO FORM RESOURCE")
            val id = getResourceThumbnails(item.path!!)
            if (id != 0) {
                //if sdcard not exist thumb, get from resource.
                imageView.setImageResource(id)
            } else {
                imageView.setImageBitmap(
                    ThumbnailUtils.createVideoThumbnail(
                        FileUtils.getDownloadLocalPath(item.path!!),
                        MediaStore.Video.Thumbnails.MINI_KIND
                    )
                )
            }
        }
    }

    private fun getResourceThumbnails(path: String): Int {
        var id = 0
        if (VIDEO_FILE_NAME_ID_BOOK.equals(path, ignoreCase = true)) {
            id = R.mipmap.thumb4
        } else if (VIDEO_FILE_NAME_ID_CARD.equals(path, ignoreCase = true)) {
            id = R.mipmap.thumb5
        } else if (VIDEO_FILE_NAME_SMART_SHOPPER_CARD.equals(path, ignoreCase = true)) {
            id = R.mipmap.thumb6
        } else if (VIDEO_FILE_NAME_FINGER_PRINT.equals(path, ignoreCase = true)) {
            id = R.mipmap.thumb3
        } else if (VIDEO_FILE_NAME_PROOF_OF_RESIDENCE.equals(path, ignoreCase = true)) {
            id = R.mipmap.thumb2
        }
        return id
    }

    @OnClick(R.id.help_video_thumb, R.id.help_video_icon_play)
    fun onClick(view: View) {
        when (view.id) {
            R.id.help_video_thumb, R.id.help_video_icon_play -> mListener.onVideoClick(view.tag as String)
            else -> {
            }
        }
    }

    companion object {
        // Video file path
        val VIDEO_FILE_NAME_ID_BOOK = "id_book.mp4"
        val VIDEO_FILE_NAME_ID_CARD = "id_card.mp4"
        val VIDEO_FILE_NAME_SMART_SHOPPER_CARD = "ss_card.mp4"
        val VIDEO_FILE_NAME_FINGER_PRINT = "fingerprint.mp4"
        val VIDEO_FILE_NAME_PROOF_OF_RESIDENCE = "proof_of_residence.mp4"
    }
}