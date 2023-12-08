package com.dicoding.scancare.ui.result

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.scancare.R
import com.dicoding.scancare.data.Ingredient
import com.dicoding.scancare.ui.detail.DetailActivity

class IngredientsAdapter(private val ingredientsList: List<Ingredient>): RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val ingredientName: TextView = itemView.findViewById(R.id.tv_item_name)
        fun bind(ingredient: Ingredient){
            ingredientName.text = ingredient.name

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("INGREDIENT_NAME", ingredient.name)
                intent.putExtra("INGREDIENT_FUNCTION", ingredient.function)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredients, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredientsList[position]
        holder.bind(ingredient)
    }

}