//package com.dicoding.scancare.ui
//
//import android.content.Intent
//import android.net.Uri
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import com.dicoding.scancare.databinding.ActivityDummyHomeBinding
//import com.dicoding.scancare.ui.scan.ScanImageActivity
//import com.dicoding.scancare.utils.getImageUri
//
//class DummyHomeActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityDummyHomeBinding
//    private var currentImageUri: Uri? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDummyHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.galleryButton.setOnClickListener {
//            startGallery()
//        }
//
//        binding.cameraButton.setOnClickListener {
//            startCamera()
//        }
//    }
//
//    private fun startGallery() {
//        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }
//
//    private val launcherGallery = registerForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            currentImageUri = uri
//            val intent = Intent(this, ScanImageActivity::class.java)
//            intent.putExtra("imageUri", uri.toString())
//            startActivity(intent)
//        } else {
//            Log.d("Photo Picker", "No media selected")
//        }
//    }
//
//    private fun startCamera() {
//        currentImageUri = getImageUri(this)
//        launcherIntentCamera.launch(currentImageUri)
//    }
//
//    private val launcherIntentCamera = registerForActivityResult(
//        ActivityResultContracts.TakePicture()
//    ) { isSuccess ->
//        if (isSuccess) {
//            val intent = Intent(this, ScanImageActivity::class.java)
//            intent.putExtra("imageUri", currentImageUri.toString())
//            startActivity(intent)
//        }
//    }
//
//}