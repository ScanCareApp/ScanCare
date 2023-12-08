package com.dicoding.scancare.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.scancare.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val productName = intent.getStringExtra("INGREDIENT_NAME")
        val function = intent.getStringExtra("INGREDIENT_FUNCTION")

        binding.apply {
            tvIngredientName.text = productName
            tvIngredientFunction.text = function
        }
    }
}