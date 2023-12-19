package com.dicoding.scancare.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.scancare.data.database.ProductEntity
import com.dicoding.scancare.databinding.ItemRowBinding
import com.dicoding.scancare.ui.history.HistoryActivity

class HistoryAdapter(
    private var history: List<ProductEntity>,
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){


    class ViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(productEntity: ProductEntity){
                binding.apply {
                    tvNamaProduk.text = productEntity.productName
                }
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, HistoryActivity::class.java)
                    intent.putExtra("NOBPOM", productEntity.noBPOM)
                    itemView.context.startActivity(intent)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(history[position])
    }

    fun updateHistory(newHistory: List<ProductEntity>) {
        history = newHistory
        notifyDataSetChanged()
    }
}