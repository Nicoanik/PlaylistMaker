package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.track_view, parent, false)) {

    private val trackNameView: TextView = itemView.findViewById(R.id.track_name)
    private val artistNameView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeView: TextView = itemView.findViewById(R.id.tv_track_time)
    private val artworkUrl100View: ImageView = itemView.findViewById(R.id.album_cover)

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = timeConversion(model.trackTime)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.album_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2, itemView.context)))
            .into(artworkUrl100View)
    }

    private fun timeConversion(time: Long?) : String {
        if (time == null) return ""
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}