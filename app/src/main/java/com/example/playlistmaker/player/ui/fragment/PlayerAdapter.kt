package com.example.playlistmaker.player.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.AddToPlaylistViewBinding
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.ui.fragment.PlaylistAdapter.OnItemClickListener

class PlayerAdapter(
    val onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<PlayerViewHolder>() {

    val playlists: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerViewHolder {
        val binding = AddToPlaylistViewBinding.inflate(LayoutInflater.from(parent.context))
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlayerViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size
}
