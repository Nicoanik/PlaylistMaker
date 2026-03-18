package com.example.playlistmaker.player.ui.fragment

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.player.ui.view_model.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.media.domain.models.dpToPx
import com.example.playlistmaker.media.domain.models.timeConversion
import com.example.playlistmaker.services.MusicService
import com.example.playlistmaker.utils.ConnectedBroadcastReceiver
import com.example.playlistmaker.utils.antiRepetition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private lateinit var viewModel: PlayerViewModel

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private lateinit var adapter: PlayerAdapter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val track by lazy {
        arguments?.getParcelable<Track>(ARGS_TRACK) ?: error("Track is required!")
    }

    private val receiver by lazy { ConnectedBroadcastReceiver() }
    private val filter = IntentFilter().apply {
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        addAction("android.net.conn.CONNECTIVITY_CHANGE")
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(
                requireContext(),
                R.string.no_permission,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel { parametersOf(track) }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {
                when (p1) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(p0: View, p1: Float) {
                binding.overlay.alpha = (p1 + 1f) / 2f
            }
        })

        onPlaylistClickDebounce = antiRepetition(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope
        ) { playlist ->
            viewModel.addTrackToPlaylist(playlist)
        }

        adapter = PlayerAdapter { playlist -> onPlaylistClickDebounce(playlist) }
        binding.rvAddToPlaylist.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvAddToPlaylist.adapter = adapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_cover_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(dpToPx(8, requireContext()))
            )
            .into(binding.ivAlbumCover)
        binding.apply {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvTrackTime.text = timeConversion(track.trackTime)
            tvCollectionName.text = track.collectionName
            tvReleaseDate.text = track.releaseDate?.substring(0, 4)
            tvPrimaryGenreName.text = track.primaryGenreName
            tvCountry.text = track.country
        }

        binding.backButtonAudioPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.queueButton.setOnClickListener {
            viewModel.getPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_createPlaylist)
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        binding.favoriteButton.setOnClickListener {
            viewModel.onFavoriteButton()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        viewModel.onResume()
        viewModel.stopForegroundService()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        requireActivity().registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        viewModel.startForegroundService()
        requireActivity().unregisterReceiver(receiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindMusicService()
        _binding = null
    }

    private fun render(state: PlayerState?) {
        when (state) {
            is PlayerState.Prepared -> {
                binding.playButton.setImage(false)
                binding.playButton.isVisible = true
                binding.tvPlaybackProgress.text = PLAYBACK_DEF
            }

            is PlayerState.Playing -> {
                binding.playButton.setImage(true)
                binding.playButton.isVisible = true
                binding.tvPlaybackProgress.text = state.progress
            }

            is PlayerState.Paused -> {
                binding.playButton.setImage(false)
                binding.playButton.isVisible = true
                binding.tvPlaybackProgress.text = state.progress
            }

            is PlayerState.IsFavorite -> {
                when (state.isFavorite) {
                    true -> binding.favoriteButton.setImageResource(R.drawable.button_favorite_true_51)
                    false -> binding.favoriteButton.setImageResource(R.drawable.button_favorite_false_51)
                }
            }

            is PlayerState.InPlaylist -> {
                when (state.inPlaylist) {
                    false -> Toast.makeText(
                        requireContext(),
                        "Трек уже добавлен в плейлист ${state.title}",
                        Toast.LENGTH_LONG
                    ).show()

                    true -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        Toast.makeText(
                            requireContext(),
                            "Добавлено в плейлист ${state.title}",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }

            is PlayerState.BottomSheetContent -> {
                if (state.playlists.isNotEmpty()) {
                    adapter.playlists.clear()
                    adapter.playlists.addAll(state.playlists)
                    adapter.notifyDataSetChanged()
                }
            }

            else -> {}
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            putExtra("track", track)
        }

        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }

    companion object {

        const val PLAYBACK_DEF = "00:00"
        private const val ARGS_TRACK = "track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(track: Track) = Bundle().apply { putParcelable(ARGS_TRACK, track) }
    }

}