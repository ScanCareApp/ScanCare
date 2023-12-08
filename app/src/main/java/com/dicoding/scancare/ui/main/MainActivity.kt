package com.dicoding.scancare.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.scancare.HistoryItem
import com.dicoding.scancare.databinding.ActivityMainBinding
import com.dicoding.scancare.ui.HistoryAdapter
import com.dicoding.scancare.ui.detail.DetailActivity
import com.dicoding.scancare.ui.result.ResultActivity
import com.dicoding.scancare.ui.scan.ScanImageActivity
import com.dicoding.scancare.utils.getImageUri

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null

    private val historyList = mutableListOf<HistoryItem>()
    private lateinit var historyAdapter: HistoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        // mengatur adapter untuk history
        historyAdapter = HistoryAdapter(historyList) { historyItem ->
            navigateToResultActivity(historyItem)
        }
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistory.adapter = historyAdapter

        // data dummy history
        historyList.add(HistoryItem("Skintific Pure Centella Acne Calming Toner", "Aplha Hydroxy Acids, Retinoids", "2023-01-01", "12:00 PM"))

        // memberitahu adapter data sudah dibah
        historyAdapter.notifyDataSetChanged()



    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun navigateToResultActivity(historyItem: HistoryItem) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("HISTORY_NAMA_PRODUK", historyItem.namaProduk)
            putExtra("HISTORY_INGREDIENTS", historyItem.ingredients)
            putExtra("HISTORY_DATE", historyItem.date)
            putExtra("HISTORY_TIME", historyItem.time)
        }
        startActivity(intent)
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

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            val intent = Intent(this, ScanImageActivity::class.java)
            intent.putExtra("imageUri", currentImageUri.toString())
            startActivity(intent)
        }
    }
}