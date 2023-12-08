package com.dicoding.scancare.ui.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.scancare.R
import com.dicoding.scancare.data.DummyData
import com.dicoding.scancare.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView: RecyclerView = findViewById(R.id.rvIngredienst)
        val productName = intent.getStringExtra("productName")

        binding.textView.text = productName
        val ingredientsList = DummyData.ingredientsList

        if (!ingredientsList.isNullOrEmpty()) {
            val adapter = IngredientsAdapter(ingredientsList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }

    }
}