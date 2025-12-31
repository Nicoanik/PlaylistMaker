package com.example.playlistmaker.media.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media.ui.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.search.domain.models.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {

    private val viewModel: CreatePlaylistViewModel by viewModel()

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireContext())
                        .load(uri)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(dpToPx(8, requireContext()))
                        )
                        .into(binding.ivPlaylistCover)
                }
            }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivPlaylistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.etTitle.addTextChangedListener { text ->
            binding.buttonCreate.isEnabled = !text.isNullOrEmpty()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun saveImageToPrivateStorage(uri: Uri) {
//        //создаём экземпляр класса File, который указывает на нужный каталог
//        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myplaylists")
//        //создаем каталог, если он не создан
//        if (!filePath.exists()){
//            filePath.mkdirs()
//        }
//        //создаём экземпляр класса File, который указывает на файл внутри каталога
//        val file = File(filePath, "first_cover.jpg")
//        // создаём входящий поток байтов из выбранной картинки
//        val inputStream = contentResolver.openInputStream(uri)
//        // создаём исходящий поток байтов в созданный выше файл
//        val outputStream = FileOutputStream(file)
//        // записываем картинку с помощью BitmapFactory
//        BitmapFactory
//            .decodeStream(inputStream)
//            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
//    }
}
