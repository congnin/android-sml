package vn.kingbee.movie.model

import androidx.annotation.NonNull

enum class Sort private constructor(private val value: String) {

    MOST_POPULAR("popularity.desc"),
    HIGHEST_RATED("vote_average.desc"),
    MOST_RATED("vote_count.desc");

    override fun toString(): String {
        return value
    }

    companion object {

        fun fromString(@NonNull string: String): Sort {
            for (sort in values()) {
                if (string == sort.toString()) {
                    return sort
                }
            }
            throw IllegalArgumentException("No constant with text $string found.")
        }
    }

}