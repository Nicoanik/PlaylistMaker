package com.example.playlistmaker.search.domain.models

import android.content.Context
import android.util.TypedValue
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String?,
    val country: String,
    val artworkUrl100: String,
    val previewUrl: String
)

fun timeConversion(time: Long?) : String {
    if (time == null) return ""
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}

fun dpToPx(dp: Int, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}
