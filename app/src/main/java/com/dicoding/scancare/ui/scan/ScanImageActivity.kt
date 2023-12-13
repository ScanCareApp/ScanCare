package com.dicoding.scancare.ui.scan

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.databinding.ActivityScanImageBinding
import com.dicoding.scancare.ui.result.ResultActivity
import com.dicoding.scancare.utils.reduceFileImage
import com.dicoding.scancare.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ScanImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanImageBinding

    private val viewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("imageUri")
        imageUri = Uri.parse(imageUriString)

        binding.photo.setImageURI(imageUri)
        binding.btnUpload.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            uploadImage()
        }
    }

    private fun uploadImage() {
        binding.progressBar.visibility = View.VISIBLE
        imageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            viewModel.predictImage(imgMultipart).observe(this) { result ->
                binding.progressBar.visibility = View.VISIBLE
                when (result) {
                    is ResultState.Loading -> {
                        // Anda mungkin ingin menampilkan progress di sini jika diperlukan
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        // Proses respons yang berhasil
                        binding.progressBar.visibility = View.GONE // Sembunyikan progress bar
                        Log.d("Response Data", result.data.toString())
                        // Lakukan tindakan setelah menerima respons yang berhasil, misalnya navigasi ke halaman lain
                        val intent = Intent(this@ScanImageActivity, ResultActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is ResultState.Error -> {
                        // Menangani kesalahan
                        binding.progressBar.visibility = View.GONE // Sembunyikan progress bar
                        Toast.makeText(this@ScanImageActivity, result.error, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}