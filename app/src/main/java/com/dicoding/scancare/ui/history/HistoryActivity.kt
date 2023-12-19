package com.dicoding.scancare.ui.history

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.scancare.R
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.databinding.ActivityHistoryBinding
import com.dicoding.scancare.ui.scan.PredictViewModel

@Suppress("DEPRECATION")
class HistoryActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityHistoryBinding
    private val viewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(application)
    }

    @SuppressLint("ClickableViewAccessibility")
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

            val noBPOM = product?.noBPOM
            val illegalKeywords = listOf("NOT", "NOTREGIS")
            val isIllegal = illegalKeywords.any { keyword ->
                noBPOM!!.startsWith(keyword, ignoreCase = true)
            }

            if (isIllegal) {
               binding.tvBpomStatusResult.text = getString(R.string.illegal)
                binding.tvBpomStatusResult.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_close_24, 0, 0, 0)
                binding.shapeView.background.setColorFilter(ContextCompat.getColor(this@HistoryActivity, R.color.red), PorterDuff.Mode.SRC_ATOP)
            } else {
                binding.tvBpomStatusResult.text = getString(R.string.bpom_certified)
            }


            Glide.with(this)
                .load(product?.imageUrl)
                .placeholder(R.drawable.image_example)
                .into(binding.resultImage)
        }

        viewModel.selectedIngredients.observe(this) { ingredients ->
            val groupedIngredients = ingredients.groupBy { it.nameIngredients }

            val uniqueIngredients = groupedIngredients.map { (_, value) ->
                value.first()
            }

            adapter.submitList(uniqueIngredients)
        }
    }
}
