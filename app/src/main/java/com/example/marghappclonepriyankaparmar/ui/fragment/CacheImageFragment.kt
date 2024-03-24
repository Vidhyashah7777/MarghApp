package com.example.marghappclonepriyankaparmar.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.marghappclonepriyankaparmar.ImageApplication
import com.example.marghappclonepriyankaparmar.R
import com.example.marghappclonepriyankaparmar.apicall.models.Hit
import com.example.marghappclonepriyankaparmar.databinding.FragmentCacheImageBinding
import com.example.marghappclonepriyankaparmar.databinding.FragmentHomeBinding
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageDao
import com.example.marghappclonepriyankaparmar.ui.adapter.SavedImageAdapter
import com.example.retrofitmvvm.retrofitAll.viewmodels.ImageViewModel
import com.example.retrofitmvvm.retrofitAll.viewmodels.ImageViewModelFactory

class CacheImageFragment : Fragment() {
    private lateinit var binding: FragmentCacheImageBinding
    private lateinit var cacheImageList: MutableList<String>

    private val imageViewModel: ImageViewModel by viewModels {
        val repository = (requireActivity().application as ImageApplication).imageRepositoiry
        ImageViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cache_image, container, false)
        cacheImageList = mutableListOf()

        imageViewModel.allData.observe(viewLifecycleOwner) { data ->
            if (data.size > 0) {
                for (i in data.indices step 1) {
                    cacheImageList.add(data[i].imageCache)
                }
                Glide.with(requireContext())
                    .load(cacheImageList[1])
                    .into(binding.image)
                Log.d("Priyanka", "onCreateView: ${cacheImageList.size}")
            } else {
                Toast.makeText(requireContext(), "No images found", Toast.LENGTH_SHORT).show()
                Log.d("Priyanka", "onCreateView: No data found")
            }
        }

        return binding.root
    }

}