package com.example.marghappclonepriyankaparmar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marghappclonepriyankaparmar.ImageApplication
import com.example.marghappclonepriyankaparmar.R
import com.example.marghappclonepriyankaparmar.apicall.models.Category
import com.example.marghappclonepriyankaparmar.apicall.models.Hit
import com.example.marghappclonepriyankaparmar.databinding.FragmentHomeBinding
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageDao
import com.example.marghappclonepriyankaparmar.ui.adapter.ImageViewAdapter
import com.example.marghappclonepriyankaparmar.ui.dialog.CategorySelectionDialogFragment
import com.example.retrofitmvvm.retrofitAll.viewmodels.ImageViewModel
import com.example.retrofitmvvm.retrofitAll.viewmodels.ImageViewModelFactory

class HomeFragment : Fragment(), CategorySelectionDialogFragment.OnCategorySelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var imageList: List<Hit>
    private lateinit var adapter: ImageViewAdapter
    private var category: String = "Backgrounds"
    private lateinit var savedImageDao: SavedImageDao

    private val imageViewModel: ImageViewModel by viewModels {
        val repository = (requireActivity().application as ImageApplication).imageRepositoiry
        ImageViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        savedImageDao = (requireActivity().application as ImageApplication).database.savedImageDao()
        binding.selectedCategory.text = category
        binding.loader.visibility = View.VISIBLE
        binding.rvImageData.visibility = View.GONE
        imageViewModel.getImages(category)
        imageViewModel.images.observe(this, Observer {
            Log.d("Get images", it.hits.toString())
            imageList = it.hits
            binding.rvImageData.layoutManager = LinearLayoutManager(requireContext())
            adapter = ImageViewAdapter(requireContext(), imageList, savedImageDao)
            binding.rvImageData.adapter = adapter
            binding.loader.visibility = View.GONE
            binding.rvImageData.visibility = View.VISIBLE
        })

        binding.searchView.setOnClickListener {
            showCategorySelectionDialog(requireActivity().supportFragmentManager)
        }
        return binding.root
    }

    private fun showCategorySelectionDialog(fragmentManager: FragmentManager) {
        val categories = listOf(
            Category("Backgrounds", R.drawable.backgrounds),
            Category("Fashion", R.drawable.fashion),
            Category("Nature", R.drawable.nature),
            Category("Science", R.drawable.science),
            Category("Education", R.drawable.education),
            Category("Feelings", R.drawable.feelings),
            Category("Health", R.drawable.health),
            Category("People", R.drawable.people),
            Category("Religion", R.drawable.religion),
            Category("Places", R.drawable.places),
            Category("Animals", R.drawable.animals),
            Category("Industry", R.drawable.industry),
            Category("Computer", R.drawable.computer),
            Category("Food", R.drawable.food),
            Category("Sports", R.drawable.sports),
            Category("Transportation", R.drawable.transportation),
            Category("Travel", R.drawable.travel),
            Category("Buildings", R.drawable.buildings),
            Category("Business", R.drawable.business),
            Category("Music", R.drawable.music)
        )

        val dialogFragment = CategorySelectionDialogFragment(categories)
        dialogFragment.setOnCategorySelectedListener(this)
        dialogFragment.show(fragmentManager, "CategorySelectionDialog")
    }

    override fun onCategorySelected(categoryName: String) {
        binding.selectedCategory.text = categoryName
        category = categoryName
        imageViewModel.getImages(category.lowercase())
    }
}