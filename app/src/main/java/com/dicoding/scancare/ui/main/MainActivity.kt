package com.dicoding.scancare.ui.main

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.scancare.R
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.databinding.ActivityMainBinding
import com.dicoding.scancare.ui.edit.EditProfileActivity
import com.dicoding.scancare.ui.register.UserViewModel
import com.dicoding.scancare.ui.scan.CameraActivity
import com.dicoding.scancare.ui.scan.PredictViewModel
import com.dicoding.scancare.ui.scan.ScanImageActivity
import com.dicoding.scancare.ui.welcome.WelcomeActivity
import com.dicoding.scancare.utils.DateConverter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private var id: String = ""
    private val viewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val userViewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(this)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.getSession().observe(this) { user ->
            id = user.userId
            displayUserProfile(id)
        }

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
        binding.logout.setOnClickListener {
            showDialogLogout()
        }

        viewModel.scannedProducts.observe(this) { products ->
            val groupedHistory = products.groupBy { it.productName }

            val uniqueHistory = groupedHistory.map { (_, value) ->
                value.first()
            }
            adapter.updateHistory(uniqueHistory)
        }
        viewModel.getAllScannedProducts(id)

    }

    private fun showDialogLogout() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_logout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val btnNo: Button = dialog.findViewById(R.id.btnNo)

        btnYes.setOnClickListener {
            userViewModel.logout()
            Toast.makeText(this, "You're logging out", Toast.LENGTH_LONG).show()
            val loginActivity = Intent(this@MainActivity, WelcomeActivity::class.java)
            startActivity(loginActivity)
            finish()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayUserProfile(id: String) {
        userViewModel.getUserProfile(id).observe(this){result->
            binding.progressBar.visibility = View.VISIBLE
            when(result){
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val userProfile = result.data
                    Log.d("Response Data", result.data.toString())
                    binding.apply {
                        tvUsername.text = userProfile.username
                        username.text = userProfile.username
                        address.text = userProfile.address
                        createAt.text = DateConverter(userProfile.creationDate)
                    }
                    Glide.with(this)
                        .load(userProfile.photo)
                        .placeholder(R.drawable.blank_profile)
                        .into(binding.imgUser)
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

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
            Log.d("Image URI", "Selected from Gallery: $currentImageUri")
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
        viewModel.getAllScannedProducts(id)
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}