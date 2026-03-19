package com.example.playlistmaker.ui.fragments.media

import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.domain.models.dpToPx
import com.example.playlistmaker.presentation.media.CreatePlaylistViewModel
import com.example.playlistmaker.ui.activity.RootActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistFragment : Fragment() {

    open val viewModel: CreatePlaylistViewModel by viewModel()

    private var _binding: FragmentCreatePlaylistBinding? = null
    open val binding get() = _binding!!

    var coverUri: Uri? = null

    private lateinit var listener: ViewTreeObserver.OnGlobalLayoutListener

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(requireContext())
                    .load(uri)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(dpToPx(8, requireContext()))
                    )
                    .into(binding.ivPlaylistCover)
                coverUri = uri
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as RootActivity).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val paramsCover = binding.ivPlaylistCover.layoutParams as ConstraintLayout.LayoutParams
        val paramsTitle = binding.inputLayoutTitle.layoutParams as ConstraintLayout.LayoutParams
        val paramsDescription = binding.inputLayoutDescription.layoutParams as ConstraintLayout.LayoutParams

        listener = ViewTreeObserver.OnGlobalLayoutListener {
            _binding?.let {
                val rect = Rect()
                binding.root.getWindowVisibleDisplayFrame(rect)
                val screenHeight = binding.root.rootView.height
                val keyboardHeight = screenHeight - rect.bottom
                if (keyboardHeight > screenHeight * 0.15) {
                    paramsCover.topToTop = ConstraintLayout.LayoutParams.UNSET
                    paramsCover.bottomToTop = binding.inputLayoutTitle.id
                    binding.ivPlaylistCover.layoutParams = paramsCover

                    paramsTitle.topToBottom = ConstraintLayout.LayoutParams.UNSET
                    paramsTitle.bottomToTop = binding.inputLayoutDescription.id
                    binding.inputLayoutTitle.layoutParams = paramsTitle

                    paramsDescription.topToBottom = ConstraintLayout.LayoutParams.UNSET
                    paramsDescription.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                    binding.inputLayoutDescription.layoutParams = paramsDescription

                    binding.buttonCreate.isVisible = false
                } else {
                    paramsCover.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    paramsCover.bottomToTop = ConstraintLayout.LayoutParams.UNSET
                    binding.ivPlaylistCover.layoutParams = paramsCover

                    paramsTitle.topToBottom = binding.ivPlaylistCover.id
                    paramsTitle.bottomToTop = ConstraintLayout.LayoutParams.UNSET
                    binding.inputLayoutTitle.layoutParams = paramsTitle

                    paramsDescription.topToBottom = binding.inputLayoutTitle.id
                    paramsDescription.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                    binding.inputLayoutDescription.layoutParams = paramsDescription

                    binding.buttonCreate.isVisible = true
                }
            }
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(listener)

        binding.apply {
            backButton.setOnClickListener {
                backNavigate()
            }

            ivPlaylistCover.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            etTitle.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            etTitle.addTextChangedListener { text ->
                binding.buttonCreate.isEnabled = !text.isNullOrEmpty()
            }

            etDescription.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

            buttonCreate.setOnClickListener {
                viewModel.createPlaylist(
                    binding.etTitle.text.toString().trim(),
                    binding.etDescription.text?.toString()?.trim(),
                    coverUri
                )
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${binding.etTitle.text} создан",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backNavigate()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as RootActivity).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        _binding = null
    }

    private fun backNavigate() {
        if ((coverUri != null) or !binding.etTitle.text.isNullOrEmpty() or !binding.etDescription.text.isNullOrEmpty()) {
            showDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.finish_create_playlist)
            .setMessage(R.string.data_will_be_to_lost)
            .setNeutralButton(R.string.cancel) { dialog, which -> }
            .setPositiveButton(R.string.complete) { dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }

}