package com.dicoding.scancare.ui.result

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
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

        binding.fabBack.setOnClickListener {
            onBackPressed()
        }

        binding.textView.text = productDetails?.productName
        binding.tvNoBpomResult.text = productDetails?.noBPOM

        val noBPOM = productDetails?.noBPOM
        val illegalKeywords = listOf("NOT", "NOTREGIS")
        val isIllegal = illegalKeywords.any { keyword ->
            noBPOM!!.startsWith(keyword, ignoreCase = true)
        }

        if (isIllegal) {
            binding.tvBpomStatusResult.text = getString(R.string.illegal)
            binding.tvBpomStatusResult.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_close_24, 0, 0, 0)
            binding.shapeView.background.setColorFilter(ContextCompat.getColor(this@ResultActivity, R.color.red), PorterDuff.Mode.SRC_ATOP)
        } else {
            binding.tvBpomStatusResult.text = getString(R.string.bpom_certified)
            binding.tvBpomStatusResult.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_check_circle_24, 0, 0, 0)
        }

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