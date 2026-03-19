package com.example.playlistmaker.ui.fragments.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AddToPlaylistViewBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.dpToPx

class PlayerViewHolder(
    private val binding: AddToPlaylistViewBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {

        Glide.with(itemView)
            .load(playlist.coverPath)
            .placeholder(R.drawable.album_cover_placeholder)
            .error(R.drawable.album_cover_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(dpToPx(2, itemView.context))
            )
            .into(binding.ivPlaylistCover)
        binding.tvPlaylistTitle.text = playlist.title
        binding.tvPlaylistSize.text = itemView.resources.getQuantityString(
            R.plurals.playlist_size,
            playlist.playlistSize,
            playlist.playlistSize
        )

    }

}