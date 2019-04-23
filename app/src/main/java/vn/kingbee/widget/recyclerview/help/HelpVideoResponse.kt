package vn.kingbee.widget.recyclerview.help

import com.google.gson.annotations.SerializedName

class HelpVideoResponse {
    @SerializedName("title")
    private var mHelpTitle: String? = null
    @SerializedName("instruction_video")
    private var mInstructionVideo: List<HelpVideo>? = null

    fun getHelpTitle(): String? {
        return mHelpTitle
    }

    fun setHelpTitle(helpTitle: String) {
        mHelpTitle = helpTitle
    }

    fun getInstructionVideo(): List<HelpVideo>? {
        return mInstructionVideo
    }

    fun setInstructionVideo(instructionVideo: List<HelpVideo>) {
        mInstructionVideo = instructionVideo
    }
}