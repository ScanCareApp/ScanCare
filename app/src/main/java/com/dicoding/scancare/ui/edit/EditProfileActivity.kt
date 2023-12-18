package com.dicoding.scancare.ui.edit

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.dicoding.scancare.R
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.data.remote.response.PutUserProfileResponse
import com.dicoding.scancare.databinding.ActivityEditProfileBinding
import com.dicoding.scancare.ui.register.UserViewModel
import com.dicoding.scancare.ui.scan.CameraActivity
import com.dicoding.scancare.utils.reduceFileImage
import com.dicoding.scancare.utils.uriToFile
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Suppress("DEPRECATION")
class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var currentImageUri: Uri? = null
    private var previousImageUri: Uri? = null
    private var id: String = ""


    private val userViewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.getSession().observe(this) { user ->
            id = user.userId
            displayUserProfile(id)
        }

        binding.picCamera.setOnClickListener {
            showDialog()
        }
        binding.btnSave.setOnClickListener {
            val usn = binding.edUsername
            val address = binding.edAddress
            Log.d("Image URI", "Value of currentImageUri: $currentImageUri")
            updateProfile(id, usn, address)
        }
        binding.fabBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun updateProfile(id: String, usn: TextInputEditText, address: TextInputEditText) {
        binding.progressBar.visibility = View.VISIBLE

        if (currentImageUri != null) {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                val username = usn.text.toString().toRequestBody("text/plain".toMediaType())
                val addr = address.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestImageFile
                )
                userViewModel.updateProfile(id, username, addr, imgMultipart)
                    .observe(this) { result ->
                        handleResult(result)
                    }
            }
        } else {
            val username = usn.text.toString().toRequestBody("text/plain".toMediaType())
            val addr = address.text.toString().toRequestBody("text/plain".toMediaType())
            userViewModel.updateProfile(id, username, addr)
                .observe(this) { result ->
                    handleResult(result)
                }
        }
    }

    private fun handleResult(result: ResultState<PutUserProfileResponse>) {
        when (result) {
            is ResultState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is ResultState.Success -> {
                binding.progressBar.visibility = View.GONE
                val response = result.data
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                finish()
            }

            is ResultState.Error -> {
                val error = result.error
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }


    private fun displayUserProfile(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        userViewModel.getUserProfile(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE

                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val userProfile = result.data
                    previousImageUri = Uri.parse(userProfile.photo)
                    binding.apply {
                        edUsername.setText(userProfile.username)
                        edAddress.setText(userProfile.address)
                        edEmail.setText(userProfile.email)
                    }
                    Glide.with(this)
                        .load(userProfile.photo)
                        .placeholder(R.drawable.blank_profile)
                        .into(binding.imgUser)
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val errorMessage = result.error
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.button_sheet_layout)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        val chooseFromCamera: ImageView = dialog.findViewById(R.id.chooseFromCamera)
        val chooseFromGallery: ImageView = dialog.findViewById(R.id.chooseFromGallery)

        chooseFromCamera.setOnClickListener {
            dialog.dismiss()
            startCamera()
        }

        chooseFromGallery.setOnClickListener {
            dialog.dismiss()
            startGallery()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            Log.d("Image URI", "Selected from Gallery: $currentImageUri")
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgUser.setImageURI(it)
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}

