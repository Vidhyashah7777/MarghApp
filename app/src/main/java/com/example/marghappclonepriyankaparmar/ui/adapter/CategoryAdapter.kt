package com.example.marghappclonepriyankaparmar.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marghappclonepriyankaparmar.apicall.models.Category
import com.example.marghappclonepriyankaparmar.databinding.ItemCategoryBinding

class CategoryAdapter(private val context: Context, private val categories: List<Category>,  private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: String) {
            binding.tvCategory.text = get
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val imageUrl = categories[position].categoryImage
        Glide.with(context)
            .load(imageUrl)
            .into(holder.binding.ivCategory)
        holder.bind(categories[position].categoryName)
        holder.itemView.setOnClickListener {
            Log.d("Priyanka demo", "onCreateView: $categories[position].categoryName")
            onItemClick(categories[position].categoryName)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}