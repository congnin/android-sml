package vn.kingbee.movie.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*


class MovieVideo : Parcelable {

    @SerializedName("id")
    var videoId: String? = null
    @SerializedName("iso_639_1")
    var languageCode: String? = null
    @SerializedName("iso_3166_1")
    var countryCode: String? = null
    @SerializedName("key")
    var key: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("site")
    var site: String? = null
    @SerializedName("size")
    var size: Int = 0
    @SerializedName("type")
    var type: String? = null

    val isYoutubeVideo: Boolean
        get() = site!!.toLowerCase(Locale.US) == SITE_YOUTUBE.toLowerCase(Locale.US)

    constructor(videoId: String) {
        this.videoId = videoId
    }

    protected constructor(`in`: Parcel) {
        this.videoId = `in`.readString()
        this.languageCode = `in`.readString()
        this.countryCode = `in`.readString()
        this.key = `in`.readString()
        this.name = `in`.readString()
        this.site = `in`.readString()
        this.size = `in`.readInt()
        this.type = `in`.readString()
    }

    //CHECKSTYLE:OFF
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val video = other as MovieVideo?

        if (size != video!!.size) return false
        if (if (videoId != null) videoId != video.videoId else video.videoId != null) return false
        if (if (languageCode != null) languageCode != video.languageCode else video.languageCode != null) return false
        if (if (countryCode != null) countryCode != video.countryCode else video.countryCode != null) return false
        if (if (key != null) key != video.key else video.key != null) return false
        if (if (name != null) name != video.name else video.name != null) return false
        if (if (site != null) site != video.site else video.site != null) return false
        return if (type != null) type == video.type else video.type == null

    }

    override fun hashCode(): Int {
        var result = if (videoId != null) videoId!!.hashCode() else 0
        result = 31 * result + if (languageCode != null) languageCode!!.hashCode() else 0
        result = 31 * result + if (countryCode != null) countryCode!!.hashCode() else 0
        result = 31 * result + if (key != null) key!!.hashCode() else 0
        result = 31 * result + if (name != null) name!!.hashCode() else 0
        result = 31 * result + if (site != null) site!!.hashCode() else 0
        result = 31 * result + size
        result = 31 * result + if (type != null) type!!.hashCode() else 0
        return result
    }
    //CHECKSTYLE:ON

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.videoId)
        dest.writeString(this.languageCode)
        dest.writeString(this.countryCode)
        dest.writeString(this.key)
        dest.writeString(this.name)
        dest.writeString(this.site)
        dest.writeInt(this.size)
        dest.writeString(this.type)
    }

    companion object {

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<MovieVideo> = object : Parcelable.Creator<MovieVideo> {
            override fun createFromParcel(source: Parcel): MovieVideo {
                return MovieVideo(source)
            }

            override fun newArray(size: Int): Array<MovieVideo?> {
                return arrayOfNulls(size)
            }
        }

        private const val SITE_YOUTUBE = "YouTube"
    }

}