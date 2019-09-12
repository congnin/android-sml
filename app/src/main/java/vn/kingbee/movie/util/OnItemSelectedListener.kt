package vn.kingbee.movie.util

import vn.kingbee.movie.model.Movie

interface OnItemSelectedListener {
    fun onItemSelected(movie: Movie)
}