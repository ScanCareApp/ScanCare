package com.dicoding.scancare.ui.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.scancare.R
import com.dicoding.scancare.data.remote.ScanCareDataHolder
import com.dicoding.scancare.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView: RecyclerView = findViewById(R.id.rvIngredienst)

        val ingredientsList = ScanCareDataHolder.getIngredients()?.filterNotNull()
        val productDetails = ScanCareDataHolder.getProductDetails()

        binding.textView.text = productDetails?.productName
        binding.tvNoBpomResult.text = productDetails?.noBPOM
        Glide.with(this)
            .load(productDetails?.imageUrl)
            .placeholder(R.drawable.image_example)
            .into(binding.resultImage)

        if (!ingredientsList.isNullOrEmpty()) {
            val adapter = IngredientsAdapter(ingredientsList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }

    }
}