package com.dicoding.scancare.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.scancare.R
import com.dicoding.scancare.data.database.IngredientEntity
import com.dicoding.scancare.ui.detail.DetailActivity

class IngredientsHistoryAdapter : ListAdapter<IngredientEntity, IngredientsHistoryAdapter.ViewHolder>(IngredientsDiffCallback()) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ingredientName: TextView = itemView.findViewById(R.id.tv_item_name)

        fun bind(ingredient: IngredientEntity) {
            ingredientName.text = ingredient.nameIngredients

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("INGREDIENT_NAME", ingredient.nameIngredients)
                intent.putExtra("INGREDIENT_FUNCTION", ingredient.fungsi)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredients, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = getItem(position)
        holder.bind(ingredient)
    }

    class IngredientsDiffCallback : DiffUtil.ItemCallback<IngredientEntity>() {
        override fun areItemsTheSame(oldItem: IngredientEntity, newItem: IngredientEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: IngredientEntity, newItem: IngredientEntity): Boolean {
            return oldItem == newItem
        }
    }
}
