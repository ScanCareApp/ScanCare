package com.dicoding.scancare.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.scancare.R
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.databinding.ActivityMainBinding
import com.dicoding.scancare.ui.edit.EditProfileActivity
import com.dicoding.scancare.ui.scan.CameraActivity
import com.dicoding.scancare.ui.scan.PredictViewModel
import com.dicoding.scancare.ui.scan.ScanImageActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(application)
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        val recyclerView: RecyclerView = findViewById(R.id.rvHistory)
        val adapter = HistoryAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnCamera.setOnClickListener {
            startCamera()
        }
        binding.edtProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        viewModel.scannedProducts.observe(this) { products ->
            adapter.updateHistory(products) // Update daftar produk pada adapter
        }
        viewModel.getAllScannedProducts()

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
            val intent = Intent(this, ScanImageActivity::class.java)
            intent.putExtra("imageUri", uri.toString())
            startActivity(intent)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            val intent = Intent(this, ScanImageActivity::class.java)
            intent.putExtra("imageUri", currentImageUri.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllScannedProducts()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}