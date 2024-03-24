package com.example.marghappclonepriyankaparmar.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marghappclonepriyankaparmar.ImageApplication
import com.example.marghappclonepriyankaparmar.R
import com.example.marghappclonepriyankaparmar.databinding.FragmentSavedBinding
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageDao
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageEntity
import com.example.marghappclonepriyankaparmar.ui.adapter.SavedImageAdapter
import com.example.retrofitmvvm.retrofitAll.viewmodels.ImageViewModel
import com.example.retrofitmvvm.retrofitAll.viewmodels.ImageViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SavedFragment : Fragment(), SavedImageAdapter.OnDeleteClickListener {
    private lateinit var binding: FragmentSavedBinding
    private lateinit var adapterImages: SavedImageAdapter
    private lateinit var savedImageDao: SavedImageDao
    private lateinit var imageList: List<SavedImageEntity>

    private val imageViewModel: ImageViewModel by viewModels {
        val repository = (requireActivity().application as ImageApplication).imageRepositoiry
        ImageViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved, container, false)
        savedImageDao = (requireActivity().application as ImageApplication).database.savedImageDao()
        binding.loader.visibility = View.VISIBLE
        binding.rvSavedImages.visibility = View.GONE

        imageViewModel.allData.observe(viewLifecycleOwner) { data ->
            if (data.size > 0) {
                imageList = data
                binding.rvSavedImages.layoutManager = LinearLayoutManager(requireContext())
                adapterImages = SavedImageAdapter(requireContext(), imageList, savedImageDao, this)
                binding.rvSavedImages.adapter = adapterImages
                binding.loader.visibility = View.GONE
                binding.errorNoImages.visibility = View.GONE
                binding.rvSavedImages.visibility = View.VISIBLE
                adapterImages.submitList(imageList)
            } else {
                binding.loader.visibility = View.GONE
                binding.rvSavedImages.visibility = View.GONE
                binding.errorNoImages.visibility = View.VISIBLE

            }

        }

        return binding.root
    }

    override fun onDeleteClick(item: SavedImageEntity) {
        showDeleteAlertDialog(item)
    }

    private fun showDeleteAlertDialog(item: SavedImageEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete this item?")
        builder.setPositiveButton("Delete") { dialog, which ->
            CoroutineScope(Dispatchers.IO).launch {
                savedImageDao.deleteSavedImage(item.imageUrl)
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}