package com.fuh.markinbook.screens.userscreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.fuh.markinbook.PreferencesManager
import com.fuh.markinbook.R
import kotlinx.android.synthetic.main.user_fragment.*
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class UserFragment : Fragment(R.layout.user_fragment) {

    private val viewModel: UserViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var currentPhotoCameraPath: String
    private lateinit var currentPhotoGalleryPath: String

    private val startForResultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                if (uri != null) {
                    compressAndSetImage(uri, userPhotoImageView, FILE_FOR_GALLERY)
                    viewModel.addProfileImage(currentPhotoGalleryPath)
                }
            }
        }
    private val startForResultCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageFile = File(currentPhotoCameraPath)
                if (imageFile.exists()) {
                    val contentUri = Uri.fromFile(imageFile)
                    compressAndSetImage(contentUri, userPhotoImageView, FILE_FOR_CAMERA)
                    viewModel.addProfileImage(currentPhotoCameraPath)
                }
            }
        }
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {map->
            map.entries.forEach {
                Timber.i("${it.key} = ${it.value}")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController =
            Navigation.findNavController(requireActivity(), R.id.navigation_host_fragment)
        viewModel.loadingLiveData.observe(viewLifecycleOwner){
                userProgressFrame.isVisible = it
        }
        viewModel.studentLiveData.observe(viewLifecycleOwner) {
            userFirstNameTextView.text = it.firstName
            userLastNameTextView.text = it.lastName
        }
        viewModel.schoolLiveData.observe(viewLifecycleOwner) {
            userSchoolTextView.text = it.title
        }
        viewModel.groupLiveData.observe(viewLifecycleOwner) {
            userGroupTextView.text = it.title
        }
        viewModel.profileImageLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide
                    .with(this)
                    .load(it)
                    .into(userPhotoImageView)

            }
        }
        userEmailTextView.text = PreferencesManager.email

        userChangePhotoButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission.launch(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    )
                )
            } else {
                val camera = requireContext().getString(R.string.camera)
                val gallery = requireContext().getString(R.string.gallery)
                val options = arrayOf(camera, gallery)
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle(requireContext().getString(R.string.choose_app))
                    .setItems(options) { _, which ->
                        if (options[which] == camera) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            val photoFile = createImageFile(FILE_FOR_CAMERA)
                            val fileProvider = FileProvider.getUriForFile(
                                requireContext(),
                                "com.fuh.markinbook.android.fileprovider",
                                photoFile
                            )
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                            if (intent.resolveActivity(requireContext().packageManager) != null) {
                                startForResultCamera.launch(intent)
                            }
                        } else {
                            val intent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startForResultGallery.launch(intent)
                        }
                    }
                builder.show()
            }
        }
    }

    private fun compressAndSetImage(uri: Uri, view: ImageView, type: String) {
        val image = context?.contentResolver?.openInputStream(uri);
        val bitmap = BitmapFactory.decodeStream(image)
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out)
        val decoded =
            BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
        userPhotoImageView.setImageBitmap(decoded)
        createImageFile(type)
        val file = if (type == FILE_FOR_CAMERA) {
            File(currentPhotoCameraPath)
        } else {
            File(currentPhotoGalleryPath)
        }
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos)
        fos.close()
        view.setImageBitmap(bitmap)
    }

    @Throws(IOException::class)
    private fun createImageFile(type: String): File {

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        if (type == FILE_FOR_CAMERA) {
            currentPhotoCameraPath = image.absolutePath
        } else {
            currentPhotoGalleryPath = image.absolutePath
        }
        return image
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 11
        private const val FILE_FOR_CAMERA = "file_for_camera"
        private const val FILE_FOR_GALLERY = "file_for_gallery"
    }
}