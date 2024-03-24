package com.example.marghappclonepriyankaparmar.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marghappclonepriyankaparmar.R
import com.example.marghappclonepriyankaparmar.apicall.models.Category
import com.example.marghappclonepriyankaparmar.ui.adapter.CategoryAdapter

class CategorySelectionDialogFragment(private val categories: List<Category>) : DialogFragment() {
    private var listener: OnCategorySelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_category, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvCategory)
        val adapter = CategoryAdapter(requireContext(), categories) { categoryName ->
            Log.d("Priyanka demo task", "onCreateView: $categoryName")
            listener?.onCategorySelected(categoryName)
            dismiss()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        return view
    }

    fun setOnCategorySelectedListener(listener: OnCategorySelectedListener) {
        this.listener = listener
    }

    interface OnCategorySelectedListener {
        fun onCategorySelected(categoryName: String)
    }

}