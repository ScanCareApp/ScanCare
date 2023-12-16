package com.dicoding.scancare.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.scancare.R
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.databinding.ActivityHistoryBinding
import com.dicoding.scancare.ui.scan.PredictViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityHistoryBinding
    private val viewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabBack.setOnClickListener {
            onBackPressed()
        }
        val noBPOM = intent.getStringExtra("NOBPOM")
        viewModel.getProductAndIngredientsByNoBPOM(noBPOM ?: "")

        val recyclerView: RecyclerView = findViewById(R.id.rvIngredienst)
        val adapter = IngredientsHistoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        viewModel.selectedProduct.observe(this) { product ->
            binding.apply {
                textView.text = product.productName
                tvNoBpomResult.text = product.noBPOM
            }
        }

        viewModel.selectedIngredients.observe(this) { ingredients ->
            adapter.submitList(ingredients)
        }
    }
}